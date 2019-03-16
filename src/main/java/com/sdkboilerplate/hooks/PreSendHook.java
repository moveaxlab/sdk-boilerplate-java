package com.sdkboilerplate.hooks;

import com.sdkboilerplate.http.SdkRequest;
import com.sdkboilerplate.lib.ApiContext;

/**
 * PreSend hook abstract class implementing Hook interface. PreSend Hook instances are constructed with the Http sdkRequest
 * object and are run before the sdkRequest is actually sent.
 */
public abstract class PreSendHook implements Hook{
    public ApiContext ctx;
    public SdkRequest sdkRequest;

    public PreSendHook(ApiContext ctx, SdkRequest sdkRequest) {
        this.ctx = ctx;
        this.sdkRequest = sdkRequest;
    }
}
