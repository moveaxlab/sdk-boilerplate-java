package org.sdkboilerplate.http.agents;

import org.sdkboilerplate.exceptions.SdkException;
import org.sdkboilerplate.http.SdkRequest;
import org.sdkboilerplate.http.SdkResponse;

import java.util.HashMap;

public abstract class UserAgent {

    private String hostname;
    private HashMap<String, Object> config;

    public abstract SdkResponse send(SdkRequest sdkRequest) throws SdkException;

    UserAgent(String hostname, HashMap<String, Object> config) {
        this.hostname = hostname;
        this.config = config;
    }
}
