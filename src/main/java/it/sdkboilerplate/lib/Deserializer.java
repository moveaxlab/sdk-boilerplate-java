package org.sdkboilerplate.lib;

import org.sdkboilerplate.exceptions.DeserializationException;
import org.sdkboilerplate.exceptions.UnknownBodyTypeException;
import org.sdkboilerplate.objects.SdkBodyType;

/**
 * Interface which allows the deserialization of raw serialized strings into SdkType instances
 */
public interface Deserializer {
    SdkBodyType deserialize(String serialized, Class<? extends SdkBodyType> sdkObjectClass) throws UnknownBodyTypeException, DeserializationException;

}
