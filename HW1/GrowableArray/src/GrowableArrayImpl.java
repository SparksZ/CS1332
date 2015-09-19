/*
 * GrowableArrayImpl.java
 *
 * Version 1.0
 * Copyright 2011 BobSoft Inc
 */

/**
 * @author Robert
 * @version 1.0
 *
 */
public class GrowableArrayImpl<T> implements GrowableArray<T> {
    /** The default size of the initial array if none specified */
    private static final int DEFAULT_CAPACITY = 10;
    
    /** For the backing store, we use a primitive array */
    private T[] store;
    
    /** the next available valid slot for data */
    private int index;
    
    /**
     * 
     * Makes a new GrowableArrayImpl of default capacity
     */
    public GrowableArrayImpl() {
        /*
         * If no size specified, use the default capacity
         */
        this(DEFAULT_CAPACITY);
    }
    
    /**
     * 
     * Makes a new GrowableArrayImpl of the requested capacity
     * @param capacity the initial capacity to use
     */
    @SuppressWarnings("unchecked")
    public GrowableArrayImpl(int capacity) {
        store = (T[]) new Object[capacity];
        index = 0;
    }

    /* (non-Javadoc)
     * @see GrowableArray#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return index == 0;
    }

    /* (non-Javadoc)
     * @see GrowableArray#capacity()
     */
    @Override
    public int capacity() {
        // TODO Auto-generated method stub
        return store.length;
    }

    /* (non-Javadoc)
     * @see GrowableArray#size()
     */
    @Override
    public int size() {
        // TODO Auto-generated method stub
        return index;
    }

    /* (non-Javadoc)
     * @see GrowableArray#get(int)
     */
    @Override
    public T get(int slot) throws IllegalArgumentException {
        if (slot < 0 ) throw new IllegalArgumentException();
        if (slot >= size()) throw new IllegalArgumentException();
        
        return store[slot];
    }

    /* (non-Javadoc)
     * @see GrowableArray#set(int, java.lang.Object)
     */
    @Override
    public void set(int slot, T data) throws IllegalArgumentException {
        if (slot < 0 ) throw new IllegalArgumentException();
        if (slot >= size()) throw new IllegalArgumentException();
        
        store[slot] = data;
    }

    /* (non-Javadoc)
     * @see GrowableArray#add(java.lang.Object)
     */
    @Override
    public void add(T data) {
        
        if (index >= store.length) regrow();
        store[index++] = data;

    }
    
    @SuppressWarnings("unchecked")
    private void regrow() {
        T[] old = store;
        store = (T[]) new Object[old.length * 2];
        for (int i = 0; i < old.length ; i++) {
            store[i] = old[i];
        }
    }

    /* (non-Javadoc)
     * @see GrowableArray#remove()
     */
    @Override
    public T remove() {
        if (isEmpty()) return null;
        T data = store[index - 1];
        index--;
        return data;
    }

}
