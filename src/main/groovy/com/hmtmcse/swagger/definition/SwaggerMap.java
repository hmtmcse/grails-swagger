package com.hmtmcse.swagger.definition;

import java.util.HashMap;

public class SwaggerMap<K, V> {

    private HashMap<K, V> hashMap = new HashMap<>();

    public SwaggerMap<K, V> set(K key, V value){
        hashMap.put(key, value);
        return this;
    }

    public HashMap<K, V> get(){
        return hashMap;
    }

    public HashMap<K, V> setGet(K key, V value){
        set(key, value);
        return get();
    }

    public static SwaggerMap<String, String> string(){
        return new SwaggerMap<String, String>();
    }

    public static SwaggerMap<Object, Object> object(){
        return new SwaggerMap<Object, Object>();
    }

}
