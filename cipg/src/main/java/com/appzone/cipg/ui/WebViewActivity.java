package com.appzone.cipg.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.appzone.cipg.global.AppState;
import com.appzone.cipg.CipgSdk;
import com.appzone.cipg.CipgWebViewClient;
import com.appzone.cipg.R;
import com.appzone.cipg.global.Constants;
import com.appzone.cipg.model.Error;
import com.appzone.cipg.model.Response;
import com.appzone.cipg.network.NetworkManger;
import com.appzone.cipg.network.model.ValidateRequest;
import com.appzone.cipg.network.model.ValidationResponse;

import java.net.URLEncoder;

import retrofit2.Call;
import retrofit2.Callback;

public class WebViewActivity extends AppCompatActivity {
    WebView webView;
    ProgressDialog progressDialog;
    Context context;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        context = WebViewActivity.this;

        webView = findViewById(R.id.webView);
        progressDialog = new ProgressDialog(context);
        //progressDialog.setCancelable(false);
        CipgWebViewClient webViewClient = new CipgWebViewClient(new SdkCallback() {
            @Override
            public void close() {
                finish();
            }

            @Override
            public void loading() {
                if(progressDialog != null){
                    progressDialog.setMessage("Loading, please wait");
                    progressDialog.show();
                }
            }

            @Override
            public void loadingCompleted() {
                //progressDialog.hide();
                Log.d("LOADING", "loading completed called");
                if(progressDialog != null){
                    progressDialog.hide();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void startTransRefValidation(final String transRef, final String orderId) {
                final ProgressDialog progressDialog = new ProgressDialog(AppState.appContext);
                progressDialog.setMessage("Almost done, Validating transaction");
                Log.d("VALIDATING", "validating transaction ref");
                progressDialog.show();
                ValidateRequest validateRequest = new ValidateRequest();
                validateRequest.setAmount(AppState.amount);
                validateRequest.setCurrencyCode(AppState.currencyCode);
                validateRequest.setMerchantId(AppState.merchantId);
                validateRequest.setOrderId(AppState.orderId);
                NetworkManger.getINSTANCE().getValidationService().validateTransRef(validateRequest).enqueue(new Callback<ValidationResponse>() {
                    @Override
                    public void onResponse(Call<ValidationResponse> call, retrofit2.Response<ValidationResponse> response) {
                        progressDialog.hide();
                        assert response.body() != null;
                        Log.v("RESPONSE BODY", "Response body"+response.body());
                        Log.v("RESPONSE", "Response"+response.toString());
                        if(response.code() == 200){
                            Log.d("VALIDATING", "transaction ref successful");
                            Response res = new Response();
                            res.setTransRef(transRef);
                            res.setOrderId(orderId);
                            CipgSdk.cipgCallback.onSuccess(res);
                            finish();
                        }else{
                            Log.d("VALIDATING", "transaction ref failed");
                            Error error = new Error();
                            if(response.body() != null){
                            error.setReason(response.body().getResponseDescription());
                            error.setStatusCode(response.body().getStatusCode());
                            }else {
                                error.setReason(response.message());
                            }
                            CipgSdk.cipgCallback.onError(error);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ValidationResponse> call, Throwable t) {
                        progressDialog.hide();
                        Error error = new Error();
                        error.setReason(t.getLocalizedMessage());
                        CipgSdk.cipgCallback.onError(error);
                    }
                });
            }
        });


        webView.setWebViewClient(webViewClient);

        String postData = "mercId=" + encode(AppState.merchantId) +
                    "&currCode=" +  encode(AppState.currencyCode) +
                    "&amt=" + encode(AppState.amount) +
                    "&orderId=" +  encode(AppState.orderId) +
                    "&prod=" +  encode(AppState.prod) +
                    "&source=" +  encode(AppState.source) +
                    "&email=" + encode(AppState.email);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.postUrl(Constants.START_URL,
                postData.getBytes());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog = null;
    }

    private String encode(String value){
        try{
            return URLEncoder.encode(value,"UTF-8");
        }catch (Exception e){
            e.printStackTrace();
            return value;
        }
    }
}
