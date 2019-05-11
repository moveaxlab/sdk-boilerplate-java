package it.sdkboilerplate.exceptions;

/**
 * Sdk Http Exception class.
 */
public abstract class SdkHttpException extends SdkException {
    private String debugInfo;


    public String getDebugInfo() {
        return debugInfo;
    }

    public void setDebugInfo(String debugInfo) {
        this.debugInfo = debugInfo;
    }

}


