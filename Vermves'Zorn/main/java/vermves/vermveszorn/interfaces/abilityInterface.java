package vermves.vermveszorn.interfaces;

import vermves.vermveszorn.models.player;
import vermves.vermveszorn.utils.Passive;

public interface abilityInterface {
	int zorn_requirement();
	int duration();
	boolean passive_permanent();

	int vitality();
	int power();
	int malice();
	int recovery();

	//triggers
	void execute(player _player, player _target);
	void onTick(player _player, Passive passive);
	void onStart(player _player);
	void onEnd(player _player);

}
