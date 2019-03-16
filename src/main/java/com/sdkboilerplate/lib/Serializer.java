package com.sdkboilerplate.lib;

import com.sdkboilerplate.exceptions.UnserializableObjectException;
import com.sdkboilerplate.objects.SdkBodyType;

/**
 * Interface which allows the serialization of Sdk objects into raw strings
 */
public interface Serializer {
    String serialize(SdkBodyType sdkObject) throws UnserializableObjectException;

}
