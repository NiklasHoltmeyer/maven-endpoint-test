package de.hsos.bachelorarbeit.nh.endpoint.coverage.util;

import java.util.HashMap;
import java.util.List;

public class CountedHashMap<K extends CountedHashMapEntity> {
    public HashMap<K,Integer> generate(List<K> keys){
        HashMap<K, Integer> integerHashMap = new HashMap<>();
        keys.forEach(x -> {
            if(integerHashMap.containsKey(x)){
                integerHashMap.put(x, integerHashMap.get(x)+1);
            }else{
                integerHashMap.put(x, 1);
            }
        });
        return integerHashMap;
    }
}
