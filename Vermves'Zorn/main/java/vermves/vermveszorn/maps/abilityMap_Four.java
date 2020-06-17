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

public class abilityMap_Four {
    private final Map<String, defaultAbilityInterface> abilities = new HashMap<>();

    public Map<String, defaultAbilityInterface> getHashmap() {
        return abilities;
    }

    public abilityMap_Four() {

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //ABILITY 3//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        abilities.put("victory_is_mine", new defaultAbilityInterface(){
            @Override
            public void execute(player _player, player _target) {
                _player.addDMG(_player.getMajorAttack());
                _player.addHEAL(_player.getMajorHealing());
                _player.addZORN(-zorn_requirement());
            }

            public int zorn_requirement(){
                return 1;
            }
            public int duration(){
                return 500;
            }
            public int power(){ return 1; }
            public int recovery(){ return 1; }
        });

        abilities.put("blood_transfer", new defaultAbilityInterface(){
            @Override
            public void execute(player _player, player _target) {
                _player.removeCondition("bleeding");
                _player.removeCondition("bleeding");
                _target.addCondition("bleeding", 12, _player.getOneTickConditionDmg());
                _target.addCondition("bleeding", 12, _player.getOneTickConditionDmg());

                //_player.addHP(_player.getHP());
                _player.addZORN(-zorn_requirement());
            }

            public int zorn_requirement(){
                return 1;
            }
            public int duration(){
                return 250;
            }
            public int vitality(){ return 1; }
            public int malice(){ return 1; }
        });

        abilities.put("rime", new defaultAbilityInterface(){ //raueis
            @Override
            public void execute(player _player, player _target) {
                _target.addCondition("freezing", 12, _player.getOneTickConditionDmg());
                _player.addZORN(-zorn_requirement());
            }

            public int zorn_requirement(){
                return 3;
            }
            public int duration(){
                return 1200;
            }
        });

        abilities.put("aftershock", new defaultAbilityInterface(){
            @Override
            public void execute(player _player, player _target) {
                _player.addDMG(_player.getMajorAttack());

                _player.addZORN(-zorn_requirement());

                if (_target.hasCondition("stunned"))
                    _player.addZORN(1);
            }

            public int zorn_requirement(){
                return 1;
            }
            public int duration(){
                return 500;
            }
        });

    }
}
