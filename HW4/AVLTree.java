import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Implementation of an AVL Binary Search Tree
 * @author Zack Sparks
 * @version 1.0
 */
public class AVLTree<E extends Comparable<? super E>> implements BSTree<E>, Iterable<E> {
    AVLNode<E> root;
    int nodeCount;

    /**
     * Constructs a new empty AVL tree
     */
    public AVLTree() {
        root = null;
        nodeCount = 0;
    }

    /**
     * Adds an item to the tree
     * @param item the item to add
     * @return Whether or not the item was added to the tree
     */
    public boolean add(E item) {
        if (item == null) {
            return false;
        }
        int oldSize = nodeCount;
        root = addMe(item, root);
        return oldSize < nodeCount;
    }

    /**
     * Helper method for add with calls to rotations
     * @param dta the item to be added to the tree
     * @param node the root of the subtree that the item is be added to
     * @return Whether or not the item was added to the tree or not
     */
    private AVLNode<E> addMe(E dta, AVLNode<E> node) {
        if (node == null) {
            nodeCount++;
            AVLNode<E> newNode = new AVLNode<>(dta);
            newNode.height = updateHeight(newNode);
            return newNode;
        } else {
            int comp = dta.compareTo(node.data);

            if (comp > 0) { //dta > node.data
                node.right = addMe(dta, node.right);
            } else if (comp < 0) { //dta < node.data
                node.left = addMe(dta, node.left);
            }
        }

        return rebalance(node);
    }

    /**
     * @return The maximum item in the tree
     */
    public E max() {
        if (root == null) {
            return null;
        }

        AVLNode<E> current = root;

        while (current.right != null) {
            current = current.right;
        }

        return current.data;
    }

    /**
     * @return the minimum item in the tree
     */
    public E min() {
        if (root == null) {
            return null;
        }

        AVLNode<E> current = root;

        while (current.left != null) {
            current = current.left;
        }

        return current.data;
    }

    /**
     * @return Whether the tree is empty or not
     */
    public boolean isEmpty() {
        return nodeCount == 0;
    }

    /**
     * @return a list of the items in the tree in PreOrder
     */
    public ArrayList<E> getPreOrder() {
        ArrayList<E> result = new ArrayList<>(nodeCount);

        if (this.size() != 0) {
            preOrder(root, result);
        }

        return result;
    }

    /**
     * Helper method for getPreOrder
     * @param node the root of the subtree from which items are to be put into pre order
     * @param accum the list which items will be added to
     */
    private void preOrder(AVLNode<E> node, ArrayList<E> accum) {
        if (node != null) {
            accum.add(node.data);
            preOrder(node.left, accum);
            preOrder(node.right, accum);
        }
    }

    /**
     * @return a list of the items in the tree in PostOrder
     */
    public ArrayList<E> getPostOrder() {
        ArrayList<E> result = new ArrayList<>(nodeCount);

        if (this.size() != 0) {
            postOrder(root, result);
        }

        return result;
    }

    /**
     * Helper method for getPostOrder
     * @param node the root of the subtree from which items are to be put into post order
     * @param accum the list which items will be added to
     */
    private void postOrder(AVLNode<E> node, ArrayList<E> accum) {
        if (node != null) {
            postOrder(node.left, accum);
            postOrder(node.right, accum);
            accum.add(node.data);
        }
    }

    /**
     * @return a list of the items in the tree in order from minimum to maximum
     */
    public ArrayList<E> getInOrder() {
        ArrayList<E> result = new ArrayList<>(nodeCount);

        if (this.size() != 0) {
            inOrder(root, result);
        }
        return result;
    }

    /**
     * Helper method for getInOrder
     * @param node the root of the subtree for which the items are to be accumulated
     * @param accum the list that the items will be added to
     */
    private void inOrder(AVLNode<E> node, ArrayList<E> accum) {
        if (node != null) {
            inOrder(node.left, accum);
            accum.add(node.data);
            inOrder(node.right, accum);
        }
    }

