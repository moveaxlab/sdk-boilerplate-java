package com.sdkboilerplate.validation;

import com.sdkboilerplate.exceptions.JsonSerializationException;
import com.sdkboilerplate.utils.Json;

import java.util.HashMap;

@SuppressWarnings("unchecked")
public class Schema {

    public HashMap<String, Object> getSchema() {
        return schema;
    }

    private HashMap<String, Object> schema;
    public Schema(){}

    public Schema(String jsonString) throws JsonSerializationException {
        this.schema = (HashMap<String, Object>)Json.load(jsonString, HashMap.class);
    }
}
