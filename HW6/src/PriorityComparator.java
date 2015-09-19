import java.util.Comparator;

/**
 * Created by Zack on 7/3/2015.
 */
public class PriorityComparator implements Comparator<WorkOrder> {

    @Override
    public int compare(WorkOrder o1, WorkOrder o2) {
        return o1.getPriority() - o2.getPriority();
    }
}
