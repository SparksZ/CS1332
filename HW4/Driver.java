/**
 * Created by Zack on 6/7/2015.
 */
public class Driver {
    public static void main(String[] args) {
        AVLTree<Integer> tree = new AVLTree<>();

        for (int i = 1; i<16; i = i + 2) {
            tree.add(i);
        }

        System.out.println(tree.getLevelOrder());
        System.out.println(tree.floor(-7));
    }
}
