package com.sdkboilerplate.lib;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdkboilerplate.exceptions.UnserializableObjectException;
import com.sdkboilerplate.objects.SdkBodyType;
import com.sdkboilerplate.objects.SdkCollection;
import com.sdkboilerplate.objects.SdkObject;


@SuppressWarnings("unchecked")
public class JsonSerializer implements Serializer {
    /**
     * Serialized an SdkObject instance into its Json Representation
     *
     * @param bodyObject SdkObject to be serialized
     * @return String in Json Format
     * @throws UnserializableObjectException if the SdkObject is malformed
     */
    public String serialize(SdkBodyType bodyObject) throws UnserializableObjectException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            if (bodyObject instanceof SdkObject) {
                return mapper.writeValueAsString(bodyObject.getClass().getMethod("toHashMap").invoke(bodyObject));
            } else if (bodyObject instanceof SdkCollection) {
                return mapper.writeValueAsString(bodyObject.getClass().getMethod("toArray").invoke(bodyObject));
            } else {
                throw new UnserializableObjectException();
            }
        } catch (ReflectiveOperationException | JsonProcessingException e) {
            throw new UnserializableObjectException();
        }
    }


}
