package org.sdkboilerplate.http.agents;

import org.sdkboilerplate.exceptions.*;
import org.sdkboilerplate.http.Headers;
import org.sdkboilerplate.http.SdkRequest;
import org.sdkboilerplate.http.SdkResponse;

import okhttp3.*;
import org.sdkboilerplate.exceptions.*;

import javax.net.ssl.*;
import java.io.IOException;

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

    private RequestBody getBody(SdkRequest sdkRequest) throws UnknownContentTypeException {
        String contentTypeHeader = sdkRequest.getHeaders().get(Headers.CONTENT_TYPE);
        String requestBody = sdkRequest.getBody();
        if (requestBody == null) return RequestBody.create(null, new byte[0]);
        switch (contentTypeHeader) {
            case org.sdkboilerplate.http.MediaType.APPLICATION_JSON:
                return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestBody);
            case org.sdkboilerplate.http.MediaType.APPLICATION_FORM:
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
}
