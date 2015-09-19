import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Zack on 6/25/2015.
 */
public class Driver {
    public static CuckooMap<MyInteger, MyString> table;
    public static List<MyInteger> intList = new ArrayList<MyInteger>();
    public static List<MyString> stringList = new ArrayList<MyString>();


    public static void main(String[] args) {

//        for (int i = 0; i < 10000 ; ++i) {
//            intList.add(new MyInteger(i));
//            stringList.add(new MyString("A" + i));
//        }

        table = new CuckooMap<>();

        insertSomeElements(2500000);

//        System.out.println(intList.get(10));
//        System.out.println(stringList.get(10));
//
//        System.out.println(table.get(intList.get(10)));
//
//        System.out.println(table.put(intList.get(10), new MyString("hello")));

        System.out.println("Checking Now, size: " + table.size());
        System.out.println(table.get(new MyInteger(65)));
    }

    /***********************
     * A utility function to put some elements into the map
     *
     * WARNING:  Precondition is that count <= 100.   There are only
     * 100 elements in list so anything more than 100 will generate.  If you
     * want to test sizes > 100, then go to constructor and increase loop limit.
     *
     * @param count the number of items (0 - 100) to insert into the map
     */

    private static void insertSomeElements(int count) {
        Random r = new Random();


        for (int i = 0; i < count ; ++i) {
            table.put(new MyInteger(r.nextInt(100000)), new MyString("A" + r.nextInt()));
        }

        table.put(new MyInteger(r.nextInt(100000)), new MyString("A" + r.nextInt()));
    }
}
