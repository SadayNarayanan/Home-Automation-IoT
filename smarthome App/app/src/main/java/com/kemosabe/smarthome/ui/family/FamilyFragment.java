package com.kemosabe.smarthome.ui.family;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.kemosabe.smarthome.R;
import com.kemosabe.smarthome.myfragments.GuestRegistrationFragment;
import com.kemosabe.smarthome.myfragments.ViewGuestFragment;

public class FamilyFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    private CardView adduserbtn, viewuserbtn;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel = ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_family, container, false);

        adduserbtn = root.findViewById(R.id.addguestbtn);
        viewuserbtn = root.findViewById(R.id.viewguestbtn);


        adduserbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                GuestRegistrationFragment nextFrag = new GuestRegistrationFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, nextFrag, "findThisFragment")
                        .addToBackStack(null)
//                        .disallowAddToBackStack()
                        .commit();
                //Option 2
//                Fragment myfrag = new GuestRegistrationFragment();
//
//                getFragmentManager().beginTransaction()
//                        .replace(R.id.nav_host_fragment, myfrag)
//                        .commit();

            }
        });


        viewuserbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ViewGuestFragment nextFrag = new ViewGuestFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, nextFrag, "findThisFragment")
//                        .addToBackStack(null)
                        .disallowAddToBackStack()
                        .commit();
            }
        });

        return root;
    }
}
