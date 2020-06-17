package vermves.vermveszorn.maps;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import vermves.vermveszorn.interfaces.defaultAbilityInterface;
import vermves.vermveszorn.models.player;
import vermves.vermveszorn.utils.Passive;

/**
 * Created by Martin on 21.01.2017.
 */

public class abilityMap_Conditions {
    private final Map<String, defaultAbilityInterface> conditions = new HashMap<>();

    public Map<String, defaultAbilityInterface> getHashmap() {
        return conditions;
    }

    public abilityMap_Conditions() {
        // Connection between String and Method
        conditions.put("bleeding", new defaultAbilityInterface(){
            @Override
            public void onTick(player _player, Passive passive) {
                final int bleeding_dmg = passive.getBaseCondDmg();
                _player.setHP(_player.getHP() - bleeding_dmg );
            }
        });

        conditions.put("burning", new defaultAbilityInterface() {
            @Override
            public void onStart(player _player) {
                _player.removeCondition("freezing");
            }
            @Override
            public void onTick(player _player, Passive passive) {
                final int burning_dmg = passive.getBaseCondDmg() * 3;
                _player.setHP(_player.getHP() - burning_dmg );
            }
        });

        conditions.put("freezing", new defaultAbilityInterface() {
            @Override
            public void onStart(player _player) {
                if ( _player.isAttacking() )
                    _player.pauseAction();

                _player.removeCondition("burning");
            }
            @Override
            public void onEnd(player _player) {
                if ( _player.isAttacking() )
                    _player.resumeAction();
            }
        });

        conditions.put("stunned", new defaultAbilityInterface() {

            @Override
            public void onEnd(player _player) {
                _player.interrupt();
                _player.resumeAction();
            }
        });

    }
}
