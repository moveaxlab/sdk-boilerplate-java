package mocks.actions;

import com.sdkboilerplate.actions.Action;
import com.sdkboilerplate.actions.ActionsFactory;
import com.sdkboilerplate.lib.ApiContext;

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
