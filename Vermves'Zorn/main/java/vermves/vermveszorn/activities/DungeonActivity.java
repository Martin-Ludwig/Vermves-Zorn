package vermves.vermveszorn.activities;

import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import vermves.vermveszorn.adapters.Dungeon_ListviewAdapter;
import vermves.vermveszorn.R;
import vermves.vermveszorn.databaseHelper.SaveGame_DataBaseHelper;
import vermves.vermveszorn.databaseHelper.Vermves_DatabaseHelper;
import vermves.vermveszorn.utils.bottom_menu;

public class DungeonActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    Vermves_DatabaseHelper VermvesDB;
    SaveGame_DataBaseHelper SaveGameDB;

    ////bottom_menu
    private RelativeLayout bottom_menu_wrapper;
    bottom_menu bottom_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dungeon);

        SaveGameDB = new SaveGame_DataBaseHelper(this);
        VermvesDB = new Vermves_DatabaseHelper(this);
        populateListView();

        //bottom_menu
        bottom_menu_wrapper = (RelativeLayout) findViewById(R.id.wrapper);
        bottom_menu = new bottom_menu(this, bottom_menu_wrapper);
    }

    @Override
    protected void onDestroy() {
        SaveGameDB.close();
        VermvesDB.close();

        super.onDestroy();
    }

    public void populateListView(){
        Cursor cursor = VermvesDB.GetPublicDungeons();

        Dungeon_ListviewAdapter myCursorAdapter = new Dungeon_ListviewAdapter(this, cursor, SaveGameDB.getRank());

        ListView myList = (ListView) findViewById(R.id.dungeon_listView);

        myList.setAdapter(myCursorAdapter);
        myList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (view.isEnabled()) {
            Cursor cursor = VermvesDB.GetPublicDungeons();

            //save the dungeon id in the intent
            Intent newIntent = new Intent(getApplicationContext(), BattleActivity.class);
            newIntent.putExtra("dungeonID", String.valueOf(id));
            newIntent.putExtra("maxDungeons", cursor.getCount());

            SaveGameDB.close();
            VermvesDB.close();
            //start battle
            startActivity(newIntent);
        }
    }

    public void OnClickFinish(View v) {
        finish();
    }

    //function of back button
    public void onBackPressed() {
        bottom_menu.onBackPressed();
    }

}
