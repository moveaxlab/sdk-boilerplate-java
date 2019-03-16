package mocks.actions;

import org.sdkboilerplate.actions.Action;
import org.sdkboilerplate.actions.ActionsFactory;
import org.sdkboilerplate.lib.ApiContext;

import java.util.HashMap;

public class TestActionFactory extends ActionsFactory {

    @Override
    public HashMap<String, Class<? extends Action>> getActions() {
        HashMap<String, Class<? extends Action>> actions = new HashMap<>();
        actions.put("testUserRetrieval", TestRetrieveUserAction.class);
        actions.put("testUserCreation", TestCreateUserAction.class);
        return actions;
    }

    public TestActionFactory(ApiContext ctx) {
        super(ctx);
    }
}
