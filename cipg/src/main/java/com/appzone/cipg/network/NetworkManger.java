package com.appzone.cipg.network;
import com.appzone.cipg.global.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkManger {
    private static NetworkManger INSTANCE;
    private static ValidationService validationService;

    private NetworkManger(){}
    public static NetworkManger getINSTANCE(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .build();
         validationService = retrofit.create(ValidationService.class);

        if(INSTANCE == null){
            INSTANCE = new NetworkManger();
        }
        return INSTANCE;
    }

    public ValidationService getValidationService(){
         return validationService;
    }
}
