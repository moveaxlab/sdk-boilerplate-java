package mocks.objects;

import com.sdkboilerplate.objects.SdkObject;
import com.sdkboilerplate.validation.Schema;

public class TestAccount extends SdkObject {
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Schema getSchema() {
        return new Schema();
    }

    public TestAccount(String name) {
        this.name = name;
    }

    public TestAccount() {
    }
}
