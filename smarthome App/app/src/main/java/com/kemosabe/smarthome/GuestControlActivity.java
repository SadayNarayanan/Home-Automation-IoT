package com.kemosabe.smarthome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.kemosabe.smarthome.myfragments.GuestRegistrationFragment;
import com.kemosabe.smarthome.ui.home.HomeFragment;

public class GuestControlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_control);


    }
}
