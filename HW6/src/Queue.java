import java.util.Comparator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Created by Zack on 7/3/2015.
 */
public class Queue implements Container {
    private LinkedList<WorkOrder> spine;

    /**
     * Constructs new Queue object
     */
    public Queue() {
        spine = new LinkedList<>();
    }

    @Override
    public void setComparator(Comparator comp) {
    }

    @Override
    public void add(WorkOrder wo) {
        spine.addLast(wo);
    }

    @Override
    public void arrange() {

    }

    @Override
    public void dumpContainer() {

    }

    @Override
    public boolean hasNext() {
        return !spine.isEmpty();
    }

    @Override
    public WorkOrder next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        return spine.pollFirst();
    }
}
