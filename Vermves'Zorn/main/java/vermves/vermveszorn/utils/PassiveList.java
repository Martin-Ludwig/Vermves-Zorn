package vermves.vermveszorn.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import vermves.vermveszorn.interfaces.defaultAbilityInterface;
import vermves.vermveszorn.maps.abilitiesMap;
import vermves.vermveszorn.models.player;

/**
 * Created by Martin on 18.01.2017.
 */

public class PassiveList {
    //ability map
    private final abilitiesMap CreateActionsMap= new abilitiesMap();
    private final Map<String, defaultAbilityInterface> abilities = CreateActionsMap.getHashmap();


    private ArrayList<Passive> passiveList;

    public PassiveList() {
        passiveList = new ArrayList<Passive>();
        //passiveList.add(new Passive(String action, boolean permanent, int ticks);
    }

    //returns indices of the action name
    //returns empty integer list if nothing was found
    private List<Integer> getIndicesByAction(String action) {
        List<Integer> indices = new ArrayList<Integer>();

        int size = passiveList.size();
        for (int i=0; i <= size; i++) {
            if (passiveList.get(i).getAction().equals(action)) {
                indices.add(i);
            }
        }

        return indices;
    }

    //returns the index of the first found action, counting upwards
    //returns -1 if nothing was found
    private int getFirstIndexOfAction(String action) {
        int size = passiveList.size();

        for (int i=0; i <= size; i++) {
            if (passiveList.get(i).getAction().equals(action))
                return i;
        }

        return -1;
    }

    //returns the index of the first found action, counting downwards
    //returns -1 if nothing was found
    private int getLastIndexOfAction(String action) {
        int size = passiveList.size() - 1;

        for (int i=size; 0 <= i; i--) {
            if (passiveList.get(i).getAction().equals(action))
                return i;
        }

        return -1;
    }

    public boolean hasPassive(String action) {
        return getLastIndexOfAction(action) >= 0;
    }


    //create and add passive
    public void add(String action, boolean permanent, int ticks, int condDmg) {
        passiveList.add(new Passive(action, permanent, ticks, condDmg));
    }
    //add passive
    public void add(Passive passive) {
        passiveList.add(passive);
    }

    public void stack(String action, int ticks, int condDmg) {
        int index = getLastIndexOfAction(action);

        if (index == -1) {
            this.add(action, false, ticks, condDmg);             //nicht vorhanden -> füge neu hinzu
        } else {
            passiveList.get(index).addTicks(ticks, condDmg);     //vorhanden -> addiere ticks
        }
    }

    public int getStacks(String action) {
        int i = 0;
        for (final Passive passive: this.passiveList) {
            if (passive.getAction().equals(action))
                i++;
        }
        return i;
    }

    //remove
    private void removeIfExpired(int index, player player) {
        if (passiveList.get(index).isExpired() && !passiveList.get(index).isPermanent()) {
            abilities.get(passiveList.get(index).getAction()).onEnd(player);
            passiveList.remove(index);
        }
    }
    private void removeFirstActionByName(String action) {
        if (getLastIndexOfAction(action) >= 0) {
            passiveList.remove(getLastIndexOfAction(action));
        }
    }
    public void removeActionFromPlayer(String action, player player) {
        if (getLastIndexOfAction(action) >= 0) {
            abilities.get(action).onEnd(player);
            passiveList.remove(getLastIndexOfAction(action));
        }
    }

    public void clear() {
        passiveList.clear();
    }

    /**
     * Trigger
     */

    public void onTick(player player) {
        //@Todo
        final int size = passiveList.size() - 1;
        if (size >= 0) {
            for (int i = size; i >= 0; i--) {
                abilities.get(passiveList.get(i).getAction()).onTick(player, passiveList.get(i));
                passiveList.get(i).removeTick();
                removeIfExpired(i, player);
            }
        }
    }
    public void onExecution(player player, player target) {
        //@Todo
    }

    public void beforeHit(player player, player target) {
        //@Todo
    }
    public void afterHit(player player, player target) {
        //@Todo
    }
    public void beforeHitTaken(player player, player target) {
        //@Todo
    }
    public void afterHitTaken(player player, player target) {
        //@Todo
    }
    public void beforeHealing(player player, player target) {
        //@Todo
    }
    public void afterHealing(player player, player target) {
        //@Todo
    }
    public void afterConditionApplied(player player, player target) {
        //@Todo
    }
    public void afterConditionRemoved(player player, player target) {
        //@Todo
    }
    public void beforeConditionDmgTaken(player player, player target) {
        //@Todo
    }
    public void afterConditionDmgTaken(player player, player target) {
        //@Todo
    }

}