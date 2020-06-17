package vermves.vermveszorn.activities;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Map;

import vermves.vermveszorn.interfaces.defaultAbilityInterface;
import vermves.vermveszorn.maps.abilitiesMap;
import vermves.vermveszorn.adapters.AbilityAdapter;
import vermves.vermveszorn.R;
import vermves.vermveszorn.databaseHelper.SaveGame_DataBaseHelper;
import vermves.vermveszorn.utils.bottom_menu;
import vermves.vermveszorn.interfaces.abilityInterface;
import vermves.vermveszorn.utils.util;

public class AbiltiesActivity extends AppCompatActivity {

    //bottom_menu
    RelativeLayout bottom_menu_wrapper;
    bottom_menu bottom_menu;

    Button btnCancel_selection;

    //Layout
    RelativeLayout AbilitiesActivity_Layout;
    RelativeLayout ability_overview;
    RelativeLayout ability_selection;

    ListView listview;
    AbilityAdapter listview_adapter;

    //various
    boolean listviewMode = false;

    //save game database
    private SaveGame_DataBaseHelper dbSaveGame;

    //Ability List
    String[] abilitylist_action0;
    String[] abilitylist_action1;
    String[] abilitylist_action2;
    String[] abilitylist_action3;

    String ablilty_raw_name;

