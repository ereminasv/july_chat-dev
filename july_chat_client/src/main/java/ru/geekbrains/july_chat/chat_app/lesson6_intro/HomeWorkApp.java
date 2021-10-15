package ru.geekbrains.july_chat.chat_app.lesson6_intro;

import java.util.Arrays;

public class HomeWorkApp {

    public static void main(String[] args) {
        int[] arr1 = {1, 2, 4, 4, 2, 3, 4, 1, 7};
        System.out.println("Исходный массив для первого метода: " + Arrays.toString(arr1));
        System.out.println("Новый массив для первого метода: " +Arrays.toString(newArr(new int[]{1, 2, 4, 4, 2, 3, 4, 1, 7})));
        System.out.println();

        int[] arr2 = {1,1,1,4,4,1,4,4};
        System.out.println("Исходный массив для второго метода: " + Arrays.toString(arr2));
        System.out.println("Результат второго метода: " + OneFourArr(new int[] {1,1,1,4,4,1,4,4}));
        System.out.println();

        int[] arr3 = {1,1,1,1,1,1};
        System.out.println("Исходный массив для второго метода: " + Arrays.toString(arr3));
        System.out.println("Результат второго метода: " + OneFourArr(new int[] {1,1,1,1,1,1}));
        System.out.println();

        int[] arr4 = {4,4,4,4};
        System.out.println("Исходный массив для второго метода: " + Arrays.toString(arr4));
        System.out.println("Результат второго метода: " + OneFourArr(new int[] {4,4,4,4}));
        System.out.println();

        int[] arr5 = {1,4,4,1,1,4,3};
        System.out.println("Исходный массив для второго метода: " + Arrays.toString(arr5));
        System.out.println("Результат второго метода: " + OneFourArr(new int[] {1,4,4,1,1,4,3}));
        System.out.println();
    }

    public static int[] newArr(int [] arr) {
        for (int i = arr.length - 1; i >= 0; i--)
            if (arr[i] == 4) {
                return Arrays.copyOfRange(arr, i + 1, arr.length);
            }
            throw new RuntimeException("В массиве нет 4");
    }

    public static boolean OneFourArr(int [] arr) {
        boolean one = false;
        boolean four = false;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 1) {
                 one = true;
            }
            else if (arr[i] == 4) {
                 four = true;
            }
            else return false;
        }
        return one && four;
    }
}


