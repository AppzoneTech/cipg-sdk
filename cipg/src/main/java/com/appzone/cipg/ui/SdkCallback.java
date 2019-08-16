package com.appzone.cipg.ui;

public interface SdkCallback {
    void close();
    void loading();
    void loadingCompleted();
    void startTransRefValidation(String transRef, String orderId);
}
