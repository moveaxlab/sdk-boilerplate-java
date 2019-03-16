package com.sdkboilerplate.lib;

import com.sdkboilerplate.exceptions.DeserializationException;
import com.sdkboilerplate.exceptions.UndefinedSetterException;
import com.sdkboilerplate.exceptions.UnknownBodyTypeException;
import com.sdkboilerplate.objects.SdkBodyType;

/**
 * Interface which allows the deserialization of raw serialized strings into SdkType instances
 */
public interface Deserializer {
    SdkBodyType deserialize(String serialized, Class<? extends SdkBodyType> sdkObjectClass) throws UnknownBodyTypeException, DeserializationException;

}
