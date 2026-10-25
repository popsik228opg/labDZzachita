import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Graph {

    private Map<String, Vertex> vertices;

    public Graph() {
        vertices = new LinkedHashMap<>();
    }

    public boolean addVertex(Vertex vertex) {
        if (vertices.containsKey(vertex.getName())) {
            return false;
        }

        vertices.put(vertex.getName(), vertex);
        return true;
    }

    public Vertex getVertex(String name) {
        return vertices.get(name);
    }

    public Collection<Vertex> getVertices() {
        return vertices.values();
    }

    public boolean addEdge(String from, String to, int weight) {
        Vertex source = getVertex(from);
        Vertex destination = getVertex(to);

        if (source == null || destination == null) {
            return false;
        }

        source.getEdges().add(new Edge(destination, weight));
        return true;
    }
}
