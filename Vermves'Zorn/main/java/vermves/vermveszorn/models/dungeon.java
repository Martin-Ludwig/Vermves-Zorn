package vermves.vermveszorn.models;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;

import java.util.Random;

import vermves.vermveszorn.R;
import vermves.vermveszorn.databaseHelper.Vermves_DatabaseHelper;
import vermves.vermveszorn.utils.util;

/**
 * Created by Martin on 19.03.2016.
 */
public class dungeon {
    private String name;
    private int dungeonID;
    private int level;
    private int maxLevel;
    private String[] aEnemies;
    private String currentEnemy;
    private String boss;
    private int rarity;

    //rank exp reward
    public final int REWARD_PER_LEVEL = 8;
    private int reward;
    private int requirement;

    Vermves_DatabaseHelper VermvesDB;

    public dungeon(Context context, String _id){
        VermvesDB = new Vermves_DatabaseHelper(context);
        Cursor cDungeon = VermvesDB.GetDungeonByID(_id);

        name = cDungeon.getString(cDungeon.getColumnIndex("name"));
        dungeonID = Integer.parseInt(_id);
        maxLevel = cDungeon.getInt(cDungeon.getColumnIndex("levels"));
        level = 1;
        aEnemies = splitEnemies(cDungeon.getString(cDungeon.getColumnIndex("enemies")));
        boss = cDungeon.getString(cDungeon.getColumnIndex("boss"));
        requirement = cDungeon.getInt(cDungeon.getColumnIndex("requirement"));
        reward = cDungeon.getInt(cDungeon.getColumnIndex("reward"));

        cDungeon.close();
    }


    public void reset(){
        level = 1;
    }

    private String[] splitEnemies(String _enemies){
        return _enemies.split(",");
    }

    public enemy createEnemy(){
        //check if normal or boss
        if (level < maxLevel){
            Random r = new Random();
            int random = r.nextInt(aEnemies.length);
            currentEnemy = aEnemies[random];

            //rarity
            rarity = R.color.RarityNormal;
            if (util.random(1,10) <= 1) {
                rarity = R.color.RarityRare;
                if (util.random(1,10) <= 1)
                    rarity = R.color.RarityLegendary;
            }

        }else {
            currentEnemy = boss;
            rarity = R.color.RarityBoss;
        }

        return VermvesDB.createEnemyByName(currentEnemy, this.level, rarity);
    }

    public int getDungeonID() {
        return dungeonID;
    }

    public int getLevel(){
        return level;
    }

    public void nextLevel(){
        level++;
    }

    public int getMaxLevel(){
        return maxLevel;
    }

    public String getDungeonName(){
        return name;
    }

    public int getReward(){
        return reward;
    }

    public int getRequirement(){
        return requirement;
    }

}
