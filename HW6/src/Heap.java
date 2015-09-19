import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * Created by Zack on 7/3/2015.
 */
public class Heap implements Container {

    private ArrayList<WorkOrder> spine;
    private Comparator<WorkOrder> comparator;

    /**
     * Constructs new Heap object
     */
    public Heap() {
        spine = new ArrayList<>();
        spine.add(null); //place holder for zero index
    }

    @Override
    public void setComparator(Comparator comp) {
        comparator = comp;
    }

    @Override
    public void add(WorkOrder wo) {
        spine.add(wo);
        int i = spine.size() - 1;

        if (spine.size() > 2) {
            while (i > 1 && comparator.compare(spine.get(i), spine.get(getParent(i))) > 0) {
                heapify(getParent(i));

                i = getParent(i);
            }
        }
    }

    @Override
    public void arrange() {
        for (int i = spine.size()/2; i > 0; i--) {
            heapify(i);
        }
    }

    @Override
    public void dumpContainer() {
        System.out.println(spine.toString());
    }

    @Override
    public boolean hasNext() {
        return spine.size() > 1;
    }

    @Override
    public WorkOrder next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        WorkOrder temp = spine.get(1);
        spine.set(1, spine.get(spine.size() - 1));
        spine.remove(spine.size() - 1);

        if (spine.size() > 2) {
            heapify(1);
        }

        return temp;
    }

    /**
     * Returns the index of the left child of the passed parent
     * @param i index of parent of left child that is wanted
     * @return the index of the left child to the passed parent
     */
    private int getLeft(int i) {
        return 2 * i;
    }

    /**
     * Returns the index of the right child of the passed parent
     * @param i index of parent of right child that is wanted
     * @return the index of the right child to the passed parent
     */
    private int getRight(int i) {
        return 2 * i + 1;
    }

    /**
     * Returns the index of the parent of the passed child's index
     * @param i the index of the child whose parent's index is needed
     * @return the index of the parent of the child's index passed
     */
    private int getParent(int i) {
        return i / 2;
    }

    /**
     * Heapifies the subtree at root i
     * @param i the root of the subtree to heapify
     */
    private void heapify(int i) {
        int left = getLeft(i);
        int right = getRight(i);
        int largest;

        if (left < spine.size() &&
                comparator.compare(spine.get(i), spine.get(left)) < 0) {
            largest = left;
        } else {
            largest = i;
        }

        if (right < spine.size() &&
                comparator.compare(spine.get(largest), spine.get(right)) < 0) {
            largest = right;
        }

        if (comparator.compare(spine.get(largest), spine.get(i)) != 0) {
            WorkOrder temp = spine.get(i);
            spine.set(i, spine.get(largest));
            spine.set(largest, temp);

            heapify(largest);
        }
    }
}
