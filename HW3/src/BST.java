import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Implementation of a Binary Search Tree
 * @author Zack Sparks
 * @version 1.0
 */
public class BST<E extends Comparable<? super E>> implements BinaryTree<E>, Iterable<E> {
    BNode<E> root;
    int nodeCount;

    /**
     * Constructs a new empty BST
     */
    public BST() {
        root = null;
        nodeCount = 0;
    }

    /**
     * Adds an item to the tree
     * @param item the item to add
     * @return Whether or not the item was added to the tree
     */
    public boolean add(E item) {
        return item != null && addMe(item, root);
    }

    /**
     * Helper method for add
     * @param dta the item to be added to the tree
     * @param node the root of the subtree that the item is be added to
     * @return Whether or not the item was added to the tree or not
     */
    private boolean addMe(E dta, BNode<E> node) {
        if (nodeCount == 0) {
            root = new BNode<>(dta);
            nodeCount++;
            return true;
        } else {
            int comp = dta.compareTo(node.data);

            if (comp > 0) {
                if (node.right == null) {
                    node.right = new BNode<>(dta);
                    nodeCount++;
                    return true;
                } else {
                    return addMe(dta, node.right);
                }
            } else if (comp < 0) {
                if (node.left == null) {
                    node.left = new BNode<>(dta);
                    nodeCount++;
                    return true;
                } else {
                    return addMe(dta, node.left);
                }
            }
        }

        return false;
    }

    /**
     * @return The maximum item in the tree
     */
    public E max() {
        if (root == null) {
            return null;
        }

        BNode<E> current = root;

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

        BNode<E> current = root;

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
    private void preOrder(BNode<E> node, ArrayList<E> accum) {
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
    private void postOrder(BNode<E> node, ArrayList<E> accum) {
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
    private void inOrder(BNode<E> node, ArrayList<E> accum) {
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
    private void levelOrder(BNode<E> node, ArrayList<E> accum) {
        ArrayDeque<BNode<E>> q = new ArrayDeque<>();

        q.addLast(node);
        while (!q.isEmpty()) {
            BNode<E> temp = q.poll();
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

        BNode<E> node = root;

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
    private BNode<E> remove(BNode<E> node, E dta) {
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

            BNode<E> temp = node;
            node = min(temp.right);
            node.right = removeMin(temp.right);
            node.left = temp.left;
        }

        return node;
    }

    /**
     * Helper method that removes the minimum item from the subtree rooted at the node specified
     * @param node the root of the subtree from which the minimum item is to be removed from
     * @return The root of the subtree specified with the item removed
     */
    private BNode<E> removeMin(BNode<E> node) {
        if (node.left == null) {
            nodeCount--;
            return node.right;
        }

        node.left = removeMin(node.left);
        return node;
    }

    /**
     * Returns the minimum node of the subtree specified by @param node
     * @param node the root of the subtree from which the minimum node is to be returned
     * @return the node which contains the minimum item from the subtree specified
     */
    private BNode<E> min(BNode<E> node) {
        if (node.left == null) {
            return node;
        } else {
            return min(node.left);
        }
    }

    /**
     * The Iterator class for BST
     * @param <T> Typed Data structure (must be comparable)
     */
    class MyBSTIterator<T extends Comparable<? super T>> implements Iterator {
        ArrayList<T> inOrder;
        int cursor;

        /**
         * Constructs a new MyBSTIterator
         * @param bST the BST to be iterator over
         */
        public MyBSTIterator(BST<T> bST) {
            inOrder = bST.getInOrder();
            cursor = 0;
        }

        /**
         * @return the next item from the BST
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
     * This is the node to be used in the BST
     * @param <T> Typed data structure
     */
    class BNode<T extends Comparable<? super T>> {
        BNode<T> left;
        BNode<T> right;
        T data;

        public BNode(T dta) {
            this(dta, null, null);
        }

        public BNode(T dta, BNode<T> l, BNode<T> r) {
            left = l;
            right = r;
            data = dta;
        }
    }

    public E floor (E value) {
        BNode<E> curr = root;
        BNode<E> min = min(root);

        if (min.data.compareTo(value) >= 0) {
            return null;
        }

        boolean flag = true;

        while (flag) {
            BNode<E> temp1 = firstLessThan(curr, value);
            BNode<E> temp2 = min(temp1.right);

            if (temp2.data.compareTo(value) >= 0) {
                return temp1.data;
            } else {
                curr = temp1.right;
            }
        }
        return null;
    }

    private BNode<E> firstLessThan(BNode<E> node, E value) {
        if (node == null) {
            return null;
        }
        int comp = value.compareTo(node.data);

        if (comp < 0) {
            return firstLessThan(node.left, value);
        } else {
            return node;
        }
    }
}