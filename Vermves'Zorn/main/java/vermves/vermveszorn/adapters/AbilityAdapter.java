package vermves.vermveszorn.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Map;

import vermves.vermveszorn.interfaces.defaultAbilityInterface;
import vermves.vermveszorn.maps.abilitiesMap;
import vermves.vermveszorn.R;
import vermves.vermveszorn.interfaces.abilityInterface;
import vermves.vermveszorn.utils.util;

/**
 * Created by Martin on 05.03.2016.
 */

public class AbilityAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] input_abilities;

    public AbilityAdapter(Context context, String[] input_abilities_) {
        super(context, -1, input_abilities_);
        this.context = context;
        this.input_abilities = input_abilities_;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.ability_listview_layout, parent, false);

        TextView textView_title = (TextView) rowView.findViewById(R.id.title);
        final RelativeLayout info_view = (RelativeLayout) rowView.findViewById(R.id.info_view);
        final TextView textView_info = (TextView) rowView.findViewById(R.id.description);
        final TextView textView_zorn_desc = (TextView) rowView.findViewById(R.id.zorn_desc);
        final ImageView imgView_zorn_icon = (ImageView) rowView.findViewById(R.id.zorn_icon);
        final TextView textView_duration_text = (TextView) rowView.findViewById(R.id.duration_text);
        final ImageView imgView_duration_icon = (ImageView) rowView.findViewById(R.id.duration_icon);

        final LinearLayout attributes_wrapper = (LinearLayout) rowView.findViewById(R.id.attributes_wrapper);

        final TextView textView_vitality = (TextView) rowView.findViewById(R.id.vitality_text);
        final TextView textView_power = (TextView) rowView.findViewById(R.id.power_text);
        final TextView textView_malice = (TextView) rowView.findViewById(R.id.malice_text);
        final TextView textView_recovery = (TextView) rowView.findViewById(R.id.recovery_text);

        final ImageView imgView_vitality = (ImageView) rowView.findViewById(R.id.vitality_icon);
        final ImageView imgView_power = (ImageView) rowView.findViewById(R.id.power_icon);
        final ImageView imgView_malice = (ImageView) rowView.findViewById(R.id.malice_icon);
        final ImageView imgView_recovery = (ImageView) rowView.findViewById(R.id.recovery_icon);

        info_view.setVisibility(View.GONE);

        final abilitiesMap CreateActionsMap= new abilitiesMap();
        final Map<String, defaultAbilityInterface> abilities = CreateActionsMap.getHashmap();

        String tmp;

        //title
        tmp = util.getStringResourceByName(context, input_abilities[position]);
        textView_title.setText(tmp);

        //description
        tmp = util.getActionDescription(context, input_abilities[position]);
        textView_info.setText(tmp);

        //zorn requirement and duration
        if (abilities.get(input_abilities[position]).passive_permanent()) {
            /* is passive permanent */
            //set text zorn req
            imgView_zorn_icon.setVisibility(View.GONE);
            textView_zorn_desc.setText(R.string.passive);

            //set text duration
            imgView_duration_icon.setVisibility(View.GONE);
            textView_duration_text.setVisibility(View.GONE);
        } else {
            textView_zorn_desc.setText(String.valueOf(abilities.get(input_abilities[position]).zorn_requirement()));
            textView_duration_text.setText(String.valueOf( (float) abilities.get(input_abilities[position]).duration() / 1000 + "s" ));
        }

        //attributes
        boolean hasAttributes = false;
        if (abilities.get(input_abilities[position]).vitality() > 0 ) {
            textView_vitality.setText(String.valueOf(abilities.get(input_abilities[position]).vitality()));
            hasAttributes = true;
        } else {
            textView_vitality.setVisibility(View.GONE);
            imgView_vitality.setVisibility(View.GONE);
        }
        if (abilities.get(input_abilities[position]).power() > 0) {
            textView_power.setText(String.valueOf(abilities.get(input_abilities[position]).power()));
            hasAttributes = true;
        } else {
            textView_power.setVisibility(View.GONE);
            imgView_power.setVisibility(View.GONE);
        }
        if (abilities.get(input_abilities[position]).malice() > 0) {
            textView_malice.setText(String.valueOf(abilities.get(input_abilities[position]).malice()));
            hasAttributes = true;
        } else {
            textView_malice.setVisibility(View.GONE);
            imgView_malice.setVisibility(View.GONE);
        }
        if (abilities.get(input_abilities[position]).recovery() > 0) {
            textView_recovery.setText(String.valueOf(abilities.get(input_abilities[position]).recovery()));
            hasAttributes = true;
        } else {
            textView_recovery.setVisibility(View.GONE);
            imgView_recovery.setVisibility(View.GONE);
        }

        if (hasAttributes) {
            attributes_wrapper.setVisibility(View.VISIBLE);
        } else {
            attributes_wrapper.setVisibility(View.GONE);
        }


        final CheckBox cb_expand = (CheckBox) rowView.findViewById(R.id.toggle_description);
        final RelativeLayout cb_wrapper = (RelativeLayout) rowView.findViewById(R.id.cb_wrapper);

        cb_wrapper.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_expand.setChecked(!cb_expand.isChecked());
                if (cb_expand.isChecked()){
                    expand(info_view);
                    cb_expand.setBackgroundResource(R.drawable.ic_expand_less_black_24dp);
                }else{
                    collapse(info_view);
                    cb_expand.setBackgroundResource(R.drawable.ic_expand_more_black_24dp);
                }
            }
        }));

        return rowView;
    }

    private static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density) * 4);
        v.startAnimation(a);
    }

    private static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density) * 4);
        v.startAnimation(a);
    }
}