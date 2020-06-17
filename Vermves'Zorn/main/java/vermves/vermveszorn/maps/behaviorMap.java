package vermves.vermveszorn.maps;

import java.util.HashMap;
import java.util.Map;

import vermves.vermveszorn.interfaces.defaultAbilityInterface;
import vermves.vermveszorn.models.enemy;
import vermves.vermveszorn.models.hero;
import vermves.vermveszorn.interfaces.ai_Interface;
import vermves.vermveszorn.interfaces.abilityInterface;
import vermves.vermveszorn.models.player;

/**
 * Created by Martin on 17.04.2016.
 */
public class behaviorMap {
    private final Map<String, ai_Interface> behaviorMap = new HashMap<>();

    //getter
    public Map<String, ai_Interface> getHashmap() {
        return behaviorMap;
    }

    //util
    private final abilitiesMap CreateActionsMap= new abilitiesMap();
    private final Map<String, defaultAbilityInterface> abilities = CreateActionsMap.getHashmap();

    private boolean CanUseAction(String _action, player _player){
        return abilities.get(_action).zorn_requirement() <= _player.getZORN();
    }

    /*

    action:
     0 -> Weapon (default Attack)
     1 -> strong Attack
     2 -> heal
     3 -> ??

     */

    public behaviorMap() {

        //default
        behaviorMap.put("default", new ai_Interface() {
            @Override
            public int iChooseAction(enemy _enemy, hero _hero) {

                // Gegner (Hero) unter 50% HP, dann greife an (action 0)
                if ( (_hero.getHP() < (_hero.getMAXHP() / 2)) && (CanUseAction(_enemy.getACTION(2), _enemy)) )
                    return 2;

                // Anwender (Gegner) unter 50% HP, dann heile (action 1)
                if ( (_enemy.getHP() < (_enemy.getMAXHP() / 2)) && (CanUseAction(_enemy.getACTION(1), _enemy)) )
                    return 1;

                //default 0
                return 0;
            }
        });


    }
}
