package com.example.application1;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.GridLayoutManager;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v7.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.graphics.Matrix;
import android.widget.Toast;
import android.net.Uri;
import android.text.Layout;

import org.json.JSONObject;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.net.URL;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.TimerTask;
import java.util.Timer;


public class SecondActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private JSONObject resObj;
    private TextView noresults;
    private ArrayList<String> wishlistitemIdarraylist = new ArrayList<String>();
    private ArrayList<String> wishlistitemImageurlarraylist = new ArrayList<String>();
    private ArrayList<String> wishlistitemTitlearraylist = new ArrayList<String>();
    private ArrayList<String> wishlistitemZiparraylist = new ArrayList<String>();
    private ArrayList<String> wishlistitemShippingarraylist = new ArrayList<String>();
    private ArrayList<String> wishlistitemConditionarraylist = new ArrayList<String>();
    private ArrayList<String> wishlistitemPricearraylist = new ArrayList<String>();
    private ArrayList<String> wishlistitemarraylist = new ArrayList<String>();
    private String Money;
    private LinearLayout pb;
    private LinearLayout content;
    private TextView num;
    private TextView title;
    private Bitmap bitmap;
    private String url;
    private String urlStr;
    private String threadres;
    public static final String PREFS_NAME = "MyPrefsFile";
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    public interface OnItemClickListener {
        void onItemClick(View view, String viewName, int position);
    }
    private OnItemClickListener mOnItemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        pb = findViewById(R.id.progressbar);
        pb.setVisibility(View.VISIBLE);
        content = findViewById(R.id.content);
        content.setVisibility(View.GONE);

        noresults = findViewById(R.id.noresults);

        Intent intent = getIntent();
        String search_res = intent.getStringExtra("search_res");
        Log.d("SecondActivity", search_res);

        num = findViewById(R.id.num);
        title = findViewById(R.id.title);
        title.setText(intent.getStringExtra("title"));
        try{
            resObj = new JSONObject(search_res);

            num.setText(resObj.getJSONArray("findItemsAdvancedResponse").getJSONObject(0).getJSONArray("searchResult").getJSONObject(0).getString("@count"));

            int num = Integer.parseInt(resObj.getJSONArray("findItemsAdvancedResponse").getJSONObject(0).getJSONArray("searchResult").getJSONObject(0).getString("@count"));

            if(num == 0){
                noresults.setVisibility(View.VISIBLE);
            }

            settings = this.getSharedPreferences(PREFS_NAME, 0);
            editor = settings.edit();
            String wishlistitemId = settings.getString("wishlistitemId", "");
            String wishlistitemImageurl = settings.getString("wishlistitemImageurl", "");
            String wishlistitemTitle = settings.getString("wishlistitemTitle", "");
            String wishlistitemZip = settings.getString("wishlistitemZip", "");
            String wishlistitemShipping = settings.getString("wishlistitemShipping", "");
            String wishlistitemCondition = settings.getString("wishlistitemCondition", "");
            String wishlistitemPrice = settings.getString("wishlistitemPrice", "");
            String wishlistitem = settings.getString("wishlistitem", "");
            Money = settings.getString("Money", "0.00");
            Log.d("SecondActivity", wishlistitemId);
            if(wishlistitemId!="") {
                JSONArray wishlistitemIdJSONarray = new JSONArray(wishlistitemId);
                JSONArray wishlistitemImageurlJSONarray = new JSONArray(wishlistitemImageurl);
                JSONArray wishlistitemTitleJSONarray = new JSONArray(wishlistitemTitle);
                JSONArray wishlistitemZipJSONarray = new JSONArray(wishlistitemZip);
                JSONArray wishlistitemShippingJSONarray = new JSONArray(wishlistitemShipping);
                JSONArray wishlistitemConditionJSONarray = new JSONArray(wishlistitemCondition);
                JSONArray wishlistitemPriceJSONarray = new JSONArray(wishlistitemPrice);
                JSONArray wishlistitemJSONarray = new JSONArray(wishlistitem);
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

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mRecyclerView = findViewById(R.id.id_recyclerview);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mRecyclerView.setAdapter(mAdapter = new MyAdapter());

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, String viewName, int position) {
                try{
                    switch (viewName){
                        case "wishbutton":
                            JSONObject Item = resObj.getJSONArray("findItemsAdvancedResponse").getJSONObject(0).getJSONArray("searchResult").getJSONObject(0).getJSONArray("item").getJSONObject(position);
                            View v = mRecyclerView.getLayoutManager().findViewByPosition(position);
                            TextView temp = v.findViewById(R.id.title);
                            /*
                            Layout layout = temp.getLayout();
                            int lineCount = temp.getLineCount();
                            StringBuilder SrcStr = new StringBuilder(temp.getText().toString());
                            Log.d("linecount", lineCount+"");

                            for(int i = 0; i < lineCount ; i++)
                            {
                                //使用getLineStart 和 getLineEnd 得到指定行的开始和结束的坐标，坐标范围是SrcStr整个字符串范围内。
                                String lineStr = SrcStr.subSequence(layout.getLineStart(i),layout.getLineEnd(i)).toString();
                                Log.d("text",lineStr);
                            }*/


                            if(wishlistitemIdarraylist.size()>0&&wishlistitemIdarraylist.contains(Item.getJSONArray("itemId").getString(0))){
                                int index = wishlistitemIdarraylist.indexOf(Item.getJSONArray("itemId").getString(0));
                                Double m = Double.parseDouble(Money);
                                if(Item.has("sellingStatus")&&Item.getJSONArray("sellingStatus").getJSONObject(0).has("currentPrice")&&Item.getJSONArray("sellingStatus").getJSONObject(0).getJSONArray("currentPrice").getJSONObject(0).has("__value__")) m = m-Double.parseDouble(Item.getJSONArray("sellingStatus").getJSONObject(0).getJSONArray("currentPrice").getJSONObject(0).getString("__value__"));

                                Money = m.toString();
                                wishlistitemIdarraylist.remove(index);
                                wishlistitemImageurlarraylist.remove(index);
                                wishlistitemTitlearraylist.remove(index);
                                wishlistitemZiparraylist.remove(index);
                                wishlistitemShippingarraylist.remove(index);
                                wishlistitemConditionarraylist.remove(index);
                                wishlistitemPricearraylist.remove(index);
                                wishlistitemarraylist.remove(index);



                                Toast.makeText(SecondActivity.this, temp.getText()+" was removed from wish list", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                wishlistitemIdarraylist.add(Item.getJSONArray("itemId").getString(0));
                                wishlistitemImageurlarraylist.add(Uri.encode(Item.getJSONArray("galleryURL").getString(0)));
                                wishlistitemTitlearraylist.add(Uri.encode(Item.getJSONArray("title").getString(0)));
                                if(Item.has("postalCode")) wishlistitemZiparraylist.add(Uri.encode("Zip: "+Item.getJSONArray("postalCode").getString(0)));
                                else wishlistitemZiparraylist.add(Uri.encode("N/A"));

                                if(Item.has("shippingInfo")&&Item.getJSONArray("shippingInfo").getJSONObject(0).has("shippingServiceCost")&&Item.getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("shippingServiceCost").getJSONObject(0).has("__value__")){
                                    String value = Item.getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("shippingServiceCost").getJSONObject(0).getString("__value__");
                                    if(Double.parseDouble(value) > 0) wishlistitemShippingarraylist.add(Uri.encode("$ "+value));
                                    else wishlistitemShippingarraylist.add(Uri.encode("Free Shipping"));
                                }
                                else{
                                    wishlistitemShippingarraylist.add(Uri.encode("N/A"));
                                }
                                if(Item.has("condition")&&Item.getJSONArray("condition").getJSONObject(0).has("conditionDisplayName")) wishlistitemConditionarraylist.add(Uri.encode(Item.getJSONArray("condition").getJSONObject(0).getJSONArray("conditionDisplayName").getString(0)));
                                else wishlistitemConditionarraylist.add(Uri.encode("N/A"));

                                if(Item.has("sellingStatus")&&Item.getJSONArray("sellingStatus").getJSONObject(0).has("currentPrice")&&Item.getJSONArray("sellingStatus").getJSONObject(0).getJSONArray("currentPrice").getJSONObject(0).has("__value__")) wishlistitemPricearraylist.add("$"+Item.getJSONArray("sellingStatus").getJSONObject(0).getJSONArray("currentPrice").getJSONObject(0).getString("__value__"));
                                else wishlistitemPricearraylist.add(Uri.encode("N/A"));

                                wishlistitemarraylist.add(Item.toString());
                                Double m = Double.parseDouble(Money);
                                if(Item.has("sellingStatus")&&Item.getJSONArray("sellingStatus").getJSONObject(0).has("currentPrice")&&Item.getJSONArray("sellingStatus").getJSONObject(0).getJSONArray("currentPrice").getJSONObject(0).has("__value__")) m = m+Double.parseDouble(Item.getJSONArray("sellingStatus").getJSONObject(0).getJSONArray("currentPrice").getJSONObject(0).getString("__value__"));

                                Money = m.toString();
                                Toast.makeText(SecondActivity.this, temp.getText()+" was added to wish list", Toast.LENGTH_SHORT).show();


                            }
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
                            mAdapter.notifyDataSetChanged();
                            WishlistFragment.wishlistitemIdarraylist = wishlistitemIdarraylist;
                            WishlistFragment.wishlistitemImageurlarraylist = wishlistitemImageurlarraylist;
                            WishlistFragment.wishlistitemConditionarraylist = wishlistitemConditionarraylist;
                            WishlistFragment.wishlistitemTitlearraylist = wishlistitemTitlearraylist;
                            WishlistFragment.wishlistitemZiparraylist = wishlistitemZiparraylist;
                            WishlistFragment.wishlistitemShippingarraylist = wishlistitemShippingarraylist;
                            WishlistFragment.wishlistitemPricearraylist = wishlistitemPricearraylist;
                            WishlistFragment.wishlistitemarraylist = wishlistitemarraylist;
                            WishlistFragment.Money = Money;
                            String n = wishlistitemIdarraylist.size()+"";
                            WishlistFragment.textnum.setText(n);
                            WishlistFragment.textprice.setText("$ "+Money);
                            if(wishlistitemIdarraylist.size()>0) WishlistFragment.nowishes.setVisibility(View.GONE);
                            else WishlistFragment.nowishes.setVisibility(View.VISIBLE);
                            WishlistFragment.mAdapter.notifyDataSetChanged();
                            break;
                        default:
                            urlStr="http://hw8571serverside-env.s84muegm5z.us-west-1.elasticbeanstalk.com/detail?itemID=";
                            urlStr+=resObj.getJSONArray("findItemsAdvancedResponse").getJSONObject(0).getJSONArray("searchResult").getJSONObject(0).getJSONArray("item").getJSONObject(position).getJSONArray("itemId").getString(0);
                            SecondActivity.DetailThread t = new SecondActivity.DetailThread();
                            t.start();
                            t.join();
                            Intent intent = new Intent("com.example.activitytest.ACTION_DETAIL");
                            intent.putExtra("detail_res", threadres);

                            urlStr="http://hw8571serverside-env.s84muegm5z.us-west-1.elasticbeanstalk.com/photos?title=";
                            urlStr+=resObj.getJSONArray("findItemsAdvancedResponse").getJSONObject(0).getJSONArray("searchResult").getJSONObject(0).getJSONArray("item").getJSONObject(position).getJSONArray("title").getString(0);
                            t = new SecondActivity.DetailThread();
                            t.start();
                            t.join();
                            intent.putExtra("detail_photos", threadres);

                            urlStr="http://hw8571serverside-env.s84muegm5z.us-west-1.elasticbeanstalk.com/similar?itemID=";
                            urlStr+=resObj.getJSONArray("findItemsAdvancedResponse").getJSONObject(0).getJSONArray("searchResult").getJSONObject(0).getJSONArray("item").getJSONObject(position).getJSONArray("itemId").getString(0);
                            t = new SecondActivity.DetailThread();
                            t.start();
                            t.join();
                            intent.putExtra("detail_similar", threadres);

                            intent.putExtra("detail_item", resObj.getJSONArray("findItemsAdvancedResponse").getJSONObject(0).getJSONArray("searchResult").getJSONObject(0).getJSONArray("item").getJSONObject(position).toString());
                            startActivity(intent);
                            break;
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        pb.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements View.OnClickListener{
        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    SecondActivity.this).inflate(R.layout.item, parent,
                    false));
            return holder;
        }



        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            MyViewHolder itemHolder = (MyViewHolder) holder;
            itemHolder.itemView.setTag(position);
            itemHolder.wishbutton.setTag(position);

            try{
                JSONObject Item = resObj.getJSONArray("findItemsAdvancedResponse").getJSONObject(0).getJSONArray("searchResult").getJSONObject(0).getJSONArray("item").getJSONObject(position);

                ImageView imageView = holder.tv.findViewById(R.id.image);
                url = Item.getJSONArray("galleryURL").getString(0);
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
                title.setText(Item.getJSONArray("title").getString(0));
                TextView zip = holder.tv.findViewById(R.id.zip);
                if(Item.has("postalCode")) zip.setText("Zip: "+Item.getJSONArray("postalCode").getString(0));
                else zip.setText("Zip: N/A");
                TextView shippingcost = holder.tv.findViewById(R.id.shippingcost);
                if(Item.has("shippingInfo")&&Item.getJSONArray("shippingInfo").getJSONObject(0).has("shippingServiceCost")&&Item.getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("shippingServiceCost").getJSONObject(0).has("__value__")){
                    String value = Item.getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("shippingServiceCost").getJSONObject(0).getString("__value__");
                    if(Double.parseDouble(value) > 0) shippingcost.setText("$ "+value);
                    else shippingcost.setText("Free Shipping");
                }
                else{
                    shippingcost.setText("N/A");
                }

                ImageView wishbutton = holder.tv.findViewById(R.id.wishbutton);
                Drawable drawable = null;
                Drawable wrappedDrawable = null;
                Drawable mutableDrawable = null;

                if(wishlistitemIdarraylist.size()>0&&wishlistitemIdarraylist.contains(Item.getJSONArray("itemId").getString(0))){
                    drawable = ContextCompat.getDrawable(SecondActivity.this, R.drawable.cart_remove);
                    wrappedDrawable = DrawableCompat.wrap(drawable);
                    mutableDrawable = wrappedDrawable.mutate();
                    DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(SecondActivity.this, R.color.Orange));
                }
                else{
                    drawable = ContextCompat.getDrawable(SecondActivity.this, R.drawable.cart_plus);
                    wrappedDrawable = DrawableCompat.wrap(drawable);
                    mutableDrawable = wrappedDrawable.mutate();
                    DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(SecondActivity.this, R.color.Silver));
                }
                wishbutton.setImageDrawable(drawable);
                TextView condition = holder.tv.findViewById(R.id.condition);
                if(Item.has("condition")&&Item.getJSONArray("condition").getJSONObject(0).has("conditionDisplayName")) condition.setText(Item.getJSONArray("condition").getJSONObject(0).getJSONArray("conditionDisplayName").getString(0));
                else condition.setText("N/A");

                TextView price = holder.tv.findViewById(R.id.price);
                if(Item.has("sellingStatus")&&Item.getJSONArray("sellingStatus").getJSONObject(0).has("currentPrice")&&Item.getJSONArray("sellingStatus").getJSONObject(0).getJSONArray("currentPrice").getJSONObject(0).has("__value__")) price.setText("$"+Item.getJSONArray("sellingStatus").getJSONObject(0).getJSONArray("currentPrice").getJSONObject(0).getString("__value__"));
                else price.setText("N/A");
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
                //Log.d("SecondActivity", "some test texts");

                temp = Integer.parseInt(resObj.getJSONArray("findItemsAdvancedResponse").getJSONObject(0).getJSONArray("searchResult").getJSONObject(0).getString("@count"));
                //Log.d("SecondActivity", temp.toString());
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
            bitmap = imageScale(bitmap, 450, 450);
            //关闭数据流
            is.close();
            } catch (Exception e) {
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
