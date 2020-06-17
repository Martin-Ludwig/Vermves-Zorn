package vermves.vermveszorn.interfaces;

import vermves.vermveszorn.models.enemy;
import vermves.vermveszorn.models.hero;

/**
 * Created by Martin on 17.04.2016.
 */
public interface ai_Interface {
    int iChooseAction(enemy _enemy, hero _hero);
}
