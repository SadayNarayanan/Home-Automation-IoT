package com.kemosabe.smarthome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class OwnerRegistrationActivity extends AppCompatActivity {


    EditText name, phone, email, password;
    String Sname, Sphone, Semail, Spassword;
    Button registerbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_registeration);


        registerbtn = findViewById(R.id.btnreg);


        name = findViewById(R.id.edtname);
        phone = findViewById(R.id.edtphone);
        email = findViewById(R.id.edtemail);
        password = findViewById(R.id.edtpswrd);


        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Sname = name.getText().toString();
                Sphone = phone.getText().toString();
                Semail = email.getText().toString();
                Spassword = password.getText().toString().trim();


                if (Sname.isEmpty()) {
                    name.setError("Enter Name");
                    name.requestFocus();
                } else if (Sphone.isEmpty()) {
                    phone.setError("Enter Name");
                    phone.requestFocus();
                } else if (Semail.isEmpty()) {
                    email.setError("Enter Name");
                    email.requestFocus();
                } else if (Spassword.isEmpty()) {
                    password.setError("Enter Name");
                    password.requestFocus();
                } else {
                    RegisterOwner();
                }
            }
        });
    }


    public void RegisterOwner() {

        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, Utility.serverURL.trim(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("******", response);

                if (response.trim().equals("success")) {

                    Toast.makeText(OwnerRegistrationActivity.this, "Registered Owner", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));

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

                map.put("key", "owner_reg");
                map.put("name", Sname);
                map.put("phone", Sphone);
                map.put("email", Semail);
                map.put("pass", Spassword);


                return map;
            }
        };
        queue.add(request);
    }

}
