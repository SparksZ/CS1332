import java.util.Iterator;

/**
 * Created by Zack on 5/15/2015.
 */
public class Driver {
    public static void main(String[] args) {
        LazyDeleteList<String> list2test = new LazyDeleteLinkedList<>();
        list2test.add("A");
        list2test.remove("A");
        int result = list2test.compress();

        list2test.add("A");

        System.out.println(result);
//        for (int i=0; i < 20 ; i++){
//            list2test.add("A" + i);
//        }
//
//        list2test.remove("A19");
//        list2test.remove("A16");
//        list2test.remove("A0");
//
//        int result = list2test.compress();
//        System.out.println("Should be 3," + result);
//        System.out.println("Should be 0, " + list2test.deletedNodeCount());
//
//        list2test.add("C");
//        list2test.add("E");
//
//        System.out.println("Should be true, " + list2test.contains("C"));
//        System.out.println("Should be true, " + list2test.contains("E"));
    }
}
