package org.example;

import java.io.IOException;
import java.util.Arrays;

public class MainApp {

    public static void main(String[] args){

        // Проверки работы методов
//        int[] arr = {0,2,3,4,5,4};
//        for (int item:splitArray(arr)) {
//            System.out.println(item);
//        }
//        int[] arr1 = { 1, 1, 1, 4, 4, 1, 4, 4 };    //-> true
//        int[] arr2 = { 1, 1, 1, 1, 1, 1 };          //-> false
//        int[] arr3 = { 4, 4, 4, 4 };                //-> false
//        int[] arr4 = { 1, 4, 4, 1, 1, 4, 3 };       //-> false
//        System.out.println(has1or4Array(arr));
//        System.out.println(has1or4Array(arr1));
//        System.out.println(has1or4Array(arr2));
//        System.out.println(has1or4Array(arr3));
//        System.out.println(has1or4Array(arr4));

        DoingTest test = new DoingTest(MyTest.class);
        //DoingTest test = new DoingTest("org.example.MyTest");
        test.start();

    }

// Метод, которому в качестве аргумента передается не пустой одномерный целочисленный массив.
// Метод должен вернуть новый массив, который получен путем вытаскивания из исходного массива элементов, идущих после последней четверки.
// Входной массив должен содержать хотя бы одну четверку, иначе в методе необходимо выбросить RuntimeException.
    public static int[] splitArray(int[] myArray){
        for(int i=myArray.length-1; i>=0; i--) {
            if (myArray[i] == 4) {
                return  Arrays.copyOfRange(myArray,i+1, myArray.length);
            }
        }
        throw new RuntimeException("Нет четверки");
    }

// Метод, который проверяет состав массива из чисел 1 и 4.
// Если в нем нет хоть одной четверки или единицы, то метод вернет false;
    public static boolean has1or4Array(int[] myArray){
        boolean flag1 = false;
        boolean flag2 = false;
        for(int item:myArray) {
            if (item != 4 && item != 1) {
                return  false;
            }
            if (item == 1) {
                flag1 = true;
            }
            if (item == 4) {
                flag2 = true;
            }
        }
        return (flag1 && flag2);
    }

}