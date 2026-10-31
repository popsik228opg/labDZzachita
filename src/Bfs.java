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

        Map<String, Integer> distances = new LinkedHashMap<>();   // расстояния
Map<String, String>  predecessors = new LinkedHashMap<>(); // откуда пришли
Map<String, Boolean> visited = new HashMap<>();            // посещённые
List<String>         order = new ArrayList<>();            // порядок обхода
Queue<Vertex>        queue = new LinkedList<>();           // очередь BFS

        List<Vertex> vertices = new ArrayList<>(graph.getVertices()); //копирую вершины графа в список
        vertices.sort(Comparator.comparing(Vertex::getName)); //сортирую по имени

        for (Vertex vertex : vertices) {
            distances.put(vertex.getName(), Integer.MAX_VALUE); // ставлю расстояние бесконечность
            visited.put(vertex.getName(), false); // посещение вершины = фалсе
        }

        distances.put(start, 0);
        visited.put(start, true); //посещение ставлю как тру чтобы пометить что тут были и положить в очередь
        queue.add(graph.getVertex(start));

        while (!queue.isEmpty()) {
            Vertex current = queue.poll(); //крч если очередь не пуста достаю вершину с начала запоминаю и фиксирую в порядке обхода
            String currentName = current.getName();
            order.add(currentName);
//использую полл а не ремув тк полл возвращает null а ремув бросает искл
            
            List<Edge> edges = new ArrayList<>(current.getEdges()); //изменяемый список самой вершины 
            edges.sort(Comparator.comparing(
                    edge -> edge.getDestVertex().getName())); //копируем ребра и сортируем по им кон вершины

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

            while (current != null) {
                path.add(current);

                if (current.equals(start)) {
                    break;
                }

                current = predecessors.get(current);
            } //идём от цели по предшественникам назад к старту, собирая вершины в path

            if (!path.get(path.size() - 1).equals(start)) {
                path.clear();
            } else {
                Collections.reverse(path);
            }
        }
//если последний элемент обратного пути — не старт, значит цепочка оборвалась (битые данные) → очищаем путь;
иначе разворачиваем — получаем путь от старта к цели.
        return new BfsResult(order, distances, predecessors, path);
    }
}
