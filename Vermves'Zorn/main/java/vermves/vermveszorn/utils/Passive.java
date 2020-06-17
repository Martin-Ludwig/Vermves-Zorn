package vermves.vermveszorn.utils;

/**
 * Created by Martin on 18.01.2017.
 */

public class Passive {

    private String action;
    private boolean permanent;
    private int ticks;
    private int baseCondDmg;

    public Passive(String action, boolean permanent, int ticks, int baseCondDmg) {
        this.action = action;
        this.permanent = permanent;
        this.ticks = ticks;
        this.baseCondDmg = baseCondDmg;
    }

    public String getAction() {
        return action;
    }

    public boolean isPermanent() {
        return permanent;
    }
    public boolean isExpired() {
        return ( (ticks <= 0) && (!permanent) );
    }

    public int getTicks() {
        return ticks;
    }

    public void addTicks(int amount, int condDmg) {
        baseCondDmg = condDmg;
        ticks += amount;
    }

    public void removeTick() {
        if (!permanent)
            ticks--;
    }

    public void removeTicks(int amount) {
        if (!permanent)
            ticks =- amount;
    }

    public int getBaseCondDmg() {
        return baseCondDmg;
    }

}
