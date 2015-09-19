/*
 * GrowableArray.java
 *
 * Version 1.0
 * Copyright 2011 BobSoft Inc
 */

/**
 * A custom array class implemented in class
 * 
 * @author Robert
 * @version 1.0
 *
 */
public interface GrowableArray<T> {
       /**
        * Checks if array is empty
        * @return true if array empty (has no elements) false otherwise
        */
       boolean isEmpty();
       
       /**
        * 
        * @return the total capacity of the current array without a regrow
        */
       int capacity();
       
       /**
        * 
        * @return the number of elements in the array
        */
       int size();
       
       /**
        * Return the data at the given index
        * 
        * @param index  the index to look for the data at
        * @return the data at that index
        * @throws IllegalArgumentException if the index < 0 or exceeds 
        *                the index of the last valid item
        */
       T get(int index) throws IllegalArgumentException;
       
       /**
        * 
        * @param indext the index to change the data at
        * @param data the new data to put at that index
        * @throws IllegalArgumentException if the index < 0 or exceeds 
        *                the index of the last valid item
        */
       void set(int index, T data) throws IllegalArgumentException;
       
       /**
        * Adds the data at the end of the array - could cause a regrow if the
        * array is full
        * @param data the data to add
        */
       void add(T data);
       
       /**
        * Remove the last element in the array.  If the array is empty, return null
        * @return the element removed OR null if nothing in array
        */
       T remove();
}
