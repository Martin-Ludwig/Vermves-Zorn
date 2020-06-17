package vermves.vermveszorn.interfaces;

import vermves.vermveszorn.models.player;
import vermves.vermveszorn.utils.Passive;

/**
 * Created by Martin on 21.01.2017.
 */

public abstract class defaultAbilityInterface implements abilityInterface {
    public int zorn_requirement(){ return 0; }
    public int duration(){ return 1000; }
    public boolean passive_permanent(){ return false; }

    public int vitality(){ return 0; }
    public int power(){ return 0; }
    public int malice(){ return 0; }
    public int recovery(){ return 0; }

    //triggers
    public void execute(player _player, player _target){ /* nothing */ }
    public void onTick(player _player, Passive passive) { /* nothing */ }
    public void onStart(player _player) { /* nothing */ }
    public void onEnd(player _player) { /* nothing */ }
}
