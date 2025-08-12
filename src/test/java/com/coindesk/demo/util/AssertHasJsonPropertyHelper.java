package com.coindesk.demo.util;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public interface AssertHasJsonPropertyHelper {
    default void assertHasJsonProperty(Class<?> clazz, String propertyName) {
        boolean hasProperty = false;
        try {
            java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
            for (java.lang.reflect.Field field : fields) {
                com.fasterxml.jackson.annotation.JsonProperty annotation =
                        field.getAnnotation(com.fasterxml.jackson.annotation.JsonProperty.class);
                if (annotation != null && annotation.value().equals(propertyName)) {
                    hasProperty = true;
                    break;
                }
            }
        } catch (Exception e) {
            fail("Error checking JSON property: " + e.getMessage());
        }
        assertTrue(hasProperty, "Class " + clazz.getSimpleName() + " should have JSON property '" + propertyName + "'");
    }
}
