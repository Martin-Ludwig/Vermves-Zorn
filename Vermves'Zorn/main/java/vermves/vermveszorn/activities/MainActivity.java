package vermves.vermveszorn.activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import vermves.vermveszorn.R;
import vermves.vermveszorn.databaseHelper.SaveGame_DataBaseHelper;
import vermves.vermveszorn.interfaces.abilityInterface;
import vermves.vermveszorn.interfaces.defaultAbilityInterface;
import vermves.vermveszorn.maps.abilitiesMap;
import vermves.vermveszorn.utils.bottom_menu;
import vermves.vermveszorn.utils.util;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout bottom_menu_wrapper;
    vermves.vermveszorn.utils.bottom_menu bottom_menu;

    //textviews
    TextView ability0;
    TextView ability1;
    TextView ability2;
    TextView ability3;

    TextView tv_vitality;
    TextView tv_power;
    TextView tv_malice;
    TextView tv_recovery;

    //rank and experience
    TextView tv_rank;
    TextView tv_exp;
    TextView tv_nextRankExp;
    ProgressBar next_rank_porgressBar;

    private int exp;
    private int nextRankExp;

    //hero actions and attributes
    private String[] actions;
    private int vitality = 0;
    private int power = 0;
    private int malice = 0;
    private int recovery = 0;

    //ability map
    private final abilitiesMap CreateActionsMap= new abilitiesMap();
    private final Map<String, defaultAbilityInterface> abilities = CreateActionsMap.getHashmap();

    //save game database
    private SaveGame_DataBaseHelper dbSaveGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottom_menu_wrapper = (RelativeLayout) findViewById(R.id.wrapper);
        bottom_menu = new bottom_menu(this, bottom_menu_wrapper);

        ability0 = (TextView) findViewById(R.id.ma_ability0_text);
        ability1 = (TextView) findViewById(R.id.ma_ability1_text);
        ability2 = (TextView) findViewById(R.id.ma_ability2_text);
        ability3 = (TextView) findViewById(R.id.ma_ability3_text);

        tv_vitality = (TextView) findViewById(R.id.ma_vitality_text);
        tv_power = (TextView) findViewById(R.id.ma_power_text);
        tv_malice = (TextView) findViewById(R.id.ma_malice_text);
        tv_recovery = (TextView) findViewById(R.id.ma_recovery_text);

        tv_rank = (TextView) findViewById(R.id.ma_rank_text);
        tv_exp = (TextView) findViewById(R.id.ma_exp_text);
        tv_nextRankExp = (TextView) findViewById(R.id.ma_nextRankExp_text);
        next_rank_porgressBar = (ProgressBar) findViewById(R.id.next_rank_progressbar);

        dbSaveGame = new SaveGame_DataBaseHelper(this);

        actions = new String[]{
                dbSaveGame.getSaveGameColumn("action0"),
                dbSaveGame.getSaveGameColumn("action1"),
                dbSaveGame.getSaveGameColumn("action2"),
                dbSaveGame.getSaveGameColumn("action3"),
        };

        for (int i=0; i<4; i++){
            vitality += abilities.get(actions[i]).vitality();
            power += abilities.get(actions[i]).power();
            malice += abilities.get(actions[i]).malice();
            recovery += abilities.get(actions[i]).recovery();
        }

        ability0.setText(util.getStringResourceByName(this, actions[0]));
        tv_vitality.setText(getString(R.string.vitality_value, vitality));

        ability1.setText(util.getStringResourceByName(this, actions[1]));
        tv_recovery.setText(getString(R.string.recovery_value, recovery));

        ability2.setText(util.getStringResourceByName(this, actions[2]));
        tv_power.setText(getString(R.string.power_value, power));

        ability3.setText(util.getStringResourceByName(this, actions[3]));
        tv_malice.setText(getString(R.string.malice_value, malice));

        exp = dbSaveGame.getExp();
        nextRankExp = dbSaveGame.getNextRankExp();

        tv_rank.setText(getString(R.string.rank, dbSaveGame.getRank()));
        tv_nextRankExp.setText(getString(R.string.amount_of_max, exp, nextRankExp));
        next_rank_porgressBar.setMax(nextRankExp);
        tv_exp.setText(getString(R.string.exp, exp));
        next_rank_porgressBar.setProgress(exp);

        //dbSaveGame.close();
    }

    @Override
    protected void onDestroy() {
        dbSaveGame.close();
        super.onDestroy();
    }

    //function of back button
    public void onBackPressed() {
        bottom_menu.onBackPressed();
    }

    public void resetExp(View v){
        Toast.makeText(this,"reset", Toast.LENGTH_SHORT).show();
        dbSaveGame.resetExp();

        exp = dbSaveGame.getExp();
        nextRankExp = dbSaveGame.getNextRankExp();

        tv_rank.setText(getString(R.string.rank, dbSaveGame.getRank()));

        tv_nextRankExp.setText(getString(R.string.amount_of_max, exp, nextRankExp));
        next_rank_porgressBar.setMax(nextRankExp);

        tv_exp.setText(getString(R.string.exp, exp));
        next_rank_porgressBar.setProgress(exp);
    }

    public void onClickRankUp(View v){

        if (exp > nextRankExp) {
            Toast.makeText(this,"next rank", Toast.LENGTH_SHORT).show();

            dbSaveGame.addExp(-1 * nextRankExp);
            dbSaveGame.addRank();

            exp = dbSaveGame.getExp();
            nextRankExp = dbSaveGame.getNextRankExp();

            tv_rank.setText(getString(R.string.rank, dbSaveGame.getRank()));

            tv_nextRankExp.setText(getString(R.string.amount_of_max, exp, nextRankExp));
            next_rank_porgressBar.setMax(nextRankExp);

            tv_exp.setText(getString(R.string.exp, exp));
            next_rank_porgressBar.setProgress(exp);


        }else{
            Toast.makeText(this,"not enough exp. " + exp + " / " + nextRankExp, Toast.LENGTH_SHORT).show();
        }
    }
}
