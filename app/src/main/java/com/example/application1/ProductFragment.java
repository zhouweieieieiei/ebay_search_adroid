package com.example.application1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProductFragment extends Fragment {
    public static final String Product_info = "object1";//product本身信息
    public static final String Product_detail = "object2";//product detail 信息
    private JSONObject info_Obj;
    private JSONObject detail_Obj;
    private Integer brandindex = -1;
    private LinearLayout Highlights;
    private LinearLayout Specifications;
    //used for image
    private Bitmap bitmap;
    private String url;

    private LinearLayout mGallery;
    private int[] mImgIds;
    private LayoutInflater mInflater;
    private HorizontalScrollView horizontalScrollView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){


        View view = inflater.inflate(R.layout.product_tab, container, false);
        Bundle bundle = getArguments();

        String info = bundle.getString(Product_info);
        Log.d("product_info", info);
        String detail = bundle.getString(Product_detail);
        Log.d("product_detail", detail);

        Drawable drawable = null;
        Drawable wrappedDrawable = null;
        Drawable mutableDrawable = null;
        drawable = ContextCompat.getDrawable(getContext(), R.drawable.information);
        wrappedDrawable = DrawableCompat.wrap(drawable);
        mutableDrawable = wrappedDrawable.mutate();
        DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(getContext(), R.color.Gray));
        ImageView highlightsicon = view.findViewById(R.id.highlightsicon);
        highlightsicon.setImageDrawable(drawable);

        drawable = ContextCompat.getDrawable(getContext(), R.drawable.wrench);
        wrappedDrawable = DrawableCompat.wrap(drawable);
        mutableDrawable = wrappedDrawable.mutate();
        DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(getContext(), R.color.Gray));
        ImageView specificationsicon = view.findViewById(R.id.specificationsicon);
        specificationsicon.setImageDrawable(drawable);

        Highlights = view.findViewById(R.id.Highlights);
        Specifications = view.findViewById(R.id.Specifications);

        try{
            info_Obj = new JSONObject(info);
            detail_Obj = new JSONObject(detail);

            mInflater = LayoutInflater.from(this.getContext());
            mGallery = view.findViewById(R.id.id_gallery);
            initView(view, detail_Obj.getJSONObject("Item").getJSONArray("PictureURL"));


            TextView title = view.findViewById(R.id.title);
            title.setText(info_Obj.getJSONArray("title").getString(0));
            TextView price = view.findViewById(R.id.price);
            price.setText("$"+detail_Obj.getJSONObject("Item").getJSONObject("CurrentPrice").getString("Value"));
            TextView shipping = view.findViewById(R.id.shipping);
            if(info_Obj.has("shippingInfo")&&info_Obj.getJSONArray("shippingInfo").getJSONObject(0).has("shippingServiceCost")&&info_Obj.getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("shippingServiceCost").getJSONObject(0).has("__value__")){
                String shippingprice = info_Obj.getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("shippingServiceCost").getJSONObject(0).getString("__value__");
                if(Double.parseDouble(shippingprice)>0) shipping.setText(" With $"+shippingprice+" Shipping");
                else shipping.setText(" With Free Shipping");
            }

            TextView subtitle = view.findViewById(R.id.subtitle);
            LinearLayout subtitle_line = view.findViewById(R.id.subtitle_line);
            if(detail_Obj.getJSONObject("Item").has("Subtitle")) {
                subtitle.setText(detail_Obj.getJSONObject("Item").getString("Subtitle"));
                subtitle_line.setVisibility(View.VISIBLE);
                Highlights.setVisibility(View.VISIBLE);
            }

            TextView price2 = view.findViewById(R.id.price2);
            LinearLayout price_line = view.findViewById(R.id.price_line);
            if(detail_Obj.getJSONObject("Item").has("CurrentPrice")&&detail_Obj.getJSONObject("Item").getJSONObject("CurrentPrice").has("Value")) {
                price2.setText("$"+detail_Obj.getJSONObject("Item").getJSONObject("CurrentPrice").getString("Value"));
                price_line.setVisibility(View.VISIBLE);
                Highlights.setVisibility(View.VISIBLE);
            }
            TextView brand = view.findViewById(R.id.brand);
            LinearLayout brand_line = view.findViewById(R.id.brand_line);
            TextView spec_info = view.findViewById(R.id.info);
            LinearLayout Specifications = view.findViewById(R.id.Specifications);
            if(detail_Obj.getJSONObject("Item").has("ItemSpecifics")&&detail_Obj.getJSONObject("Item").getJSONObject("ItemSpecifics").has("NameValueList")&&detail_Obj.getJSONObject("Item").getJSONObject("ItemSpecifics").getJSONArray("NameValueList").length()>0) {
                Specifications.setVisibility(View.VISIBLE);
                for(Integer i = 0; i<detail_Obj.getJSONObject("Item").getJSONObject("ItemSpecifics").getJSONArray("NameValueList").length(); ++i){
                    Log.d("test", detail_Obj.getJSONObject("Item").getJSONObject("ItemSpecifics").getJSONArray("NameValueList").getJSONObject(i).getString("Value"));
                    if(detail_Obj.getJSONObject("Item").getJSONObject("ItemSpecifics").getJSONArray("NameValueList").getJSONObject(i).getString("Name").equals("Brand")){
                        brandindex = i;
                        brand.setText(detail_Obj.getJSONObject("Item").getJSONObject("ItemSpecifics").getJSONArray("NameValueList").getJSONObject(i).getJSONArray("Value").getString(0));
                        brand_line.setVisibility(View.VISIBLE);
                        Highlights.setVisibility(View.VISIBLE);
                        break;
                    }
                }
                String str = "";
                String temp = "";
                if(brandindex > 0){
                    temp=detail_Obj.getJSONObject("Item").getJSONObject("ItemSpecifics").getJSONArray("NameValueList").getJSONObject(brandindex).getJSONArray("Value").getString(0);
                    temp = temp.substring(0,1).toUpperCase()+temp.substring(1);
                    str+='\u2022'+temp+'\n';
                }

                for(Integer i = 0; i<detail_Obj.getJSONObject("Item").getJSONObject("ItemSpecifics").getJSONArray("NameValueList").length(); ++i){
                    if(i == brandindex) continue;
                    temp=detail_Obj.getJSONObject("Item").getJSONObject("ItemSpecifics").getJSONArray("NameValueList").getJSONObject(i).getJSONArray("Value").getString(0);
                    temp = temp.substring(0,1).toUpperCase()+temp.substring(1);
                    str+='\u2022'+temp+'\n';
                }
                spec_info.setText(str);

            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    private void initView(View view, JSONArray imgs) {

        mGallery = view.findViewById(R.id.id_gallery);
        for (int i = 0; i < imgs.length(); i++) {

            View view2 = mInflater.inflate(R.layout.gallery_item,
                    mGallery, false);
            ImageView img = view2
                    .findViewById(R.id.id_index_gallery_item_image);
            try{
                url = imgs.getString(i);
                GetThread t = new GetThread();
                t.start();
                t.join();
            }catch (Exception e) {
                e.printStackTrace();
            }
            img.setImageBitmap(bitmap);
            //img.setImageResource(mImgIds[i]);
            mGallery.addView(view2);
        }
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
                bitmap = imageScale(bitmap, 400, 400);
                //关闭数据流
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
}
