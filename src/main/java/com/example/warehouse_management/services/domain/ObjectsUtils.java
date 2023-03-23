package com.example.warehouse_management.services.domain;

import java.util.Date;
import java.util.Set;

public class ObjectsUtils {
    public static boolean equal(String obj1, String obj2) {
        if (obj1 == null || obj2 == null)
            return obj1 != obj2;

        return obj2 != null && obj1 != null ? obj1.equals(obj2) : true;
    }
    public static boolean equalBool(Boolean obj1, Boolean obj2) {
        if (obj1 == null || obj2 == null)
            return obj1 != obj2;
        return obj2 != null && obj1 != null ? obj1.equals(obj2) : true;
    }
    public static boolean equalDouble(Double obj1, Double obj2) {
        if (obj1 == null || obj2 == null)
            return obj1 != obj2;
        return obj2 != null && obj1 != null ? obj1.equals(obj2) : true;
    }
    public static boolean equalInt(Integer obj1, Integer obj2) {
        if (obj1 == null || obj2 == null)
            return obj1 != obj2;
        return obj2 != null && obj1 != null ? obj1.equals(obj2) : true;
    }
    public static boolean equalDate(Date obj1, Date obj2) {
        if (obj1 == null || obj2 == null)
            return obj1 != obj2;
        return obj2 != null && obj1 != null ? obj1.equals(obj2) : true;
    }
    public static boolean equalSet(Set<?> obj1, Set<?> obj2){
        if (obj1 == null || obj2 == null)
            return obj1 != obj2;
        return obj2 != null && obj1 != null ? obj1.equals(obj2) : true;
    }
}

