package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class PaymentActivity extends AppCompatActivity {
    Toolbar paymentToolbar;
    TextInputLayout address1, address2, city, state, postCode, country, amountBox;
    String cus_name, cus_email, cus_add1, cus_add2, cus_city, cus_state, cus_postcode, cus_country, amount, currency, tran_id, desc, store_id, signature_key, source, source_details, user_imei, device_type, user_phone, app_name, app_version, app_id, opt_a, opt_b, opt_c, opt_d;
    WebView webView;
    Spinner bankList;
    Button submitPayment,payButton;
    List<String> bankName;
    ArrayList<String> paymentLinks;
    int linkIndex;
    String track;
    TextView methodPayment;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }
        webView=new WebView(PaymentActivity.this);

        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);



        paymentToolbar = findViewById(R.id.paymentToolbar);
        setSupportActionBar(paymentToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        paymentToolbar.setNavigationIcon(R.drawable.ic_back);

        initialize();
        paymentToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        submitPayment = findViewById(R.id.submitPayment);
        submitPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if ( Objects.requireNonNull(amountBox.getEditText()).getText().toString().trim().isEmpty() ) {
                        amountBox.setErrorEnabled(true);
                        amountBox.setError("Please Enter Amount");
                    } else if ( Integer.parseInt(amountBox.getEditText().getText().toString().trim()) < 10 ) {
                        amountBox.setErrorEnabled(true);
                        amountBox.setError("Minimum Deposit is 10");
                    } else {
                        try {
                            amountBox.setErrorEnabled(false);
                            new Task().execute();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        try {
            bankList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    linkIndex = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(PaymentActivity.this,"Server Error",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.setWebViewClient(new WebViewClient(){
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        if(url.contains("securepay.easypayway.com/sdk/result.php"))
                        {
                            try {
                                //ConstraintLayout paymentLayout=findViewById(R.id.paymentLayout);
                                getResult();
                                webView.clearHistory();
                                finish();

                                startActivity(new Intent(PaymentActivity.this,PaymentResult.class));

                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                webView.loadUrl(paymentLinks.get(linkIndex));
                setContentView(webView);
            }
        });

    }
    private void getResult()
    {

        HttpURLConnection conn=null;
        try{
            String postData1 = "track=" + URLEncoder.encode(track, "UTF-8");
            URL urlObj = new URL("https://securepay.easypayway.com/sdk/result.php");
            conn = (HttpURLConnection) urlObj.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);

            conn.connect();


            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(postData1);
            wr.flush();
            wr.close();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            System.out.println("Result:"+ result);
            JSONObject resultJSON=new JSONObject(result.toString());
            String pay_status=resultJSON.getString("pay_status");
            String epw_txnid=resultJSON.getString("epw_txnid");
            String pay_time=resultJSON.getString("pay_time");
            TransactionInfo transactionInfo=new TransactionInfo(pay_status,epw_txnid,pay_time,amount);
            DataHolder.setTransactionInfo(transactionInfo);


            System.out.println("Pay"+ pay_status);

            //Log.d("test", "result from server: " + result.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
    private void sendPayment()
    {
        try {
            cus_name = DataHolder.getUserProfile().getFirstName() + " " + DataHolder.getUserProfile().getLastName();
            cus_email = DataHolder.getUserProfile().getEmail();
            cus_add1 = Objects.requireNonNull(address1.getEditText()).getText().toString();
            cus_add2 = Objects.requireNonNull(address2.getEditText()).getText().toString();
            cus_city = Objects.requireNonNull(city.getEditText()).getText().toString();
            cus_state = Objects.requireNonNull(state.getEditText()).getText().toString();
            cus_postcode = Objects.requireNonNull(postCode.getEditText()).getText().toString();
            cus_country = Objects.requireNonNull(country.getEditText()).getText().toString();
            amount = Objects.requireNonNull(amountBox.getEditText()).getText().toString();
            currency = DataHolder.getUserProfile().getCurrency();
            Random rand = new Random();
            tran_id = "BPR"+rand.nextLong();
            System.out.println("Tran "+tran_id);

            desc = "N/A";

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        String postData;
        String url = "https://securepay.easypayway.com/sdk/index.php";

        try {
            postData = "cus_name=" + URLEncoder.encode(cus_name, "UTF-8") +
                    "&cus_phone=" + URLEncoder.encode(user_phone, "UTF-8") +
                    "&cus_email=" + URLEncoder.encode(cus_email, "UTF-8") +
                    "&cus_add1=" + URLEncoder.encode(cus_add1, "UTF-8") +
                    "&cus_add2=" + URLEncoder.encode(cus_add2, "UTF-8") +
                    "&cus_city=" + URLEncoder.encode(cus_city, "UTF-8") +
                    "&cus_state=" + URLEncoder.encode(cus_state, "UTF-8") +
                    "&cus_postcode=" + URLEncoder.encode(cus_postcode, "UTF-8") +
                    "&cus_country=" + URLEncoder.encode(cus_country, "UTF-8") +
                    "&amount=" + URLEncoder.encode(amount, "UTF-8") +
                    "&currency=" + URLEncoder.encode(currency, "UTF-8") +
                    "&tran_id=" + URLEncoder.encode(tran_id, "UTF-8") +
                    "&desc=" + URLEncoder.encode(desc, "UTF-8") +
                    "&store_id=" + URLEncoder.encode(store_id, "UTF-8") +
                    "&signature_key=" + URLEncoder.encode(signature_key, "UTF-8") +
                    "&source=" + URLEncoder.encode(source, "UTF-8") +
                    "&user_imei=" + URLEncoder.encode(user_imei, "UTF-8")+
                    "&device_type=" + URLEncoder.encode(device_type, "UTF-8") +
                    "&user_phone=" + URLEncoder.encode(user_phone, "UTF-8") +
                    "&app_name=" + URLEncoder.encode(app_name, "UTF-8") +
                    "&app_version =" + URLEncoder.encode(app_version, "UTF-8") +
                    "&app_id=" + URLEncoder.encode(app_id, "UTF-8") +
                    "&opt_a=" + URLEncoder.encode(opt_a, "UTF-8") +
                    "&opt_b=" + URLEncoder.encode(opt_b, "UTF-8") +
                    "&opt_c=" + URLEncoder.encode(opt_c, "UTF-8") +
                    "&opt_d=" + URLEncoder.encode(opt_d, "UTF-8");
            // webview.postUrl(url, postData.getBytes());
            HttpURLConnection conn=null;
            try{
                URL urlObj = new URL(url);
                conn = (HttpURLConnection) urlObj.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Accept-Charset", "UTF-8");

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);

                conn.connect();


                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(postData);
                wr.flush();
                wr.close();

                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                JSONObject resultJSON=new JSONObject(result.toString());
                bankName=new ArrayList<>();
                paymentLinks=new ArrayList<>();
                final JSONArray cards=resultJSON.getJSONArray("cards");
                track=resultJSON.getString("track");
                for(int i=0;i<cards.length();i++)
                {
                    String bankDetails = cards.get(i).toString();
                    JSONObject bankDetailsJSON = new JSONObject(bankDetails);
                    bankName.add(bankDetailsJSON.getString("sdk_text"));
                    paymentLinks.add(bankDetailsJSON.getString("url"));


                }
                final ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        this, android.R.layout.simple_spinner_item, bankName);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        bankList.setVisibility(View.VISIBLE);
                        methodPayment.setVisibility(View.VISIBLE);
                        submitPayment.setVisibility(View.GONE);
                        amountBox.setVisibility(View.GONE);
                        payButton.setVisibility(View.VISIBLE);
                        bankList.setAdapter(adapter);
                    }
                });


                //Log.d("test", "result from server: " + result.toString());
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(PaymentActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                Toast.makeText(PaymentActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
            finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }




        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void initialize() {
        try {
            address1 = findViewById(R.id.address1);
            address2 = findViewById(R.id.address2);
            city = findViewById(R.id.city);
            state = findViewById(R.id.state);
            postCode = findViewById(R.id.postCode);
            country = findViewById(R.id.country);
            amountBox = findViewById(R.id.amountBox);
            store_id = "eazygamers";
            signature_key = "4d56cd6f4bdd242b3fc1edb60f8444c6";
            source = "Android";
            source_details = "source_details";
            user_imei = getUniqueIMEIId(this);
            device_type = getDeviceName();
            user_phone = DataHolder.getUserProfile().getMobileNumber();
            app_name = "BattleGround's Paid Room";
            try {
                PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                app_version = pInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            app_id = "com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom";
            opt_a = "N/A";
            opt_b = "N/A";
            opt_c = "N/A";
            opt_d = "N/A";
            bankList = findViewById(R.id.bankList);
            payButton = findViewById(R.id.payButton);
            methodPayment=findViewById(R.id.methodPayment);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if ( TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }
    @SuppressLint("HardwareIds")
    public static String getUniqueIMEIId(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return "";
            }
            String imei = telephonyManager.getDeviceId();
            Log.e("imei", "=" + imei);
            if (imei != null && !imei.isEmpty()) {
                return imei;
            } else {
                return android.os.Build.SERIAL;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "not_found";
    }

    @SuppressLint("StaticFieldLeak")
    private class Task extends AsyncTask<Void,Void,Void> {
        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... voids) {
            sendPayment();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(PaymentActivity.this,android.R.style.Theme_DeviceDefault_Dialog);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please Wait!");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }

    }


    @Override
    public void onBackPressed() {
        finish();
    }
}

