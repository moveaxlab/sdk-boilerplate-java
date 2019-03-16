package org.sdkboilerplate.http;

import org.sdkboilerplate.exceptions.DeserializationException;
import org.sdkboilerplate.exceptions.UnknownBodyTypeException;
import org.sdkboilerplate.exceptions.UnknownContentTypeException;
import org.sdkboilerplate.lib.Deserializer;
import org.sdkboilerplate.lib.DeserializerFactory;

import org.sdkboilerplate.objects.SdkBodyType;

import java.util.HashMap;

public class SdkResponse {
    public Integer getStatusCode() {
        return this.statusCode;
    }

    public String getRawBody() {
        return this.body;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    private Integer statusCode;
    private String body;
    private HashMap<String, String> headers;

    public SdkResponse(Integer statusCode, String body, HashMap<String, String> headers) {
        this.statusCode = statusCode;
        this.body = body;
        this.headers = headers;
    }

    /**
     * @return True iff the response status code is is 200<= <=299
     */
    public boolean isFailed() {
        return 200 > this.statusCode || this.statusCode > 299;
    }

    /**
     * Formats the raw response body into the appropriate object class
     *
     * @param sdkObjectClass Class of the sdkObject
     * @return SdkObject instance
     * @throws UnknownContentTypeException When the response content type is not handled by the sdk
     */
    public <T extends SdkBodyType> T format(Class<? extends SdkBodyType> sdkObjectClass) throws UnknownContentTypeException, UnknownBodyTypeException, DeserializationException {
        if (sdkObjectClass == null) {
            return null;
        }
        Deserializer deserializer = DeserializerFactory.make(this.headers.get(Headers.CONTENT_TYPE));
        return (T) deserializer.deserialize(this.body, sdkObjectClass);

    }
}