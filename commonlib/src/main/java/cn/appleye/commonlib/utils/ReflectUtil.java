package cn.appleye.commonlib.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author newhope1106
 * @date 2018/8/13
 * @description 反射工具类
 */
public class ReflectUtil {
    private ReflectUtil(){}

    /**
     * 创建实例
     * @param clsName 类名
     * @param args 参数
     * @param argType 参数类型
     * @return 实例
     * */
    public static Object newInstance(String clsName, Object[] args, Class[] argType) throws
            ClassNotFoundException, IllegalAccessException, InstantiationException,
            NoSuchMethodException, InvocationTargetException{
        Class newCls = Class.forName(clsName);
        if(args == null){
            return newCls.newInstance();
        } else {
            Constructor constructor = newCls.getConstructor(argType);
            return constructor.newInstance(args);
        }
    }

    /**
     * 获取对象属性
     * @param owner 对象
     * @param fieldName 属性名
     * @return 属性
     * */
    public static Object getProperty(Object owner, String fieldName) throws Exception{
        Class ownerCls = owner.getClass();
        Field field = ownerCls.getField(fieldName);
        return field.get(owner);
    }

    /**
     * 设置对象属性
     * @param owner 对象
     * @param fieldName 属性名
     * @param value 属性值
     * */
    public static void setProperty(Object owner, String fieldName, Object value) throws Exception{
        Class ownerCls = owner.getClass();
        Field field = ownerCls.getField(fieldName);
        field.set(owner, value);
    }

    /**
     * 获取静态属性
     * @param clsName 类名
     * @param fieldName 属性名
     * @return 属性
     * */
    public static Object getStaticProperty(String clsName, String fieldName) throws Exception{
        Class ownerCls = Class.forName(clsName);
        Field field = ownerCls.getField(fieldName);
        return field.get(ownerCls);
    }

    /**
     * 获取静态属性
     * @param clsName 类名
     * @param fieldName 属性名
     ** @param value 属性值
     * */
    public static void setStaticProperty(String clsName, String fieldName, Object value) throws Exception{
        Class ownerCls = Class.forName(clsName);
        Field field = ownerCls.getField(fieldName);
        field.set(ownerCls, value);
    }

    /**
     * 调用对象方法
     * @param owner 对象
     * @param methodName 方法名
     * @param args 参数
     * @return 调用结果
     * */
    public static Object invokeMethod(Object owner, String methodName, Object[] args) throws Exception{
        Class ownerCls = owner.getClass();

        if(args == null){
            Method method = ownerCls.getMethod(methodName, null);
            return method.invoke(owner, null);
        } else {
            Class[] argsCls = new Class[args.length];
            for (int i = 0, j = args.length; i < j; i++) {
                argsCls[i] = args[i].getClass();
            }

            Method method = ownerCls.getMethod(methodName, argsCls);
            return method.invoke(owner, args);
        }
    }

    /**
     * 调用静态方法
     * @param clsName 类名
     * @param methodName 方法名
     * @param args 参数
     * @return 调用结果
     * */
    public static Object invokeStaticMethod(String clsName, String methodName, Object[] args) throws Exception{
        Class ownerCls = Class.forName(clsName);

        if(args == null){
            Method method = ownerCls.getMethod(methodName, null);
            return method.invoke(ownerCls, null);
        } else {
            Class[] argsCls = new Class[args.length];
            for (int i = 0, j = args.length; i < j; i++) {
                argsCls[i] = args[i].getClass();
            }

            Method method = ownerCls.getMethod(methodName, argsCls);
            return method.invoke(ownerCls, args);
        }
    }
}
