package vermves.vermveszorn.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import vermves.vermveszorn.maps.iconMap;

/**
 * Created by Martin on 20.11.2016.
 */

public class imageViewList {
    private LinearLayout linearLayout;
    private Context context;
    private String align;

    private iconMap iconMap;

    public imageViewList(Context context, LinearLayout linearLayout, String align){
        this.context = context;
        this.linearLayout = linearLayout;
        this.align = align;

        if (align.equals("right") || align.equals("left")){
            this.linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        }else if (align.equals("top") || align.equals("bottom")){
            this.linearLayout.setOrientation(LinearLayout.VERTICAL);
        }

        iconMap = new iconMap();
    }

    //returns the position of the tag (int)
    //returns -1 if not found
    private int getIndexByTag(int tag){
        int i = 0;
        int size = linearLayout.getChildCount();

        while (i < size){
            if ((int) linearLayout.getChildAt(i).getTag() == tag){
                return i;
            }
            i++;
        }

        return -1;
    }

    //adds an image to list.
    public void add(String conditionName) {
        int imgResource = iconMap.getImageResource(conditionName);

        if (getIndexByTag(imgResource) == -1) {
            ImageView item = new ImageView(context);
            item.setImageResource(imgResource);
            item.setTag(imgResource);

            int position = 0;
            if (this.align.equals("right") || this.align.equals("bottom")) {
                position = linearLayout.getChildCount();
            }

            linearLayout.addView(item, position);
        }
    }

    //removes an image if exists
    public void remove(String conditionName) {
        int imgResource = iconMap.getImageResource(conditionName);
        int pos = getIndexByTag(imgResource);

        if (pos != -1)
            linearLayout.removeViewAt(pos);
    }
}
