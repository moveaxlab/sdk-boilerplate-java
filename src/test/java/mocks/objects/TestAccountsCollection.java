package mocks.objects;

import com.sdkboilerplate.objects.SdkBodyType;
import com.sdkboilerplate.objects.SdkCollection;
import com.sdkboilerplate.validation.Schema;

import java.util.ArrayList;

public class TestAccountsCollection extends SdkCollection<TestAccount> {
    public TestAccountsCollection(ArrayList<TestAccount> accounts) {
        super(accounts);
    }


    public static Class<? extends SdkBodyType> getElementsClass() {
        return TestAccount.class;
    }

    public Schema getSchema() {
        return new Schema();
    }



}
