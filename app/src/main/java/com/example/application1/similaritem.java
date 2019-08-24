package com.example.application1;

import org.json.JSONObject;

public class similaritem {
    public String Title;
    public Double Price = 0.0;
    public String price = "0.0";
    public Integer Days = 0;
    public String daysLeft = "N/A";
    public String shipping = "N/A";
    public String imgurl;
    public String url = "https://www.ebay.com/";

    public similaritem(JSONObject Item){
        try{
            this.Title = Item.getString("title");
            if(Item.has("buyItNowPrice")&&Item.getJSONObject("buyItNowPrice").has("__value__")){
                this.price = Item.getJSONObject("buyItNowPrice").getString("__value__");
                this.Price = Double.parseDouble(Item.getJSONObject("buyItNowPrice").getString("__value__"));
            }

            if(Item.has("timeLeft")){
                String cost = Item.getString("timeLeft");
                cost = cost.substring(cost.indexOf('P')+1);
                cost = cost.substring(0, cost.indexOf('D'));
                this.Days = Integer.parseInt(cost);
                if(Integer.parseInt(cost)>1) {
                    this.daysLeft = cost+" Days Left";
                }
                else this.daysLeft = cost+" Day Left";
            }
            if(Item.has("shippingCost")&&Item.getJSONObject("shippingCost").has("__value__")){
                String cost = Item.getJSONObject("shippingCost").getString("__value__");
                if(Double.parseDouble(cost) > 0) this.shipping = "$"+cost;
                else this.shipping = "Free Shipping";
            }
            this.imgurl = Item.getString("imageURL");

            if(Item.has("viewItemURL")) {
                url = Item.getString("viewItemURL");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
