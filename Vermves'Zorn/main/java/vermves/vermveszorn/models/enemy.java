package vermves.vermveszorn.models;

import android.graphics.Color;

import java.util.Map;

import vermves.vermveszorn.maps.behaviorMap;
import vermves.vermveszorn.interfaces.ai_Interface;

/**
 * Created by Martin on 25.03.2016.
 */

public class enemy extends player {

    private int rarity;
    private String sBehavior;
    final behaviorMap oBehaviorMap = new behaviorMap();
    final Map<String, ai_Interface> mBehavior = oBehaviorMap.getHashmap();

    public enemy(String name,
                 int health,   int attack, int healing_power, int condition_dmg,
                 float speed, String[] actions, int level, String _behavior, int rarity) {

        super(name, health, attack, healing_power, condition_dmg, speed, actions, level);

        this.sBehavior = _behavior;
        this.sBehavior = "default";

        this.rarity = rarity;
    }

    public void chooseAction(hero _hero){
        int iChosenAction = mBehavior.get(this.sBehavior).iChooseAction(this, _hero);
        this.setACTION(this.getACTION(iChosenAction));
    }

    public int getRarity(){
        return rarity;
    }
}
