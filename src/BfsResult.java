import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BfsResult {

    private List<String> order;
    private Map<String, Integer> distances;
    private Map<String, String> predecessors;
    private List<String> path;

    public BfsResult(
            List<String> order,
            Map<String, Integer> distances,
            Map<String, String> predecessors,
            List<String> path) {

        this.order = new ArrayList<>(order);
        this.distances = new LinkedHashMap<>(distances);
        this.predecessors = new LinkedHashMap<>(predecessors);
        this.path = new ArrayList<>(path);
    }

    public List<String> getOrder() {
        return new ArrayList<>(order);
    }

    public Map<String, Integer> getDistances() {
        return new LinkedHashMap<>(distances);
    }

    public Map<String, String> getPredecessors() {
        return new LinkedHashMap<>(predecessors);
    }

    public List<String> getPath() {
        return new ArrayList<>(path);
    }

    public boolean hasPath() {
        return !path.isEmpty();
    }

    public int getPathLength() {
        if (path.isEmpty()) {
            return -1;
        }

        return path.size() - 1;
    }
}
