package custom.jiten.com.merchanttest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.paytm.pgsdk.PaymentSdkMainClass;
import com.paytm.pgsdk.PaytmClientCertificate;
import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.sdknative.LoginActivity;
import com.paytm.pgsdk.sdknative.PaymentBalanceAvailableActivity;

import java.util.LinkedHashMap;
import java.util.Random;

public class LaunchPaymentActivity extends AppCompatActivity {

    private PaymentSdkMainClass paymentSdkMainClass;
    private EditText orderId;
    private EditText customerId;
    private EditText transactionAmount;
    private EditText merchantId;
    private EditText industryType;
    private EditText mobileNumber;
    private EditText comment;
    private static String PLATFORM_NAME = "PayTM";
    private static String CURRENCY_CODE = "INR";

    private static String OPERATION_TYPE_VALUE="WITHDRAW_MONEY";
    private String mToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initOrderId();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        paymentSdkMainClass = new PaymentSdkMainClass(this);
        initView();

    }

    private void initView() {
        orderId = (EditText) findViewById(R.id.order_id);
        customerId = (EditText) findViewById(R.id.customer_id);
        transactionAmount = (EditText) findViewById(R.id.transaction_amount);
        merchantId = (EditText) findViewById(R.id.merchant_id);
        industryType = (EditText) findViewById(R.id.industry_type_id);
        mobileNumber = (EditText) findViewById(R.id.cust_mobile_no);
        comment = (EditText) findViewById(R.id.comment_id);
    }

    //This is to refresh the order id: Only for the Sample App's purpose.
    @Override
    protected void onStart() {
        super.onStart();
        initOrderId();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    private void initOrderId() {
        Random r = new Random(System.currentTimeMillis());
        String orderId = "ORDER" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000);
        EditText orderIdEditText = (EditText) findViewById(R.id.order_id);
        orderIdEditText.setText(orderId);
    }
    public void nativeStartTransaction(View view) {
        Intent intent = new Intent(LaunchPaymentActivity.this,PaymentBalanceAvailableActivity.class);
        //intent.putExtra(LoginActivity.PASSED_MID, ((EditText) findViewById(R.id.merchant_id)).getText().toString());

        //certificate data
        intent.putExtra(LoginActivity.CERTIFICATE_PASSWORD, "admin");
        intent.putExtra(LoginActivity.CERTIFICATE_FILENAME, "client");
        intent.putExtra(LoginActivity.CHECKSUM_GENERATION_URL, "https://pguat.paytm.com/paytmchecksum/CheckSumGenerator.jsp");

        startActivity(intent);
    }

    public void onStartTransaction(View view) {

        PaytmMerchant merchant = new PaytmMerchant(getCheckSumGenerationUrl()
                /*"https://pguat.paytm.com/paytmchecksum/paytmCheckSumGenerator.jsp"*/,
                getCheckSumvalidationUrl());



        //set permissions for color code , cc,dc ,NB


        boolean isStaging = ((CheckBox) findViewById(R.id.is_staging)).isChecked();
        boolean bSendAllChecksumResponseParametersToPGServer = ((ToggleButton) findViewById(R.id.send_all_checksum_response_parameters_to_pg_server)).isChecked();
        boolean mHideHeader = ((ToggleButton) findViewById(R.id.hide_header)).isChecked();
        boolean mHideArrow = ((ToggleButton) findViewById(R.id.hide_arrow)).isChecked();
        boolean allowPaytmAssist=((ToggleButton) findViewById(R.id.allow_paytmAssist)).isChecked();

        boolean enableCC=((ToggleButton) findViewById(R.id.allow_enablecc)).isChecked();

        boolean enableNB=((ToggleButton) findViewById(R.id.allow_enablenb)).isChecked();

        boolean enableWallet=((ToggleButton) findViewById(R.id.allow_enablewallet)).isChecked();

        String colorCode = ((EditText) findViewById(R.id.edit_color_code)).getEditableText().toString();

        paymentSdkMainClass.setSendAllChecksumResponseParametersToPGServer(bSendAllChecksumResponseParametersToPGServer);
        paymentSdkMainClass.setHideHeader(mHideHeader);

        paymentSdkMainClass.setDyanamicValues(enableCC,enableNB,enableWallet,colorCode);
        LinkedHashMap<String, String> paramMap = new LinkedHashMap<String, String>();

        // these are mandatory parameters

        paramMap.put("ORDER_ID", ((EditText) findViewById(R.id.order_id)).getText().toString());
        paramMap.put("MID", ((EditText) findViewById(R.id.merchant_id)).getText().toString());
        paramMap.put("CUST_ID", ((EditText) findViewById(R.id.customer_id)).getText().toString());
        paramMap.put("CHANNEL_ID", ((EditText) findViewById(R.id.channel_id)).getText().toString());
        paramMap.put("INDUSTRY_TYPE_ID", ((EditText) findViewById(R.id.industry_type)).getText().toString());
        paramMap.put("WEBSITE", ((EditText) findViewById(R.id.website)).getText().toString());
        paramMap.put("TXN_AMOUNT", ((EditText) findViewById(R.id.transaction_amount)).getText().toString());
        paramMap.put("THEME", ((EditText) findViewById(R.id.theme)).getText().toString());
        paramMap.put("EMAIL", ((EditText) findViewById(R.id.cust_email_id)).getText().toString());
        paramMap.put("MSISDN", ((EditText) findViewById(R.id.cust_mobile_no)).getText().toString());
        //new fields
        paramMap.put("MERC_UNQ_REF", ((EditText) findViewById(R.id.merc_ref)).getText().toString());
        //paramMap.put("CALLBACK_URL", ((EditText) findViewById(R.id.txt_callback)).getText().toString());

        //new


        PaytmClientCertificate certificate = new PaytmClientCertificate("admin", "client");
        paymentSdkMainClass.pay(paramMap, merchant, mHideArrow, certificate, isStaging,allowPaytmAssist, new PaytmPaymentTransactionCallback() {

            @Override
            public void someUIErrorOccurred(String arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(LaunchPaymentActivity.this, "UI Error Occur", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTransactionSuccess(Bundle arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(LaunchPaymentActivity.this, "Transaction Successful", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onTransactionFailure(String arg0, Bundle arg1) {
                // TODO Auto-generated method stub
                Toast.makeText(LaunchPaymentActivity.this, "Transaction Failuer", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onErrorLoadingWebPage(int arg0, String arg1, String arg2) {
                // TODO Auto-generated method stub
                Toast.makeText(LaunchPaymentActivity.this, "Error On Loading", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBackPressedCancelTransaction() {
                // TODO Auto-generated method stub
                Toast.makeText(LaunchPaymentActivity.this, "Back Button pressed by user", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void networkNotAvailable() {
                // TODO Auto-generated method stub
                Toast.makeText(LaunchPaymentActivity.this, "Check Network connection", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void clientAuthenticationFailed(String arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(LaunchPaymentActivity.this, "Client Authentication Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private interface URLS {


        String PRODUCTION_CHECKSUM_GENERATION = "https://pguat.paytm.com/paytmchecksum/paytmCheckSumGenerator.jsp";
        String PRODUCTION_CHECKSUM_VALIDATION = "https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp";

        //        String STAGING_CHECKSUM_GENERATION = "http://10.0.28.74:8080/appEvoke/paytmCheckSumGenerator2.jsp";
        String STAGING_CHECKSUM_GENERATION =/*"https://pguat.paytm.com/paytmchecksum/paytmCheckSumGenerator.jsp"*/"https://pguat.paytm.com/paytmchecksum/CheckSumGenerator.jsp";
        String STAGING_CHECKSUM_VALIDATION ="https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp";
      /*String STAGING_CHECKSUM_VALIDATION = "http://testngetjp.irctc.co.in/eticketing/PaytmVerify";*/
    }

    /**
     * if checked means staging else production
     *
     * @return checksum geeration url according to checkbox value
     */
    private String getCheckSumGenerationUrl() {
        if (((CheckBox) findViewById(R.id.is_staging)).isChecked()) {
            return URLS.STAGING_CHECKSUM_GENERATION;

        } else {
            return URLS.PRODUCTION_CHECKSUM_GENERATION;
        }

    }

    /**
     * if checked means staging else production
     *
     * @return checksum validation url according to checkbox value
     */
    public String getCheckSumvalidationUrl() {
        if (((CheckBox) findViewById(R.id.is_staging)).isChecked()) {
            return URLS.STAGING_CHECKSUM_VALIDATION;

        } else {
            return URLS.PRODUCTION_CHECKSUM_VALIDATION;
        }

    }



}
