package com.appzone.cipg;

import com.appzone.cipg.model.Error;
import com.appzone.cipg.model.Response;

public interface CipgCallback{
    void onSuccess(Response response);
    void  onError(Error error);
}
