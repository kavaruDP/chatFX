package org.example;

public class MyTest {
    @Test(prioritet = 7)
    public static void test7(){
        System.out.println("test7");
    }
    @BeforeSuite
    public static void testBefore(){
        System.out.println("testBefore");
    }

    @Test(prioritet = 5)
    public static void test1(){
        System.out.println("test5");
    }

    @Test(prioritet = 2)
    public static void test2(){
        System.out.println("test2");
    }

    @Test(prioritet = 4)
    public static void test4(){
        System.out.println("test4");
    }

    @Test(prioritet = 3)
    public static void test3(){
        System.out.println("test3");
    }

    @AfterSuite
    public static void testAfter(){
        System.out.println("testAfter");
    }
}
