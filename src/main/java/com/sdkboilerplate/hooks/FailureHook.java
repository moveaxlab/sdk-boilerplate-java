package com.sdkboilerplate.hooks;

import com.sdkboilerplate.exceptions.SdkHttpException;
import com.sdkboilerplate.http.SdkRequest;
import com.sdkboilerplate.http.SdkResponse;
import com.sdkboilerplate.lib.ApiContext;

/**
 * Class FailureHook implementing Hook interface. Failure Hook instances are run if the http sdkRequest returns an
 * error status code.
 * FailureHook instances are constructed with the exception extracted from the action mapping
 */
public abstract class FailureHook implements Hook {
    public ApiContext ctx;
    public SdkRequest sdkRequest;
    public SdkResponse sdkResponse;
    public SdkHttpException exception;

    public FailureHook(ApiContext ctx, SdkRequest sdkRequest, SdkResponse sdkResponse, SdkHttpException exception) {
        this.ctx = ctx;
        this.sdkRequest = sdkRequest;
        this.sdkResponse = sdkResponse;
        this.exception = exception;
    }

}
