package com.example.application1;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.Toast;
import android.content.Intent;
import android.text.TextWatcher;
import android.text.Editable;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.io.BufferedInputStream;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class SearchFragment extends Fragment {
    private EditText TextKeyword;
    private AppCompatAutoCompleteTextView TextZipcode;
    private EditText TextMiles;
    private Spinner SpinnerCategory;
    private Button ButtonSearch;
    private Button ButtonClear;
    private TextView keyworderror;
    private TextView zipcodeerror;
    private CheckBox CheckboxEnableNearbySearch;
    private CheckBox Local_Pickup;
    private CheckBox Free_Shipping;
    private CheckBox New;
    private CheckBox Used;
    private CheckBox Unspecified;
    private LinearLayout NearbySearchField;
    private RadioButton RadioButtonCurrentLocation;
    private RadioButton RadioButtonHere;
    private String urlStr;
    private String currentzip;
    private String threadres;
    private TextView selectedText;
    public static final String PREFS_NAME = "MyPrefsFile";

    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    private AutoSuggestAdapter autoSuggestAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.search_tab, container, false);
        TextKeyword = view.findViewById(R.id.TextKeyword);
        TextZipcode = view.findViewById(R.id.TextZipcode);
        TextMiles = view.findViewById(R.id.TextMiles);
        SpinnerCategory = view.findViewById(R.id.SpinnerCategory);
        ButtonSearch = view.findViewById(R.id.ButtonSearch);
        ButtonClear = view.findViewById(R.id.ButtonClear);
        keyworderror = view.findViewById(R.id.keyworderror);
        zipcodeerror = view.findViewById(R.id.zipcodeerror);
        CheckboxEnableNearbySearch = view.findViewById(R.id.CheckboxEnableNearbySearch);
        Local_Pickup = view.findViewById(R.id.Local_Pickup);
        Free_Shipping = view.findViewById(R.id.Free_Shipping);
        New = view.findViewById(R.id.New);
        Used = view.findViewById(R.id.Used);
        Unspecified = view.findViewById(R.id.Unspecified);
        NearbySearchField = view.findViewById(R.id.NearbySearchField);
        RadioButtonCurrentLocation = view.findViewById(R.id.RadioButtonCurrentLocation);
        RadioButtonHere = view.findViewById(R.id.RadioButtonHere);
        setListener();

        final AppCompatAutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.TextZipcode);
        selectedText = view.findViewById(R.id.selected_item);
        autoSuggestAdapter = new AutoSuggestAdapter(getContext(), android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(autoSuggestAdapter);
        autoCompleteTextView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        //selectedText.setText(autoSuggestAdapter.getObject(position));
                    }
                });
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompleteTextView.getText())) {
                        makeApiCall(autoCompleteTextView.getText().toString());
                    }
                }
                return false;
            }
        });

        return view;
    }

    private void makeApiCall(String text) {
        urlStr = "http://hw8571serverside-env.s84muegm5z.us-west-1.elasticbeanstalk.com/zip?zipcode=";
        urlStr+=text;
        List<String> stringList = new ArrayList<>();
        GetThread t = new GetThread();
        try{
            t.start();
            t.join();
            Log.d("main", threadres);
            JSONObject temp = new JSONObject(threadres);
            for (int i = 0; i < temp.getJSONArray("postalCodes").length(); i++) {
                stringList.add(temp.getJSONArray("postalCodes").getJSONObject(i).getString("postalCode"));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        autoSuggestAdapter.setData(stringList);
        autoSuggestAdapter.notifyDataSetChanged();
    }

    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    private void setListener(){
        Onclick onclick = new Onclick();
        ButtonSearch.setOnClickListener(onclick);
        ButtonClear.setOnClickListener(onclick);
        CheckboxEnableNearbySearch.setOnClickListener(onclick);
        RadioButtonCurrentLocation.setOnClickListener(onclick);
        RadioButtonHere.setOnClickListener(onclick);

        ArrayList<String> list = new ArrayList<String>();
        list.add("All");
        list.add("Art");
        list.add("Baby");
        list.add("Books");
        list.add("Clothing, Shoes & Accessories");
        list.add("Computers, Tablets & Networking");
        list.add("Health & Beauty");
        list.add("Music");
        list.add("Video Games & Consoles");

        //为下拉列表定义一个适配器
        final ArrayAdapter<String> cg = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);
        //设置下拉菜单样式。
        cg.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //添加数据
        SpinnerCategory.setAdapter(cg);
    }

    class Onclick implements View.OnClickListener{
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.ButtonSearch:
                    keyworderror.setVisibility(View.GONE);
                    zipcodeerror.setVisibility(View.GONE);
                    boolean fielderror = false;
                    if(TextKeyword.getText().toString().length()==0){
                        keyworderror.setText("Please enter mandatory field");
                        keyworderror.setVisibility(View.VISIBLE);
                        fielderror = true;
                    }
                    if(RadioButtonHere.isChecked()){
                        if(TextZipcode.getText().toString().length()==0){
                            zipcodeerror.setText("Please enter mandatory field");
                            zipcodeerror.setVisibility(View.VISIBLE);
                            fielderror = true;
                        }
                        else if(TextZipcode.getText().toString().length()!=5||!isNumeric(TextZipcode.getText().toString())){
                            zipcodeerror.setText("Invalid field");
                            zipcodeerror.setVisibility(View.VISIBLE);
                            fielderror = true;
                        }

                    }
                    if(fielderror){
                        Toast.makeText(getContext(), "Please fix all fields with errors", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        try{
                            urlStr = "http://ip-api.com/json";
                            GetThread t1 = new GetThread();
                            t1.start();
                            t1.join();
                            Log.d("main", threadres);
                            JSONObject resObj = new JSONObject(threadres);
                            currentzip = resObj.getString("zip");
                            Log.d("main", currentzip);
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                        urlStr="http://hw8571serverside-env.s84muegm5z.us-west-1.elasticbeanstalk.com/search";
                        urlStr+="?keyword=";
                        urlStr+=TextKeyword.getText().toString();
                        urlStr+="&category=";
                        urlStr+=SpinnerCategory.getSelectedItem();
                        urlStr+="&zipcode=";
                        if(CheckboxEnableNearbySearch.isChecked()) {
                            urlStr+=TextZipcode.getText().toString();
                        }
                        else urlStr+="-1";
                        urlStr+="&currentzipcode=";
                        if(CheckboxEnableNearbySearch.isChecked()) {
                            urlStr+=currentzip;
                        }
                        else urlStr+="-1";
                        urlStr+="&distance=";
                        if(CheckboxEnableNearbySearch.isChecked()) {
                            urlStr+=TextMiles.getText().toString().length()==0?"10":TextMiles.getText().toString();
                        }
                        else urlStr+="-1";
                        urlStr+="&free_shipping=";
                        urlStr+=Free_Shipping.isChecked();
                        urlStr+="&local_pickup=";
                        urlStr+=Local_Pickup.isChecked();
                        urlStr+="&new=";
                        urlStr+=New.isChecked();
                        urlStr+="&used=";
                        urlStr+=Used.isChecked();
                        urlStr+="&unspecified=";
                        urlStr+=Unspecified.isChecked();

                        try{
                            GetThread t2 = new GetThread();
                            t2.start();
                            t2.join();
                            JSONObject resObj = new JSONObject(threadres);
                            Intent intent = new Intent("com.example.activitytest.ACTION_SEARCH");
                            intent.putExtra("search_res", threadres);
                            SharedPreferences settings = getContext().getSharedPreferences(PREFS_NAME, 0);
                            String wishlistitemId = settings.getString("wishlistitemId", "");
                            intent.putExtra("wishlistitemId", wishlistitemId);
                            intent.putExtra("title", TextKeyword.getText().toString());
                            startActivity(intent);
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case R.id.CheckboxEnableNearbySearch:
                    if(CheckboxEnableNearbySearch.isChecked()){
                        NearbySearchField.setVisibility(View.VISIBLE);
                    }
                    else {
                        TextMiles.setText("");
                        TextZipcode.setText("");
                        RadioButtonCurrentLocation.setChecked(true);
                        RadioButtonHere.setChecked(false);
                        TextZipcode.setFocusable(false);
                        TextZipcode.setFocusableInTouchMode(false);
                        NearbySearchField.setVisibility(View.GONE);
                        zipcodeerror.setVisibility(View.GONE);
                    }
                    break;
                case R.id.ButtonClear:
                    TextKeyword.setText("");
                    TextMiles.setText("");
                    CheckboxEnableNearbySearch.setChecked(false);
                    keyworderror.setVisibility(View.GONE);
                    zipcodeerror.setVisibility(View.GONE);
                    NearbySearchField.setVisibility(View.GONE);
                    RadioButtonCurrentLocation.setChecked(true);
                    RadioButtonHere.setChecked(false);
                    TextZipcode.setText("");
                    break;
                case R.id.RadioButtonCurrentLocation:
                    zipcodeerror.setVisibility(View.GONE);
                    TextZipcode.setText("");
                    RadioButtonHere.setChecked(false);
                    TextZipcode.setFocusable(false);
                    TextZipcode.setFocusableInTouchMode(false);
                    break;
                case R.id.RadioButtonHere:
                    RadioButtonCurrentLocation.setChecked(false);
                    TextZipcode.setFocusableInTouchMode(true);
                    TextZipcode.setFocusable(true);
                    TextZipcode.requestFocus();
                    break;
            }
        }
    }

    class GetThread extends Thread{
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
