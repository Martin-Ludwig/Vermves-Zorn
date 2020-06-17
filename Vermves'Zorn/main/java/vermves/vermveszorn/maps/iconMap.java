package vermves.vermveszorn.maps;

import java.util.HashMap;
import java.util.Map;

import vermves.vermveszorn.R;

/**
 * Created by Martin on 20.11.2016.
 */

public class iconMap {
    private Map iconMap;

    public iconMap(){
        iconMap = new HashMap();

        //iconMap.put("default", R.drawable.ic_placeholder);
        iconMap.put("bleed", R.drawable.ic_bleed);
        iconMap.put("burn", R.drawable.ic_burn);
    }

    public int getImageResource(String conditionName){
        if (iconMap.containsKey(conditionName)) {
            return (int) iconMap.get(conditionName);
        }
        return (int) iconMap.get("default");
    }

    public int getSize(){
        return iconMap.size();
    }
}
