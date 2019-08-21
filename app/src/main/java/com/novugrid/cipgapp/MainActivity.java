package com.novugrid.cipgapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.appzone.cipg.CipgCallback;
import com.appzone.cipg.CipgSdk;
import com.appzone.cipg.model.Charge;
import com.appzone.cipg.model.Error;
import com.appzone.cipg.model.Response;

public class MainActivity extends AppCompatActivity {

    Button btnPay;
    Context context;
    String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnPay = findViewById(R.id.btnPay);
        context = this;
        CipgSdk.init("http://paygatetest.fidelitybank.ng/CIPGForCard");
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleButtonClick();
            }
        });
    }

    void handleButtonClick(){
        Charge charge = new Charge();
        charge.setAmount("2000");
        charge.setCurrencyCode("566");
        charge.setCustomerEmail("jl1aw1al@appzonegroup.com");
        charge.setMerchantId("00037");
        charge.setProductName("Test Merchant");
        charge.setOrderId("119163065");

        try{
            CipgSdk.pay((Activity) context, charge, new CipgCallback() {
                @Override
                public void onSuccess(Response response) {
                    Log.d(TAG, "onSuccess event in main app");
                    Log.d(TAG, "Transaction Ref-> "+response.getTransRef());
                    Log.d(TAG, "Order Id-> "+response.getOrderId());
                    Toast.makeText(context, "Payment successful", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(Error error) {
                    Log.d(TAG, "onError event in main app");
                    Log.d(TAG, error.getReason());
                    Toast.makeText(context, "Error: "+error.getReason(), Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(context, "Error occurred :"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}
