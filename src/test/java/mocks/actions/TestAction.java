package mocks.actions;

import org.sdkboilerplate.actions.Action;
import org.sdkboilerplate.hooks.FailureHook;
import org.sdkboilerplate.hooks.PreSendHook;
import org.sdkboilerplate.hooks.SuccessHook;
import org.sdkboilerplate.lib.ApiContext;

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
