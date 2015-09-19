import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Zack on 5/30/2015.
 */
public class Driver {

    public static void main(String[] args) {
        Integer[] addThese = {24,34,14,20,27,45,18};
        BST<Integer> tree = new BST<>();


        for (Integer i : addThese) {
            tree.add(i);
        }

        //System.out.println("Floor of 21 should be 20: " + tree.floor(21));
        System.out.println("Floor of 14 should be null: " + tree.floor(14));
        System.out.println("Floor of 17 should be 14: " + tree.floor(17));
        System.out.println("Floor of 45 should be 34: " + tree.floor(45));
        System.out.println("Floor of 27 should be 24: " + tree.floor(27));
    }

    public static BST<String> addMany() {
        String[] noRotateTraversals = {"M", "H", "S", "D", "K", "N", "T", "A", "L", "U"};

        BST<String> tree = new BST<>();

        for (String noRotateTraversal : noRotateTraversals) {
            System.out.println("Adding: " + noRotateTraversal + "; working: " + tree.add(noRotateTraversal));
        }
        return tree;
    }
}
