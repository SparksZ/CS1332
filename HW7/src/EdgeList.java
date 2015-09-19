import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * This class represents a graph that uses an edge list for it's representation
 * 
 * @author robertwaters
 *
 */
public class EdgeList {
	
	/**
	 * The list of edges in the graph
	 */
	private Collection<Edge> edges;
    
    /**
     * The set of vertexes in the graph
     */
    private Set<Vertex> vertexes;
	
	/**
	 * Construct an EdgeList from a given file
	 * 
	 * @param filename the name of the file
	 * @throws IOException Any of the exceptions that might occur while we process the file
	 */
	public EdgeList(String filename) throws IOException {
		BufferedReader br = null;
        edges = new ArrayList<>();
        vertexes = new HashSet<>();
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
            vertexes.add(v0);
            vertexes.add(v1);
            edges.add(new Edge(v0, v1, weight));
		}
	}
	
	@Override
	public String toString() {
		// TODO Extra Credit
		return "EdgeList";
	}
	
	/**
	 * 
	 * @return all the edges in the graph
	 */
	public Collection<Edge> getEdges() {
		return edges;
	}
    
    public Set<Vertex> getVertexes() {
        return vertexes;
    }
}
