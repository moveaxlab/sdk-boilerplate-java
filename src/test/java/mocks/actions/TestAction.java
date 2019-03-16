package mocks.actions;

import com.sdkboilerplate.actions.Action;
import com.sdkboilerplate.hooks.FailureHook;
import com.sdkboilerplate.hooks.PreSendHook;
import com.sdkboilerplate.hooks.SuccessHook;
import com.sdkboilerplate.http.SdkResponse;
import com.sdkboilerplate.lib.ApiContext;

import java.util.ArrayList;

public abstract class TestAction extends Action {
    public TestAction(ApiContext ctx) {
        super(ctx);
    }

    @Override
    public ArrayList<Class<? extends FailureHook>> getFailureHooks() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Class<? extends SuccessHook>> getSuccessHooks() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Class<? extends PreSendHook>> getPreSendHooks() {
        return new ArrayList<>();
    }

}
