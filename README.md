# CIPG-SDK

## Intsallation
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.AppzoneTech:cipg-sdk:0.1.2-19'
	}
  
 ## Usage
  
 ```java

     //innitialize the sdk in the onCreate method of the Application or activity
     
    CipgSdk.init("yourBaseUrl");
  
  
     void youInvokingMethod(){
        Charge charge = new Charge();
        charge.setAmount("2000");
        charge.setCurrencyCode("566");
        charge.setCustomerEmail("test@mail.com");
        charge.setMerchantId("00037");
        charge.setProductName("Test Merchant");
        charge.setOrderId(String.valueOf(orderId++));

            CipgSdk.pay((Activity) this, charge, new CipgCallback() {
                @Override
                public void onSuccess(Response response) {
                    Log.d(TAG, "Transaction Ref-> "+response.getTransRef());
                    Log.d(TAG, "Order Id-> "+response.getOrderId());
                    Toast.makeText(context, "Payment successful", Toast.LENGTH_LONG).show();
                }
                @Override
                public void onError(Error error) {
                    Log.d(TAG, error.getReason());
                    Toast.makeText(context, "Error: "+error.getReason(), Toast.LENGTH_LONG).show();
                }
            });
    }  
  ```
