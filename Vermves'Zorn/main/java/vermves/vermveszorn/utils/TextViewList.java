package vermves.vermveszorn.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Martin on 26.02.2016.
 */
public class TextViewList {
    private final LinearLayout linearLayout;
    private final Context context;
    private String align;
    private int index;

    public TextViewList(LinearLayout linearLayout, Context context, String align) {
        this.linearLayout = linearLayout;
        this.context = context;
        this.align = align;
        this.index = 0;

        if (align.equals("right") || align.equals("left")){
            this.linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        }else if (align.equals("top") || align.equals("bottom")){
            this.linearLayout.setOrientation(LinearLayout.VERTICAL);
        }
    }

    public void add(String text) {
        TextView item = new TextView(context);
        item.setText(text);

        //First in First out
        int position= 0;
        if (this.align.equals("right") || this.align.equals("bottom")){
            //Last in Last out
            position = index;
        }

        linearLayout.addView(item, position);
        index++;
        //animation + delete on animation end
        animateTextView(position);
    }

    public void animateTextView(int position) {
        if (index > position) {
            final View item = linearLayout.getChildAt(position);

            Animation animation = new AlphaAnimation(1.0f, 0.0f);
            animation.setDuration(1000);
            animation.setStartOffset(1000);
            animation.setFillAfter(true);
            animation.setFillEnabled(true);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    linearLayout.removeView(item);
                    index--;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

            item.startAnimation(animation);
        }
    }

}
