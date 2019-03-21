package org.sdkboilerplate.lib;

import org.sdkboilerplate.exceptions.UnknownContentTypeException;
import org.sdkboilerplate.http.MediaType;

public class DeserializerFactory {
    public static Deserializer make(String mediaType) throws UnknownContentTypeException {
        switch (mediaType) {
            case (MediaType.APPLICATION_JSON):
                return new JsonDeserializer();
            case (MediaType.APPLICATION_FORM):
                return new FormDeserializer();
            default:
                throw new UnknownContentTypeException();
        }
    }
}
