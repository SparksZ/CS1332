import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This class implements the union find algorithm
 * 
 * @author robertwaters
 *
 */
public class UnionFind {
    // Bi-Map
	HashMap<Vertex, Integer> vertexMap;
    HashMap<Integer, Vertex> intMap;
    int[] sets;
	
	/**
	 * This should implement the make_set function of union find
	 * 
	 * @param edgeList  the list to create the disjoint sets from
	 */
	public UnionFind(Set<Vertex> vertexes) {
        vertexMap = new HashMap<>();
        intMap = new HashMap<>();

        sets = new int[vertexes.size()];
        Iterator<Vertex> it = vertexes.iterator();
        for (int i = 0; i < vertexes.size(); i++) {
            Vertex vert = it.next();
            vertexMap.put(vert, i);
            intMap.put(i, vert);

            sets[i] = -1;
        }
	}

	/**
	 * Assume that u is a vertex. Determine if they are currently
	 * in the same component of this UnionFind structure
	 * 
	 * @param u the vertex we want to find the set for
	 * @return the name of the set that u is in
	 */
	public int find(Vertex u) {
        int uI = vertexMap.get(u);

		if (sets[uI] < 0) {
            return vertexMap.get(u);
        } else {
            return find(intMap.get(sets[uI]));
        }
	}

	/**
	 * Assume that u and v are vertices that were in the edgeList. Assume that u and v are in
	 * different components (you have already checked find). Union the component containing u and the component containing v
	 * together.
	 * 
	 * @param u
	 *            a vertex
	 * @param v
	 *            a vertex
	 */
	public void union(Vertex u, Vertex v) {
        sets[find(v)] = find(u);
	}
}
