package com.example.application1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.widget.ImageView;
import android.widget.FrameLayout;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.util.Log;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ThirdActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String detail_res;
    private String detail_item;
    private String detail_photos;
    private String detail_similar;
    private ArrayList<String> wishlistitemIdarraylist = new ArrayList<String>();
    private ArrayList<String> wishlistitemImageurlarraylist = new ArrayList<String>();
    private ArrayList<String> wishlistitemTitlearraylist = new ArrayList<String>();
    private ArrayList<String> wishlistitemZiparraylist = new ArrayList<String>();
    private ArrayList<String> wishlistitemShippingarraylist = new ArrayList<String>();
    private ArrayList<String> wishlistitemConditionarraylist = new ArrayList<String>();
    private ArrayList<String> wishlistitemPricearraylist = new ArrayList<String>();
    private ArrayList<String> wishlistitemarraylist = new ArrayList<String>();
    private String Money;
    private String itemId;
    private String itemTitle;
    private JSONObject Item;
    private LinearLayout pb;
    private LinearLayout content;
    private FloatingActionButton fab;
    public static final String PREFS_NAME = "MyPrefsFile";
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        pb = findViewById(R.id.progressbar);
        pb.setVisibility(View.VISIBLE);
        content = findViewById(R.id.content);
        content.setVisibility(View.GONE);
        fab = findViewById(R.id.floatbutton);
        fab.hide();


        Intent intent = getIntent();
        detail_res = intent.getStringExtra("detail_res");
        detail_item = intent.getStringExtra("detail_item");
        detail_photos = intent.getStringExtra("detail_photos");
        detail_similar = intent.getStringExtra("detail_similar");

        viewPager = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tab);

        MyFragmentAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager());

        getSupportActionBar().setElevation(0);
        viewPager.setAdapter(adapter);
        //tabLayout.setupWithViewPager(viewPager);


        /*
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });*/

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
        try {
            Item = new JSONObject(detail_item);
            if (wishlistitemId != "") {
                JSONArray wishlistitemIdJSONarray = new JSONArray(wishlistitemId);
                JSONArray wishlistitemImageurlJSONarray = new JSONArray(wishlistitemImageurl);
                JSONArray wishlistitemTitleJSONarray = new JSONArray(wishlistitemTitle);
                JSONArray wishlistitemZipJSONarray = new JSONArray(wishlistitemZip);
                JSONArray wishlistitemShippingJSONarray = new JSONArray(wishlistitemShipping);
                JSONArray wishlistitemConditionJSONarray = new JSONArray(wishlistitemCondition);
                JSONArray wishlistitemPriceJSONarray = new JSONArray(wishlistitemPrice);
                JSONArray wishlistitemJSONarray = new JSONArray(wishlistitem);
                for (int i = 0; i < wishlistitemIdJSONarray.length(); i++) {
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
            JSONObject temp = new JSONObject(detail_item);
            itemId = temp.getJSONArray("itemId").getString(0);
            itemTitle = temp.getJSONArray("title").getString(0);
        }catch (Exception e) {
            e.printStackTrace();
        }
        FloatingActionButton btn = findViewById(R.id.floatbutton);
        if(wishlistitemIdarraylist.size()>0 && wishlistitemIdarraylist.contains(itemId)){
            Drawable drawable = null;
            Drawable wrappedDrawable = null;
            Drawable mutableDrawable = null;
            drawable = ContextCompat.getDrawable(ThirdActivity.this, R.drawable.cart_remove);
            wrappedDrawable = DrawableCompat.wrap(drawable);
            mutableDrawable = wrappedDrawable.mutate();
            DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(ThirdActivity.this, R.color.White));
            btn.setImageDrawable(drawable);
            //btn.setImageDrawable(getResources().getDrawable((R.drawable.cart_remove)));
        }
        else {
            Drawable drawable = null;
            Drawable wrappedDrawable = null;
            Drawable mutableDrawable = null;
            drawable = ContextCompat.getDrawable(ThirdActivity.this, R.drawable.cart_plus);
            wrappedDrawable = DrawableCompat.wrap(drawable);
            mutableDrawable = wrappedDrawable.mutate();
            DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(ThirdActivity.this, R.color.White));
            btn.setImageDrawable(drawable);
            //btn.setImageDrawable(getResources().getDrawable((R.drawable.cart_plus)));
        }

        Onclick onclick = new Onclick();
        btn.setOnClickListener(onclick);

        Drawable drawable = null;
        Drawable wrappedDrawable = null;
        Drawable mutableDrawable = null;

        TabLayout.Tab tab1 = tabLayout.newTab();
        View inflate1 = View.inflate(this, R.layout.tab, null);
        TextView textView1 = inflate1.findViewById(R.id.tab_text);
        textView1.setText("PRODUCT");
        textView1.setTextColor(getResources().getColor(R.color.White));
        drawable = ContextCompat.getDrawable(ThirdActivity.this, R.drawable.information_variant);
        wrappedDrawable = DrawableCompat.wrap(drawable);
        mutableDrawable = wrappedDrawable.mutate();
        DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(ThirdActivity.this, R.color.White));
        ImageView imgView1 = inflate1.findViewById(R.id.tab_icon);
        imgView1.setImageDrawable(drawable);
        tab1.setCustomView(inflate1);
        tabLayout.addTab(tab1);

        TabLayout.Tab tab2 = tabLayout.newTab();
        View inflate2 = View.inflate(this, R.layout.tab, null);
        TextView textView2 = inflate2.findViewById(R.id.tab_text);
        textView2.setText("SHIPPING");
        drawable = ContextCompat.getDrawable(ThirdActivity.this, R.drawable.truck);
        wrappedDrawable = DrawableCompat.wrap(drawable);
        mutableDrawable = wrappedDrawable.mutate();
        DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(ThirdActivity.this, R.color.Silver));
        ImageView imgView2 = inflate2.findViewById(R.id.tab_icon);
        imgView2.setImageDrawable(drawable);
        tab2.setCustomView(inflate2);
        tabLayout.addTab(tab2);

        TabLayout.Tab tab3 = tabLayout.newTab();
        View inflate3 = View.inflate(this, R.layout.tab, null);
        TextView textView3 = inflate3.findViewById(R.id.tab_text);
        textView3.setText("PHOTOS");
        drawable = ContextCompat.getDrawable(ThirdActivity.this, R.drawable.google);
        wrappedDrawable = DrawableCompat.wrap(drawable);
        mutableDrawable = wrappedDrawable.mutate();
        DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(ThirdActivity.this, R.color.Silver));
        ImageView imgView3 = inflate3.findViewById(R.id.tab_icon);
        imgView3.setImageDrawable(drawable);
        tab3.setCustomView(inflate3);
        tabLayout.addTab(tab3);

        TabLayout.Tab tab4 = tabLayout.newTab();
        View inflate4 = View.inflate(this, R.layout.tab, null);
        TextView textView4 = inflate4.findViewById(R.id.tab_text);
        textView4.setText("SIMILAR");
        drawable = ContextCompat.getDrawable(ThirdActivity.this, R.drawable.equal);
        wrappedDrawable = DrawableCompat.wrap(drawable);
        mutableDrawable = wrappedDrawable.mutate();
        DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(ThirdActivity.this, R.color.Silver));
        ImageView imgView4 = inflate4.findViewById(R.id.tab_icon);
        imgView4.setImageDrawable(drawable);
        tab4.setCustomView(inflate4);
        tabLayout.addTab(tab4);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                TextView temp = tab.getCustomView().findViewById(R.id.tab_text);
                ImageView temp2 = tab.getCustomView().findViewById(R.id.tab_icon);
                temp.setTextColor(getResources().getColor(R.color.White));
                Drawable drawable = temp2.getDrawable();
                Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
                Drawable mutableDrawable = wrappedDrawable.mutate();
                DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(ThirdActivity.this, R.color.White));
                temp2.setImageDrawable(drawable);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                TextView temp = tab.getCustomView().findViewById(R.id.tab_text);
                ImageView temp2 = tab.getCustomView().findViewById(R.id.tab_icon);
                temp.setTextColor(getResources().getColor(R.color.Silver));
                Drawable drawable = temp2.getDrawable();
                Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
                Drawable mutableDrawable = wrappedDrawable.mutate();
                DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(ThirdActivity.this, R.color.Silver));
                temp2.setImageDrawable(drawable);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.setScrollPosition(position, 0f, true);
                TextView temp = tabLayout.getTabAt(position).getCustomView().findViewById(R.id.tab_text);
                ImageView temp2 = tabLayout.getTabAt(position).getCustomView().findViewById(R.id.tab_icon);
                temp.setTextColor(getResources().getColor(R.color.White));
                Drawable drawable = temp2.getDrawable();
                Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
                Drawable mutableDrawable = wrappedDrawable.mutate();
                DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(ThirdActivity.this, R.color.White));
                temp2.setImageDrawable(drawable);

                if(position>0){
                    temp = tabLayout.getTabAt(position-1).getCustomView().findViewById(R.id.tab_text);
                    temp2 = tabLayout.getTabAt(position-1).getCustomView().findViewById(R.id.tab_icon);
                    temp.setTextColor(getResources().getColor(R.color.Silver));
                    drawable = temp2.getDrawable();
                    wrappedDrawable = DrawableCompat.wrap(drawable);
                    mutableDrawable = wrappedDrawable.mutate();
                    DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(ThirdActivity.this, R.color.Silver));
                    temp2.setImageDrawable(drawable);
                }
                if(position<3){
                    temp = tabLayout.getTabAt(position+1).getCustomView().findViewById(R.id.tab_text);
                    temp2 = tabLayout.getTabAt(position+1).getCustomView().findViewById(R.id.tab_icon);
                    temp.setTextColor(getResources().getColor(R.color.Silver));
                    drawable = temp2.getDrawable();
                    wrappedDrawable = DrawableCompat.wrap(drawable);
                    mutableDrawable = wrappedDrawable.mutate();
                    DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(ThirdActivity.this, R.color.Silver));
                    temp2.setImageDrawable(drawable);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(itemTitle);
            /*
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            ActionBar.TabListener tabListener = new ActionBar.TabListener() {
                @Override
                public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
                    viewPager.setCurrentItem(tab.getPosition());
                }
                @Override
                public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
                }
                @Override
                public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
                }
            };
            for (int i = 0; i < adapter.getCount(); i++) {
                actionBar.addTab(actionBar.newTab()
                        .setIcon(R.mipmap.ic_launcher)
                        .setText(adapter.getPageTitle(i))
                        .setTabListener(tabListener));
            }
            */
        }
        pb.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
        fab.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.facebook, menu);
        return super.onCreateOptionsMenu(menu);
    }


    class Onclick implements View.OnClickListener{
        @Override
        public void onClick(View v){
            try {
            switch (v.getId()) {
                    case R.id.floatbutton:
                        FloatingActionButton btn = findViewById(R.id.floatbutton);
                        if (wishlistitemIdarraylist.size() > 0 && wishlistitemIdarraylist.contains(itemId)) {
                            Drawable drawable = null;
                            Drawable wrappedDrawable = null;
                            Drawable mutableDrawable = null;
                            drawable = ContextCompat.getDrawable(ThirdActivity.this, R.drawable.cart_plus);
                            wrappedDrawable = DrawableCompat.wrap(drawable);
                            mutableDrawable = wrappedDrawable.mutate();
                            DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(ThirdActivity.this, R.color.White));
                            btn.setImageDrawable(drawable);

                            //btn.setImageDrawable(getResources().getDrawable((R.drawable.cart_plus)));
                            int index = wishlistitemIdarraylist.indexOf(itemId);
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



                            Toast.makeText(ThirdActivity.this, itemTitle + " was removed from wish list", Toast.LENGTH_SHORT).show();
                        } else {
                            Drawable drawable = null;
                            Drawable wrappedDrawable = null;
                            Drawable mutableDrawable = null;
                            drawable = ContextCompat.getDrawable(ThirdActivity.this, R.drawable.cart_remove);
                            wrappedDrawable = DrawableCompat.wrap(drawable);
                            mutableDrawable = wrappedDrawable.mutate();
                            DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(ThirdActivity.this, R.color.White));
                            btn.setImageDrawable(drawable);

                            //btn.setImageDrawable(getResources().getDrawable((R.drawable.cart_remove)));
                            wishlistitemIdarraylist.add(Item.getJSONArray("itemId").getString(0));
                            wishlistitemImageurlarraylist.add(Uri.encode(Item.getJSONArray("galleryURL").getString(0)));
                            wishlistitemTitlearraylist.add(Uri.encode(Item.getJSONArray("title").getString(0)));
                            if (Item.has("postalCode"))
                                wishlistitemZiparraylist.add(Uri.encode("Zip: " + Item.getJSONArray("postalCode").getString(0)));
                            else wishlistitemZiparraylist.add(Uri.encode("N/A"));

                            if (Item.has("shippingInfo") && Item.getJSONArray("shippingInfo").getJSONObject(0).has("shippingServiceCost") && Item.getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("shippingServiceCost").getJSONObject(0).has("__value__")) {
                                String value = Item.getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("shippingServiceCost").getJSONObject(0).getString("__value__");
                                if (Double.parseDouble(value) > 0)
                                    wishlistitemShippingarraylist.add(Uri.encode("$ " + value));
                                else wishlistitemShippingarraylist.add(Uri.encode("Free Shipping"));
                            } else {
                                wishlistitemShippingarraylist.add(Uri.encode("N/A"));
                            }
                            if (Item.has("condition") && Item.getJSONArray("condition").getJSONObject(0).has("conditionDisplayName"))
                                wishlistitemConditionarraylist.add(Uri.encode(Item.getJSONArray("condition").getJSONObject(0).getJSONArray("conditionDisplayName").getString(0)));
                            else wishlistitemConditionarraylist.add(Uri.encode("N/A"));

                            if (Item.has("sellingStatus") && Item.getJSONArray("sellingStatus").getJSONObject(0).has("currentPrice") && Item.getJSONArray("sellingStatus").getJSONObject(0).getJSONArray("currentPrice").getJSONObject(0).has("__value__"))
                                wishlistitemPricearraylist.add("$" + Item.getJSONArray("sellingStatus").getJSONObject(0).getJSONArray("currentPrice").getJSONObject(0).getString("__value__"));
                            else wishlistitemPricearraylist.add(Uri.encode("N/A"));

                            wishlistitemarraylist.add(Item.toString());

                            Double m = Double.parseDouble(Money);
                            if(Item.has("sellingStatus")&&Item.getJSONArray("sellingStatus").getJSONObject(0).has("currentPrice")&&Item.getJSONArray("sellingStatus").getJSONObject(0).getJSONArray("currentPrice").getJSONObject(0).has("__value__")) m = m+Double.parseDouble(Item.getJSONArray("sellingStatus").getJSONObject(0).getJSONArray("currentPrice").getJSONObject(0).getString("__value__"));

                            Money = m.toString();
                            Toast.makeText(ThirdActivity.this, itemTitle + " was added to wish list", Toast.LENGTH_SHORT).show();
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
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public class MyFragmentAdapter extends FragmentPagerAdapter{
        public MyFragmentAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position){
            Fragment fragment = null;
            Bundle bundle = new Bundle();
            switch (position){
                case 0:
                    fragment = new ProductFragment();
                    //Log.d("detail_res", detail_res);
                    //Log.d("detail_item", detail_item);
                    bundle.putString(((ProductFragment) fragment).Product_detail, detail_res);
                    bundle.putString(((ProductFragment) fragment).Product_info, detail_item);
                    fragment.setArguments(bundle);
                    break;
                case 1:
                    fragment = new ShippingFragment();
                    bundle.putString(((ShippingFragment) fragment).Product_detail, detail_res);
                    bundle.putString(((ShippingFragment) fragment).Product_info, detail_item);
                    fragment.setArguments(bundle);
                    break;
                case 2:
                    fragment = new PhotosFragment();
                    bundle.putString(((PhotosFragment) fragment).Product_photos, detail_photos);
                    fragment.setArguments(bundle);
                    break;
                case 3:
                    fragment = new SimilarFragment();
                    bundle.putString(((SimilarFragment) fragment).Product_similar, detail_similar);
                    fragment.setArguments(bundle);
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount(){
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position){
            switch (position){
                case 0:
                    return "PRODUCT";
                case 1:
                    return "SHIPPING";
                case 2:
                    return "PHOTOS";
                case 3:
                    return "SIMILAR";
                default:
                    return "";
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.facebook:
                try{
                    JSONObject detail = new JSONObject(detail_res);
                    detail = detail.getJSONObject("Item");
                    Log.d("detail_str", detail.toString());
                    String url = "http://www.facebook.com/sharer/sharer.php?u=";
                    url+=Uri.encode(detail.getString("ViewItemURLForNaturalSearch"));
                    url+="&quote=";
                    url+="Buy ";
                    url+=detail.getString("Title");
                    url+=" at $";
                    url+=detail.getJSONObject("CurrentPrice").getString("Value");
                    url+=" from LINK below.";
                    url+="&hashtag=%23CSCI571Spring2019Ebay";
                    Log.d("url", url);
                    Intent intent = new Intent();//创建Intent对象
                    intent.setAction(Intent.ACTION_VIEW);//为Intent设置动作
                    intent.setData(Uri.parse(url));//为Intent设置数据
                    startActivity(intent);
                }catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);

        /*if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);*/
        }
    }
}


