package it.sdkboilerplate.lib;

import it.sdkboilerplate.exceptions.UnknownContentTypeException;
import it.sdkboilerplate.http.MediaType;

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
