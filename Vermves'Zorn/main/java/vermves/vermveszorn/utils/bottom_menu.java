package vermves.vermveszorn.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import vermves.vermveszorn.R;
import vermves.vermveszorn.activities.AbiltiesActivity;
import vermves.vermveszorn.activities.DungeonActivity;
import vermves.vermveszorn.activities.MainActivity;

/**
 * Created by Martin on 10.10.2016.
 */

public class bottom_menu {
    Context context;

    RelativeLayout wrapper;

    LinearLayout linearLayout;
    LinearLayout.LayoutParams linearLayoutParams;

    private ImageView[] buttons;
    private int btn_qty;
    private Class[] aActivities;
    private int[] aImageRes;
    private int activeButton;

    private int iClrPrimary;
    private int iClrAccent;

    private boolean isVisible;

    public bottom_menu(Context context, RelativeLayout wrapper){
        this.context = context;
        this.wrapper = wrapper;

        //colors
        iClrPrimary = R.color.colorPrimaryDark;
        iClrAccent = R.color.colorAccent;

        btn_qty = 3;
        //activities
        aActivities = new Class[btn_qty];
        aActivities[0] = MainActivity.class;
        aActivities[1] = DungeonActivity.class;
        aActivities[2] = AbiltiesActivity.class;

        //images
        aImageRes = new int[btn_qty];
        aImageRes[0] = R.drawable.ic_camping_tent;
        aImageRes[1] = R.drawable.ic_wooden_door;
        aImageRes[2] = R.drawable.ic_module_small_white;//R.drawable.ic_switch_ability;


        //get and set active activity
        switch (context.getClass().getSimpleName()){
            case "MainActivity":
                activeButton = 0;
                break;
            case "DungeonActivity":
                activeButton = 1;
                break;
            case "AbiltiesActivity":
                activeButton = 2;
                break;
            default:
                activeButton = 0;
        }

        createView(btn_qty);

        wrapper.addView(linearLayout, linearLayoutParams);

        isVisible = true;
    }

    private void createView(int quantity){
        linearLayout = new LinearLayout(context);

        linearLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.BOTTOM);
        linearLayout.setLayoutParams(linearLayoutParams);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setBackground(context.getDrawable(R.drawable.shadow_top));

        float button_width = 100 / quantity; //in percentage
        LinearLayout.LayoutParams ButtonParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, button_width);
        ButtonParams.gravity = Gravity.BOTTOM | Gravity.CENTER;

        buttons = new ImageView[quantity];
        for (int i=0; i < buttons.length; i++){
            buttons[i] = new ImageView(context);
            buttons[i].setLayoutParams(ButtonParams);
            buttons[i].setPadding(16,32,16,32);
            buttons[i].setId(i);
            buttons[i].setImageResource(aImageRes[i]);

            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickButton(v);
                }
            });

            if (i == activeButton) {
                buttons[i].setBackgroundResource(iClrAccent);
            }else {
                buttons[i].setBackgroundResource(iClrPrimary);
            }

            linearLayout.addView(buttons[i], ButtonParams);
        }
    }

    private void startActivity(int id){
        Intent newIntent = new Intent(context.getApplicationContext(), aActivities[id]);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity( newIntent );
    }

    private void onClickButton(View v){
        if (activeButton != v.getId()) {
            startActivity(v.getId());
            //context.startActivity(new Intent(context.getApplicationContext(), aActivities[ v.getId()]));
        }
    }

    public void onBackPressed(){
        if (activeButton != 0){
            startActivity(0);
        }else{
            //go to homescreen
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public void hide(){
        this.linearLayout.setVisibility(View.GONE);
        isVisible = false;
    }

    public void show(){
        this.linearLayout.setVisibility(View.VISIBLE);
        isVisible = true;
    }

    public boolean isVisible(){
        return isVisible;
    }

}
