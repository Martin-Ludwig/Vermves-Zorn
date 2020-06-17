package vermves.vermveszorn.models;

import android.util.Log;

import java.util.Map;

import vermves.vermveszorn.interfaces.defaultAbilityInterface;
import vermves.vermveszorn.maps.abilitiesMap;
import vermves.vermveszorn.utils.CountDownTimerWithPause;
import vermves.vermveszorn.utils.PassiveList;

public class player {
	private String name;

	private int hp;

	private int zorn;
	private int max_zorn;
	private float speed;
	private final String[] actions;
	private int level;

	private CountDownTimerWithPause countDownTimer;
	private boolean isAttacking;

	//ability map
	private final abilitiesMap CreateActionsMap= new abilitiesMap();
	private final Map<String, defaultAbilityInterface> abilities = CreateActionsMap.getHashmap();

	//passive lists
	private PassiveList passive_permanent;
	private PassiveList passive_buffs;
	private PassiveList passive_debuffs;
	private PassiveList passive_conditions;

	//base stats
	private final int MAJOR_MULTIPLIER = 12;

	private final int health;

	private int attack;
	private final int minor_attack = (int) Math.floor(attack / MAJOR_MULTIPLIER);

	private int condition_dmg;
	private final int minor_condition_dmg = (int) Math.floor(condition_dmg / MAJOR_MULTIPLIER);

	private int healing_power;
	private final int minor_healing_power = (int) Math.floor(healing_power / MAJOR_MULTIPLIER);

	//attributes
	private int vitality;
	private int power;
	private int malice;
	private int recovery;

	//scaling
	final int VITALITY_MULTIPLIER = MAJOR_MULTIPLIER * 10;

	private final int POWER_MINOR_MULTIPLIER = 1;
	private final int POWER_MAJOR_MULTIPLIER = MAJOR_MULTIPLIER;

	private final int MALICER_MINOR_MULTIPLIER = 1;
	private final int MALICER_MAJOR_MULTIPLIER = MAJOR_MULTIPLIER;

	private final int RECOVERY_MINOR_MULTIPLIER = 1;
	private final int RECOVERY_MAJOR_MULTIPLIER = MAJOR_MULTIPLIER;


	//temporary values for battle calculations
	private String action;
	private int dealsDmg;
	private int healsHP;

	player(String _name,
		   int health, int attack, int healing_power, int condition_dmg,
		   float speed, String[] actions, int level) {

		this.name = _name;
		this.level = level;
		this.speed = speed;

		this.actions = actions;
		isAttacking = false;

		//base stats
		this.health= health;
		this.attack= attack;
		this.condition_dmg = condition_dmg;
		this.healing_power = healing_power;

		//attributes
		this.vitality = 0;
		this.power = 0;
		this.malice = 0;
		this.recovery = 0;

		for (int i=0; i<4; i++){
			this.vitality += abilities.get(actions[i]).vitality();
			this.power += abilities.get(actions[i]).power();
			this.malice += abilities.get(actions[i]).malice();
			this.recovery += abilities.get(actions[i]).recovery();
		}

		this.hp= this.getMAXHP();
		this.zorn= 0;
		this.max_zorn = 5;

		passive_conditions = new PassiveList();

		passive_permanent = new PassiveList();
		for (String action: actions) {
			if (abilities.get(action).passive_permanent())
				passive_permanent.add(action, true, 0, 0);
		}
	}

	//name
	public String getNAME(){
		return this.name;
	}

	public int getLevel(){
		return this.level;
	}
	public void setLevel(int _level){
		this.level = _level;
	}
	void incLevel(){
		this.level++;
	}

	//health points
	public int getHP(){
		return this.hp;
	}
	public int getHP_in_Percent(){
		return Math.round((this.getHP() / (float)this.getMAXHP()) * 100);
	}
	public void setHP(int _hp){
		hp= _hp;
	}

	public void addHP(int _hp){
		if ((getHP() + _hp) > getMAXHP()) {
			hp = getMAXHP();
		}else{
			hp += _hp;
		}
	}
	public int getAddHP(int _hp) {
		if ((getHP() + _hp) > getMAXHP()) {
			return getMAXHP();
		} else {
			return hp + _hp;
		}
	}

	public int getMAXHP(){
		//when changing gradient, also change on lvlup
		return health + (VITALITY_MULTIPLIER * (vitality + level - 1));
	}

