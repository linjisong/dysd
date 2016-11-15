/**
 * Copyright (c) 2016-2017, the original author or authors (dysd_2016@163.com).
 * <p>
 * Licensed under the GPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/gpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dysd.util.base;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.dysd.util.exception.ExceptionCodes;
import org.dysd.util.exception.Throw;
import org.dysd.util.logger.CommonLogger;
import org.springframework.objenesis.SpringObjenesis;

/**
 * 反射工具类
 * @author linjisong
 * @version 0.0.1
 * @since 0.0.1
 * @date 2016-11-8
 */
public abstract class BaseReflectUtils {

	private static final BaseReflectUtils instance = new BaseReflectUtils(){};
	private BaseReflectUtils(){
	}
	
	/**
	 * 获取单实例
	 * @return
	 */
	/*package*/ static BaseReflectUtils getInstance(){
		return instance;
	}
	
	/**
	 * Spring的类创建对象
	 */
	private static final SpringObjenesis objenesis = new SpringObjenesis();
	private static final Class<?>[] ORDERED_PRIMITIVE_TYPES = { Byte.TYPE, Short.TYPE, Character.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE };
	
	/**
	 * 获取默认类加载器
	 * @return 默认类加载器
	 */
	public ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		}
		catch (Throwable ex) {
		}
		if (cl == null) {
			cl = BaseReflectUtils.class.getClassLoader();
			if (cl == null) {
				try {
					cl = ClassLoader.getSystemClassLoader();
				}catch (Throwable ex) {
				}
			}
		}
		return cl;
	}
	
	/**
	 * 加载类
	 * @param name 类名
	 * @return 类对象
	 */
	public Class<?> forName(String name){
		return forName(name, null);
	}
	
	/**
	 * 使用指定类加载器加载类
	 * @param name 类名
	 * @param classLoader 类加载器
	 * @return 类对象
	 */
	public Class<?> forName(String name, ClassLoader classLoader){
		try {
			if(null == classLoader){
				classLoader = getDefaultClassLoader();
			}
            return org.springframework.util.ClassUtils.forName(name, classLoader);
        } catch (Exception ex) {
        	throw Throw.createException(ExceptionCodes.DYSD010005, ex, classLoader, name);
        }
	}
	
	/**
	 * 转换一组类名为一组类对象
	 * @param classNames 类名称列表
	 * @return 类对象列表
	 */
	public List<Class<?>> convertClassNamesToClasses(List<String> classNames) {
		if (classNames == null) {
            return null;
        }
        List<Class<?>> classes = new ArrayList<Class<?>>(classNames.size());
        ClassLoader classLoader = getDefaultClassLoader();
        for (String className : classNames) {
            try {
                classes.add(forName(className, classLoader));
            } catch (Exception ex) {
                classes.add(null);
            }
        }
        return classes;
    }

	/**
	 * 转换一组类对象为一组类名
	 * @param classes 类对象
	 * @return 类名列表
	 */
    public List<String> convertClassesToClassNames(List<Class<?>> classes) {
    	if (classes == null) {
            return null;
        }
        List<String> classNames = new ArrayList<String>(classes.size());
        for (Class<?> cls : classes) {
            if (cls == null) {
                classNames.add(null);
            } else {
                classNames.add(cls.getName());
            }
        }
        return classNames;
    }
    
    /**
	 * 转换一组类对象为一组类名
	 * @param classes 类对象
	 * @return 类名列表
	 */
    public List<String> convertClassesToClassNames(Class<?>[] classes) {
        if (classes == null) {
            return null;
        }
        List<String> classNames = new ArrayList<String>(classes.length);
        for (Class<?> cls : classes) {
            if (cls == null) {
                classNames.add(null);
            } else {
                classNames.add(cls.getName());
            }
        }
        return classNames;
    }
	
	/**
	 * 判断类是否存在
	 * @param className 类名
	 * @return 是否存在
	 */
	public boolean isPresent(String className){
		return isPresent(className, null);
	}
	
	/**
	 * 判断类是否存在
	 * @param className   类名
	 * @param classLoader 类加载器
	 * @return 是否存在
	 */
	public boolean isPresent(String className, ClassLoader classLoader) {
		return org.springframework.util.ClassUtils.isPresent(className, resolverClassLoader(classLoader));
	}
	
	/**
	 * 获取枚举类型实例
	 * @param enumClass 枚举类
	 * @param enumName	枚举名
	 * @return 枚举实例
	 */
	public <E extends Enum<E>> E getEnum(Class<E> enumClass, String enumName){
        try {
        	if (enumName == null) {
                return null;
            }
            return Enum.valueOf(enumClass, enumName);
        } catch (IllegalArgumentException ex) {
            return null;
        }
	}
	
	/**
	 * 获取枚举类型实例
	 * @param enumClass 枚举类
	 * @param enumName	枚举名
	 * @param defaultValue 默认值
	 * @return 枚举实例
	 */
	public <E extends Enum<E>> E getEnum(Class<E> enumClass, String enumName, E defaultValue){
        E value = getEnum(enumClass, enumName);
        return value == null ? defaultValue : value;
	}
	
	/**
	 * 复制对象
	 * @param obj 源对象
	 * @return 复制后对象
	 */
	public <T> T clone(final T obj) {
		return ObjectUtils.clone(obj);
	}
	
	/**
	 * 返回用户定义的类，一般用于CGLIB生成的动态子类实例
	 * @param instance 类实例
	 * @return 用户定义的类
	 */
	public Class<?> getUserClass(Object instance) {
		return getUserClass(instance.getClass());
	}

	/**
	 * 返回用户定义的类，一般用于CGLIB生成的动态子类
	 * @param clazz 类名
	 * @return 用户定义的类
	 */
	public Class<?> getUserClass(Class<?> clazz) {
		return org.springframework.util.ClassUtils.getUserClass(clazz);
	}
	
	/**
	 * 获取所有父类
	 * @param cls 类
	 * @return 所有父类
	 */
	public List<Class<?>> getAllSuperclasses(Class<?> cls) {
        return ClassUtils.getAllSuperclasses(cls);
    }
	
	/**
	 * 获取所有接口
	 * @param cls 类
	 * @return 所有接口
	 */
	public List<Class<?>> getAllInterfaces(Class<?> cls) {
        return ClassUtils.getAllInterfaces(cls);
	}
	
	/**
	 * 是否简单类型(8种基本类型及其包装类型，3种字符串类型（String、StringBuilder、StringBuffer），2种高精度数据类型（BigIngeger、BigDecimal），日期类型（Date）)
	 * @param cls 类
	 * @return 是否简单类型
	 */
	public boolean isSimpleCls(Class<?> cls) {
		if(null == cls){
			return false;
		}
		return cls.equals(byte.class) || cls.equals(Byte.class)
			|| cls.equals(char.class) || cls.equals(Character.class)
			|| cls.equals(short.class) || cls.equals(Short.class)
			|| cls.equals(int.class) || cls.equals(Integer.class)
			|| cls.equals(long.class) || cls.equals(Long.class)
			|| cls.equals(float.class) || cls.equals(Float.class)
			|| cls.equals(double.class) || cls.equals(Double.class)
			|| cls.equals(boolean.class) || cls.equals(Boolean.class)
			//|| CharSequence.class.isAssignableFrom(type)//String.class StringBuilder.class StringBuffer.class
			|| cls.equals(String.class) || cls.equals(StringBuilder.class) || cls.equals(StringBuffer.class)
			|| cls.equals(BigInteger.class) || cls.equals(BigDecimal.class)
			|| cls.equals(Date.class);
	}
	
	/**
	 * cls 是否 toClass的超类
	 * @param cls
	 * @param toClass
	 * @return 布尔值
	 */
	public boolean isAssignable(Class<?> cls, Class<?> toClass) {
       return ClassUtils.isAssignable(cls, toClass);
    }
	
	/**
	 * classArray是否为toClassArray的超类数组
	 * @param classArray
	 * @param toClassArray
	 * @return 布尔值
	 */
	public boolean isAssignable(Class<?>[] classArray, Class<?>... toClassArray){
		return ClassUtils.isAssignable(classArray, toClassArray);
	}
	
	/**
	 * 是否内部类
	 * @param cls 类
	 * @return 是否内部类
	 */
	public boolean isInnerClass(Class<?> cls){
		return cls != null && cls.getEnclosingClass() != null;
	}
	
	/**
	 * 创建类实例
	 * @param cls 类，没有默认构造器的，会使用SpringObjenesis创建，这种情况下类中属性是没有初始化的
	 * @return 类实例
	 */
	public <T> T newInstance(Class<T> cls) {
		try{
			if(null == cls){
				return null;
			}
			return cls.newInstance();
		}catch(Exception e){
			CommonLogger.warn("can not create instance by default constructor, try use the objenesis method, the class : " + cls);
			return objenesis.newInstance(cls);
		}
	}
	
	/**
	 * 创建类实例
	 * @param className 类名
	 * @return 类实例
	 */
	public Object newInstance(String className) {
		if(null == className){
			return null;
		}
		Class<?> cls = forName(className);
		return newInstance(cls);
	}
	
	/**
	 * 创建类实例
	 * @param className 类名
	 * @param cls       目标类型
	 * @return 类实例
	 */
	public <T> T newInstance(String className, Class<T> cls) {
		if(null == className){
			return null;
		}
		Class<?> clss = forName(className);
		return cls.cast(newInstance(clss));
	}
	
	/**
	 * 获取最匹配的构造器
	 * @param cls			   类
	 * @param parameterTypes 参数类型
	 * @return 构造器
	 */
	public <T> Constructor<T> getMatchingAccessibleConstructor(Class<T> cls, Class<?>... parameterTypes){
		return ConstructorUtils.getMatchingAccessibleConstructor(cls, parameterTypes);
	}
	
	/**
	 * 设置构造器可访问
	 * @param ctor 构造器
	 */
	public void makeAccessible(Constructor<?> ctor) {
		if ((!Modifier.isPublic(ctor.getModifiers()) ||
				!Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) && !ctor.isAccessible()) {
			ctor.setAccessible(true);
		}
	}
	
	/**
	 * 调用构造器方法
	 * @param cls  类
	 * @param args 参数
	 * @return 类实例
	 */
	public <T> T invokeConstructor(Class<T> cls, Object... args){
		try{
			return ConstructorUtils.invokeConstructor(cls, args);
		}catch(Exception e){
			throw Throw.createException(ExceptionCodes.DYSD010006, e, cls);
		}
	}
	
	/**
	 * 调用构造器方法
	 * @param cls			  类
	 * @param args			 参数
	 * @param parameterTypes 参数类型
	 * @return 类实例
	 */
	public <T> T invokeConstructor(Class<T> cls, Object[] args, Class<?>[] parameterTypes){
		try{
			return ConstructorUtils.invokeConstructor(cls, args, parameterTypes);
		}catch(Exception e){
			throw Throw.createException(ExceptionCodes.DYSD010006, e, cls);
		}
	}
	
	/**
	 * 是否常量（public static final）
	 * @param field 属性
	 * @return 是否常量
	 */
	public boolean isPublicStaticFinalField(Field field) {
		int modifiers = field.getModifiers();
		return (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers));
	}
	
	/**
	 * 获取属性
	 * @param cls		类
	 * @param fieldName 属性名称
	 * @return 属性对象
	 */
	public Field getField(final Class<?> cls, String fieldName){
		return FieldUtils.getField(cls, fieldName, true);
	}
	
	/**
	 * 设置属性可访问
	 * @param field 属性对象
	 */
	public void makeAccessible(Field field) {
		if ((!Modifier.isPublic(field.getModifiers()) ||
				!Modifier.isPublic(field.getDeclaringClass().getModifiers()) ||
				Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
			field.setAccessible(true);
		}
	}
	
	/**
	 * 读静态属性
	 * @param field 属性
	 * @return 属性值
	 */
	public Object readStaticField(Field field){
		try{
			return FieldUtils.readStaticField(field, true);
		}catch(Exception e){
			throw Throw.createException(ExceptionCodes.DYSD010007, e, field);
		}
	}
	
	/**
	 * 读静态属性
	 * @param cls		类
	 * @param fieldName 属性名
	 * @return 属性值
	 */
	public Object readStaticField(Class<?> cls, String fieldName){
		try{
			return FieldUtils.readStaticField(cls, fieldName, true);
		}catch(Exception e){
			throw Throw.createException(ExceptionCodes.DYSD010007, e, cls.getName()+"."+fieldName);
		}
	}
	
	/**
	 * 读属性
	 * @param field  属性
	 * @param target 目标对象
	 * @return 属性值
	 */
	public Object readField(Field field, Object target){
		try{
			return FieldUtils.readField(field, target, true);
		}catch(Exception e){
			throw Throw.createException(ExceptionCodes.DYSD010007, e, field);
		}
	}
	
	/**
	 * 读属性
	 * @param target    目标对象
	 * @param fieldName 属性名
	 * @return 属性值
	 */
	public Object readField(Object target, String fieldName){
		try{
			return FieldUtils.readField(target, fieldName, true);
		}catch(Exception e){
			throw Throw.createException(ExceptionCodes.DYSD010007, e, target.getClass().getName()+"."+fieldName);
		}
	}
	
	/**
	 * 写静态属性
	 * @param field 属性
	 * @param value 属性值
	 */
	public void writeStaticField(Field field, Object value){
		try{
			FieldUtils.writeStaticField(field, value, true);
		}catch(Exception e){
			throw Throw.createException(ExceptionCodes.DYSD010007, e, field);
		}
	}
	
	/**
	 * 写静态属性
	 * @param cls		类
	 * @param fieldName 属性名
	 * @param value		属性值
	 */
	public void writeStaticField(Class<?> cls, String fieldName, Object value){
		try{
			FieldUtils.writeStaticField(cls, fieldName, value, true);
		}catch(Exception e){
			throw Throw.createException(ExceptionCodes.DYSD010007, e, cls.getName()+"."+fieldName);
		}
	}
	
	/**
	 * 写属性
	 * @param field	   属性
	 * @param target 目标对象
	 * @param value  属性值
	 */
	public void writeField(Field field, Object target, Object value){
		try{
			FieldUtils.writeField(field, target, value, true);
		}catch(Exception e){
			throw Throw.createException(ExceptionCodes.DYSD010007, e, field);
		}
	}
	
	/**
	 * 写属性
	 * @param target	目标对象
	 * @param fieldName 属性名
	 * @param value		属性值
	 */
	public void writeField(Object target, String fieldName, Object value){
		try{
			FieldUtils.writeField(target, fieldName, value, true);
		}catch(Exception e){
			throw Throw.createException(ExceptionCodes.DYSD010007, e, target.getClass().getName()+"."+fieldName);
		}
	}
	
	/**
	 * 获取最匹配的方法
	 * @param cls				类
	 * @param methodName		方法名
	 * @param parameterTypes	参数类型
	 * @return	最匹配的方法对象
	 */
	public Method getMatchingAccessibleMethod(Class<?> cls, String methodName, Class<?>... parameterTypes){
		return MethodUtils.getMatchingAccessibleMethod(cls, methodName, parameterTypes);
	}
	
	/**
	 * 获取最匹配的类组
	 * @param clss 候选的类组
	 * @param cls 目标类组
	 * @return 最匹配的类组
	 */
	public Class<?>[] getBestMatchClasses(Collection<Class<?>[]> clss, Class<?>[] cls){
		if(null == clss || null == cls || clss.isEmpty()){
			return null;
		}else{
			Set<Class<?>[]> matches = new HashSet<Class<?>[]>();
			for(Class<?>[] c : clss){
				if(isAssignable(cls, c)){
					matches.add(c);
				}
			}
			if(matches.isEmpty()){
				return null;
			}else if(matches.size() == 1){
				return matches.iterator().next();
			}else{
				Iterator<Class<?>[]> i = matches.iterator();
				Class<?>[] m = i.next();
				for(; i.hasNext(); ){
					Class<?>[] c = i.next();
					if(compareParameterTypes(c, m, cls) < 0){
						m = c;
					}
				}
				return m;
			}
		}
	}
	
	/**
	 * 获取最匹配的类
	 * @param clss 候选的类
	 * @param cls 目标类
	 * @return 最匹配的类
	 */
	public Class<?> getBestMatchClass(Collection<Class<?>> clss, Class<?> cls){
		if(null == clss || null == cls || clss.isEmpty()){
			return null;
		}else{
			Set<Class<?>> matches = new HashSet<Class<?>>();
			for(Class<?> c : clss){
				if(isAssignable(cls, c)){
					matches.add(c);
				}
			}
			if(matches.isEmpty()){
				return null;
			}else if(matches.size() == 1){
				return matches.iterator().next();
			}else{
				Iterator<Class<?>> i = matches.iterator();
				Class<?> m = i.next();
				for(; i.hasNext(); ){
					Class<?> c = i.next();
					if(compareParameterTypes(c, m, cls) < 0){
						m = c;
					}
				}
				return m;
			}
		}
	}
    
	/**
	 * 设置方法可访问
	 * @param method 方法
	 */
	public void makeAccessible(Method method) {
		if ((!Modifier.isPublic(method.getModifiers()) ||
				!Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
			method.setAccessible(true);
		}
	}
	
	/**
	 * 调用方法
	 * @param target	 目标对象
	 * @param methodName 方法名
	 * @param args 调用参数
	 * @return 方法返回值
	 */
	public Object invokeMethod(Object target, String methodName, Object... args){
		try {
			return MethodUtils.invokeMethod(target, methodName, args);
		} catch (Exception e) {
			throw Throw.createException(ExceptionCodes.DYSD010008, e, target.getClass().getName()+"."+methodName);
		}
	}
	
	/**
	 * 调用方法
	 * @param target		 目标对象
	 * @param methodName	 方法名
	 * @param args			 方法参数
	 * @param parameterTypes 方法参数类型
	 * @return 方法返回值
	 */
	public Object invokeMethod(Object target, String methodName, Object[] args, Class<?>[] parameterTypes){
		try {
			return MethodUtils.invokeMethod(target, methodName, args, parameterTypes);
		} catch (Exception e) {
			throw Throw.createException(ExceptionCodes.DYSD010008, e, target.getClass().getName()+"."+methodName);
		}
	}
	
	/**
	 * 调用静态方法
	 * @param cls		  类 
	 * @param methodName 方法名
	 * @param args		 方法参数
	 * @return 方法返回值
	 */
	public Object invokeStaticMethod(Class<?> cls, String methodName, Object[] args){
		try {
			return MethodUtils.invokeStaticMethod(cls, methodName, args);
		} catch (Exception e) {
			throw Throw.createException(ExceptionCodes.DYSD010008, e, cls.getName()+"."+methodName);
		}
	}
	
	/**
	 * 调用静态方法
	 * @param cls			 类
	 * @param methodName     方法名
	 * @param args			 方法参数
	 * @param parameterTypes 方法参数类型
	 * @return 方法返回值
	 */
	public Object invokeStaticMethod(Class<?> cls, String methodName, Object[] args, Class<?>[] parameterTypes){
		try {
			return MethodUtils.invokeStaticMethod(cls, methodName, args, parameterTypes);
		} catch (Exception e) {
			throw Throw.createException(ExceptionCodes.DYSD010008, e, cls.getName()+"."+methodName);
		}
	}
	
	
	/**
	 * =========以下为私有的辅助方法===============
	 */
	
	
	private ClassLoader resolverClassLoader(ClassLoader classLoader){
		if(null == classLoader){
			return this.getDefaultClassLoader();
		}
		return classLoader;
	}
	
	private int compareParameterTypes(final Class<?> left, final Class<?> right, final Class<?> actual) {
        final float leftCost = getObjectTransformationCost(actual, left);
        final float rightCost = getObjectTransformationCost(actual, right);
        return leftCost < rightCost ? -1 : rightCost < leftCost ? 1 : 0;
    }
	
	private int compareParameterTypes(final Class<?>[] left, final Class<?>[] right, final Class<?>[] actual) {
        final float leftCost = getTotalTransformationCost(actual, left);
        final float rightCost = getTotalTransformationCost(actual, right);
        return leftCost < rightCost ? -1 : rightCost < leftCost ? 1 : 0;
    }
	
	private float getTotalTransformationCost(final Class<?>[] srcArgs, final Class<?>[] destArgs) {
        float totalCost = 0.0f;
        for (int i = 0; i < srcArgs.length; i++) {
            Class<?> srcClass, destClass;
            srcClass = srcArgs[i];
            destClass = destArgs[i];
            totalCost += getObjectTransformationCost(srcClass, destClass);
        }
        return totalCost;
    }
	
	private float getObjectTransformationCost(Class<?> srcClass, final Class<?> destClass) {
        if (destClass.isPrimitive()) {
            return getPrimitivePromotionCost(srcClass, destClass);
        }
        float cost = 0.0f;
        while (srcClass != null && !destClass.equals(srcClass)) {
            if (destClass.isInterface() && ClassUtils.isAssignable(srcClass, destClass)) {
                cost += 0.25f;
                break;
            }
            cost++;
            srcClass = srcClass.getSuperclass();
        }
        if (srcClass == null) {
            cost += 1.5f;
        }
        return cost;
    }
	
    private float getPrimitivePromotionCost(final Class<?> srcClass, final Class<?> destClass) {
        float cost = 0.0f;
        Class<?> cls = srcClass;
        if (!cls.isPrimitive()) {
            cost += 0.1f;
            cls = ClassUtils.wrapperToPrimitive(cls);
        }
        for (int i = 0; cls != destClass && i < ORDERED_PRIMITIVE_TYPES.length; i++) {
            if (cls == ORDERED_PRIMITIVE_TYPES[i]) {
                cost += 0.1f;
                if (i < ORDERED_PRIMITIVE_TYPES.length - 1) {
                    cls = ORDERED_PRIMITIVE_TYPES[i + 1];
                }
            }
        }
        return cost;
    }
}
