package vermves.vermveszorn.utils;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

import vermves.vermveszorn.R;

/**
 * Created by martin on 06.01.17.
 */

public final class util {

    //randomizer
    public static int random(int min, int max) {
        Random r = new Random();
        int ret = r.nextInt(max - min + 1) + min;
        return ret;
    }

    //gets the String-value by ID
    public static String getStringResourceByName(Context context, String string) {
        int resId = context.getResources().getIdentifier(string, "string", "vermves.vermveszorn");
        return context.getString(resId);
    }

    //get textview by string
    public static TextView getTextviewByString(Context context, String name) {
        int id = context.getResources().getIdentifier(name, "id", "vermves.vermveszorn");
        Activity activity = (Activity) context;
        return (TextView) activity.findViewById(id);
    }
    //get button by string
    public static Button getButtonByString(Context context, String name) {
        int id = context.getResources().getIdentifier(name, "id", "vermves.vermveszorn");
        Activity activity = (Activity) context;
        return (Button) activity.findViewById(id);
    }
    //get imageview by string
    public static ImageView getImageViewByString(Context context, String name) {
        int id = context.getResources().getIdentifier(name, "id", "vermves.vermveszorn");
        Activity activity = (Activity) context;
        return (ImageView) activity.findViewById(id);
    }
    //get RelativeLayout by string
    public static RelativeLayout getRelativeLayoutByString(Context context, String name) {
        int id = context.getResources().getIdentifier(name, "id", "vermves.vermveszorn");
        Activity activity = (Activity) context;
        return (RelativeLayout) activity.findViewById(id);
    }

    public static String getActionDescription(Context context, String action) {
        String desc_id = action + "_desc";
        return getStringResourceByName(context, desc_id);
    }

    //animate Textview color
    public static void animateTextviewColor(TextView tv, int animateColorFrom, int animateColorTo) {
        tv.setTextColor(animateColorFrom);

        //animate text colors
        ObjectAnimator textColorAnimation = ObjectAnimator.ofArgb(tv, "textColor", animateColorFrom, animateColorTo);
        textColorAnimation.setDuration(250);
        textColorAnimation.setStartDelay(250);
        textColorAnimation.setEvaluator(new ArgbEvaluator());
        textColorAnimation.start();
    }

}
