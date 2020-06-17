package vermves.vermveszorn.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.os.Handler;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Random;

import vermves.vermveszorn.R;
import vermves.vermveszorn.databaseHelper.SaveGame_DataBaseHelper;
import vermves.vermveszorn.models.player;
import vermves.vermveszorn.utils.CountDownTimerWithPause;
import vermves.vermveszorn.utils.TextViewList;
import vermves.vermveszorn.models.dungeon;
import vermves.vermveszorn.models.hero;
import vermves.vermveszorn.utils.imageViewList;
import vermves.vermveszorn.utils.sharedPrefs;
import vermves.vermveszorn.utils.util;

public class BattleActivity extends AppCompatActivity {
    private static final String TAG = BattleActivity.class.getSimpleName();

    RelativeLayout activityView;

    //dungeon progress textviews
    private TextView text_level;
    private TextView hero_level;
    private TextView tv_countdown;
    private TextView tv_start_countdown;

    //player, hero textviews
    private TextView enemy_name;
    private LinearLayout enemy_debuff_layout;
    private imageViewList enemy_debuff_list;
    private TextView enemy_hp;
    private ProgressBar enemy_hp_bar;
    private ProgressBar enemy_ability_bar;
    private TextView enemy_ability_text;
    private LinearLayout enemy_dmg_log_layout;
    private TextViewList enemy_dmg_log_list;

    private TextView hero_hp;
    private ProgressBar hero_hp_bar;
    private TextView hero_zorn;
    private LinearLayout hero_debuff_layout;
    private imageViewList hero_debuff_list;
    private ProgressBar hero_ability_bar;
    private TextView hero_ability_text;
    private LinearLayout hero_dmg_log_layout;
    private TextViewList hero_dmg_log_list;

    Button action0;
    Button action1;
    Button action2;
    Button action3;

    Button new_game_button;
    Button menu_button;

    //save game
    private SaveGame_DataBaseHelper dbSaveGame;

    //player
    private String[] actions;
    private vermves.vermveszorn.models.hero hero;

    //enemy
    private vermves.vermveszorn.models.enemy enemy;

    //dungeon
    vermves.vermveszorn.models.dungeon dungeon;
    private TextView dungeon_text;
    private boolean ActionOverTime_isAlive = true;
    private boolean ActionOverTime_isRunning = false;
    private final int ActionOverTime_interval = 250;

    //battle end
    private TextView battle_message;

    //EndScreen
    private RelativeLayout BattleScreen;
    private RelativeLayout EndScreen;
    private TextView endscreen_message;
    private TextView endscreen_info;
    private TextView lostExp_gained;

    //various
    private int maxPublicDungeons;
    private boolean endscreenMode = false;

    private int ProgressBar_interval = 10;

