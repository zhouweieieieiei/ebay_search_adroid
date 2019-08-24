package com.example.application1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.view.View.OnClickListener;
import android.net.Uri;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;


public class SimilarFragment extends Fragment {
    public static final String Product_similar = "object";
    private JSONObject similar_Obj;
    private Spinner sortstyle;
    private Spinner sortorder;
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private Bitmap bitmap;
    private String url;
    private ArrayList<similaritem> similarlist_default;
    private ArrayList<similaritem> similarlist;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.similar_tab, container, false);
        Bundle bundle = getArguments();
        String similar = bundle.getString(Product_similar);
        Log.d("similar", similar);

        sortstyle = view.findViewById(R.id.sortstyle);
        sortorder = view.findViewById(R.id.sortorder);
        setListener();
        sortorder.setEnabled(false);

        try{
            similar_Obj = new JSONObject(similar);
            similarlist = new ArrayList<similaritem>();
            similarlist_default = new ArrayList<similaritem>();
            for(int i = 0; i < similar_Obj.getJSONObject("getSimilarItemsResponse").getJSONObject("itemRecommendations").getJSONArray("item").length(); ++i){
                similarlist.add(new similaritem(similar_Obj.getJSONObject("getSimilarItemsResponse").getJSONObject("itemRecommendations").getJSONArray("item").optJSONObject(i)));
            }
            Log.d("similar", "similarlist num:"+similarlist.size());
            similarlist_default.addAll(similarlist);
            Log.d("similar", "similarlist_default num:"+similarlist_default.size());
        }catch (Exception e) {
            e.printStackTrace();
        }


        mRecyclerView = view.findViewById(R.id.id_recyclerview);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        mRecyclerView.setAdapter(mAdapter = new MyAdapter());

        mAdapter.setOnItemClickLitener(new OnItemClickLitener() {

            @Override
            public void onItemClick(View view, int position) {
                String url = similarlist.get(position).url;
                Log.d("url", url);
                Intent intent=new Intent();//创建Intent对象
                intent.setAction(Intent.ACTION_VIEW);//为Intent设置动作
                intent.setData(Uri.parse(url));//为Intent设置数据
                startActivity(intent);
            }
        });

        return view;
    }

    private void setListener(){
        ArrayList<String> list1 = new ArrayList<String>();
        list1.add("Default");
        list1.add("Name");
        list1.add("Price");
        list1.add("Days");

        ArrayList<String> list2 = new ArrayList<String>();
        list2.add("Ascending");
        list2.add("Descending");

        //为下拉列表定义一个适配器
        final ArrayAdapter<String> cg1 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, list1);
        //设置下拉菜单样式。
        cg1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //添加数据
        sortstyle.setAdapter(cg1);

        final ArrayAdapter<String> cg2 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, list2);
        //设置下拉菜单样式。
        cg1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //添加数据
        sortorder.setAdapter(cg2);

        sortstyle.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Log.d("style", sortstyle.getSelectedItem().toString());
                String style = sortstyle.getSelectedItem().toString();
                if(style == "Default") {
                    sortorder.setEnabled(false);
                    similarlist.clear();
                    similarlist.addAll(similarlist_default);
                }
                else{
                    sortorder.setEnabled(true);
                    resort(style, sortorder.getSelectedItem().toString());
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        sortorder.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Log.d("order", sortorder.getSelectedItem().toString());
                resort(sortstyle.getSelectedItem().toString(), sortorder.getSelectedItem().toString());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    Comparator<similaritem> NameAscending = new Comparator<similaritem>() {
        @Override
        public int compare(similaritem s1, similaritem s2) {
            return s1.Title.compareTo(s2.Title);
        }
    };

    Comparator<similaritem> NameDescending = new Comparator<similaritem>() {
        @Override
        public int compare(similaritem s1, similaritem s2) {
            return s2.Title.compareTo(s1.Title);
        }
    };

    Comparator<similaritem> PriceAscending = new Comparator<similaritem>() {
        @Override
        public int compare(similaritem s1, similaritem s2) {
            return s1.Price.compareTo(s2.Price);
        }
    };

    Comparator<similaritem> PriceDescending = new Comparator<similaritem>() {
        @Override
        public int compare(similaritem s1, similaritem s2) {
            return s2.Price.compareTo(s1.Price);
        }
    };

    Comparator<similaritem> DaysAscending = new Comparator<similaritem>() {
        @Override
        public int compare(similaritem s1, similaritem s2) {
            return s1.Days.compareTo(s2.Days);
        }
    };

    Comparator<similaritem> DaysDescending = new Comparator<similaritem>() {
        @Override
        public int compare(similaritem s1, similaritem s2) {
            return s2.Days.compareTo(s1.Days);
        }
    };

    public void resort(String style, String order){
        if(style == "Name"){
            if(order == "Ascending") Collections.sort(similarlist, NameAscending);
            else Collections.sort(similarlist, NameDescending);
        }
        else if(style == "Price") {
            if(order == "Ascending") Collections.sort(similarlist, PriceAscending);
            else Collections.sort(similarlist, PriceDescending);
        }
        else if(style == "Days"){
            if(order == "Ascending") Collections.sort(similarlist, DaysAscending);
            else Collections.sort(similarlist, DaysDescending);
        }
    }

    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
    }

    class MyAdapter extends RecyclerView.Adapter<SimilarFragment.MyAdapter.MyViewHolder> {

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            MyAdapter.MyViewHolder holder = new MyAdapter.MyViewHolder(LayoutInflater.from(
                    getContext()).inflate(R.layout.item2, parent,
                    false));
            return holder;
        }

        private OnItemClickLitener mOnItemClickLitener;

        public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
        {
            this.mOnItemClickLitener = mOnItemClickLitener;
        }

        @Override
        public void onBindViewHolder(final MyAdapter.MyViewHolder holder, final int position) {
            try{
                JSONObject Item = similar_Obj.getJSONObject("getSimilarItemsResponse").getJSONObject("itemRecommendations").getJSONArray("item").optJSONObject(position);

                ImageView imageView = holder.tv.findViewById(R.id.image);
                url = similarlist.get(position).imgurl;
                //url = Item.getString("imageURL");
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
                title.setText(similarlist.get(position).Title);
                //title.setText(Item.getString("title"));
                TextView shipping = holder.tv.findViewById(R.id.shipping);
                shipping.setText(similarlist.get(position).shipping);
                /*if(Item.has("shippingCost")&&Item.getJSONObject("shippingCost").has("__value__")){
                    String cost = Item.getJSONObject("shippingCost").getString("__value__");
                    if(Double.parseDouble(cost) > 0) shipping.setText("$"+cost);
                    else shipping.setText("Free Shipping");
                }
                else shipping.setText("N/A");*/
                TextView daysleft = holder.tv.findViewById(R.id.daysleft);
                daysleft.setText(similarlist.get(position).daysLeft);
                /*if(Item.has("timeLeft")){
                    String cost = Item.getString("timeLeft");
                    cost = cost.substring(cost.indexOf('P')+1);
                    cost = cost.substring(0, cost.indexOf('D'));
                    if(Integer.parseInt(cost)>1) daysleft.setText(cost+" Days Left");
                    else daysleft.setText(cost+" Day Left");
                }
                else daysleft.setText("N/A");*/

                TextView price = holder.tv.findViewById(R.id.price);
                /*if(Item.has("buyItNowPrice")&&Item.getJSONObject("buyItNowPrice").has("__value__")){
                    price.setText("$"+Item.getJSONObject("buyItNowPrice").getString("__value__"));
                }
                else price.setText("N/A");*/
                price.setText("$"+similarlist.get(position).price);

                if (mOnItemClickLitener != null) {
                    holder.itemView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int pos = holder.getLayoutPosition();
                            mOnItemClickLitener.onItemClick(holder.itemView, pos);
                        }
                    });
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount()
        {   Integer temp = 0;
            try{
                temp = similar_Obj.getJSONObject("getSimilarItemsResponse").getJSONObject("itemRecommendations").getJSONArray("item").length();
                return temp;
            }catch (Exception e) {
                e.printStackTrace();
            }
            return temp;
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {

            CardView tv;

            public MyViewHolder(View view)
            {
                super(view);
                tv = view.findViewById(R.id.id_num);
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
}
