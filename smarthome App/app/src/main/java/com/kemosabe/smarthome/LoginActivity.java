package com.kemosabe.smarthome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    TextView regtext;
    EditText logphone, logpass;
    String Slogphone, Slogpass;
    Button loginbtn;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {

                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_SMS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.FOREGROUND_SERVICE,
                Manifest.permission.CALL_PHONE
        };

        if (!hasPermissions(this, PERMISSIONS)) {

            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        regtext = findViewById(R.id.regtxt);
        logphone = findViewById(R.id.logphone);
        logpass = findViewById(R.id.logpass);
        loginbtn = findViewById(R.id.btnlogin);


        regtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(getApplicationContext(), OwnerRegistrationActivity.class));
            }
        });


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Slogphone = logphone.getText().toString();
                Slogpass = logpass.getText().toString().trim();


                if (Slogphone.isEmpty()) {
                    logphone.setError("Enter Registered number");
                    logpass.requestFocus();
                } else if (Slogpass.isEmpty()) {
                    logpass.setError("Enter Password");
                    logpass.requestFocus();

                } else {

                    Log.d("***", "sending volley");


                    Loginvolley();

                }

            }
        });

    }

    public void Loginvolley() {

        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, Utility.serverURL.trim(), new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                Log.d("******", response);

                if (!response.trim().equals("failed")) {

                    String[] respoarray = response.split("#");

                    SharedPreferences.Editor editor = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
                    editor.putString("reg_id", respoarray[0].trim());
                    editor.putString("usertype", respoarray[1].trim());
                    editor.putString("access", respoarray[2].trim());
                    editor.apply();

                    Log.d("******", respoarray[1]);

//                    Toast.makeText(LoginActivity.this, "" + response, Toast.LENGTH_SHORT).show();
//
                    if (respoarray[1].trim().equals("owner")) {
                        finishAffinity();
                        startActivity(new Intent(getApplicationContext(), OwnerHomeActivity.class));

                    } else if (respoarray[1].trim().equals("TEMPORARY")) {

                        finishAffinity();
                        startActivity(new Intent(getApplicationContext(), GuestControlActivity.class));

                    }
//                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "my Error :" + error, Toast.LENGTH_LONG).show();
                Log.i("My Error", "" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> map = new HashMap<String, String>();

                map.put("key", "login");
                map.put("phone", Slogphone.trim());
                map.put("pass", Slogpass.trim());


                return map;
            }
        };
        queue.add(request);
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
            finishAffinity();
            System.exit(0);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
