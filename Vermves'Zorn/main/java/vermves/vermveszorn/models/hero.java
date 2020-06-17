package vermves.vermveszorn.models;

import android.util.Log;

/**
 * Created by Martin on 25.03.2016.
 */
public class hero extends player {
    private int progress;
    private int limit;

    public hero(String name,
                int health,   int attack, int healing_power, int condition_dmg,
                float speed, String[] actions){

        super(name, health, attack, healing_power, condition_dmg, speed, actions, 1);


        this.setLevel(1);
        this.progress = 0;
        this.limit = 65;
    }

    public int getProgress(){
        return this.progress;
    }

    public void addXP(){
        this.progress += Math.round(( this.getHP() / (float) this.getMAXHP() ) * 100 );
        Log.i("hero.java","addXP():XP gained= " +String.valueOf( Math.round(( this.getHP() / (float) this.getMAXHP() ) * 100)) );
        this.lvlup();
    }

    private void lvlup(){
        while (this.progress >= this.limit) {
            this.incLevel();
            this.addHP(VITALITY_MULTIPLIER);
            this.progress -= this.limit;
        }
    }

    /**
     * resets game values
     * Level = 1,
     * exp progress = 0,
     * zorn = 0,
     * clears condition list
     */
    public void reset(){
        this.setLevel(1);
        this.progress = 0;
        this.setZORN(0);

        this.getConditionList().clear();
        setIsAttacking(false);
    }

}