    //ability map
    final abilitiesMap CreateActionsMap= new abilitiesMap();
    final Map<String, defaultAbilityInterface> abilities = CreateActionsMap.getHashmap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abilties);

        //Layout
        AbilitiesActivity_Layout = (RelativeLayout) findViewById(R.id.AbilitiesActivity_Layout);
        ability_overview = (RelativeLayout) findViewById(R.id.ability_overview);
        ability_overview.setVisibility(View.VISIBLE);
        ability_selection = (RelativeLayout) findViewById(R.id.ability_selection);
        ability_selection.setVisibility(View.GONE);
        ability_selection.setAlpha(0f);

        //save game
        dbSaveGame = new SaveGame_DataBaseHelper(this);

        //set text in view
        setAbilityTexts(0);
        setAbilityTexts(1);
        setAbilityTexts(2);
        setAbilityTexts(3);

        listview = (ListView) findViewById(R.id.ability_listview);

        abilitylist_action0 = new String[]{
                "dagger",
                "sword",
                "halberd",
                "icecrack"
        };

        abilitylist_action1 = new String[]{
                "final_thrust",
                "gash",
                "meteor",
                "earthquake"
        };

        abilitylist_action2 = new String[]{
                "bandage_wounds",
                "blood_is_power",
                "recovery_passive"
        };

        abilitylist_action3 = new String[]{
                "victory_is_mine",
                "blood_transfer",
                "rime",
                "aftershock"
        };

        //bottom_menu
        bottom_menu_wrapper = (RelativeLayout) findViewById(R.id.wrapper);
        bottom_menu = new bottom_menu(this, bottom_menu_wrapper);

        btnCancel_selection = (Button) findViewById(R.id.cancel_selection);
    }

    public final void OnClickShowListView(int id){
        final int buttonTag = id;
        final String [] selected_ability_array;

        switch(buttonTag){
            case 0:
                selected_ability_array = abilitylist_action0;
                break;
            case 1:
                selected_ability_array = abilitylist_action1;
                break;
            case 2:
                selected_ability_array = abilitylist_action2;
                break;
            case 3:
                selected_ability_array = abilitylist_action3;
                break;
            default:
                selected_ability_array = new String[]{};
        }

        listview_adapter = new AbilityAdapter(this, selected_ability_array);
        listview.setAdapter(listview_adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int pos, long id) {
                String ability = String.valueOf(selected_ability_array[pos]);
                dbSaveGame.insertAction(buttonTag, ability);
                //remove view
                RelativeLayout wrapper = util.getRelativeLayoutByString(AbiltiesActivity.this, "ability" + buttonTag + "_wrapper");
                wrapper.removeAllViewsInLayout();
                //add view
                setAbilityTexts(buttonTag);

                HideListView();
            }
        });

        btnCancel_selection.setVisibility(View.VISIBLE);
        ShowListView();

    }


    public void ShowListView(){
        listviewMode = true;
        LinearLayoutFadeInAndOut(ability_selection, ability_overview, 250, 0);
    }
    public void HideListView(){
        listviewMode = false;
        LinearLayoutFadeInAndOut(ability_overview, ability_selection, 250, 0);
        btnCancel_selection.setVisibility(View.GONE);
    }

    public void OnClickHideListView(View v){
        HideListView();
    }

    public void OnClickFinish(View v) {
        dbSaveGame.close();
        finish();
    }

    private void LinearLayoutFadeInAndOut(final RelativeLayout _FadeIn, final RelativeLayout _FadeOut, int duration, int delay) {
        if (_FadeIn.getAlpha() == 0f) {
            _FadeOut.setAlpha(1f);

            // Animate the new view to 100% opacity
            _FadeIn.animate()
                    .alpha(1f)
                    .setDuration(duration)
                    .setStartDelay(delay)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            _FadeIn.setVisibility(View.VISIBLE);
                            _FadeIn.setAlpha(0f);
                        }
                    });

            // Animate the current view to 0% opacity.
            _FadeOut.animate()
                    .alpha(0f)
                    .setDuration(duration)
                    .setStartDelay(delay)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            _FadeOut.setVisibility(View.GONE);
                        }
                    });
        }
    }

    private void setAbilityTexts(final int id) {
        final LayoutInflater inflater = getLayoutInflater();
        final RelativeLayout wrapper = util.getRelativeLayoutByString(this, "ability" + id + "_wrapper");
        final View view = inflater.inflate(R.layout.activity_ability_content_layout, wrapper, false);

        ablilty_raw_name = dbSaveGame.getSaveGameColumn("action" + id);

        //button
        final Button abilityButton = (Button) view.findViewById(R.id.ability);
        abilityButton.setText(util.getStringResourceByName(this, ablilty_raw_name));
        abilityButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OnClickShowListView(id);
            }
        });

        //desc
        final TextView abilityDesc = (TextView) view.findViewById(R.id.ability_description);
        abilityDesc.setText(util.getActionDescription(this, ablilty_raw_name));

        //zorn req
        final ImageView abilityZornIcon = (ImageView) view.findViewById(R.id.zorn_icon);
        final TextView abilityZornReq = (TextView) view.findViewById(R.id.ability_zorn_req);
        //duration
        final ImageView abilityDurationIcon = (ImageView) view.findViewById(R.id.duration_icon);
        final TextView abilityDuration = (TextView) view.findViewById(R.id.ability_duration);


        if ( abilities.get(ablilty_raw_name).passive_permanent() ) {
            //is passive permanent
            abilityZornReq.setText(R.string.passive);
            abilityZornIcon.setVisibility(View.GONE);
            abilityDurationIcon.setVisibility(View.GONE);
            abilityDuration.setVisibility(View.GONE);
        } else {
            abilityZornReq.setText( String.valueOf(abilities.get(ablilty_raw_name).zorn_requirement()));
            abilityDuration.setText( String.valueOf((float) abilities.get(ablilty_raw_name).duration() / 1000 + "s"));
        }

        final TextView textView_vitality = (TextView) view.findViewById(R.id.vitality_text);
        final TextView textView_power = (TextView) view.findViewById(R.id.power_text);
        final TextView textView_malice = (TextView) view.findViewById(R.id.malice_text);
        final TextView textView_recovery = (TextView) view.findViewById(R.id.recovery_text);

        final ImageView imgView_vitality = (ImageView) view.findViewById(R.id.vitality_icon);
        final ImageView imgView_power = (ImageView) view.findViewById(R.id.power_icon);
        final ImageView imgView_malice = (ImageView) view.findViewById(R.id.malice_icon);
        final ImageView imgView_recovery = (ImageView) view.findViewById(R.id.recovery_icon);

        if (abilities.get(ablilty_raw_name).vitality() > 0 ) {
            textView_vitality.setText(String.valueOf(abilities.get(ablilty_raw_name).vitality()));
        } else {
            imgView_vitality.setVisibility(View.GONE);
            textView_vitality.setVisibility(View.GONE);
        }
        if (abilities.get(ablilty_raw_name).power() > 0 ) {
            textView_power.setText(String.valueOf(abilities.get(ablilty_raw_name).power()));
        } else {
            imgView_power.setVisibility(View.GONE);
            textView_power.setVisibility(View.GONE);
        }
        if (abilities.get(ablilty_raw_name).malice() > 0 ) {
            textView_malice.setText(String.valueOf(abilities.get(ablilty_raw_name).malice()));
        } else {
            imgView_malice.setVisibility(View.GONE);
            textView_malice.setVisibility(View.GONE);
        }
        if (abilities.get(ablilty_raw_name).recovery() > 0 ) {
            textView_recovery.setText(String.valueOf(abilities.get(ablilty_raw_name).recovery()));
        } else {
            imgView_recovery.setVisibility(View.GONE);
            textView_recovery.setVisibility(View.GONE);
        }

        wrapper.addView(view);
    }

    //function of back button
    public void onBackPressed() {
        if (listviewMode) {
            HideListView();
        } else {
            bottom_menu.onBackPressed();
        }
    }

}





