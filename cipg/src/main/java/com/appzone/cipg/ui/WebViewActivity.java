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
import com.appzone.cipg.model.Response;

import java.net.URLEncoder;

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
//                progressDialog.hide();
                Log.d("LOADING", "loading completed called");
                if(progressDialog != null){
                    progressDialog.hide();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void startTransRefValidation(String transRef, String orderId) {
                //Todo: call validate api to validate transaction ref;

                //if valid, call success callback
                Response response = new Response();
                response.setTransRef(transRef);
                response.setOrderId(orderId);
                CipgSdk.cipgCallback.onSuccess(response);
                finish();

                //if inValid, call success callback
//                Error error = new Error();
//                CipgSdk.cipgCallback.onError(error);
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
