package vermves.vermveszorn.maps;

import java.util.HashMap;
import java.util.Map;

import vermves.vermveszorn.interfaces.defaultAbilityInterface;
import vermves.vermveszorn.models.player;

/**
 * Created by Martin on 21.01.2017.
 */

public class abilityMap_Two {
    private final Map<String, defaultAbilityInterface> abilities = new HashMap<>();

    public Map<String, defaultAbilityInterface> getHashmap() {
        return abilities;
    }

    public abilityMap_Two() {
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //ABILITY 1//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        abilities.put("final_thrust", new defaultAbilityInterface(){ //Endstoß
            @Override
            public void execute(player _player, player _target) {
                if (_target.getHP_in_Percent() < 50){
                    _player.addDMG( 2 * _player.getMajorAttack() );
                    _target.addCondition("bleeding", 4, _player.getOneTickConditionDmg());
                    _target.addCondition("bleeding", 4, _player.getOneTickConditionDmg());
                }else{
                    _player.addDMG( 2 * _player.getMajorAttack() );
                }

                _player.addZORN(-zorn_requirement());
            }
            public int zorn_requirement(){
                return 1;
            }
            public int duration(){
                return 800;
            }
            public int power(){ return 2; }
        });

        abilities.put("gash", new defaultAbilityInterface(){ //Schnittwunde
            @Override
            public void execute(player _player, player _target) {
                _player.addDMG(_player.getMajorAttack());
                _target.addCondition("bleeding", 16, _player.getOneTickConditionDmg());
                _player.addZORN(-zorn_requirement());
            }
            public int zorn_requirement(){
                return 1;
            }
            public int duration(){
                return 600;
            }
            public int power(){ return 1; }
            public int malice(){ return 1; }
        });

        abilities.put("meteor", new defaultAbilityInterface(){ //meteor
            @Override
            public void execute(player _player, player _target) {
                _player.addDMG(_player.getMajorAttack());
                _target.addCondition("burning", 4, _player.getOneTickConditionDmg());
                _target.interrupt();

                _player.addZORN(-zorn_requirement());
            }
            public int zorn_requirement(){
                return 1;
            }
            public int duration(){
                return 1000;
            }
            public int power(){ return 1; }
            public int malice(){ return 1; }
        });

        abilities.put("earthquake", new defaultAbilityInterface(){ //erdbeben
            @Override
            public void execute(player _player, player _target) {
                _player.addDMG(_player.getMajorAttack());
                _target.addCondition("stunned", 5, _player.getOneTickConditionDmg());

                _player.addZORN(-zorn_requirement());
            }
            public int zorn_requirement(){
                return 2;
            }
            public int duration(){
                return 1200;
            }
            public int power(){ return 2; }
        });

    }
}
