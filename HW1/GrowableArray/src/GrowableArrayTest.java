
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/*
 * GrowableArrayTest.java
 *
 * Version 1.0
 * Copyright 2011 BobSoft Inc
 */

/**
 * @author Robert
 * @version 1.0
 *
 */
public class GrowableArrayTest {
    GrowableArray<String> array;
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        array = new GrowableArrayImpl<String>();
    }
    
    @Test
    public void testCapacity() {
        assertEquals(10, array.capacity());   
        array = new GrowableArrayImpl<String>(50);
        assertEquals(50, array.capacity());
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(array.isEmpty());
        array.add("A");
        assertFalse(array.isEmpty());
        array.remove();
        assertTrue(array.isEmpty());
    }
    
    @Test
    public void testSize() {
        assertEquals(0, array.size());
        for (int i = 0; i < 6; i++) {
            array.add("A" + i);
        }
        assertEquals(6, array.size());
        array.remove();
        assertEquals(5, array.size());
    }
    
    @Test
    public void testAdd() {
        array.add("A");
        assertEquals(1, array.size());
        String s = array.remove();
        assertEquals("A", s);
        for (int i = 0; i < 6; i++) {
            array.add("A" + i);
        }
        assertEquals("A0", array.get(0));
        assertEquals("A3", array.get(3));
    }
    
    @Test
    public void testRemove() {
        assertNull(array.remove());
        array.add("A");
        String s = array.remove();
        assertEquals("A", s);
        for (int i = 0; i < 6; i++) {
            array.add("A" + i);
        }
        
        for (int i = 5; i >=0 ; i--) {
            String str = array.remove();
            assertEquals("A" + i , str);
        }
    }
    
    @Test
    public void testGet() {
        try {
            array.get(5);
            assertFalse(true);
        } catch (IllegalArgumentException ae) {
            assertTrue(true);
        }
        
        try { 
            array.get(-1);
            assertFalse(true);
        } catch (IllegalArgumentException ae) {
            assertTrue(true);
        }
        
        for (int i = 0; i < 6; i++) {
            array.add("A" + i);
        }
        
        assertEquals("A0", array.get(0));
        assertEquals("A5", array.get(5));
        assertEquals("A3", array.get(3));
        
    }
    
    @Test
    public void testSet() {
        try {
            array.set(-1, "A");
            assertFalse(true);
        } catch (IllegalArgumentException ie) {
            assertTrue(true);
        }
        
        for (int i = 0; i < 6; i++) {
            array.add("A" + i);
        }
        
        assertEquals("A3", array.get(3));
        array.set(3, "B");
        assertEquals("B", array.get(3));
        assertEquals("A5", array.get(5));
        array.set(5, "C");
        assertEquals("C", array.get(5));
        
        try {
            array.set(6,"B");
            assertFalse(true);
        } catch (IllegalArgumentException ie) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testRegrow() {
        for (int i = 0; i < 23; i++) {
            array.add("A" + i);
        }
        
        assertEquals("A22", array.get(22));
        assertEquals(40, array.capacity());
        assertEquals(23, array.size());
    }

}
