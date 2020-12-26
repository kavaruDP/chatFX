import org.example.MainApp;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class MassArrayTest1 {
    private int[] arr;
    private boolean flag;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new int[]{ 1, 1, 1, 1, 1, 1, 1, 1 },false},
                {new int[]{ 4, 4, 4, 4, 4, 4, 4, 4 },false},
                {new int[]{ 1, 1, 1, 4, 4, 1, 4, 3 },false},
                {new int[]{ 1, 1, 1, 4, 4, 1, 4, 1 },true}
        });
    }

    public MassArrayTest1(int[] arr, boolean flag) {
        this.arr = arr;
        this.flag = flag;
    }

    @Test
    public void test() {
        Assert.assertEquals(flag,MainApp.has1or4Array(arr));
    }
}
