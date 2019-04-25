package it.sdkboilerplate.http.agents;

import it.sdkboilerplate.exceptions.*;
import it.sdkboilerplate.http.Headers;
import it.sdkboilerplate.http.SdkRequest;
import it.sdkboilerplate.http.SdkResponse;
import okhttp3.*;
import okhttp3.internal.tls.OkHostnameVerifier;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class OkHttpClientAgent extends UserAgent {
    private String hostname;
    private HashMap<String, Object> config;

    public OkHttpClientAgent(String hostname, HashMap<String, Object> config) {
        super(hostname, config);
        this.hostname = hostname;
        this.config = config;
    }

    public SdkResponse send(SdkRequest sdkRequest) throws SdkException {
        Request.Builder requestBuilder = new Request.Builder();
        this.setUri(sdkRequest, requestBuilder);
        this.setHeaders(sdkRequest, requestBuilder);
        this.setMethod(sdkRequest, requestBuilder);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout((int) this.config.get("timeout"), TimeUnit.SECONDS)
                .writeTimeout((int) this.config.get("timeout"), TimeUnit.SECONDS)
                .readTimeout((int) this.config.get("timeout"), TimeUnit.SECONDS);

        if (!(boolean) this.config.get("verifySSL")) {
            builder = this.setupUnverifiedRequests(builder);
        }
        if (this.isProxyRequest())
            builder = this.setRequestProxy(builder);
        OkHttpClient client = builder.build();

        try {
            Response response = this.sendRequest(client, requestBuilder.build());
            return this.convertResponse(response);
        } catch (IOException e) {
            throw new CouldNotSendRequest();
        }
    }

    private Response sendRequest(OkHttpClient client, Request request) throws IOException {
        return client.newCall(request).execute();
    }

    private SdkResponse convertResponse(Response response) throws IOException {
        return new SdkResponse(response.code(), response.body().string(), this.getResponseHeaders(response));
    }

    private OkHttpClient.Builder setupUnverifiedRequests(OkHttpClient.Builder builder) throws SdkException {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };


            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder;
        } catch (Exception e) {
            throw new UserAgentSetupException();
        }

    }

    private void setHeaders(SdkRequest sdkRequest, Request.Builder builder) {
        HashMap<String, String> headers = sdkRequest.getHeaders();
        for (HashMap.Entry header : headers.entrySet()) {
            builder.addHeader(header.getKey().toString(), header.getValue().toString());
        }
    }

    private void setUri(SdkRequest sdkRequest, Request.Builder builder) throws MalformedUrlException {
        String uri = this.hostname + sdkRequest.getRoute();
        HttpUrl url = HttpUrl.parse(uri);
        if (url == null) {
            throw new MalformedUrlException();
        }
        HttpUrl.Builder urlBuilder = url.newBuilder();

        for (HashMap.Entry queryParameter : sdkRequest.getQueryParameters().entrySet()) {
            urlBuilder.addQueryParameter(queryParameter.getKey().toString(), queryParameter.getValue().toString());
        }
        builder.url(urlBuilder.build().toString());
    }

    private void setMethod(SdkRequest sdkRequest, Request.Builder builder) throws SdkException {
        String verb = sdkRequest.getVerb().toLowerCase();
        switch (verb) {
            case "get":
                builder.get();
                break;
            case "post":
                builder.post(this.getBody(sdkRequest));
                break;
            case "put":
                builder.put(this.getBody(sdkRequest));
                break;
            case "delete":
                builder.delete();
                break;
            case "patch":
                builder.patch(this.getBody(sdkRequest));
                break;
            default:
                throw new UnknownVerbException();

        }
    }

    private OkHttpClient.Builder setRequestProxy(OkHttpClient.Builder builder) {
        Proxy proxy = new Proxy(this.getProxyType(), new InetSocketAddress((String) this.getProxyConfig().get("hostname"), (int) this.getProxyConfig().get("port")));
        builder.proxy(proxy);
        if (this.isProxyAuthenticated()) {
            Authenticator proxyAuthenticator = new Authenticator() {
                @Override
                public Request authenticate(Route route, Response response) throws IOException {
                    HashMap proxyCredentials = OkHttpClientAgent.this.getProxyCredentials();
                    String credential = Credentials.basic((String) proxyCredentials.get("user"), (String) proxyCredentials.get("password"));
                    return response.request().newBuilder()
                            .header("Proxy-Authorization", credential)
                            .build();
                }
            };
            builder.proxyAuthenticator(proxyAuthenticator);
        }
        return builder;
    }

    private Proxy.Type getProxyType() {
        String protocol = (String) this.getProxyConfig().get("protocol");
        switch (protocol) {
            case "http":
                return Proxy.Type.HTTP;
            case "https":
                return Proxy.Type.HTTP;
            default:
                throw new UnknownProxyTypeException();
        }
    }

    private RequestBody getBody(SdkRequest sdkRequest) throws UnknownContentTypeException {
        String contentTypeHeader = sdkRequest.getHeaders().get(Headers.CONTENT_TYPE);
        String requestBody = sdkRequest.getBody();
        if (requestBody == null) return RequestBody.create(null, new byte[0]);
        switch (contentTypeHeader) {
            case it.sdkboilerplate.http.MediaType.APPLICATION_JSON:
                return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestBody);
            case it.sdkboilerplate.http.MediaType.APPLICATION_FORM:
                return RequestBody.create(okhttp3.MediaType.parse("application/x-www-form-urlencoded"), requestBody);
            default:
                throw new UnknownContentTypeException();

        }
    }

    private HashMap<String, String> getResponseHeaders(Response response) {
        okhttp3.Headers responseHeaders = response.headers();
        HashMap<String, String> sdkResponseHeaders = new HashMap<>();
        for (String header : responseHeaders.names()) {
            sdkResponseHeaders.put(header, responseHeaders.get(header));
        }
        return sdkResponseHeaders;

    }

    private boolean isProxyRequest() {
        return this.config.containsKey("proxy");
    }

    private HashMap<String, Object> getProxyConfig() {
        return (HashMap<String, Object>) this.config.get("proxy");
    }

    private boolean isProxyAuthenticated() {
        return this.getProxyConfig().containsKey("credentials");
    }

    private HashMap<String, String> getProxyCredentials() {
        return (HashMap) this.getProxyConfig().get("credentials");
    }
}
