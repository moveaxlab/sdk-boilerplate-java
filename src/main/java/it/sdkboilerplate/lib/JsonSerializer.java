package org.sdkboilerplate.lib;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sdkboilerplate.exceptions.UnserializableObjectException;
import org.sdkboilerplate.objects.SdkBodyType;
import org.sdkboilerplate.objects.SdkCollection;
import org.sdkboilerplate.objects.SdkObject;


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