    //functions
                private void LinearLayoutFadeInAndOut(final RelativeLayout _FadeIn, final RelativeLayout _FadeOut, int duration, int delay) {
                    //Log.d("MainActivity" ,"_FadeIn.getAlpha() = " + _FadeIn.getAlpha() );
                    if (_FadeIn.getAlpha() == 0f) {
                        action0.setClickable(false);
                        action1.setClickable(false);
                        action2.setClickable(false);
                        action3.setClickable(false);
                        new_game_button.setClickable(false);
                        menu_button.setClickable(false);
                        _FadeOut.setAlpha(0.8f);


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

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        //_FadeIn.setVisibility(View.VISIBLE);
                                        action0.setClickable(true);
                                        action1.setClickable(true);
                                        action2.setClickable(true);
                                        action3.setClickable(true);
                                        activityView.setAlpha(1);
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
                                        new_game_button.setClickable(true);
                                        menu_button.setClickable(true);
                                    }
                                });
                    }
                }

                public void onBackPressed() {
                    //disables return-button
                    if (endscreenMode) {
                        backToMenu();
                    }
                }


    //onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_battle);
        activityView = (RelativeLayout) findViewById(R.id.activityView);

        /**
         * Dungeon
         */

        //gets the dungeon id from the previously created intent
        Intent intent = getIntent();
        maxPublicDungeons = intent.getIntExtra("maxDungeons", 1);

        dungeon = new dungeon(this, intent.getStringExtra("dungeonID"));

        dungeon_text = (TextView) findViewById(R.id.dungeon_text);
        dungeon_text.setText(util.getStringResourceByName(this, dungeon.getDungeonName()));

        tv_countdown = (TextView) findViewById(R.id.countdown);
        tv_start_countdown = (TextView) findViewById(R.id.start_countdown);

        /**
         * Enemy
         */

        enemy = dungeon.createEnemy();

        enemy_name = (TextView) findViewById(R.id.enemy_name);
        enemy_name.setText(util.getStringResourceByName(this, enemy.getNAME()));
        util.animateTextviewColor(enemy_name, getResources().getColor(R.color.colorAccent), getResources().getColor(enemy.getRarity()));

        enemy_debuff_layout = (LinearLayout) findViewById(R.id.enemy_debuff_list);
        enemy_debuff_list = new imageViewList(this, enemy_debuff_layout, "left");

        enemy_hp = (TextView) findViewById(R.id.enemy_hp_text);
        enemy_hp.setText(getString(R.string.health_val, String.valueOf(enemy.getHP())));
        enemy_hp_bar = (ProgressBar) findViewById(R.id.enemy_hp_bar);
        enemy_hp_bar.setMax(enemy.getMAXHP());
        enemy_hp_bar.setProgress(enemy.getHP());

        enemy_ability_bar = (ProgressBar) findViewById(R.id.enemy_ability_bar);
        enemy_ability_bar.setVisibility(View.GONE);
        enemy_ability_text = (TextView) findViewById(R.id.enemy_ability_text);
        enemy_ability_text.setVisibility(View.GONE);

        enemy_dmg_log_layout = (LinearLayout) findViewById(R.id.enemy_dmg_log);
        enemy_dmg_log_list = new TextViewList(enemy_dmg_log_layout, this, "left");

        /**
         * Hero
         */

        dbSaveGame = new SaveGame_DataBaseHelper(this);

        actions = new String[]{
                dbSaveGame.getSaveGameColumn("action0"),
                dbSaveGame.getSaveGameColumn("action1"),
                dbSaveGame.getSaveGameColumn("action2"),
                dbSaveGame.getSaveGameColumn("action3"),
        };

        //hero attributes
        int health = 600;
        int attack = 60;
        int healing_power = 60;
        int condition_dmg = 60;

        int speed = 1;

        hero = new hero("Hero", health, attack, healing_power, condition_dmg, speed, actions); //hp, dmg, speed-factor

        hero_hp = (TextView) findViewById(R.id.hero_hp_text);
        hero_hp.setText(getString(R.string.health_val, String.valueOf(hero.getHP())));
        hero_hp_bar = (ProgressBar) findViewById(R.id.hero_hp_bar);
        hero_hp_bar.setMax(hero.getMAXHP());
        hero_hp_bar.setProgress(hero.getHP());
        hero_dmg_log_layout = (LinearLayout) findViewById(R.id.hero_dmg_log);
        hero_dmg_log_list = new TextViewList(hero_dmg_log_layout, this, "left");
        hero_zorn = (TextView) findViewById(R.id.hero_anger);
        hero_zorn.setText(String.valueOf(hero.getZORN()));
        hero_debuff_layout = (LinearLayout) findViewById(R.id.hero_debuff_list);
        hero_debuff_list = new imageViewList(this, hero_debuff_layout, "left");

        hero_ability_bar = (ProgressBar) findViewById(R.id.hero_ability_bar);
        hero_ability_text = (TextView) findViewById(R.id.hero_ability_text);
        hero_ability_text.setVisibility(View.GONE);
        hero_ability_bar.setVisibility(View.GONE);

        text_level = (TextView) findViewById(R.id.level);
        text_level.setText(getString(R.string.level, dungeon.getLevel(), dungeon.getMaxLevel()));
        hero_level = (TextView) findViewById(R.id.hero_level);
        hero_level.setText(getString(R.string.hero_level, String.valueOf(hero.getLevel())));

        action0 = (Button) findViewById(R.id.action0);
        action1 = (Button) findViewById(R.id.action1);
        action2 = (Button) findViewById(R.id.action2);
        action3 = (Button) findViewById(R.id.action3);

        //action0.setText(util.getStringResourceByName(this, hero.getACTION(0)) + " (" + hero.zorn_req(0) + ")");
        action0.setText(util.getStringResourceByName(this, hero.getACTION(0)));
        action1.setText(util.getStringResourceByName(this, hero.getACTION(1)));
        action2.setText(util.getStringResourceByName(this, hero.getACTION(2)));
        action3.setText(util.getStringResourceByName(this, hero.getACTION(3)));

        ActionButtonZornToggle();

        /**
         * Endscreen
         */

        BattleScreen = (RelativeLayout) findViewById(R.id.BattleScreen);
        BattleScreen.setAlpha(1f);
        BattleScreen.setVisibility(View.VISIBLE);
        EndScreen = (RelativeLayout) findViewById(R.id.EndScreen);
        EndScreen.setAlpha(0f);
        EndScreen.setVisibility(View.GONE);

        battle_message = (TextView) findViewById(R.id.battle_message);
        battle_message.setVisibility(View.GONE);

        endscreen_message = (TextView) findViewById(R.id.endscreen_message);
        endscreen_message.setText(getString(R.string.level, dungeon.getLevel(), dungeon.getMaxLevel()));
        endscreen_info = (TextView) findViewById(R.id.endscreen_info);

        lostExp_gained = (TextView) findViewById(R.id.lostExp_gained);

        new_game_button = (Button) findViewById(R.id.new_game);
        menu_button = (Button) findViewById(R.id.menu_button);


        /**
         * Start Game
         */

        AOT();
        startCountdown();
    }

    @Override
    protected void onDestroy() {
        ActionOverTime_isAlive = false;
        dbSaveGame.close();
        super.onDestroy();
    }

    private void backToMenu(){
        ActionOverTime_isAlive = false;
        dbSaveGame.close();

        Intent newIntent = new Intent(getApplicationContext(), MainActivity.class);
        newIntent.addCategory(Intent.CATEGORY_HOME);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(newIntent);
    }

    //back to menu
    public void OnClickFinish(View v) {
        backToMenu();
    }

    //button click
    public void onActionClick(View v) {
        if ((hero.getHP() > 0) && (enemy.getHP() > 0)) {

            Button b = (Button) v;
            int buttonTag = Integer.valueOf((String) b.getTag());

            if (hero.getZORN() >= hero.zorn_req(buttonTag)) {
                hero.setACTION(hero.getACTION(buttonTag));
                if (!hero.hasCondition("freezing") && !hero.hasCondition("stunned"))
                    hero_attack();
            }
        }
    }

    //new game
    private long mLastClickTime = 0;
    public void onClickNewGame(View v) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        //reset values
        dungeon.reset();
        hero.reset();
        hero_level.setText(getString(R.string.hero_level, String.valueOf(hero.getLevel())));
        hero.setHP(hero.getMAXHP());

        enemy = dungeon.createEnemy();
        enemy_name.setText(util.getStringResourceByName(this, enemy.getNAME()));
        util.animateTextviewColor(enemy_name, getResources().getColor(R.color.colorAccent), getResources().getColor(enemy.getRarity()));
        enemy_hp_bar.setMax(enemy.getMAXHP());

        //update text
        enemy_hp.setText(getString(R.string.health_val, String.valueOf(enemy.getHP())));
        enemy_hp_bar.setProgress(enemy.getHP());
        hero_hp.setText(getString(R.string.health_val, String.valueOf(hero.getHP())));
        hero_hp_bar.setMax(hero.getMAXHP());
        hero_hp_bar.setProgress(hero.getHP());
        hero_zorn.setText(String.valueOf(hero.getZORN()));
        text_level.setText(getString(R.string.level, dungeon.getLevel(), dungeon.getMaxLevel()));

        hero_ability_text.setVisibility(View.GONE);
        hero_ability_bar.setVisibility(View.GONE);
        enemy_ability_text.setVisibility(View.GONE);
        enemy_ability_bar.setVisibility(View.GONE);

        battle_message.setVisibility(View.GONE);
        //change to BattleScreen
        LinearLayoutFadeInAndOut(BattleScreen, EndScreen, 0, 0);

        ActionOverTime_isRunning = true;
        startCountdown();
    }

    //ui update
    public void UI_Update(){
        if (hero.getHP() <= 0) {
            //hero lost
            ActionOverTime_isRunning = false;
            if (hero.isAttacking())
                hero.cancelAction();

            enemy.cancelAction();

            battle_message.setVisibility(View.VISIBLE);
            battle_message.setText(getString(R.string.lost));

            String end_msg = getString(R.string.level, dungeon.getLevel(), dungeon.getMaxLevel());
            endscreen_message.setText(end_msg);

            String end_info = getString(R.string.lost_info, util.getStringResourceByName(this, String.valueOf(enemy.getNAME())), util.getStringResourceByName(this, String.valueOf(enemy.getACTION())));
            endscreen_info.setText(end_info);
            //Change to EndScreen
            LinearLayoutFadeInAndOut(EndScreen, BattleScreen, 1000, 2000);
            endscreenMode = true;

            //set lost xp to current dungeon
            int currentXP = dbSaveGame.getExp();
            if (currentXP > 0) {
                lostExp_gained.setText(getString(R.string.battle_exp_lost, currentXP));
            }else{
                lostExp_gained.setVisibility(View.GONE);
            }
            dbSaveGame.updateLostValues(dungeon.getDungeonID(), currentXP);

            //roll new event dungeon
            sharedPrefs sharedPrefs = new sharedPrefs(this);
            int newEventId = util.random(1, maxPublicDungeons);
            sharedPrefs.setDungeonEventId(newEventId);

        }else if (enemy.getHP() <= 0) {
            //enemy lost
            if (enemy.isAttacking())
                enemy.cancelAction();

            hero.addXP();
            hero_hp_bar.setMax(hero.getMAXHP());
            hero_hp_bar.setProgress(hero.getHP());

            Log.i("enemy lost", "level = " + String.valueOf(hero.getLevel()) + ", XP progress = " + String.valueOf(hero.getProgress()) );
            hero_level.setText(getString(R.string.hero_level, String.valueOf(hero.getLevel())));

            dungeon.nextLevel();
            if (dungeon.getLevel() > dungeon.getMaxLevel()) {
                //dungeon finished, win
                ActionOverTime_isRunning = false;

                battle_message.setVisibility(View.VISIBLE);
                battle_message.setText(getString(R.string.won));

                String end_msg = getString(R.string.dungeon_complete, util.getStringResourceByName(this, dungeon.getDungeonName()));
                endscreen_message.setText(end_msg);


                //erhaltene erfahrung, reward
                sharedPrefs sharedPrefs = new sharedPrefs(this);

                int dungeon_base_reward = dungeon.getReward();
                if ( sharedPrefs.getDungeonEventId() == dungeon.getDungeonID() ) {
                    dungeon_base_reward *= 2;
                }

                int gainedExp = (int) ( ( 0.75 * Math.pow(dungeon.getRequirement(), 2) ) + ( dungeon.getMaxLevel() * dungeon.REWARD_PER_LEVEL ) + dungeon_base_reward );

                //collect lost exp
                int gainedLostExp = 0;
                if ( (dbSaveGame.getLostDungeon() == dungeon.getDungeonID()) && (dbSaveGame.getLostExp() > 0) ) {
                    gainedLostExp = dbSaveGame.getLostExp();
                    lostExp_gained.setText(getString(R.string.battle_lostExp_gained, gainedLostExp));
                    dbSaveGame.updateLostValues(0, 0);
                }else{
                    lostExp_gained.setVisibility(View.GONE);
                }

                dbSaveGame.addExp(gainedExp + gainedLostExp);

                endscreen_info.setText(getString(R.string.dungeon_reward, gainedExp));

                //Change to EndScreen
                LinearLayoutFadeInAndOut(EndScreen, BattleScreen, 1000, 2000);
                endscreenMode = true;

                //roll new dungeon event
                int newEventId = util.random(1, maxPublicDungeons);
                sharedPrefs.setDungeonEventId(newEventId);

            } else {
                //next enemy
                text_level.setText(getString(R.string.level, dungeon.getLevel(), dungeon.getMaxLevel()));

                enemy = dungeon.createEnemy();


                enemy_name.setText(util.getStringResourceByName(this, enemy.getNAME()));
                util.animateTextviewColor(enemy_name, getResources().getColor(R.color.colorAccent), getResources().getColor(enemy.getRarity()));
                enemy_hp_bar.setMax(enemy.getMAXHP());

                Log.i("enemy lost", "------ next enemy ------");
                Log.i("enemy lost", "name: " + enemy.getNAME());
                Log.i("enemy lost", "level: " + enemy.getLevel());
                Log.i("enemy lost", "max hp: " + enemy.getMAXHP());
                Log.i("enemy lost", "one tick attack: " + enemy.getOneTickAttack());
                Log.i("enemy lost", "major attack: " + enemy.getMajorAttack() );
                Log.i("enemy lost", "one tick healing: " + enemy.getOneTickHealing());
                Log.i("enemy lost", "major healing: " + enemy.getMajorHealing());
                Log.i("enemy lost", "one tick cond dmg: " + enemy.getOneTickConditionDmg());
                Log.i("enemy lost", "major cond dmg: " + enemy.getMajorConditionDmg());

                Log.i("enemy lost", "------ hero ------");
                Log.i("enemy lost", "level: " + hero.getLevel());
                Log.i("enemy lost", "max hp: " + hero.getMAXHP());
                Log.i("enemy lost", "one tick attack: " + hero.getOneTickAttack());
                Log.i("enemy lost", "major attack: " + hero.getMajorAttack() );
                Log.i("enemy lost", "one tick healing: " + hero.getOneTickHealing());
                Log.i("enemy lost", "major healing: " + hero.getMajorHealing());
                Log.i("enemy lost", "one tick cond dmg: " + hero.getOneTickConditionDmg());
                Log.i("enemy lost", "major cond dmg: " + hero.getMajorConditionDmg());
                Log.i("enemy lost", "------");

                KI_attack();
            }
        }

        enemy_hp.setText(getString(R.string.health_val, String.valueOf(enemy.getHP())));
        enemy_hp_bar.setProgress(enemy.getHP());

        hero_hp.setText(getString(R.string.health_val, String.valueOf(hero.getHP())));
        hero_zorn.setText(String.valueOf(hero.getZORN()));
        hero_hp_bar.setProgress(hero.getHP());

        UpdateDebufflist();

        ActionButtonZornToggle();
    }

    //enemy attack
    public void KI_attack() {
        if (enemy.isAttacking())
            enemy.cancelAction();

        enemy.chooseAction(hero);

        enemy_ability_text.setText(util.getStringResourceByName(this, enemy.getACTION()));
        enemy_ability_text.setVisibility(View.VISIBLE);
        enemy_ability_bar.setProgress(0);
        enemy_ability_bar.setVisibility(View.VISIBLE);

        final int duration = enemy.getActionDuration();
        enemy_ability_bar.setMax(duration);

        enemy.setIsAttacking(true);
        enemy.setAction(
            new CountDownTimerWithPause(duration, ProgressBar_interval) {
                public void onTick(long millisUntilFinished) {
                    enemy_ability_bar.setProgress(duration - (int) millisUntilFinished);
                    if ((enemy.getHP() <= 0) || (hero.getHP() <= 0)) {
                        this.cancel();
                    }

                    //stunned
                    if (enemy.hasCondition("stunned")) {
                        enemy_ability_text.setVisibility(View.INVISIBLE);
                        enemy_ability_bar.setVisibility(View.INVISIBLE);
                        pause();
                    }

                    //interrupt
                    if (!enemy.isAttacking()) {
                        KI_attack();
                        this.cancel();
                    }

                }

                public void onFinish() {
                    enemy_ability_bar.setProgress(duration);
                    enemy.use_action(hero);

                    DamageStep(enemy, hero);

                    //enemy damage log
                    if (enemy.getDMG() > 0)
                        enemy_dmg_log_list.add(" -" + enemy.getDMG() + " ");

                    if (enemy.getHEAL() > 0)
                        enemy_dmg_log_list.add(" +" + enemy.getHEAL() + " ");

                    UI_Update();

                    enemy_ability_text.setVisibility(View.GONE);
                    enemy_ability_bar.setVisibility(View.GONE);

                    if (enemy.getHP() >= 0 && hero.getHP() >= 0) {
                        enemy.setIsAttacking(false);
                        KI_attack();
                        this.cancel();
                    }else{
                        this.cancel();
                    }
                }
            }
        );
        enemy.startAction();
    }

    //hero attack
    private void hero_attack(){
        if (hero.isAttacking())
            hero.cancelAction();

        hero_ability_text.setText(util.getStringResourceByName(this, hero.getACTION()));

        hero_ability_bar.setProgress(0);
        final int duration = hero.getActionDuration();
        hero_ability_bar.setMax(duration);

        hero_ability_text.setVisibility(View.VISIBLE);
        hero_ability_bar.setVisibility(View.VISIBLE);

        hero.setIsAttacking(true);
        hero.setAction(
            new CountDownTimerWithPause(duration, ProgressBar_interval) {
                public void onTick(long millisUntilFinished) {
                    hero_ability_bar.setProgress(duration - (int) millisUntilFinished);
                    //on Win or Lose stop
                    if ( ( hero.getHP() <= 0 ) || (dungeon.getLevel() > dungeon.getMaxLevel()) || !hero.isAttacking() ) {
                        this.cancel();
                    }

                    //stunned
                    if (hero.hasCondition("stunned")) {
                        hero_ability_text.setVisibility(View.INVISIBLE);
                        hero_ability_bar.setVisibility(View.INVISIBLE);
                        pause();
                    }
                }

                public void onFinish() {
                    if (hero.getHP() <= 0) {
                        this.cancel();
                    }else {
                        hero_ability_bar.setProgress(duration);
                        hero.use_action(enemy);

                        DamageStep(hero, enemy);

                        if (hero.getDMG() > 0)
                            hero_dmg_log_list.add(" -" + hero.getDMG() + " ");
                        if (hero.getHEAL() > 0)
                            hero_dmg_log_list.add(" +" + hero.getHEAL() + " ");

                        UI_Update();

                        hero_ability_text.setVisibility(View.GONE);
                        hero_ability_bar.setVisibility(View.GONE);
                        hero.setIsAttacking(false);
                    }
                }
            }
        );
        hero.startAction();
    }

    //damage calculation
    private void DamageStep(player _player, player _target) {

        //BEFORE damage calculation

        //DAMAGE CALCULATION

        //physical damage
        if (_player.getDMG() > 0) {
            Log.i("DamageStep",_player.getNAME() + " dmgs " + _player.getDMG());
            _target.DamageStepPhysical(_player.getDMG());
        }


        //healing
        if (_player.getHEAL() > 0) {
            Log.i("DamageStep", _player.getNAME() + " heals " + _player.getHEAL());
            _player.DamageStepHealing(_player.getHEAL());
        }

        //AFTER damage calculation

        _player.clearDMG();
    }


    private void ActionButtonZornToggle(){
        //@TODO: i-wie als schleife
        int zorn_tmp = hero.getZORN();

        if (zorn_tmp >= hero.zorn_req(0)) {
            action0.setEnabled(true);
        }else{
            action0.setEnabled(false);
        }
        if (zorn_tmp >= hero.zorn_req(1)) {
            action1.setEnabled(true);
        }else{
            action1.setEnabled(false);
        }
        if (zorn_tmp >= hero.zorn_req(2)) {
            action2.setEnabled(true);
        }else{
            action2.setEnabled(false);
        }
        if (zorn_tmp >= hero.zorn_req(3)) {
            action3.setEnabled(true);
        }else{
            action3.setEnabled(false);
        }
    }

    private void UpdateDebufflist(){
        //condition update enemy
        /*
        if (enemy.getBleedStackSize() == 0) {
            enemy_debuff_list.remove("bleed");
        }else{
            enemy_debuff_list.add("bleed");
        }
        if (enemy.getBurnStacks() == 0) {
            enemy_debuff_list.remove("burn");
        }else{
            enemy_debuff_list.add("burn");
        }
        //condition update hero
        if (hero.getBleedStackSize() == 0) {
            hero_debuff_list.remove("bleed");
        }else{
            hero_debuff_list.add("bleed");
        }
        if (hero.getBurnStacks() == 0) {
            hero_debuff_list.remove("burn");
        }else{
            hero_debuff_list.add("burn");
        }
        */
    }

    private void startCountdown(){
        //start delay
        tv_start_countdown.setVisibility(View.VISIBLE);
        tv_countdown.setText("300");
        hero.setAction(
            new CountDownTimerWithPause(3000, 100) {
                public void onTick(long millisUntilFinished) {
                    //tv_countdown = (TextView) findViewById(R.id.countdown);
                    float c = Math.round(millisUntilFinished / 10);
                    tv_start_countdown.setText( String.format(Locale.getDefault(), "%.1f", ( c / 100) ));
                    //on action start game
                    if (hero.isAttacking()) {
                        this.onFinish();
                        this.cancel();
                    }
                }
                public void onFinish() {
                    //Game start
                    ActionOverTime_isRunning = true;
                    KI_attack();
                    tv_start_countdown.setVisibility(View.GONE);
                    BattleCountdown();
                }
            }
        );
        hero.startAction();

    }

    private void BattleCountdown(){
        new CountDownTimer(300000, 100) {
            public void onTick(long millisUntilFinished) {
                tv_countdown.setText(String.valueOf(millisUntilFinished / 1000));
                if ( ( hero.getHP() <= 0 ) || ( dungeon.getLevel() > dungeon.getMaxLevel() ) )
                    this.cancel();
            }
            public void onFinish() {
                //hero lost
                ActionOverTime_isRunning = false;
                if (hero.isAttacking())
                    hero.cancelAction();

                enemy.cancelAction();

                battle_message.setVisibility(View.VISIBLE);
                battle_message.setText(getString(R.string.lost));

                String end_msg = getString(R.string.level, dungeon.getLevel(), dungeon.getMaxLevel());
                endscreen_message.setText(end_msg);

                endscreen_info.setText(getString(R.string.time_over));
                //Change to EndScreen
                LinearLayoutFadeInAndOut(EndScreen, BattleScreen, 1000, 2000);
                endscreenMode = true;

                sharedPrefs sharedPrefs = new sharedPrefs(getApplicationContext());
                int newEventId = util.random(1, maxPublicDungeons);
                sharedPrefs.setDungeonEventId(newEventId);
            }
        }.start();
    }

    //Action Over Time Thread
    private void AOT() {
        new Thread(new Runnable() {
            final Handler handler = new Handler();

            @Override
            public void run() {
                while (ActionOverTime_isAlive) {
                    if (ActionOverTime_isRunning) {
                        try {
                            //hero
                            //buffs
                            //conditions
                            //hero.conditionDamageTick(enemy);
                            hero.getConditionList().onTick(hero);
                            hero.getPassivePermanentList().onTick(hero);

                            //enemy
                            //buffs
                            //conditions
                            //enemy.conditionDamageTick(hero);
                            enemy.getConditionList().onTick(enemy);
                            enemy.getPassivePermanentList().onTick(enemy);

                            //UI Update
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    UI_Update();
                                }
                            });

                            Thread.sleep(ActionOverTime_interval);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }
}
