package com.metric.auth.common.utils;

import org.springframework.cglib.beans.BeanCopier;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @description: obj拷贝
 * @author: YangShu
 * @create: 2024-06-30
 **/
public class BeanCopierUtils {

    private static final ConcurrentMap<String, BeanCopier> BC_CACHE = new ConcurrentHashMap<>();
    private static final String KEY_SPLITOR = "->";
    private static final ConcurrentMap<String, Constructor> CONST_CACHE = new ConcurrentHashMap<>();

    public static <S, D> void copy(S srcObject, D destObject) {
        BeanCopier bc = getBeanCopier(srcObject.getClass(), destObject.getClass());
        bc.copy(srcObject, destObject, null);
    }

    public static <S, D> D copy(S srcObject, Class<D> destClass)
            throws NoSuchMethodException, IllegalAccessException,
            InstantiationException, InvocationTargetException {
        Class<?> srcClass = srcObject.getClass();
        BeanCopier bc = getBeanCopier(srcClass, destClass);
        Constructor<D> constructor = getConstructor(destClass);
        D d = constructor.newInstance();
        bc.copy(srcObject, d, null);
        return d;
    }

    private static BeanCopier getBeanCopier(Class<?> srcClass, Class<?> destClass) {
        String key = generateKey(srcClass, destClass);
        return BC_CACHE.computeIfAbsent(
                key, k -> BeanCopier.create(srcClass, destClass, false));
    }

    private static String generateKey(Class<?> srcClass, Class<?> destClass) {
        StringBuilder sb = new StringBuilder("");
        String srcClassName = srcClass.getName();
        String destClassName = destClass.getName();
        sb.append(srcClassName).append(KEY_SPLITOR).append(destClassName);
        return sb.toString();
    }

    private static <D> Constructor<D> getConstructor(Class<D> destClass)
            throws NoSuchMethodException {
        Constructor constructor = CONST_CACHE.get(destClass.getName());
        if (constructor == null) {
            constructor = destClass.getConstructor();
            CONST_CACHE.put(destClass.getName(), constructor);
        }
        return constructor;
    }
}
