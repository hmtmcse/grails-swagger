package com.hmtmcse.swagger.definition;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class SwaggerMap<K, V> {

    private LinkedHashMap<K, V> hashMap = new LinkedHashMap<>();

    public SwaggerMap<K, V> set(K key, V value){
        hashMap.put(key, value);
        return this;
    }

    public LinkedHashMap<K, V> get(){
        return hashMap;
    }

    public LinkedHashMap<K, V> setGet(K key, V value){
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
