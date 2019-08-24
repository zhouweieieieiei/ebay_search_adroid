package com.example.application1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.view.WindowManager;
import android.content.Context;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PhotosFragment extends Fragment {
    public static final String Product_photos = "object";
    private JSONObject photos_Obj;
    private Bitmap bitmap;
    private String url;
    private Integer width;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.photos_tab, container, false);
        Bundle bundle = getArguments();
        String photos = bundle.getString(Product_photos);
        Log.d("photos", photos);

        ImageView imageView = view.findViewById(R.id.img1);
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        imageView.measure(w, h);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();

        try{
            photos_Obj = new JSONObject(photos);
            if(!photos_Obj.has("items")||photos_Obj.getJSONArray("items").length() == 0){
                view.findViewById(R.id.Nophotomessage).setVisibility(View.VISIBLE);
            }
            else{
                Integer img_num = photos_Obj.getJSONArray("items").length();
                Log.d("photonum", img_num+"");
                ImageView img;
                if(img_num>0){
                    url = photos_Obj.getJSONArray("items").getJSONObject(0).getString("link");
                    GetThread t = new GetThread();
                    t.start();
                    t.join();
                    img = view.findViewById(R.id.img1);
                    img.setImageBitmap(bitmap);
                }

                if(img_num>1){
                    url = photos_Obj.getJSONArray("items").getJSONObject(1).getString("link");
                    GetThread t = new GetThread();
                    t.start();
                    t.join();
                    img = view.findViewById(R.id.img2);
                    img.setImageBitmap(bitmap);
                }

                if(img_num>2){
                    url = photos_Obj.getJSONArray("items").getJSONObject(2).getString("link");
                    GetThread t = new GetThread();
                    t.start();
                    t.join();
                    img = view.findViewById(R.id.img3);
                    img.setImageBitmap(bitmap);
                }

                if(img_num>3){
                    url = photos_Obj.getJSONArray("items").getJSONObject(3).getString("link");
                    GetThread t = new GetThread();
                    t.start();
                    t.join();
                    img = view.findViewById(R.id.img4);
                    img.setImageBitmap(bitmap);
                }

                if(img_num>4){
                    url = photos_Obj.getJSONArray("items").getJSONObject(4).getString("link");
                    GetThread t = new GetThread();
                    t.start();
                    t.join();
                    img = view.findViewById(R.id.img5);
                    img.setImageBitmap(bitmap);
                }

                if(img_num>5){
                    url = photos_Obj.getJSONArray("items").getJSONObject(5).getString("link");
                    GetThread t = new GetThread();
                    t.start();
                    t.join();
                    img = view.findViewById(R.id.img6);
                    img.setImageBitmap(bitmap);
                }

                if(img_num>6){
                    url = photos_Obj.getJSONArray("items").getJSONObject(6).getString("link");
                    GetThread t = new GetThread();
                    t.start();
                    t.join();
                    img = view.findViewById(R.id.img7);
                    img.setImageBitmap(bitmap);
                }

                if(img_num>7){
                    url = photos_Obj.getJSONArray("items").getJSONObject(7).getString("link");
                    GetThread t = new GetThread();
                    t.start();
                    t.join();
                    img = view.findViewById(R.id.img8);
                    img.setImageBitmap(bitmap);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public static Bitmap imageScale(Bitmap bitmap, int dst_w, int dst_h) {
        int src_w = bitmap.getWidth();
        int src_h = bitmap.getHeight();
        float scale_w = ((float) dst_w) / src_w;
        float scale_h = ((float) dst_h) / src_h;
        Matrix matrix = new Matrix();
        matrix.postScale(scale_w, scale_h);
        Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, src_w, src_h, matrix,
                true);
        return dstbmp;
    }

    class GetThread extends Thread {
        public void run() {

            URL myFileURL;
            try{
                myFileURL = new URL(url);
                //获得连接
                HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
                //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
                conn.setConnectTimeout(6000);
                //连接设置获得数据流
                conn.setDoInput(true);
                //不使用缓存
                conn.setUseCaches(false);
                //这句可有可无，没有影响
                conn.connect();
                //得到数据流
                InputStream is = conn.getInputStream();
                //解析得到图片

                bitmap = BitmapFactory.decodeStream(is);
                int src_w = bitmap.getWidth();
                int src_h = bitmap.getHeight();
                Log.d("width", width+"");
                bitmap = imageScale(bitmap, width, src_h*width/src_w);
                //关闭数据流
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
