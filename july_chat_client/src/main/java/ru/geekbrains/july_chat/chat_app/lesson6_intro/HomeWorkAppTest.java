package ru.geekbrains.july_chat.chat_app.lesson6_intro;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;

class HomeWorkAppTest {

    @Disabled
    @org.junit.jupiter.api.Test
    void newArr() {
        Assertions.assertArrayEquals(new int[]{1, 7}, HomeWorkApp.newArr(new int[]{1, 2, 4, 4, 2, 3, 4, 1, 7}));
    }

//    @CsvSourse ({
//        "new int[]{1, 2, 4, 4, 2, 3, 4, 1, 7}, new int[]{1, 7}",
//        "new int[]{5,7,8,4,8,2}, new int[]{5,7,8,4,8,2}",
//        "new int[]{4},new int[]{}"
//    })
//    @ParametrizedTest
//    void newArr(int [] inArr, int [] outArr) {
//        Assertions.assertArrayEquals(outArr, HomeWorkApp.newArr(inArr));
//    }

    @org.junit.jupiter.api.Test
    void testException() {
        Assertions.assertThrows(RuntimeException.class, ()->HomeWorkApp.newArr(new int[]{8,5,3,2,7}));
    }

    @org.junit.jupiter.api.Test
    void oneFourArr() {
        Assertions.assertTrue(HomeWorkApp.OneFourArr(new int[] {1,1,1,4,4,1,4,4}));
    }

}