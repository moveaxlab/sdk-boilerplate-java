package it.sdkboilerplate.lib;

import it.sdkboilerplate.exceptions.UnknownContentTypeException;
import it.sdkboilerplate.http.MediaType;

public class SerializerFactory {
    public static Serializer make(String mediaType) throws UnknownContentTypeException {
        switch (mediaType) {
            case (MediaType.APPLICATION_JSON):
                return new JsonSerializer();
            case (MediaType.APPLICATION_FORM):
                return new FormSerializer();
            default:
                throw new UnknownContentTypeException();
        }
    }
}
