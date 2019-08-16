package com.appzone.cipg.events;

import com.appzone.cipg.model.Error;

public class ErrorEvent {
    private Error error;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
