package com.kemosabe.smarthome.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.media.ExifInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kemosabe.smarthome.Base64;
import com.kemosabe.smarthome.R;
import com.kemosabe.smarthome.pojos.ViewGuestPOJO;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Adrian Seban on 3/16/2020.
 */
public class ViewGuestAdapter extends ArrayAdapter<ViewGuestPOJO> {


    Activity context;
    ArrayList<ViewGuestPOJO> myguestlist;

    public ViewGuestAdapter(@NonNull Activity context, ArrayList<ViewGuestPOJO> guest_list) {

        super(context, R.layout.custom_row_viewguest, guest_list);
        this.context = context;


        this.myguestlist = guest_list;
        // TODO Auto-generated constructor stub
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_row_viewguest, null, true);


        TextView guest_name = (TextView) view.findViewById(R.id.g_name);
        TextView guest_email = (TextView) view.findViewById(R.id.g_email);
        TextView guest_contact = (TextView) view.findViewById(R.id.g_contact);
        TextView guest_relation = (TextView) view.findViewById(R.id.g_relation);
        ImageView guest_image = (ImageView) view.findViewById(R.id.g_image);


//        guest_image.getLayoutParams().height = 200;
//        guest_image.getLayoutParams().width = 200;
//        guest_image.setScaleType(ImageView.ScaleType.FIT_XY);

        guest_name.setText(myguestlist.get(position).getName());
        guest_email.setText(myguestlist.get(position).getEmail());
        guest_contact.setText(myguestlist.get(position).getContact());
        guest_relation.setText(myguestlist.get(position).getRelation());

        String base = myguestlist.get(position).getImage();


        try {
            byte[] imageAsBytes = Base64.decode(base.getBytes());
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            guest_image.setImageBitmap(getCircularBitmapWithWhiteBorder(bitmap, 2));

//            guest_image.setImageBitmap(getCircularBitmapWithWhiteBorder(modifyOrientation(bitmap, null), 2));

        } catch (IOException e) {

            e.printStackTrace();
        }


        return view;
//        return super.getView(position, convertView, parent);


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
