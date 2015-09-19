import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * A class to allow you to write drivers if you wish to test out your code
 * 
 * @author robertwaters
 *
 */
public class RunProblems {

	/**
	 * TEST YOUR CODE here (if you do not implement JUnit tests). This method is for your benefit
	 * and will not be graded.
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
	}
	/**
	 * Create an adjacency list (using the AdjacencyList constructor) from the given txtFile and run
	 * Dijkstra's algorithm with the given AdjacencyList and the given source vertex
	 * 
	 * @param source  the name of the start vertex
	 * @param txtFile the name of the file that contains the graph information
	 * @return a map keyed by vertex that contains the shortest path to the vertex from the source
	 */
	public static List<TableEntry> runDijkstra(AdjacencyList list, String source) throws IOException {
        AdjacencyList test = new AdjacencyList("graphdata.txt",
				GraphType.UNDIRECTED);
		return Dijkstra.dijkstra(source, test);
	}

	/**
	 * run Kruskal's
	 * algorithm with that EdgeList, return the list of edges that are in the MST
	 * 
	 * @param a graph in EdgeList format
	 * @return the edges in the MST
	 */
	public static Collection<Edge> runKruskal(EdgeList edges) throws IOException {
        EdgeList test2 = new EdgeList("graphdata.txt");

        return KruskalsAlgorithm.kruskal(test2);
	}
}
