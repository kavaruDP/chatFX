package org.example;
import java.lang.reflect.*;
import java.util.*;

public class DoingTest {

    private static String className;
    private static Class classObject;

    public DoingTest(String className) {
        this.className = className;
        try {
            classObject = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public DoingTest(Class classObject) {
        this.classObject = classObject;
    }

    public static void start() {
        List<Method> methods2Run = new ArrayList();

        List<Method> listOfMethods = Arrays.asList(classObject.getMethods());
        int i = 0;
        int j = 0;
        for (Method m : listOfMethods) {
            if (m.isAnnotationPresent(BeforeSuite.class)) {
                i++;
                if (i > 1) {
                        throw new RuntimeException("В классе более одного метода с аннотацией BeforeSuite");
                }
                methods2Run.add(m);

            }
            if (m.isAnnotationPresent(Test.class)) {
                methods2Run.add(m);
            }
            if (m.isAnnotationPresent(AfterSuite.class)) {
                j++;
                if (j > 1) {
                    throw new RuntimeException("В классе более одного метода с аннотацией AfterSuite");
                }
                methods2Run.add(m);
            }
        }
        Comparator testMethodComparator = new TestMethodComparator();
        Collections.sort(methods2Run, testMethodComparator);
        for (Method m : methods2Run) {
            try {
                m.invoke(null);
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            //System.out.println(m.getName());
        }
    }
}
