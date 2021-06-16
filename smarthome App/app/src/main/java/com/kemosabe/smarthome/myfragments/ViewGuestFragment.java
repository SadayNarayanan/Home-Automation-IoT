package com.kemosabe.smarthome.myfragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.kemosabe.smarthome.LoginActivity;
import com.kemosabe.smarthome.OwnerHomeActivity;
import com.kemosabe.smarthome.R;
import com.kemosabe.smarthome.Utility;
import com.kemosabe.smarthome.adapters.ViewGuestAdapter;
import com.kemosabe.smarthome.pojos.ViewGuestPOJO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewGuestFragment extends Fragment {

    public ViewGuestFragment() {
        // Required empty public constructor
    }

    ListView guestlistv;
    String prefid;
    ArrayList<ViewGuestPOJO> arrayList;
    ViewGuestAdapter adpater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_view_guest, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
        prefid = prefs.getString("reg_id", "No id defined");//"No name defined" is the default value.

        guestlistv = root.findViewById(R.id.viewguestlist);


        arrayList = new ArrayList<ViewGuestPOJO>();
        adpater = new ViewGuestAdapter((Activity) getContext(), arrayList);


        volley_viewguestlist();

        return root;

    }

    public void volley_viewguestlist() {

        final ProgressDialog pd;
        pd = new ProgressDialog(getActivity());
        pd.setCancelable(false);

        pd.setTitle("Getting Details..!");
        pd.show();
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest request = new StringRequest(Request.Method.POST, Utility.serverURL.trim(), new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {


                Log.d("******", response);

                if (!response.trim().equals("NOGUESTS")) {

                    Gson gson = new Gson();
                    ViewGuestPOJO[] beans = gson.fromJson(response, ViewGuestPOJO[].class);

                    for (ViewGuestPOJO ListBean : beans) {
                        ViewGuestPOJO pro = new ViewGuestPOJO();

                        pro.setGuestid(ListBean.getGuestid());
                        pro.setName(ListBean.getName());
                        pro.setContact(ListBean.getContact());
                        pro.setEmail(ListBean.getEmail());
                        pro.setGuestid(ListBean.getGuestid());
                        pro.setImage(ListBean.getImage());
                        pro.setRelation(ListBean.getRelation());

                        pd.dismiss();

                        arrayList.add(pro);
                        guestlistv.setAdapter(adpater);

                        guestlistv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                            }
                        });

                    }
                } else {
                    Toast.makeText(getContext(), "No Guests Found", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss(); // write it there

                Toast.makeText(getContext(), "my Error :" + error, Toast.LENGTH_LONG).show();
                Log.i("My Error", "" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> map = new HashMap<String, String>();

                map.put("key", "viewownerguests");
                map.put("ownerid", prefid.trim());


                return map;
            }
        };
        queue.add(request);
    }


}



