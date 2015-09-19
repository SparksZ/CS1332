import java.util.Comparator;

/**
 * Created by Zack on 7/3/2015.
 */
public class HoursComparator implements Comparator<WorkOrder> {

    @Override
    public int compare(WorkOrder o1, WorkOrder o2) {
        return o1.getHours() - o2.getHours();
    }
}
