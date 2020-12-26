package org.example;

import java.lang.reflect.Method;
import java.util.Comparator;

public class TestMethodComparator implements Comparator<Method> {
    @Override
    public int compare(Method m1, Method m2) {
        if(m1.isAnnotationPresent(BeforeSuite.class) && m2.isAnnotationPresent(BeforeSuite.class)) {
            return 0;
        }
        if(m1.isAnnotationPresent(BeforeSuite.class) && m2.isAnnotationPresent(AfterSuite.class)) {
            return 1;
        }
        if(m1.isAnnotationPresent(BeforeSuite.class) && m2.isAnnotationPresent(Test.class)) {
            return 1;
        }
        if(m1.isAnnotationPresent(Test.class) && m2.isAnnotationPresent(BeforeSuite.class)) {
            return -1;
        }
        if(m1.isAnnotationPresent(AfterSuite.class) && m2.isAnnotationPresent(AfterSuite.class)) {
            return 0;
        }
        if(m1.isAnnotationPresent(AfterSuite.class) && m2.isAnnotationPresent(BeforeSuite.class)) {
            return -1;
        }
        if(m1.isAnnotationPresent(AfterSuite.class) && m2.isAnnotationPresent(Test.class)) {
            return -1;
        }
        if(m1.isAnnotationPresent(Test.class) && m2.isAnnotationPresent(AfterSuite.class)) {
            return 1;
        }
        if(m1.isAnnotationPresent(Test.class) && m2.isAnnotationPresent(Test.class)) {
            return m1.getAnnotation(Test.class).prioritet() - m2.getAnnotation(Test.class).prioritet();
        }
        return 0;

    }
}
