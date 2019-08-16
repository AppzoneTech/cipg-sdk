package com.appzone.cipg.events;

import com.appzone.cipg.model.Response;

public class SuccessEvent {
   private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
