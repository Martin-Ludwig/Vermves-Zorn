package vermves.vermveszorn.databaseHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.graphics.Color;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.Map;

import vermves.vermveszorn.R;
import vermves.vermveszorn.interfaces.abilityInterface;
import vermves.vermveszorn.maps.abilitiesMap;
import vermves.vermveszorn.models.enemy;

/**
 * Created by Martin on 11.02.2016.
 */

public class Vermves_DatabaseHelper extends SQLiteAssetHelper {
    private static final String TAG = Vermves_DatabaseHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "vermves.db";
    private static final int DATABASE_VERSION = 37;

    /*
     Dungeons
     */

    //table name
    private static final String DATABASE_TABLE_DUNGEONS = "dungeons";

    //columns
    public static final String DUNGEONS_KEY_ID = "_id";
    public static final String DUNGEONS_KEY_NAME = "name";
    public static final String DUNGEONS_KEY_LEVELS = "levels";
    public static final String DUNGEONS_KEY_REQUIREMENT = "requirement";
    private static final String DUNGEONS_KEY_PUBLIC = "public";

    private static final String [] ALL_COLUMNS_DUNGEONS = {
            DUNGEONS_KEY_ID,
            DUNGEONS_KEY_NAME,
            "enemies",
            "boss",
            DUNGEONS_KEY_LEVELS,
            DUNGEONS_KEY_REQUIREMENT,
            "reward",
            "description",
            DUNGEONS_KEY_PUBLIC
    };




    public Vermves_DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgrade();
    }

    public Cursor GetDungeonByID(String _id){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String sqlTables = DATABASE_TABLE_DUNGEONS;
        String [] sqlSelect = ALL_COLUMNS_DUNGEONS;
        String [] selectionArgs = {_id};

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, DUNGEONS_KEY_ID + " like ?", selectionArgs,
                null, null, null);

        c.moveToFirst();
        return c;
    }

    public Cursor GetPublicDungeons(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String sqlTables = DATABASE_TABLE_DUNGEONS;
        String [] sqlSelect = ALL_COLUMNS_DUNGEONS;

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, DUNGEONS_KEY_PUBLIC + " = 1", null,
                null, null, null);

        c.moveToFirst();
        return c;
    }

    /**********************************************************************************************************************************************
     Enemies table
    **********************************************************************************************************************************************/

    private static final String DATABASE_TABLE_ENEMIES = "enemies";

    private static final String [] ALL_COLUMNS_ENEMIES = {
            "_id",
            "name",
            "health",
            "attack",
            "healing_power",
            "condition_dmg",
            "speed",
            "actions",
            "behavior"
    };

    public enemy createEnemyByName(String name, int _level, int rarity){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String sqlTables = DATABASE_TABLE_ENEMIES;
        String [] sqlSelect = ALL_COLUMNS_ENEMIES;
        String[] selectionArgs = {name};

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, "name like ?", selectionArgs,
                null, null, null);

        c.moveToFirst();

        return createEnemy(c, _level, rarity);
    }

    private enemy createEnemy(Cursor cursor, int level, int rarity){
        String name = cursor.getString(cursor.getColumnIndex("name"));
        int health = cursor.getInt(cursor.getColumnIndex("health"));
        int attack = cursor.getInt(cursor.getColumnIndex("attack"));
        int healing_power = cursor.getInt(cursor.getColumnIndex("healing_power"));
        int condition_dmg = cursor.getInt(cursor.getColumnIndex("condition_dmg"));
        float speed = cursor.getFloat(cursor.getColumnIndex("speed"));
        String sAbility = cursor.getString(cursor.getColumnIndex("actions"));
        String[] actions = splitString(sAbility);
        String behavior = cursor.getString(cursor.getColumnIndex("behavior"));


        switch (rarity) {
            case R.color.RarityRare:
                level += 1;
                health *= 2;
                break;
            case R.color.RarityLegendary:
                level += 1;
                health *= 3;
                break;
        }

        return new enemy(name, health, attack, healing_power, condition_dmg, speed, actions, level, behavior, rarity);
    }

    private String[] splitString(String _string){
        return _string.split(",");
    }

}