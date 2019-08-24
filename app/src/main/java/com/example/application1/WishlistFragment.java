package com.example.application1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Context;
import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONObject;
import android.widget.ImageView;
import android.support.v7.widget.CardView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class WishlistFragment extends Fragment {
    private RecyclerView mRecyclerView;
    static public MyAdapter mAdapter;
    private JSONObject resObj;
    static public ArrayList<String> wishlistitemIdarraylist = new ArrayList<String>();
    static public ArrayList<String> wishlistitemImageurlarraylist = new ArrayList<String>();
    static public ArrayList<String> wishlistitemTitlearraylist = new ArrayList<String>();
    static public ArrayList<String> wishlistitemZiparraylist = new ArrayList<String>();
    static public ArrayList<String> wishlistitemShippingarraylist = new ArrayList<String>();
    static public ArrayList<String> wishlistitemConditionarraylist = new ArrayList<String>();
    static public ArrayList<String> wishlistitemPricearraylist = new ArrayList<String>();
    static public ArrayList<String> wishlistitemarraylist = new ArrayList<String>();
    static public String Money;
    static public TextView textnum;
    static public TextView textprice;
    static public TextView nowishes;
    private Bitmap bitmap;
    private String url;
    private String urlStr;
    private String threadres;
    public static final String PREFS_NAME = "MyPrefsFile";
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.wishlist_tab, container, false);
        textnum = view.findViewById(R.id.wishnum);
        textprice = view.findViewById(R.id.money);
        nowishes = view.findViewById(R.id.nowish);
        try{
            settings = getContext().getSharedPreferences(PREFS_NAME, 0);
            editor = settings.edit();
            /*
            editor.putString("wishlistitemId", "");
            editor.putString("wishlistitemImageurl", "");
            editor.putString("wishlistitemTitle", "");
            editor.putString("wishlistitemZip", "");
            editor.putString("wishlistitemShipping", "");
            editor.putString("wishlistitemCondition", "");
            editor.putString("wishlistitemPrice", "");
            editor.putString("wishlistitem", "");
            editor.putString("Money", "0.00");
            editor.commit();*/
            String wishlistitemId = settings.getString("wishlistitemId", "");
            String wishlistitemImageurl = settings.getString("wishlistitemImageurl", "");
            String wishlistitemTitle = settings.getString("wishlistitemTitle", "");
            String wishlistitemZip = settings.getString("wishlistitemZip", "");
            String wishlistitemShipping = settings.getString("wishlistitemShipping", "");
            String wishlistitemCondition = settings.getString("wishlistitemCondition", "");
            String wishlistitemPrice = settings.getString("wishlistitemPrice", "");
            String wishlistitem = settings.getString("wishlistitem", "");
            Money = settings.getString("Money", "0.00");

            TextView money = view.findViewById(R.id.money);
            money.setText("$ "+Money);
            if(wishlistitemId!="") {
                JSONArray wishlistitemIdJSONarray = new JSONArray(wishlistitemId);
                JSONArray wishlistitemImageurlJSONarray = new JSONArray(wishlistitemImageurl);
                JSONArray wishlistitemTitleJSONarray = new JSONArray(wishlistitemTitle);
                JSONArray wishlistitemZipJSONarray = new JSONArray(wishlistitemZip);
                JSONArray wishlistitemShippingJSONarray = new JSONArray(wishlistitemShipping);
                JSONArray wishlistitemConditionJSONarray = new JSONArray(wishlistitemCondition);
                JSONArray wishlistitemPriceJSONarray = new JSONArray(wishlistitemPrice);
                JSONArray wishlistitemJSONarray = new JSONArray(wishlistitem);
                TextView number = view.findViewById(R.id.wishnum);
                String n = wishlistitemJSONarray.length()+"";
                Log.d("num", n);
                number.setText(n);
                if(wishlistitemJSONarray.length()>0) nowishes.setVisibility(View.GONE);
                for(int i=0;i<wishlistitemIdJSONarray.length();i++){
                    wishlistitemIdarraylist.add(wishlistitemIdJSONarray.getString(i));
                    wishlistitemImageurlarraylist.add(wishlistitemImageurlJSONarray.getString(i));
                    wishlistitemTitlearraylist.add(wishlistitemTitleJSONarray.getString(i));
                    wishlistitemZiparraylist.add(wishlistitemZipJSONarray.getString(i));
                    wishlistitemShippingarraylist.add(wishlistitemShippingJSONarray.getString(i));
                    wishlistitemConditionarraylist.add(wishlistitemConditionJSONarray.getString(i));
                    wishlistitemPricearraylist.add(wishlistitemPriceJSONarray.getString(i));
                    wishlistitemarraylist.add(wishlistitemJSONarray.getString(i));
                }
            }


        }catch (Exception e) {
            e.printStackTrace();
        }
        mRecyclerView = view.findViewById(R.id.id_recyclerview);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        mRecyclerView.setAdapter(mAdapter = new MyAdapter(wishlistitemIdarraylist));

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, String viewName, int position) {
                try{
                    switch (viewName){
                        case "wishbutton":
                            Log.d("wishlist_delete", "position:"+position+" wishlen:"+wishlistitemIdarraylist.size());
                            Toast.makeText(getContext(), Uri.decode(wishlistitemTitlearraylist.get(position)) + " was removed from wish list", Toast.LENGTH_SHORT).show();
                            Double m = Double.parseDouble(Money);
                            String s = Uri.decode(wishlistitemPricearraylist.get(position));
                            if(!s.equals("N/A")) m = m-Double.parseDouble(s.substring(1));
                            Money = m.toString();
                            wishlistitemIdarraylist.remove(position);
                            wishlistitemImageurlarraylist.remove(position);
                            wishlistitemTitlearraylist.remove(position);
                            wishlistitemZiparraylist.remove(position);
                            wishlistitemShippingarraylist.remove(position);
                            wishlistitemConditionarraylist.remove(position);
                            wishlistitemPricearraylist.remove(position);
                            wishlistitemarraylist.remove(position);

                            editor.putString("wishlistitemId", wishlistitemIdarraylist.toString());
                            editor.putString("wishlistitemImageurl", wishlistitemImageurlarraylist.toString());
                            editor.putString("wishlistitemTitle", wishlistitemTitlearraylist.toString());
                            editor.putString("wishlistitemZip", wishlistitemZiparraylist.toString());
                            editor.putString("wishlistitemShipping", wishlistitemShippingarraylist.toString());
                            editor.putString("wishlistitemCondition", wishlistitemConditionarraylist.toString());
                            editor.putString("wishlistitemPrice", wishlistitemPricearraylist.toString());
                            editor.putString("wishlistitem", wishlistitemarraylist.toString());
                            editor.putString("Money", Money);
                            editor.commit();

                            String n = wishlistitemIdarraylist.size()+"";
                            Log.d("num", n);
                            textnum.setText(n);

                            textprice.setText("$ "+Money);
                            if(wishlistitemIdarraylist.size()>0) nowishes.setVisibility(View.GONE);
                            else nowishes.setVisibility(View.VISIBLE);
                            mAdapter.notifyDataSetChanged();
                            //mAdapter.notifyItemRemoved(position);
                            break;
                        default:
                            urlStr="http://hw8571serverside-env.s84muegm5z.us-west-1.elasticbeanstalk.com/detail?itemID=";
                            urlStr+=wishlistitemIdarraylist.get(position);
                            DetailThread t = new DetailThread();
                            t.start();
                            t.join();
                            Intent intent = new Intent("com.example.activitytest.ACTION_DETAIL");
                            intent.putExtra("detail_res", threadres);

                            urlStr="http://hw8571serverside-env.s84muegm5z.us-west-1.elasticbeanstalk.com/photos?title=";
                            urlStr+=wishlistitemTitlearraylist.get(position);
                            t = new DetailThread();
                            t.start();
                            t.join();
                            intent.putExtra("detail_photos", threadres);

                            urlStr="http://hw8571serverside-env.s84muegm5z.us-west-1.elasticbeanstalk.com/similar?itemID=";
                            urlStr+=wishlistitemIdarraylist.get(position);
                            t = new DetailThread();
                            t.start();
                            t.join();
                            intent.putExtra("detail_similar", threadres);
                            intent.putExtra("detail_item", wishlistitemarraylist.get(position));
                            startActivity(intent);
                            break;
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, String viewName, int position);
    }
    private OnItemClickListener mOnItemClickListener;


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements View.OnClickListener{
        private ArrayList<String> dataList;
        public MyAdapter(ArrayList<String> list) {
            dataList = list;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            MyAdapter.MyViewHolder holder = new MyAdapter.MyViewHolder(LayoutInflater.from(
                    getContext()).inflate(R.layout.item, parent,
                    false));
            return holder;
        }



        @Override
        public void onBindViewHolder(final MyAdapter.MyViewHolder holder, final int position) {
            MyAdapter.MyViewHolder itemHolder = (MyAdapter.MyViewHolder) holder;
            itemHolder.itemView.setTag(position);
            itemHolder.wishbutton.setTag(position);

            try{
                ImageView imageView = holder.tv.findViewById(R.id.image);
                url = Uri.decode(wishlistitemImageurlarraylist.get(position));
                //得到可用的图片

                try{
                    GetThread t = new GetThread();
                    t.start();
                    t.join();
                }catch (Exception e) {
                    e.printStackTrace();
                }

                imageView.setImageBitmap(bitmap);

                TextView title = holder.tv.findViewById(R.id.title);
                title.setText(Uri.decode(wishlistitemTitlearraylist.get(position)));
                TextView zip = holder.tv.findViewById(R.id.zip);
                zip.setText(Uri.decode(wishlistitemZiparraylist.get(position)));
                TextView shippingcost = holder.tv.findViewById(R.id.shippingcost);
                shippingcost.setText(Uri.decode(wishlistitemShippingarraylist.get(position)));
                ImageView wishbutton = holder.tv.findViewById(R.id.wishbutton);
                Drawable drawable = null;
                Drawable wrappedDrawable = null;
                Drawable mutableDrawable = null;
                drawable = ContextCompat.getDrawable(getContext(), R.drawable.cart_remove);
                wrappedDrawable = DrawableCompat.wrap(drawable);
                mutableDrawable = wrappedDrawable.mutate();
                DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(getContext(), R.color.Orange));
                wishbutton.setImageDrawable(drawable);
                TextView condition = holder.tv.findViewById(R.id.condition);
                condition.setText(Uri.decode(wishlistitemConditionarraylist.get(position)));
                TextView price = holder.tv.findViewById(R.id.price);
                price.setText(Uri.decode(wishlistitemPricearraylist.get(position)));
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View v) {
            //注意这里使用getTag方法获取数据
            int position = (int) v.getTag();
            if (mOnItemClickListener != null) {
                switch (v.getId()){
                    case R.id.wishbutton:
                        mOnItemClickListener.onItemClick(v, "wishbutton", position);
                        break;
                    default:
                        mOnItemClickListener.onItemClick(v, "wholeview", position);
                        break;
                }
            }
        }

        @Override
        public int getItemCount()
        {   Integer temp = 0;
            try{
                temp = wishlistitemIdarraylist.size();
                return temp;
            }catch (Exception e) {
                e.printStackTrace();
            }
            return temp;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            private ImageView wishbutton;
            CardView tv;

            public MyViewHolder(View view)
            {
                super(view);
                tv = view.findViewById(R.id.id_num);
                view.setOnClickListener(MyAdapter.this);
                wishbutton = view.findViewById(R.id.wishbutton);
                wishbutton.setOnClickListener(MyAdapter.this);
            }
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

    class DetailThread extends Thread{
        public void run(){
            HttpURLConnection conn=null;
            Log.d("main", urlStr);
            InputStream is = null;
            String result = "";
            try {
                URL url = new URL(urlStr);
                conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("GET");

                if(conn.getResponseCode()==200){
                    is = conn.getInputStream();
                    InputStreamReader isreader = new InputStreamReader(is);
                    BufferedReader bufferReader = new BufferedReader(isreader);
                    String responseline  = "";
                    while((responseline = bufferReader.readLine()) != null){
                        result += responseline + "\n";
                    }
                    threadres = result;
                    Log.d("main", threadres);
                }
                else Log.d("main", "connect failed!!!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
