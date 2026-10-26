import java.util.Arrays;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        Graph graph = createMainGraph();
        Bfs bfs = new Bfs();

        BfsResult result = bfs.search(graph, "A", "F");

        System.out.println("Вариант: A11");
        System.out.println("Алгоритм: BFS");
        System.out.println("Старт: A");
        System.out.println("Цель: F");

        System.out.println("\nПорядок посещения:");
        System.out.println(String.join(" -> ", result.getOrder()));

        System.out.println("\nРасстояния от A:");

        for (Map.Entry<String, Integer> entry
                : result.getDistances().entrySet()) {
            String distance = entry.getValue() == Integer.MAX_VALUE
                    ? "INF"
                    : String.valueOf(entry.getValue());

            System.out.println(entry.getKey() + " = " + distance);
        }

        System.out.println("\nПуть A -> F:");

        if (result.hasPath()) {
            System.out.println(String.join(" -> ", result.getPath()));
            System.out.println("Количество рёбер: " + result.getPathLength());
        } else {
            System.out.println("Путь из A в F отсутствует.");
        }

        runTests(bfs, graph, result);
    }

    private static Graph createMainGraph() {
        Graph graph = new Graph();

        for (String name : Arrays.asList("A", "B", "C", "D", "E", "F")) {
            graph.addVertex(new Vertex(name));
        }

        graph.addEdge("A", "B", 7);
        graph.addEdge("A", "C", 9);
        graph.addEdge("C", "B", -3);
        graph.addEdge("B", "D", -1);
        graph.addEdge("B", "E", -2);
        graph.addEdge("C", "E", 2);
        graph.addEdge("D", "E", 1);
        graph.addEdge("D", "F", 3);
        graph.addEdge("E", "F", 3);

        return graph;
    }

    private static void runTests(
            Bfs bfs, Graph mainGraph, BfsResult firstResult) {

        System.out.println("\nПроверки:");

        Graph single = new Graph();
        single.addVertex(new Vertex("A"));

        check("Одна вершина",
                bfs.search(single, "A", "A").getPath()
                        .equals(Arrays.asList("A")));

        Graph unreachable = new Graph();
        unreachable.addVertex(new Vertex("A"));
        unreachable.addVertex(new Vertex("F"));

        BfsResult unreachableResult = bfs.search(unreachable, "A", "F");

        check("Недостижимая F",
                !unreachableResult.hasPath()
                        && unreachableResult.getOrder()
                                .equals(Arrays.asList("A")));

        Graph cycle = new Graph();
        addVertices(cycle, "A", "B", "F");
        cycle.addEdge("A", "B", 1);
        cycle.addEdge("B", "A", 1);
        cycle.addEdge("B", "F", 1);

        check("Граф с циклом",
                bfs.search(cycle, "A", "F").getOrder()
                        .equals(Arrays.asList("A", "B", "F")));

        Graph noCycle = new Graph();
        addVertices(noCycle, "A", "B", "F");
        noCycle.addEdge("A", "B", 1);
        noCycle.addEdge("B", "F", 1);

        check("Граф без цикла",
                bfs.search(noCycle, "A", "F").getPath()
                        .equals(Arrays.asList("A", "B", "F")));

        BfsResult secondResult = bfs.search(mainGraph, "A", "F");

        check("Повторный запуск",
                firstResult.getOrder().equals(secondResult.getOrder())
                        && firstResult.getDistances()
                                .equals(secondResult.getDistances())
                        && firstResult.getPath()
                                .equals(secondResult.getPath()));
    }

    private static void addVertices(Graph graph, String... names) {
        for (String name : names) {
            graph.addVertex(new Vertex(name));
        }
    }

    private static void check(String name, boolean passed) {
        System.out.println(name + ": " + (passed ? "PASS" : "FAIL"));
    }
}
