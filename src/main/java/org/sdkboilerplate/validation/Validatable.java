package org.sdkboilerplate.validation;

import org.sdkboilerplate.exceptions.JsonSerializationException;

public interface Validatable {
    Schema getSchema() throws JsonSerializationException;
}
