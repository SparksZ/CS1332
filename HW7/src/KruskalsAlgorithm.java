import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Union;

import java.util.ArrayList;
import java.util.Collection;
import java.util.PriorityQueue;


/**
 * This class encapsulates Kruskal's algorithm for finding a minimum spanning tree 
 * 
 * @author robertwaters
 *
 */
public final class KruskalsAlgorithm {
    private static PriorityQueue<Edge> queue;

    /**
     * Implements Kruskal's Algo!
     * @param edgeList the EdgeList representation of the graph
     * @return the MST
     */
	public static Collection<Edge> kruskal(EdgeList edgeList) {
        queue = new PriorityQueue<>();
        Collection<Edge> eL = edgeList.getEdges();
        UnionFind uF = new UnionFind(edgeList.getVertexes());
        ArrayList<Edge> result = new ArrayList<>();

        for (Edge e : eL) {
            queue.add(e);
        }

        while (!queue.isEmpty()) {
            Edge temp = queue.poll();

            Vertex src = temp.getSource();
            Vertex des = temp.getDestination();

            int f1 = uF.find(src);
            int f2 = uF.find(des);

            if (f1 != f2) {
                uF.union(src, des);
                result.add(temp);
            }
        }

		return result;
	}
	
}
