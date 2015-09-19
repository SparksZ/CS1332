import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by Zack on 7/3/2015.
 */
public class SortedList implements Container {
    private ArrayList<WorkOrder> spine;
    private Comparator<WorkOrder> comparator;

    /**
     * Constructs new SortedList
     */
    public SortedList() {
        spine = new ArrayList<>();
    }

    @Override
    public void setComparator(Comparator comp) {
        comparator = comp;
    }

    @Override
    public void add(WorkOrder wo) {
        spine.add(wo);
    }

    @Override
    public void arrange() {
        if (spine.size() > 10) {
            quickSort(0, spine.size());
        } else {
            spine = insertionSort(spine);
        }
    }

    @Override
    public void dumpContainer() {

    }

    @Override
    public boolean hasNext() {
        return spine.size() > 0;
    }

    @Override
    public WorkOrder next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        return spine.remove(0);
    }

    /**
     * Implementation of quick sort
     * @param begin the beginning index of the parition to be sorted
     * @param length the length of the partition to be sorted
     */
    private void quickSort(int begin, int length) {
        if (length <= 1) {
            return;
        }

        final int end = begin + length - 1;

        IndexWo pivotEntry = getMedianPivot(begin, length);
        WorkOrder pivot = pivotEntry.getValue();
        int pivotPos = pivotEntry.getKey();

        swapOrder(pivotPos, end);

        int p = begin;
        for (int i = begin; i != end; i++) {
            if (comparator.compare(spine.get(i), pivot) <= 0) {
                swapOrder(i, p++);
            }
        }

        swapOrder(p, end);

        quickSort(begin, p - begin);
        quickSort(p + 1, end - p);
    }

    /**
     * Insertion Sort Implementation
     * @param list List to be sorted
     * @return the list passed, but sorted
     */
    private ArrayList<WorkOrder> insertionSort(ArrayList<WorkOrder> list) {
        for (int i = 1; i < list.size(); i++) {
            WorkOrder temp = list.get(i);

            int j;
            for (j = i - 1; j >= 0 && comparator.compare(temp, list.get(j)) < 0;
                 j--) {
                list.set(j + 1, list.get(j));
            }
            list.set(j + 1, temp);
        }

        return list;
    }

    /**
     * @param begin the beginning of the sub list of spine to get the median for
     * @param length the length of the sub list
     * @return a Map.Entry of the median WorkOrder with index and object
     */
    private IndexWo getMedianPivot(int begin, int length) {
        ArrayList<WorkOrder> getMedian = new ArrayList<>();
        int pivot;

        getMedian.add(spine.get(begin));
        getMedian.add(spine.get(begin + length - 1));
        getMedian.add(spine.get(begin + (length / 2)));

        getMedian = insertionSort(getMedian);

        if (getMedian.get(1) == spine.get(begin)) {
            pivot = begin;
        } else if (getMedian.get(1) == spine.get(begin + length - 1)) {
            pivot = begin + length - 1;
        } else {
            pivot = begin + (length / 2);
        }

        return new IndexWo(pivot, getMedian.get(1));
    }

    /**
     * Swaps two indices in the spine
     * @param index1 the first index to be swapped
     * @param index2 the second index to be swapped with the first
     */
    private void swapOrder(int index1, int index2) {
        WorkOrder temp = spine.get(index1);
        spine.set(index1, spine.get(index2));
        spine.set(index2, temp);
    }

    /**
     * Needed both the actual work order and its index. This class allows
     * for the median getter to return both.
     */
    private class IndexWo implements Map.Entry<Integer, WorkOrder> {

        Integer k;
        WorkOrder v;

        public IndexWo(Integer k, WorkOrder v) {
            this.k = k;
            this.v = v;
        }

        @Override
        public Integer getKey() {
            return k;
        }

        @Override
        public WorkOrder getValue() {
            return v;
        }

        @Override
        public WorkOrder setValue(WorkOrder value) {
            WorkOrder temp = v;
            v = value;
            return v;
        }
    }
}
