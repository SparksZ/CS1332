import java.util.Map;

/**
 * Created by Zack on 7/25/2015.
 */
public class Driver {

    public static void main(String[] args) {
        HuffmanEncoder hE = new HuffmanEncoder();
        String coded = hE.encode("The best laid plans of mice and men often go awry!");
        System.out.println(coded);
        System.out.println(hE.decode(hE.getCipher(), coded));
    }
}
