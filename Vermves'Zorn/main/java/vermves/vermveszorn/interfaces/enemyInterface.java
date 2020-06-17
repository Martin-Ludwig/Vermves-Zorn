package vermves.vermveszorn.interfaces;

import vermves.vermveszorn.models.enemy;
import vermves.vermveszorn.models.player;

/**
 * Created by Martin on 11.02.2016.
 */
public interface enemyInterface {
    enemy getEnemy(int _level);
    String chooseAbility(player _hero, player _enemy);
}
