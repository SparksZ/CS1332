import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * Created by Zack on 7/17/2015.
 */
public class SkipTest {
    SkipList<Integer, String> testSL;

    @Before
    public void setUp() {
        testSL = new SkipList<>();
    }

    @Test
    public void testClear() {
        addEntries(100);
        testSL.clear();
        Assert.assertTrue("Array is not empty", testSL.isEmpty());
        Assert.assertFalse("Array contains a deleted element",
                testSL.containsKey(50));
    }

    @Test
    public void testContainsKey() {
        addEntries(100);
        Assert.assertTrue("Doesn't contain key it is suppose to contain",
                testSL.containsKey(50));
        Assert.assertFalse("Doesn't return false when key is contained",
                testSL.containsKey(150));
    }

    @Test
    public void testContainsValue() {
        addEntries(100);
        Assert.assertTrue("Doesn't contain value it is suppose to contain",
                testSL.containsValue("A99"));
        Assert.assertFalse("Doesn't return null when key is contained",
                testSL.containsValue("A150"));
    }

    @Test
    public void testGet() {
        addEntries(100);
        Assert.assertTrue("Doesn't return correct value",
                testSL.get(50).equals("A50"));
        Assert.assertNull("Doesn't return null when not present",
                testSL.get(150));
    }

    @Test
    public void testIsEmptyAndRemove() {
        addEntries(1);
        testSL.remove(0);
        Assert.assertTrue("Not empty after removal",
                testSL.isEmpty());

        addEntries(100);
        testSL.remove(50);
        Assert.assertFalse("Contains key after removal",
                testSL.containsKey(50));
        Assert.assertEquals("Wrong size after removal",
                testSL.size(), 99);
    }

    @Test
    public void testPut() {
        addEntries(100);
        Assert.assertEquals("Wrong number of entries",
                testSL.size(), 100);
    }

    @Test
    public void testValueChangeAfterPut() {
        addEntries(100);
        testSL.put(9, "A150");

        Collection<String> values = testSL.values();

        Assert.assertTrue("Not All Values Changed on put",
                values.contains("A150"));
    }


    private void addEntries(int num) {
        for (int i = 0; i < num; i++) {
            testSL.put(i, "A" + i);
        }
    }
}
