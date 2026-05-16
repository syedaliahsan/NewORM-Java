package com.sa.orm.util;

public class TypeCastUtils {
    
   public static Object convertToPrimitiveIfNeeded(Class type, Object value) {
     if (value == null && type.isPrimitive()) {
       if (type == int.class) return 0;
       if (type == long.class) return 0L;
       if (type == short.class) return (short) 0;
       if (type == byte.class) return (byte) 0;
       if (type == double.class) return 0.0;
       if (type == float.class) return 0.0f;
       if (type == boolean.class) return false;
       if (type == char.class) return '\0';
     }
     return value;
   }

   public static Object convertValue(Object value, Class<?> destType) {
     if(value == null) return null;
     if(destType == int.class || destType == Integer.class) {
       if(value instanceof Number) return ((Number)value).intValue();
     }
     if(destType == long.class || destType == Long.class) {
       if(value instanceof Number) return ((Number)value).longValue();
     }
     if(destType == double.class || destType == Double.class) {
       if(value instanceof Number) return ((Number)value).doubleValue();
     }
     if(destType == float.class || destType == Float.class) {
       if(value instanceof Number) return ((Number)value).floatValue();
     }
     if(destType == short.class || destType == Short.class) {
       if(value instanceof Number) return ((Number)value).shortValue();
     }
     if(destType == byte.class || destType == Byte.class) {
       if(value instanceof Number) return ((Number)value).byteValue();
     }
     if(destType == boolean.class || destType == Boolean.class) {
       if(value instanceof Boolean) return value;
       if(value instanceof String) return Boolean.parseBoolean((String)value);
     }
     return value;
   }

   public static boolean isBoxingMatch(Class<?> srcType, Class<?> destType) {
     if(srcType == int.class) return destType == Integer.class;
     if(srcType == long.class) return destType == Long.class;
     if(srcType == boolean.class) return destType == Boolean.class;
     if(srcType == double.class) return destType == Double.class;
     if(srcType == float.class) return destType == Float.class;
     if(srcType == short.class) return destType == Short.class;
     if(srcType == byte.class) return destType == Byte.class;
     if(srcType == char.class) return destType == Character.class;
     return false;
   }

   public static boolean isUnboxingMatch(Class<?> srcType, Class<?> destType) {
     if(srcType == Integer.class) return destType == int.class;
     if(srcType == Long.class) return destType == long.class;
     if(srcType == Boolean.class) return destType == boolean.class;
     if(srcType == Double.class) return destType == double.class;
     if(srcType == Float.class) return destType == float.class;
     if(srcType == Short.class) return destType == short.class;
     if(srcType == Byte.class) return destType == byte.class;
     if(srcType == Character.class) return destType == char.class;
     return false;
   }

}
