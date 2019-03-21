package org.sdkboilerplate.lib;

import org.sdkboilerplate.exceptions.UnserializableObjectException;
import org.sdkboilerplate.objects.SdkBodyType;

/**
 * Interface which allows the serialization of Sdk objects into raw strings
 */
public interface Serializer {
    String serialize(SdkBodyType sdkObject) throws UnserializableObjectException;

}
