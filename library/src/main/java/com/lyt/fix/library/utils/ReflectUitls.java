package com.lyt.fix.library.utils;

import java.lang.reflect.Field;



public class ReflectUitls {
    private static final String DEXELEMENTS = "dexElements";
    private static final String PATH_LIST = "pathList";
    private static final String CLASS_LOADER = "dalvik.system.BaseDexClassLoader";

    private static Object getField(Object obj,Class<?> clazz,String field)throws NoSuchFieldException,IllegalAccessException{
        Field localField = clazz.getDeclaredField(field);
        localField.setAccessible(true);
        return localField.get(obj);
    }

    public static void setField(Object obj,Class<?> clazz,Object value)throws NoSuchFieldException,IllegalAccessException{
        Field localField = clazz.getDeclaredField(DEXELEMENTS);
        localField.setAccessible(true);
            localField.set(obj,value);
    }

    public static Object getPathList(Object classLoader) throws NoSuchFieldException,IllegalAccessException,ClassNotFoundException{
      return getField(classLoader,Class.forName(CLASS_LOADER),PATH_LIST);
    }

    public static Object getDexElements(Object pathList) throws NoSuchFieldException,IllegalAccessException,ClassNotFoundException {
        return getField(pathList,pathList.getClass(),DEXELEMENTS);
    }
}
