import java.util.LinkedList;

public class Vertex {

    private String name;
    private LinkedList<Edge> edgeList;

    public Vertex(String name) {
        this.name = name;
        edgeList = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public LinkedList<Edge> getEdges() {
        return edgeList;
    }
}
