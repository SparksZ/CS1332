import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * @author Zack Sparks
 * @version 1.0
 * */
public class LazyDeleteLinkedList<T> implements LazyDeleteList<T>, Iterable<T> {
    private Node<T> head;
    private Node<T> tail;
    private int count, deletedCount;
    private Stack<Node<T>> deleted;

    public LazyDeleteLinkedList() {
        head = null;
        tail = null;
        count = 0;
        deletedCount = 0;
        deleted = new Stack<>();
    }

    public int deletedNodeCount() {
        return deletedCount;
    }

    public int compress() {
        int nodesDeleted = 0;

        while (!deleted.empty()) {
            Node<T> tBD = deleted.pop();

            if (deletedCount == 1 && count == 0) {
                head = null;
                tail = null;
            } else if (tBD == tail) {
                tail = tBD.left;
                tail.left = null;
            } else if (tBD == head) {
                head = tBD.right;
                head.left = null;
            } else {
                tBD.left.right = tBD.right;
            }
            nodesDeleted++;
            deletedCount--;
        }

        return nodesDeleted;
    }

    public void clear() {
        head = null;
        tail = null;
        count = 0;
        deletedCount = 0;
        deleted = new Stack<>();
    }

    public void add(T data) {
        if (deleted.empty()) {
            if (head == null) {
                head = new Node<T>(data);
                tail = head;
            } else {
                tail.right = new Node<>(data, tail, null);
                tail = tail.right;
            }
        } else {
            Node<T> insert = deleted.pop();
            insert.data = data;
            insert.isDeleted = false;

            deletedCount--;
        }

        count++;
    }

    public boolean remove(T item) {
        if(size() == 0) {
            return false;
        }

        Node<T> cursor = head;

        while (cursor != null) {
            if (cursor.data.equals(item) && !cursor.isDeleted) {
                cursor.isDeleted = true;
                deleted.push(cursor);
                deletedCount++;
                count--;
                return true;
            }

            cursor = cursor.right;
        }

        return false;
    }

    public boolean contains(T data) {
        Node<T> cursor = head;

        while (cursor != null) {
            if (cursor.data.equals(data) && !cursor.isDeleted) {
                return true;
            }

            cursor = cursor.right;
        }

        return false;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public int size() {
        return count;
    }

    public Iterator<T> iterator() {
        return new LazyListIterator<T>(head);
    }

    private class LazyListIterator<T> implements Iterator {
        private Node<T> cursor;

        public LazyListIterator(Node head) {
            compress();
            cursor = head;
        }

        public boolean hasNext() {
            Node<T> temp = cursor;

            return !(temp == null);
        }

        public T next() {
            if (!hasNext()) {throw new NoSuchElementException();}

            Node<T> temp = cursor;

            cursor = cursor.right;
            return temp.data;
        }

        public void remove() {
            throw new UnsupportedOperationException("remove isn't supported");
        }
    }

    private class Node<T> {
        public T data;
        public Node<T> right, left;
        public boolean isDeleted;

        public Node(T data) {
            this(data, null, null);
        }

        public Node(T data, Node left, Node right) {
            this.data = data;
            this.right = right;
            this.left = left;
            isDeleted = false;
        }
    }
}
