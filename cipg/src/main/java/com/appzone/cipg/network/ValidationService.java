package com.appzone.cipg.network;

import com.appzone.cipg.network.model.ValidateRequest;
import com.appzone.cipg.network.model.ValidationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface  ValidationService {
        @POST("TransactionStatus/ConfirmTransaction")
        Call<ValidationResponse> validateTransRef(@Body ValidateRequest request);
}
