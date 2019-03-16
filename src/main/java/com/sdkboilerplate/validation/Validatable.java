package com.sdkboilerplate.validation;

import com.sdkboilerplate.exceptions.JsonSerializationException;

public interface Validatable {
    Schema getSchema() throws JsonSerializationException;
}
