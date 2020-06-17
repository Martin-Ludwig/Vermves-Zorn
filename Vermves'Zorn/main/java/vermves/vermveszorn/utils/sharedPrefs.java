package vermves.vermveszorn.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Martin on 10.12.2016.
 */

public class sharedPrefs {
    private final String PREFS_NAME = "vermveszorn";
    private SharedPreferences settings;
    private final String dungeonEventId = "dungeonEventId";

    public sharedPrefs(Context context){
        // Restore preferences
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public int getDungeonEventId() {
        return settings.getInt(dungeonEventId, 0);
    }

    public void setDungeonEventId(int newId) {
        settings.edit().putInt(dungeonEventId, newId).apply();
    }

}
