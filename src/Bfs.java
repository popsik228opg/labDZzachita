import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Bfs {

    public BfsResult search(Graph graph, String start, String target) {
        if (graph.getVertex(start) == null) {
            throw new IllegalArgumentException(
                    "Начальная вершина не найдена: " + start);
        }

        Map<String, Integer> distances = new LinkedHashMap<>();    // расстояния
        Map<String, String> predecessors = new LinkedHashMap<>();  // откуда пришли
        Map<String, Boolean> visited = new HashMap<>();            // посещённые
        List<String> order = new ArrayList<>();                    // порядок обхода
        Queue<Vertex> queue = new LinkedList<>();                  // очередь BFS

        // копирую вершины графа в список и сортирую по имени
        List<Vertex> vertices = new ArrayList<>(graph.getVertices());
        vertices.sort(Comparator.comparing(Vertex::getName));

        for (Vertex vertex : vertices) {
            distances.put(vertex.getName(), Integer.MAX_VALUE); // расстояние = бесконечность
            visited.put(vertex.getName(), false);               // пока не посещали
        }

        distances.put(start, 0);
        visited.put(start, true); // помечаем старт посещённым и кладём в очередь
        queue.add(graph.getVertex(start));

        while (!queue.isEmpty()) {
            // достаю вершину из начала очереди и фиксирую в порядке обхода
            // poll() вместо remove(): при пустой очереди возвращает null, а не бросает исключение
            Vertex current = queue.poll();
            String currentName = current.getName();
            order.add(currentName);

            // копирую рёбра вершины и сортирую по имени конечной вершины
            List<Edge> edges = new ArrayList<>(current.getEdges());
            edges.sort(Comparator.comparing(
                    edge -> edge.getDestVertex().getName()));

            for (Edge edge : edges) {
                Vertex next = edge.getDestVertex();
                String nextName = next.getName();

                if (!visited.get(nextName)) {
                    visited.put(nextName, true);
                    distances.put(nextName, distances.get(currentName) + 1);
                    predecessors.put(nextName, currentName);
                    queue.add(next);
                }
            }
        }

        List<String> path = new ArrayList<>();

        if (distances.containsKey(target)
                && distances.get(target) != Integer.MAX_VALUE) {
            String current = target;

            // идём от цели по предшественникам назад к старту, собирая вершины в path
            while (current != null) {
                path.add(current);

                if (current.equals(start)) {
                    break;
                }

                current = predecessors.get(current);
            }

            // если последний элемент обратного пути не старт, значит цепочка оборвалась
            // и путь очищаем; иначе разворачиваем: получаем путь от старта к цели
            if (!path.get(path.size() - 1).equals(start)) {
                path.clear();
            } else {
                Collections.reverse(path);
            }
        }

        return new BfsResult(order, distances, predecessors, path);
    }
}
