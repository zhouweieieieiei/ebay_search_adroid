package com.example.application1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TableRow;
import android.graphics.drawable.Drawable;
import 	android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.content.ContextCompat;
import com.wssholmes.stark.circular_score.CircularScoreView;

import org.json.JSONObject;

public class ShippingFragment extends Fragment {
    public static final String Product_info = "object1";//product本身信息
    public static final String Product_detail = "object2";//product detail 信息
    private JSONObject info_Obj;
    private JSONObject detail_Obj;
    private ImageView soldbyicon;
    private LinearLayout soldbysection;
    private ImageView shippinginfoicon;
    private LinearLayout shippinginfosection;
    private ImageView returnpolicyicon;
    private LinearLayout returnpolicysection;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.shipping_tab, container, false);
        Bundle bundle = getArguments();
        String info = bundle.getString(Product_info);
        String detail = bundle.getString(Product_detail);

        soldbyicon = view.findViewById(R.id.soldbyicon);
        soldbysection = view.findViewById(R.id.soldbysection);
        shippinginfoicon = view.findViewById(R.id.shippinginfoicon);
        shippinginfosection = view.findViewById(R.id.shippinginfosection);
        returnpolicyicon = view.findViewById(R.id.returnpolicyicon);
        returnpolicysection = view.findViewById(R.id.returnpolicysection);

        Drawable drawable = null;
        Drawable wrappedDrawable = null;
        Drawable mutableDrawable = null;
        drawable = ContextCompat.getDrawable(getContext(), R.drawable.truck);
        wrappedDrawable = DrawableCompat.wrap(drawable);
        mutableDrawable = wrappedDrawable.mutate();
        DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(getContext(), R.color.Gray));
        soldbyicon.setImageDrawable(drawable);

        drawable = ContextCompat.getDrawable(getContext(), R.drawable.ferry);
        wrappedDrawable = DrawableCompat.wrap(drawable);
        mutableDrawable = wrappedDrawable.mutate();
        DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(getContext(), R.color.Gray));
        shippinginfoicon.setImageDrawable(drawable);

        drawable = ContextCompat.getDrawable(getContext(), R.drawable.dump_truck);
        wrappedDrawable = DrawableCompat.wrap(drawable);
        mutableDrawable = wrappedDrawable.mutate();
        DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(getContext(), R.color.Gray));
        returnpolicyicon.setImageDrawable(drawable);

        try{
            info_Obj = new JSONObject(info);
            detail_Obj = new JSONObject(detail);
            TextView storename = view.findViewById(R.id.storename);
            TableRow storenamerow = view.findViewById(R.id.storenamerow);
            if(info_Obj.has("storeInfo")&&info_Obj.getJSONArray("storeInfo").getJSONObject(0).has("storeName")){
                storename.setText(Html.fromHtml("<a href=\'"+info_Obj.getJSONArray("storeInfo").getJSONObject(0).getJSONArray("storeURL").getString(0)+"\'>"+info_Obj.getJSONArray("storeInfo").getJSONObject(0).getJSONArray("storeName").getString(0)+"</a>"));
                storename.setMovementMethod(LinkMovementMethod.getInstance());
                storenamerow.setVisibility(View.VISIBLE);
                soldbysection.setVisibility(View.VISIBLE);
                view.findViewById(R.id.Noshippingmessage).setVisibility(View.GONE);
            }

            TextView feedbackscore = view.findViewById(R.id.feedbackscore);
            TableRow feedbackscorerow = view.findViewById(R.id.feedbackscorerow);
            if(info_Obj.has("sellerInfo")&&info_Obj.getJSONArray("sellerInfo").getJSONObject(0).has("feedbackScore")){
                feedbackscore.setText(info_Obj.getJSONArray("sellerInfo").getJSONObject(0).getJSONArray("feedbackScore").getString(0));
                feedbackscorerow.setVisibility(View.VISIBLE);
                soldbysection.setVisibility(View.VISIBLE);
                view.findViewById(R.id.Noshippingmessage).setVisibility(View.GONE);
            }

            CircularScoreView popularitycircle = view.findViewById(R.id.popularitycircle);
            TableRow popularityrow = view.findViewById(R.id.popularityrow);
            if(info_Obj.has("sellerInfo")&&info_Obj.getJSONArray("sellerInfo").getJSONObject(0).has("positiveFeedbackPercent")){
                String popularity = info_Obj.getJSONArray("sellerInfo").getJSONObject(0).getJSONArray("positiveFeedbackPercent").getString(0);
                Integer pop = (int) Double.parseDouble(popularity);
                popularitycircle.setScore(pop);
                popularityrow.setVisibility(View.VISIBLE);
                soldbysection.setVisibility(View.VISIBLE);
                view.findViewById(R.id.Noshippingmessage).setVisibility(View.GONE);
            }


            ImageView feedbackstar = view.findViewById(R.id.feedbackstar);
            TableRow feedbackstarrow = view.findViewById(R.id.feedbackstarrow);
            if(info_Obj.has("sellerInfo")&&info_Obj.getJSONArray("sellerInfo").getJSONObject(0).has("feedbackRatingStar")){
                drawable = null;
                wrappedDrawable = null;
                mutableDrawable = null;
                String starstyle = info_Obj.getJSONArray("sellerInfo").getJSONObject(0).getJSONArray("feedbackRatingStar").getString(0);
                switch (starstyle){
                    case "Blue":
                        drawable = ContextCompat.getDrawable(this.getContext(), R.drawable.star_circle_outline);
                        wrappedDrawable = DrawableCompat.wrap(drawable);
                        mutableDrawable = wrappedDrawable.mutate();
                        DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(this.getContext(), R.color.Blue));
                        break;
                    case "Green":
                        drawable = ContextCompat.getDrawable(this.getContext(), R.drawable.star_circle_outline);
                        wrappedDrawable = DrawableCompat.wrap(drawable);
                        mutableDrawable = wrappedDrawable.mutate();
                        DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(this.getContext(), R.color.Green));
                        break;
                    case "GreenShooting":
                        drawable = ContextCompat.getDrawable(this.getContext(), R.drawable.star_circle);
                        wrappedDrawable = DrawableCompat.wrap(drawable);
                        mutableDrawable = wrappedDrawable.mutate();
                        DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(this.getContext(), R.color.Green));
                        break;
                    case "Purple":
                        drawable = ContextCompat.getDrawable(this.getContext(), R.drawable.star_circle_outline);
                        wrappedDrawable = DrawableCompat.wrap(drawable);
                        mutableDrawable = wrappedDrawable.mutate();
                        DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(this.getContext(), R.color.Purple));
                        break;
                    case "PurpleShooting":
                        drawable = ContextCompat.getDrawable(this.getContext(), R.drawable.star_circle);
                        wrappedDrawable = DrawableCompat.wrap(drawable);
                        mutableDrawable = wrappedDrawable.mutate();
                        DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(this.getContext(), R.color.Purple));
                        break;
                    case "Red":
                        drawable = ContextCompat.getDrawable(this.getContext(), R.drawable.star_circle_outline);
                        wrappedDrawable = DrawableCompat.wrap(drawable);
                        mutableDrawable = wrappedDrawable.mutate();
                        DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(this.getContext(), R.color.Red));
                        break;
                    case "RedShooting":
                        drawable = ContextCompat.getDrawable(this.getContext(), R.drawable.star_circle);
                        wrappedDrawable = DrawableCompat.wrap(drawable);
                        mutableDrawable = wrappedDrawable.mutate();
                        DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(this.getContext(), R.color.Red));
                        break;
                    case "SilverShooting":
                        drawable = ContextCompat.getDrawable(this.getContext(), R.drawable.star_circle);
                        wrappedDrawable = DrawableCompat.wrap(drawable);
                        mutableDrawable = wrappedDrawable.mutate();
                        DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(this.getContext(), R.color.Silver));
                        break;
                    case "Turquoise":
                        drawable = ContextCompat.getDrawable(this.getContext(), R.drawable.star_circle_outline);
                        wrappedDrawable = DrawableCompat.wrap(drawable);
                        mutableDrawable = wrappedDrawable.mutate();
                        DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(this.getContext(), R.color.Turquoise));
                        break;
                    case "TurquoiseShooting":
                        drawable = ContextCompat.getDrawable(this.getContext(), R.drawable.star_circle);
                        wrappedDrawable = DrawableCompat.wrap(drawable);
                        mutableDrawable = wrappedDrawable.mutate();
                        DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(this.getContext(), R.color.Turquoise));
                        break;
                    case "Yellow":
                        drawable = ContextCompat.getDrawable(this.getContext(), R.drawable.star_circle_outline);
                        wrappedDrawable = DrawableCompat.wrap(drawable);
                        mutableDrawable = wrappedDrawable.mutate();
                        DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(this.getContext(), R.color.Yellow));
                        break;
                    case "YellowShooting":
                        drawable = ContextCompat.getDrawable(this.getContext(), R.drawable.star_circle);
                        wrappedDrawable = DrawableCompat.wrap(drawable);
                        mutableDrawable = wrappedDrawable.mutate();
                        DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(this.getContext(), R.color.Yellow));
                        break;
                }
                feedbackstar.setImageDrawable(drawable);
                feedbackstarrow.setVisibility(View.VISIBLE);
                soldbysection.setVisibility(View.VISIBLE);
                view.findViewById(R.id.Noshippingmessage).setVisibility(View.GONE);
            }

            TextView shippingcost = view.findViewById(R.id.shippingcost);
            TableRow shippingcostrow = view.findViewById(R.id.shippingcostrow);
            if(info_Obj.has("shippingInfo")&&info_Obj.getJSONArray("shippingInfo").getJSONObject(0).has("shippingServiceCost")&&info_Obj.getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("shippingServiceCost").getJSONObject(0).has("__value__")){
                String value = info_Obj.getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("shippingServiceCost").getJSONObject(0).getString("__value__");
                if(Double.parseDouble(value) > 0) shippingcost.setText("$ "+value);
                else shippingcost.setText("Free Shipping");
                shippingcostrow.setVisibility(View.VISIBLE);
                shippinginfosection.setVisibility(View.VISIBLE);
                view.findViewById(R.id.Noshippingmessage).setVisibility(View.GONE);
            }
            TextView globalshipping = view.findViewById(R.id.globalshipping);
            TableRow globalshippingrow = view.findViewById(R.id.globalshippingrow);
            if(detail_Obj.getJSONObject("Item").has("GlobalShipping")){
                Boolean value = detail_Obj.getJSONObject("Item").getBoolean("GlobalShipping");
                if(value) globalshipping.setText("Yes");
                else globalshipping.setText("No");
                globalshippingrow.setVisibility(View.VISIBLE);
                shippinginfosection.setVisibility(View.VISIBLE);
                view.findViewById(R.id.Noshippingmessage).setVisibility(View.GONE);
            }

            TextView handlingtime = view.findViewById(R.id.handlingtime);
            TableRow handlingtimerow = view.findViewById(R.id.handlingtimerow);
            if(detail_Obj.getJSONObject("Item").has("HandlingTime")){
                Integer value = detail_Obj.getJSONObject("Item").getInt("HandlingTime");
                if(value>1) handlingtime.setText(value+" days");
                else handlingtime.setText(value+" day");
                handlingtimerow.setVisibility(View.VISIBLE);
                shippinginfosection.setVisibility(View.VISIBLE);
                view.findViewById(R.id.Noshippingmessage).setVisibility(View.GONE);
            }

            TextView condition = view.findViewById(R.id.condition);
            TableRow conditionrow = view.findViewById(R.id.conditionrow);
            if(detail_Obj.getJSONObject("Item").has("ConditionDisplayName")){
                condition.setText(detail_Obj.getJSONObject("Item").getString("ConditionDisplayName"));
                conditionrow.setVisibility(View.VISIBLE);
                shippinginfosection.setVisibility(View.VISIBLE);
                view.findViewById(R.id.Noshippingmessage).setVisibility(View.GONE);
            }

            TextView policy = view.findViewById(R.id.policy);
            TableRow policyrow = view.findViewById(R.id.policyrow);
            if(detail_Obj.getJSONObject("Item").has("ReturnPolicy")&&detail_Obj.getJSONObject("Item").getJSONObject("ReturnPolicy").has("ReturnsAccepted")){
                policy.setText(detail_Obj.getJSONObject("Item").getJSONObject("ReturnPolicy").getString("ReturnsAccepted"));
                policyrow.setVisibility(View.VISIBLE);
                returnpolicysection.setVisibility(View.VISIBLE);
                view.findViewById(R.id.Noshippingmessage).setVisibility(View.GONE);
            }

            TextView returnswithin = view.findViewById(R.id.returnswithin);
            TableRow returnswithinrow = view.findViewById(R.id.returnswithinrow);
            if(detail_Obj.getJSONObject("Item").has("ReturnPolicy")&&detail_Obj.getJSONObject("Item").getJSONObject("ReturnPolicy").has("ReturnsWithin")){
                returnswithin.setText(detail_Obj.getJSONObject("Item").getJSONObject("ReturnPolicy").getString("ReturnsWithin"));
                returnswithinrow.setVisibility(View.VISIBLE);
                returnpolicysection.setVisibility(View.VISIBLE);
                view.findViewById(R.id.Noshippingmessage).setVisibility(View.GONE);
            }

            TextView refundmode = view.findViewById(R.id.refundmode);
            TableRow refundmoderow = view.findViewById(R.id.refundmoderow);
            if(detail_Obj.getJSONObject("Item").has("ReturnPolicy")&&detail_Obj.getJSONObject("Item").getJSONObject("ReturnPolicy").has("Refund")){
                refundmode.setText(detail_Obj.getJSONObject("Item").getJSONObject("ReturnPolicy").getString("Refund"));
                refundmoderow.setVisibility(View.VISIBLE);
                returnpolicysection.setVisibility(View.VISIBLE);
                view.findViewById(R.id.Noshippingmessage).setVisibility(View.GONE);
            }

            TextView shippedby = view.findViewById(R.id.shippedby);
            TableRow shippedbyrow = view.findViewById(R.id.shippedbyrow);
            if(detail_Obj.getJSONObject("Item").has("ReturnPolicy")&&detail_Obj.getJSONObject("Item").getJSONObject("ReturnPolicy").has("ShippingCostPaidBy")){
                shippedby.setText(detail_Obj.getJSONObject("Item").getJSONObject("ReturnPolicy").getString("ShippingCostPaidBy"));
                shippedbyrow.setVisibility(View.VISIBLE);
                returnpolicysection.setVisibility(View.VISIBLE);
                view.findViewById(R.id.Noshippingmessage).setVisibility(View.GONE);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
}
