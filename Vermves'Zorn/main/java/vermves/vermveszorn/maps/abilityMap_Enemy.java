package vermves.vermveszorn.maps;

import java.util.HashMap;
import java.util.Map;

import vermves.vermveszorn.interfaces.defaultAbilityInterface;
import vermves.vermveszorn.models.player;

/**
 * Created by Martin on 21.01.2017.
 */

public class abilityMap_Enemy {
    private final Map<String, defaultAbilityInterface> abilities = new HashMap<>();

    public Map<String, defaultAbilityInterface> getHashmap() {
        return abilities;
    }

    public abilityMap_Enemy() {

        abilities.put("scratch", new defaultAbilityInterface(){
            @Override
            public void execute(player _player, player _target) {
                _player.addDMG(_player.getMajorAttack());
                _player.addZORN(+1);
            }

            public int duration(){
                return 600;
            }
        });

    }
}
