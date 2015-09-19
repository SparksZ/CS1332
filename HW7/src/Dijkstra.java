import java.util.*;


/**
 * This class implements Dijkstra's algorithm
 * 
 * @author robertwaters
 *
 */
public final class Dijkstra {
	
	/**
	 * 
	 * @param source  The vertex to start the algorithm from
	 * @param adjList the graph to perform the search on.
	 * @return  
	 */
	public static List<TableEntry> dijkstra(String source, AdjacencyList adjList) {
        ArrayList<TableEntry> result = new ArrayList<>();
        Map<Vertex, Map<Vertex, Integer>> aL = adjList.getAdjList();

        PriorityQueue<TableEntry> queue = new PriorityQueue<>();

        // Make the source table entry
        Vertex s = adjList.findVertexByName(source);
        TableEntry s1 = new TableEntry(s);
        s1.setWeight(0);
        s1.setParent(s);
        queue.add(s1);

        /* Couldn't figure out what exactly how to access things in the
           priority queue, so I made this HashMap to compare the best values
           so far.
         */
        HashMap<Vertex, Integer> v2W = new HashMap<>();
        v2W.put(s1.getVertex(), 0);

        /*
            Makes all the other table entries for the queue and v2W
         */
        for (Vertex v : adjList.getAdjList().keySet()) {
            if (!v.equals(s)) {
                queue.add(new TableEntry(v));
                v2W.put(v, Integer.MAX_VALUE);
            }
        }

        while (!queue.isEmpty()) {
            TableEntry u = queue.poll(); // shortest
            u.setKnown(true);
            result.add(u);

            Vertex uTemp = u.getVertex();
            Map<Vertex, Integer> neighbors = aL.get(uTemp);

            for (Vertex v : neighbors.keySet()) {
                int distanceThroughU = u.getWeight() + neighbors.get(v);

                if (distanceThroughU < v2W.get(v)) {
                    queue.remove(new TableEntry(v));

                    TableEntry newTE = new TableEntry(v);
                    newTE.setWeight(distanceThroughU);
                    newTE.setParent(uTemp);
                    v2W.put(v, distanceThroughU);
                    queue.add(newTE);
                }
            }
        }

		return result;
	}
	
}
