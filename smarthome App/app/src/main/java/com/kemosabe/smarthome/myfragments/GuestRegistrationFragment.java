package com.kemosabe.smarthome.myfragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kemosabe.smarthome.LoginActivity;
import com.kemosabe.smarthome.OwnerHomeActivity;
import com.kemosabe.smarthome.R;
import com.kemosabe.smarthome.Utility;
import com.kemosabe.smarthome.ui.family.FamilyFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class GuestRegistrationFragment extends Fragment {


    private EditText edtname, edtcontact, edtemail, edtpassword;
    private String Sedtname, Sedtcontact, Sedtemail, Sedtpassword, Scategory;
    private Button regbtn;
    private RadioGroup category;
    private RadioButton temprdio;
    private ImageView ImgPickBtn;
    private Bitmap selectedimg;
    private String imageString;
    String prefid;

    public GuestRegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_guest_registration, container, false);


        edtname = root.findViewById(R.id.name);
        edtcontact = root.findViewById(R.id.contact);
        edtemail = root.findViewById(R.id.email);
        edtpassword = root.findViewById(R.id.password);
        regbtn = root.findViewById(R.id.regbtn);
        category = root.findViewById(R.id.category);
        ImgPickBtn = root.findViewById(R.id.pickimg);


        SharedPreferences prefs = getActivity().getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
        prefid = prefs.getString("reg_id", "No id defined");//"No name defined" is the default value.


        category.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                temprdio = category.findViewById(i);
                Scategory = temprdio.getText().toString().trim().toUpperCase();
//                Toast.makeText(getContext(), "" + Scategory, Toast.LENGTH_SHORT).show();
            }
        });


        ImgPickBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Sedtname = edtname.getText().toString().trim();
                Sedtcontact = edtcontact.getText().toString().trim();
                Sedtemail = edtemail.getText().toString().trim();
                Sedtpassword = edtpassword.getText().toString().trim();

                if (Sedtname.isEmpty()) {
                    edtname.requestFocus();
                    edtname.setError("Please enter name");
                } else if (Sedtcontact.isEmpty()) {
                    edtcontact.requestFocus();
                    edtcontact.setError("Please enter Contact");
                } else if (Sedtemail.isEmpty()) {
                    edtemail.requestFocus();
                    edtemail.setError("Enter Email");

                } else if (Scategory.isEmpty()) {
                    Toast.makeText(getContext(), "choose Relation", Toast.LENGTH_SHORT).show();

                } else if (Sedtpassword.isEmpty()) {
                    edtpassword.requestFocus();
                    edtpassword.setError("Enter Password");

                } else {

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    Bitmap bitmap = selectedimg;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                    byte[] imageBytes = baos.toByteArray();
                    imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);


                    volley_registration();
                }


            }
        });
        return root;
    }

    public void volley_registration() {


        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.serverURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("******", response);

                if (response.trim().equals("Registered Account")) {

                    Toast.makeText(getContext(), "Registration Success", Toast.LENGTH_SHORT).show();
                    try {
                        String message = "Hi " + Sedtname + ",\nYou Have been granted permission to use SmartHome as " + Scategory + ".\nThe credentials to login are given below.\nlogin number :" + Sedtcontact.trim() + "\npassword : " + Sedtpassword;
//                        SmsManager smsManager = SmsManager.getDefault();
//                        smsManager.sendTextMessage(Sedtcontact.trim(), null, message, null, null);
                        Log.d("******", "CONTENTY :" + message);


                        Uri uri = Uri.parse("smsto:" + Sedtcontact.trim());
                        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                        it.putExtra("sms_body", message);
                        startActivity(it);

                    } catch (Exception ex) {
                        Log.d("******", "ERRORY :" + ex);

                    }


//                    FamilyFragment nextFrag = new FamilyFragment();
//                    getFragmentManager().beginTransaction()
//                            .replace(R.id.nav_host_fragment, nextFrag, "findThisFragment")
////                        .addToBackStack(null)
//                            .disallowAddToBackStack()
//                            .commit();

                } else {

                    Toast.makeText(getContext(), response.trim().toUpperCase(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), "my Error :" + error, Toast.LENGTH_LONG).show();
                Log.i("My Error", "" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> map = new HashMap<String, String>();
//                SharedPreferences sp=getSharedPreferences("booking_info", Context.MODE_PRIVATE);
                map.put("key", "register_guest");
                map.put("name", Sedtname.trim());
                map.put("contact", Sedtcontact);
                map.put("email", Sedtemail);
                map.put("category", Scategory.toUpperCase());
                map.put("password", Sedtpassword);
                map.put("status", "approved");
                map.put("image", imageString.trim());
                map.put("userid", prefid.trim());

                return map;
            }
        };
        queue.add(request);
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                if (requestCode == 1) {
                    File f = new File(Environment.getExternalStorageDirectory().toString());
                    Log.w("******************", "PATH :" + f);

                    for (File temp : f.listFiles()) {
                        if (temp.getName().equals("temp.jpg")) {
                            f = temp;
                            break;
                        }
                    }
                    try {
                        Bitmap bitmap;
                        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                        bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);

//                    ImgPickBtn.setImageBitmap(bitmap);

//                    ImgPickBtn.setImageBitmap(getCircularBitmapWithWhiteBorder(bitmap, 1));

                        try {
                            ImgPickBtn.setImageBitmap(getCircularBitmapWithWhiteBorder(modifyOrientation(bitmap, f.getAbsolutePath()), 1));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        selectedimg = bitmap;
                        String path = android.os.Environment
                                .getExternalStorageDirectory()
                                + File.separator
                                + "Phoenix" + File.separator + "default";
                        f.delete();
                        OutputStream outFile = null;
                        File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                        try {
                            outFile = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outFile);
                            outFile.flush();
                            outFile.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (requestCode == 2) {
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    c.close();
                    Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                    Log.w("******************", picturePath + "");


//                ImgPickBtn.setImageBitmap(thumbnail);
//                ImgPickBtn.setImageBitmap(getCircularBitmapWithWhiteBorder(thumbnail, 1));
                    try {
                        ImgPickBtn.setImageBitmap(getCircularBitmapWithWhiteBorder(modifyOrientation(thumbnail, picturePath), 1));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    selectedimg = thumbnail;
                }
            } catch (Exception exy) {
                Toast.makeText(getContext(), "ERRORY " + exy, Toast.LENGTH_SHORT).show();
                Log.w("******************", exy + "");

            }
        }
    }

    public static Bitmap getCircularBitmapWithWhiteBorder(Bitmap bitmap, int borderWidth) {


        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }

        final int width = bitmap.getWidth() + borderWidth;
        final int height = bitmap.getHeight() + borderWidth;

        Bitmap canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        Canvas canvas = new Canvas(canvasBitmap);
        float radius = width > height ? ((float) height) / 2f : ((float) width) / 2f;
        canvas.drawCircle(width / 2, height / 2, radius, paint);
        paint.setShader(null);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(borderWidth);
        canvas.drawCircle(width / 2, height / 2, radius - borderWidth / 2, paint);
        return canvasBitmap;
    }

    //******************************************************************************************************************************************************8

    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
