import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AdjacencyList {

	/**
	 * This is a map (Hash Table) of vertices to their adjacent vertices and the weight associated with that edge
	 */
	private Map<Vertex, Map<Vertex, Integer>> adjList;
    private GraphType myType = GraphType.UNDIRECTED;

    /**
     * Create an adjacency list from a file
     */
	public AdjacencyList(String filename, GraphType pType) throws IOException {
        BufferedReader br = null;
        adjList = new HashMap<>();
        String sCurrentLine;

        br = new BufferedReader(new FileReader(filename));

        while ((sCurrentLine = br.readLine()) != null) {
            // Split line into parts
            String[] splitLine = sCurrentLine.split(",");

            // Make objects from data
            Vertex v0 = new Vertex(splitLine[0]);
            Vertex v1 = new Vertex(splitLine[1]);
            Integer weight = Integer.parseInt(splitLine[2]);

            // Add v0 to list
            if (!adjList.containsKey(v0)) { // key doesn't exist
                HashMap<Vertex, Integer> value = new HashMap<>();
                value.put(v1, weight);

                adjList.put(v0, value);
            } else { // key exists
                adjList.get(v0).put(v1, weight);
            }

            // Adds v1 to list
            if (!adjList.containsKey(v1)) {
                HashMap<Vertex, Integer> value = new HashMap<>();
                value.put(v0, weight);

                adjList.put(v1, value);
            } else {
                adjList.get(v1).put(v0, weight);
            }
        }
    }

	@Override
	public String toString() {
		return "AdjacencyList";
	}
	
    /**
     *  Find a vertex by its name
     *  @param  name   the name of the vertex to find
     *  @return the vertex with that name (or null if none)
     */
	public Vertex findVertexByName(String name) {
        Set<Vertex> keys = adjList.keySet();

        for (Vertex v : keys) {
            if (v.getName().equals(name)) {
                return v;
            }
        }

        return null;
    }
    
    /**
     * @return the number of vertexes in the graph
     */
    public int vertexCount() {
        return adjList.size();
    }

    /**
     * @return the adjacency list of the graph
     */
	public Map<Vertex, Map<Vertex, Integer>> getAdjList() {
		return adjList;
	}
}