    /**
     * @return a list of the items in the tree in Level Order
     */
    public ArrayList<E> getLevelOrder() {
        ArrayList<E> result = new ArrayList<>(nodeCount);

        if (this.size() != 0) {
            levelOrder(root, result);
        }
        return result;
    }

    /**
     * Helper method for getLevelOrder
     * @param node the root of the subtree for which the items are to be accumulated
     * @param accum the list that the items will be added to
     */
    private void levelOrder(AVLNode<E> node, ArrayList<E> accum) {
        ArrayDeque<AVLNode<E>> q = new ArrayDeque<>();

        q.addLast(node);
        while (!q.isEmpty()) {
            AVLNode<E> temp = q.poll();
            accum.add(temp.data);
            if (temp.left != null) {
                q.addLast(temp.left);
            }
            if (temp.right != null) {
                q.addLast(temp.right);
            }
        }
    }

    /**
     * @return the size of the tree
     */
    public int size() {
        return nodeCount;
    }

    /**
     * Checks if the tree contains the specified item
     * @param dta the item to look for in the tree
     * @return Whether or not the tree contains the item specified
     */
    public boolean contains(E dta) {
        if (this.size() == 0) {
            return false;
        }

        AVLNode<E> node = root;

        while (true) {
            int comp = dta.compareTo(node.data);

            if (comp == 0) {
                return true;
            } else if (comp > 0) {
                if (node.right == null) {
                    return false;
                } else {
                    node = node.right;
                }
            } else if (comp < 0) {
                if (node.left == null) {
                    return false;
                } else {
                    node = node.left;
                }
            }
        }
    }

    /**
     * Clears the tree
     */
    public void clear() {
        root = null;
        nodeCount = 0;
    }

    /**
     * @return an in order iterator of the tree
     */
    public Iterator<E> iterator() {
        return new MyBSTIterator(this);
    }

    /**
     * Removes the specified item from the tree
     * @param dta the item to be removed from the tree
     * @return Whether or not the item was removed from the tree
     */
    public boolean remove(E dta) {
        if (dta == null || root == null) {
            return false;
        }
        int beginSize = this.size();
        root = remove(root, dta);
        return beginSize != this.size();
    }

    /**
     * Helper function for remove method
     * @param node Specifies the root of the subtree to be searched/have the item removed from
     * @param dta The item to be removed from the subtree
     * @return the root of the new subtree sans the item specified
     */
    private AVLNode<E> remove(AVLNode<E> node, E dta) {
        if (node == null) {
            return null;
        }

        int comp = dta.compareTo(node.data);

        if (comp < 0) {
            node.left = remove(node.left, dta);
        } else if (comp > 0) {
            node.right = remove(node.right, dta);
        } else {
            if (node.right == null) {
                nodeCount--;
                return node.left;
            }

            if (node.left == null) {
                nodeCount--;
                return node.right;
            }

            AVLNode<E> temp = node;
            node = min(temp.right);
            node.right = removeMin(temp.right);
            node.left = temp.left;
        }
        return node;
        //return rebalance(node);
    }

    /**
     * Helper method that removes the minimum item from the subtree rooted at the node specified
     * @param node the root of the subtree from which the minimum item is to be removed from
     * @return The root of the subtree specified with the item removed
     */
    private AVLNode<E> removeMin(AVLNode<E> node) {
        if (node.left == null) {
            nodeCount--;
            return node.right;
        }

        node.left = removeMin(node.left);
        return rebalance(node);
    }

    /**
     * Returns the minimum node of the subtree specified by @param node
     * @param node the root of the subtree from which the minimum node is to be returned
     * @return the node which contains the minimum item from the subtree specified
     */
    private AVLNode<E> min(AVLNode<E> node) {
        if (node.left == null) {
            return node;
        } else {
            return min(node.left);
        }
    }

    /**
     * Rotates the subtree of the specified node right
     * @param node root of subtree to be rotated
     * @return new node of rotated subtree
     */
    private AVLNode<E> rotateRight(AVLNode<E> node) {
        AVLNode<E> temp = node;
        node = temp.left;
        temp.left = node.right;
        node.right = temp;

        node.right.height = updateHeight(node.right);
        node.height = updateHeight(node);
        return node;
    }

