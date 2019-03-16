package com.sdkboilerplate.http.agents;

import com.sdkboilerplate.lib.ApiContext;

public class UserAgentFactory {
    public static UserAgent make(ApiContext ctx) {
        return new OkHttpClientAgent(ctx.getHostname(), ctx.getConfig());
    }
}
