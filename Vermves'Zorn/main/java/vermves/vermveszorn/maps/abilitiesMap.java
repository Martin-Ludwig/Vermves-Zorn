package vermves.vermveszorn.maps;

import android.provider.Settings;

import java.util.HashMap;
import java.util.Map;

import vermves.vermveszorn.interfaces.abilityInterface;
import vermves.vermveszorn.interfaces.defaultAbilityInterface;
import vermves.vermveszorn.models.player;

public class abilitiesMap {
	private final Map<String, defaultAbilityInterface> abilities = new HashMap<>();

    public Map<String, defaultAbilityInterface> getHashmap() {
        return abilities;
    }

	private abilityMap_Conditions conditionMap;
	private abilityMap_One abilityMapOne;
	private abilityMap_Two abilityMapTwo;
	private abilityMap_Three abilityMapThree;
	private abilityMap_Four abilityMapFour;
	private abilityMap_Enemy abilityMapEnemy;

    public abilitiesMap(){

		abilityMapOne = new abilityMap_One();
		abilityMapTwo = new abilityMap_Two();
		abilityMapThree = new abilityMap_Three();
		abilityMapFour = new abilityMap_Four();
		abilityMapEnemy = new abilityMap_Enemy();
		conditionMap = new abilityMap_Conditions();

		abilities.putAll(abilityMapOne.getHashmap());
		abilities.putAll(abilityMapTwo.getHashmap());
		abilities.putAll(abilityMapThree.getHashmap());
		abilities.putAll(abilityMapFour.getHashmap());
		abilities.putAll(abilityMapEnemy.getHashmap());
		abilities.putAll(conditionMap.getHashmap());

		abilityMapOne = null;
		abilityMapTwo = null;
		abilityMapThree = null;
		abilityMapFour = null;
		abilityMapEnemy = null;
		conditionMap = null;

		abilities.put("empty", new defaultAbilityInterface(){
		});
    }
}
