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

public class abilityMap_Three {
    private final Map<String, defaultAbilityInterface> abilities = new HashMap<>();

    public Map<String, defaultAbilityInterface> getHashmap() {
        return abilities;
    }

    public abilityMap_Three() {
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //ABILITY 2//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        abilities.put("bandage_wounds", new defaultAbilityInterface(){
            @Override
            public void execute(player _player, player _target) {
                _player.addHEAL(_player.getMajorHealing() * 3);
                _player.addZORN(-zorn_requirement());
            }
            public int zorn_requirement(){
                return 2;
            }
            public int duration(){
                return 1250;
            }
            public int vitality(){ return 1; }
            public int power(){ return 0; }
            public int malice(){ return 0; }
            public int recovery(){ return 1; }
        });

        abilities.put("blood_is_power", new defaultAbilityInterface(){
            @Override
            public void execute(player _player, player _target) {
                _player.addHEAL(_player.getMajorHealing() * 4);
                _player.addCondition("bleeding", 8, _player.getOneTickConditionDmg());
                _player.addZORN(-zorn_requirement());
            }
            public int zorn_requirement(){
                return 2;
            }
            public int duration(){
                return 1000;
            }
            public int malice(){ return 1; }
            public int recovery(){ return 1; }
        });

        abilities.put("recovery_passive", new defaultAbilityInterface() {
            @Override
            public void onTick(player _player, Passive passive) {
                int healing = Math.round(_player.getOneTickHealing() * 2);
                _player.setHP(_player.getAddHP(healing));
            }
            @Override
            public boolean passive_permanent() {
                return true;
            }
            public int zorn_requirement(){
                return 6;
            }
        });
    }
}
