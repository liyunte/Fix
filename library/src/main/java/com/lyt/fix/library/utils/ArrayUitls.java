package com.lyt.fix.library.utils;

import java.lang.reflect.Array;

public class ArrayUitls {
    public static Object combineArray(Object myDexElements, Object sysDexElements) {
        Class<?> localClass = myDexElements.getClass().getComponentType();
        int i = Array.getLength(myDexElements);
        int j = i+Array.getLength(sysDexElements);
        Object result = Array.newInstance(localClass,j);
        for (int k=0;k<j;k++){
            if (k<i){
                Array.set(result,k,Array.get(myDexElements,k));
            }else {
                Array.set(result,k,Array.get(sysDexElements,k-i));
            }
        }
        return result;
    }
}
