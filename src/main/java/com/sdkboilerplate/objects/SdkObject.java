package com.sdkboilerplate.objects;

import java.lang.reflect.Field;
import java.util.HashMap;

public abstract class SdkObject  extends SdkBodyType {

    public static HashMap<String, Class<? extends SdkBodyType>> getSubObjects() {
        return new HashMap<>();
    }

    /**
     * Helper method to convert an SdkObject into an HashMap<String, Object></String,>
     *
     * @return HashMap representation of the sdk object
     * @throws ReflectiveOperationException On object malformed
     */
    public HashMap<String, Object> toHashMap() throws ReflectiveOperationException {
        HashMap<String, Object> serialization = new HashMap<>();
        // We get every field defined by the object
        Field[] objectAttributes = this.getClass().getDeclaredFields();
        for (Field field : objectAttributes) {
            field.setAccessible(true);
            Object fieldValue = field.get(this);
            Object value = null;
            // If the value is an SdkObject, we call the method recursively to get the HashMap
            if (fieldValue instanceof SdkObject) {
                value = fieldValue.getClass().getMethod("toHashMap").invoke(fieldValue);
                // If the value is an SdkCollection, we call the method recursively to get its array representation
            } else if (fieldValue instanceof SdkCollection) {
                value = fieldValue.getClass().getMethod("toArrayList").invoke(fieldValue);
            } else {
                // Else we get the value as it is since its a primitive type
                value = fieldValue;
            }
            serialization.put(field.getName(), value);
        }
        return serialization;
    }
}