    /**
     * Rotates the subtree of the specified node left
     * @param node root of subtree to be rotated
     * @return new node of rotated subtree
     */
    private AVLNode<E> rotateLeft(AVLNode<E> node) {
        AVLNode<E> temp = node;
        node = temp.right;
        temp.right = node.left;
        node.left = temp;

        node.left.height = updateHeight(node.left);
        node.height = updateHeight(node);
        return node;
    }

    /**
     * Rebalances the current node if necessary
     * @param node the node up for consideration of a rebalancing
     * @return balanced subtree with (potentially) new root
     */
    private AVLNode<E> rebalance(AVLNode<E> node) {
        int balance = getBalance(node);

        if (balance > 1) {
            if (getHeight(node.left.left) >= getHeight(node.left.right)) {
                return rotateRight(node);
            } else {
                node.left = rotateLeft(node.left);
                return rotateRight(node);
            }
        }

        if (balance < -1) {
            if (getHeight(node.right.right) >= getHeight(node.right.left)) {
                return rotateLeft(node);
            } else {
                node.right = rotateRight(node.right);
                return rotateLeft(node);
            }
        }

        node.height = updateHeight(node);
        return node;
    }

    /**
     * Returns the height of the specified node
     * @param node the specified node to get the height of
     * @return the height of the node specified
     */
    private int updateHeight(AVLNode node) {
        if (node == null) {
            return -1;
        } else {
            return Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        }
    }

    /**
     * Simply returns the specified node's current height
     * @param node specified to have the height measured
     * @return the height of the specified node
     */
    private int getHeight(AVLNode node) {
        if (node == null) {
            return -1;
        } else {
            return node.height;
        }
    }

    public E floor(E value) {
        if (value == null) {
            return null;
        }

        int comp = value.compareTo(min(root).data);

        if (comp <= 0) {
            return null;
        }

        return floor(root, value).data;
    }

    private AVLNode<E> floor(AVLNode<E> node, E value) {
        if (node == null || value == null) {
            return null;
        }

        int comp = value.compareTo(node.data);

        if (comp <= 0) {
            if (node.left != null) {
                return floor(node.left, value);
            } else {
                return null;
            }
        } else {
            if (node.right == null) {
                return node;
            } else {
                int cmp = value.compareTo(min(node.right).data);

                if (cmp > 0) {
                    return floor(node.right, value);
                } else {
                    return node;
                }
            }
        }
    }

    /**
     * Returns the balance factor of the specified node
     * @param node node to have balanced factor returned for
     * @return the balance factor of the node specified
     */
    private int getBalance(AVLNode node) {
        return getHeight(node.left) - getHeight(node.right);
    }

    /**
     * The Iterator class for AVL
     * @param <T> Typed Data structure (must be comparable)
     */
    class MyBSTIterator<T extends Comparable<? super T>> implements Iterator {
        ArrayList<T> inOrder;
        int cursor;

        /**
         * Constructs a new MyBSTIterator
         * @param aVL the AVL to be iterator over
         */
        public MyBSTIterator(AVLTree<T> aVL) {
            inOrder = aVL.getInOrder();
            cursor = 0;
        }

        /**
         * @return the next item from the AVL
         */
        public T next() {
            if (!hasNext()) {
                throw new IndexOutOfBoundsException();
            }

            return inOrder.get(cursor++);
        }

        /**
         * @return whether or not there is another item to be returned
         */
        public boolean hasNext() {
            return inOrder != null && cursor < inOrder.size();
        }

        /**
         * Remove method is not supported and will throw an exception
         */
        public void remove() {
            throw new UnsupportedOperationException("Remove not supported");
        }
    }

    /**
     * This is the node to be used in the AVL
     * @param <T> Typed data structure
     */
    class AVLNode<T extends Comparable<? super T>> {
        AVLNode<T> left;
        AVLNode<T> right;
        T data;
        int height;

        public AVLNode(T dta) {
            this(dta, null, null);
        }

        public AVLNode(T dta, AVLNode<T> l, AVLNode<T> r) {
            left = l;
            right = r;
            data = dta;
            height = 0;
        }
    }
}