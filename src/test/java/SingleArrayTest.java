import org.example.MainApp;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class SingleArrayTest {
    @Test
    public void test1() {
        int[] arr1 = { 1, 1, 1, 4, 4, 1, 4, 4 };
        int[] arr2 = {};
        Assert.assertArrayEquals(arr2, MainApp.splitArray(arr1));
    }
    @Test
    public void test2() {
        int[] arr1 = { 1, 1, 1, 4, 4, 1, 4, 3 };
        int[] arr2 = {3};
        Assert.assertArrayEquals(arr2, MainApp.splitArray(arr1));
    }
    @Test(expected = RuntimeException.class)
    public void test3() {
        int[] arr = { 1, 1, 1, 3, 3, 1, 2, 2 };
        MainApp.splitArray(arr);
    }
    @Test
    public void test3a() {
        int[] arr = { 1, 1, 1, 3, 3, 1, 2, 2 };
        Assertions.assertThrows(RuntimeException.class,() -> {
            MainApp.splitArray(arr);
        });
    }

    @Test
    public void test4() {
        int[] arr = {};
        if (arr.length == 0) {
            Assert.fail();
        }
    }
    @Test
    public void test5() {
        int[] arr = { 1, 1, 1, 4, 4, 1, 4, 3 };
        Assert.assertFalse(MainApp.has1or4Array(arr));
    }
    @Test
    public void test6() {
        int[] arr = { 1, 1, 1, 4, 4, 1, 4, 1 };
        Assert.assertTrue(MainApp.has1or4Array(arr));
    }

}
