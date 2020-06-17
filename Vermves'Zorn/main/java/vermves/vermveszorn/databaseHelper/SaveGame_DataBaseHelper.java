package vermves.vermveszorn.databaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

/**
 * Created by Martin on 07.02.2016.
 */
public class SaveGame_DataBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = SaveGame_DataBaseHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "gameprogress.db";
    private static final int DATABASE_VERSION = 23;

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
    private static final String TABLE_NAME = "hero";
    private static final String COLUMN_ID = "_id";
    private static final String DEFAULT_ID = "0";
    private static final String COLUMN_ACTION0 = "action0";
    private static final String COLUMN_ACTION1 = "action1";
    private static final String COLUMN_ACTION2 = "action2";
    private static final String COLUMN_ACTION3 = "action3";
    private static final String COLUMN_RANK = "rank";
    private static final String COLUMN_EXP = "exp";
    private static final String COLUMN_LOST_DUNGEON = "lost_dungeon";
    private static final String COLUMN_LOST_EXP = "lost_exp";

    private static final String CREATE_DATABASE = CREATE_TABLE + TABLE_NAME + " ( " +
            " _id " + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_ACTION0 + " TEXT DEFAULT 'sword' , " +
            COLUMN_ACTION1 + " TEXT DEFAULT 'final_thrust' , " +
            COLUMN_ACTION2 + " TEXT DEFAULT 'bandage_wounds' , " +
            COLUMN_ACTION3 + " TEXT DEFAULT 'victory_is_mine' , " +
            COLUMN_EXP     + " INTEGER DEFAULT 0, " +
            COLUMN_RANK    + " INTEGER DEFAULT 1, " +
            COLUMN_LOST_DUNGEON + " INTEGER DEFAULT 0, " +
            COLUMN_LOST_EXP     + " INTEGER DEFAULT 0 " +
            ");";

    private static final String INSERT_DEFAULTS =
            "INSERT INTO " + TABLE_NAME
                    + " ( " +
                        COLUMN_ID
                    + " ) VALUES ( " +
                        DEFAULT_ID
                    + " );";

    public SaveGame_DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DATABASE);
        db.execSQL(INSERT_DEFAULTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertAction(int ActionNr, String _NewAction){
        String COLUMN;

        switch(ActionNr) {
            case 0:
                COLUMN = "action0";
                break;
            case 1:
                COLUMN = "action1";
                break;
            case 2:
                COLUMN = "action2";
                break;
            case 3:
                COLUMN = "action3";
                break;
            default:
                COLUMN = "stopInsert";
        }

        if (!(COLUMN.equals("stopInsert"))){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, DEFAULT_ID);
            values.put(COLUMN, _NewAction);

            db.update(TABLE_NAME, values, null, null);
        }else{
            Log.i(TAG, "database insertAction Fehler: falsche parameter");
        }
    }

    public String getSaveGameColumn(String COLUMN){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String sqlTables = TABLE_NAME;
        String [] sqlSelect = {COLUMN};

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);

        String result = "error column";
        if(c != null && c.moveToFirst()){
            result = c.getString(c.getColumnIndex(COLUMN));
            c.close();
        }

        return result;
    }

    public int getExp() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {COLUMN_EXP};

        qb.setTables(TABLE_NAME);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);

        int result = 0;
        if(c != null && c.moveToFirst()){
            result = c.getInt(c.getColumnIndex(COLUMN_EXP));
            c.close();
        }

        return result;
    }

    public void addExp(int exp) {
        SQLiteDatabase db = getReadableDatabase();

        db.execSQL("UPDATE " + TABLE_NAME + " SET " + COLUMN_EXP + " = " + COLUMN_EXP + " + " + exp);
    }

    public void resetExp() {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + COLUMN_RANK + " = 1, " + COLUMN_EXP + " = 0 ;");
    }

    public int getRank() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {COLUMN_RANK};

        qb.setTables(TABLE_NAME);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);

        int result = 0;
        if(c != null && c.moveToFirst()){
            result = c.getInt(c.getColumnIndex(COLUMN_RANK));
            c.close();
        }

        return result;
    }

    public void addRank(){
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + COLUMN_RANK + " = " + COLUMN_RANK + " + 1;");
    }

    public int getNextRankExp() {
        int x = getRank() - 1;
        int nextRankExp = (int) ( (0.12 * Math.pow(x,3)) + (8 * Math.pow(x,2)) + (100 * x) + 100 );
        return nextRankExp;
    }

    public int getLostDungeon() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {COLUMN_LOST_DUNGEON};

        qb.setTables(TABLE_NAME);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);

        int result = 0;
        if(c != null && c.moveToFirst()){
            result = c.getInt(c.getColumnIndex(COLUMN_LOST_DUNGEON));
            c.close();
        }

        return result;
    }

    public int getLostExp() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {COLUMN_LOST_EXP};

        qb.setTables(TABLE_NAME);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);

        int result = 0;
        if(c != null && c.moveToFirst()){
            result = c.getInt(c.getColumnIndex(COLUMN_LOST_EXP));
            c.close();
        }

        return result;
    }

    public void updateLostValues(int dungeonID, int exp) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("UPDATE " + TABLE_NAME + " SET " +
                COLUMN_LOST_DUNGEON + " = " + dungeonID + ", " +
                COLUMN_LOST_EXP     + " = " + exp + ", " +
                COLUMN_EXP          + " = 0 ;");
    }
}
