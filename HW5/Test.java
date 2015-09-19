/**
 * Created by Zack on 6/25/2015.
 */
public class Test {
    public static void main(String[] args) {
        int[] testArray = {1, 2, 3, 4, 5, 6, 7};

        int[] reference = testArray;

        reference[2] = 10;

        System.out.println(reference);
        System.out.println(testArray);
    }
}
