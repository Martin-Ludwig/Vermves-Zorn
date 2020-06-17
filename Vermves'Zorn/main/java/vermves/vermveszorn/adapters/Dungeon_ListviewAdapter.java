package vermves.vermveszorn.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import vermves.vermveszorn.R;
import vermves.vermveszorn.databaseHelper.SaveGame_DataBaseHelper;
import vermves.vermveszorn.databaseHelper.Vermves_DatabaseHelper;
import vermves.vermveszorn.utils.sharedPrefs;
import vermves.vermveszorn.utils.util;

/**
 * Created by Martin on 17.10.2016.
 */

public class Dungeon_ListviewAdapter extends CursorAdapter {
    private Context context;
    private LayoutInflater inflater;
    private int rank;
    private sharedPrefs sharedPrefs;
    private int dungeonEventId;
    private SaveGame_DataBaseHelper dbSaveGame;
    private int lostDungeon;
    private int lostExp;

    public Dungeon_ListviewAdapter(Context context, Cursor c, int rank) {
        super(context, c);
        this.context = context;
        this.rank = rank;
        sharedPrefs = new sharedPrefs(context);
        dungeonEventId = sharedPrefs.getDungeonEventId();

        dbSaveGame = new SaveGame_DataBaseHelper(context);
        lostDungeon = dbSaveGame.getLostDungeon();
        lostExp = dbSaveGame.getLostExp();

        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor c, ViewGroup parent) {
        return inflater.inflate(R.layout.dungeon_listview_layout, parent, false);
    }

    @Override
    public void bindView(View v, Context context, Cursor c) {
        String title = c.getString(c.getColumnIndexOrThrow(Vermves_DatabaseHelper.DUNGEONS_KEY_NAME));
        int id = c.getInt(c.getColumnIndexOrThrow(Vermves_DatabaseHelper.DUNGEONS_KEY_ID));
        int iAmountEnemies = c.getInt(c.getColumnIndexOrThrow(Vermves_DatabaseHelper.DUNGEONS_KEY_LEVELS));
        int dungeonRank = c.getInt(c.getColumnIndexOrThrow(Vermves_DatabaseHelper.DUNGEONS_KEY_REQUIREMENT));

        //Textviews
        TextView tv_Dungeon_name = (TextView) v.findViewById(R.id.listview_dungeon_name);
        TextView tv_Dungeon_rank = (TextView) v.findViewById(R.id.listview_dungeon_rank);
        LinearLayout tv_Dungeon_subinfo = (LinearLayout) v.findViewById(R.id.listview_dungeon_subinfo);
        TextView tv_Dungeon_levels = (TextView) v.findViewById(R.id.listview_dungeon_levels);
        TextView tv_Dungeon_quest = (TextView) v.findViewById(R.id.listview_dungeon_quest);
        TextView tv_Dungeon_lostExp = (TextView) v.findViewById(R.id.listview_dungeon_lostExp);
        tv_Dungeon_lostExp.setVisibility(View.GONE);

        if (dungeonRank > this.rank) {
            //locked
            v.setClickable(true);
            v.setEnabled(false);

            tv_Dungeon_name.setText(context.getResources().getString(R.string.locked));
            tv_Dungeon_rank.setText(context.getResources().getString(R.string.rank_requirement, dungeonRank));
            tv_Dungeon_subinfo.setVisibility(View.GONE);

        }else {
            //open
            v.setClickable(false);
            v.setEnabled(true);

            tv_Dungeon_name.setText(util.getStringResourceByName(context, title));
            tv_Dungeon_rank.setText(context.getResources().getString(R.string.rank_requirement, dungeonRank));
            tv_Dungeon_subinfo.setVisibility(View.VISIBLE);
            tv_Dungeon_levels.setText(context.getResources().getString(R.string.levels, iAmountEnemies));

            if ((lostDungeon == id) && (lostExp > 0)) {
                tv_Dungeon_lostExp.setText(context.getResources().getString(R.string.dungeon_lost_exp, lostExp));
                tv_Dungeon_lostExp.setVisibility(View.VISIBLE);
            }
        }

        //event, quest
        tv_Dungeon_quest.setVisibility(View.GONE);
        if (dungeonEventId == id){
            tv_Dungeon_quest.setVisibility(View.VISIBLE);
        }

    }

}
