package vermves.vermveszorn.maps;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import vermves.vermveszorn.interfaces.defaultAbilityInterface;
import vermves.vermveszorn.models.player;

/**
 * Created by Martin on 21.01.2017.
 */

public class abilityMap_One {
    private final Map<String, defaultAbilityInterface> abilities = new HashMap<>();

    public Map<String, defaultAbilityInterface> getHashmap() {
        return abilities;
    }

    public abilityMap_One() {
        // Connection between String and Method

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //WEAPONS//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        abilities.put("dagger", new defaultAbilityInterface(){
            @Override
            public void execute(player _player, player _target) {
                _target.addCondition("bleeding", 10, _player.getOneTickConditionDmg());
                _player.addDMG(_player.getOneTickAttack());
                _player.addZORN(+1);
            }
            public int malice() { return 1; }
            public int duration(){
                return 500;
            }
            public String description(){ return "dagger_desc"; }
        });
        abilities.put("sword", new defaultAbilityInterface(){
            @Override
            public void execute(player _player, player _target) {
                _player.addDMG(_player.getMajorAttack());
                _player.addZORN(+1);
            }
            public int power() { return 2; }
            public int duration(){
                return 750;
            }
        });
        abilities.put("halberd", new defaultAbilityInterface(){
            @Override
            public void execute(player _player, player _target) {
                _player.addDMG(_player.getMajorAttack());
                _target.addCondition("bleeding", 4, _player.getOneTickConditionDmg());
                _player.addZORN(+1);
            }
            public int duration(){
                return 1100;
            }

            @Override
            public int malice() { return 1; }
            public int power() { return 1; }
            public int vitality() { return 1; }
        });

        abilities.put("icecrack", new defaultAbilityInterface(){
            @Override
            public void execute(player _player, player _target) {
                if (_target.hasCondition("freezing")) {
                    _player.addDMG((int) Math.round( _player.getMajorAttack() * 1.5 ));
                    Log.i("icecrack", "enemy is freezing; normal dmg = " + _player.getMajorAttack() + "; * 1.5 = " + (int) Math.round( _player.getMajorAttack() * 1.5 ) );
                } else {
                    _player.addDMG(_player.getMajorAttack());
                }

                _player.addZORN(+1);
            }
            public int duration(){
                return 750;
            }

            @Override
            public int power() { return 1; }
        });

    }
}