	//attack
	public int getOneTickAttack() {
		return minor_attack + (POWER_MINOR_MULTIPLIER * (power + level - 1));
	}
	public int getMajorAttack() {
		return attack + (POWER_MAJOR_MULTIPLIER * (power + level - 1));
	}

	/**
	 * retuns healing
	 * Multiplier = 1
	 * @return int healing_power + (RECOVERY_MULTIPLIER * this.recovery + (this.level - 1))
     */
	public int getOneTickHealing(){
		return minor_healing_power + (RECOVERY_MINOR_MULTIPLIER * (recovery + level - 1));
	}
	/**
	 * retuns healing
	 * Multiplier = 12
	 * @return int healing_power + (12 * (recovery + this.level - 1))
	 */
	public int getMajorHealing(){
		return healing_power + (RECOVERY_MAJOR_MULTIPLIER * (recovery + level - 1));
	}

	public int getOneTickConditionDmg(){
		return  getOneTickConditionDmg(this.malice);
	}
	public int getOneTickConditionDmg(int malice) {
		return minor_condition_dmg + (MALICER_MINOR_MULTIPLIER * (malice + level - 1));
	}
	public int getMajorConditionDmg() {
		return condition_dmg + (MALICER_MAJOR_MULTIPLIER * (malice + level - 1));
	}

	//dealt damage
	public void clearDMG(){
		this.dealsDmg= 0;
		this.healsHP= 0;
	}
	public int getDMG(){
		return this.dealsDmg;
	}
	public void addDMG(int _i){
		this.dealsDmg =+ _i;
	}

	//heal

	/**
	 * returns the temporary healing amount on execution
	 * @return int healsHP
     */
	public int getHEAL(){
		return this.healsHP;
	}
	public void addHEAL(int _i){
		this.healsHP =+ _i;
	}

	//Zorn
	public int getZORN(){
		return this.zorn;
	}
	void setZORN(int _zorn){
		this.zorn= _zorn;
	}
	public void addZORN(int _zorn){
		this.zorn += _zorn;
		if (this.zorn > max_zorn){
			this.zorn = max_zorn;
		}
	}

	//get action from actions pool
	public String getACTION(int i){
		return this.actions[i];
	}

	//used action
	public void setACTION(String _action){
		this.action= _action;
	}
	public String getACTION(){
		return this.action;
	}

	public void use_action(player _target){
		//use action
		if (abilities.containsKey(this.getACTION())){
			abilities.get(this.getACTION()).execute(this, _target);
		}

	}

	public void use_action(player _target, String action) {
		//use action
		if (abilities.containsKey(action)){
			abilities.get(action).execute(this, _target);
		}
	}

	//Speed
	public int getActionDuration(){
		return Math.round(this.speed * abilities.get(this.getACTION()).duration());
	}

	public int zorn_req(int i){
		if (abilities.containsKey(this.getACTION(i))){
			return abilities.get(this.getACTION(i)).zorn_requirement();
		} else {
			return 250;
		}
	}

	//physical damage calculation
	public void DamageStepPhysical(int dmg_taken) {
		this.setHP(this.getHP() - dmg_taken);
	}
	//healing
	public void DamageStepHealing(int healing_taken) {
		this.addHP(healing_taken);
	}

	public int getMalice() {
		return malice;
	}


	public PassiveList getConditionList() {
		return passive_conditions;
	}

	public void addCondition(String action, int ticks,  int malice) {
		if (action.matches("bleeding") ) {
			//add new condition
			passive_conditions.add(action, false, ticks, malice);
		} else {
			//stack ticks
			passive_conditions.stack(action, ticks, malice);
		}
		abilities.get(action).onStart(this);
	}

	public void removeCondition(String action) {
		passive_conditions.removeActionFromPlayer(action, this);
	}
	public PassiveList getPassivePermanentList() {
		return passive_permanent;
	}
	public boolean hasCondition(String condition) {
		return getConditionList().hasPassive(condition);
	}

	//interrupt
	public boolean isAttacking() {
		return isAttacking;
	}
	public void setIsAttacking(boolean b) {
		isAttacking = b;
	}
	public void interrupt() {
		//countDownTimer.cancel();
		setIsAttacking(false);
	}

	//countdowntimer
	public void setAction(CountDownTimerWithPause setter) {
		countDownTimer = setter;
	}
	public void startAction() {
		countDownTimer.start();
	}
	public void resumeAction() { countDownTimer.resume(); }
	public void pauseAction() {
		countDownTimer.pause();
	}
	public void cancelAction() {
		countDownTimer.cancel();
	}

}