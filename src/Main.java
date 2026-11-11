import java.util.Arrays;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        Graph graph = createMainGraph();
        Bfs bfs = new Bfs(); // создаём объект алгоритма BFS

        // запускаем BFS: ищем путь из "A" в "F", сохраняем полный результат
        BfsResult result = bfs.search(graph, "A", "F");

        System.out.println("Вариант: A11");  // номер варианта
        System.out.println("Алгоритм: BFS"); // название алгоритма
        System.out.println("Старт: A");      // стартовая вершина
        System.out.println("Цель: F");       // целевая вершина

        System.out.println("\nПорядок посещения:");
        System.out.println(String.join(" -> ", result.getOrder()));

        System.out.println("\nРасстояния от A:");

        // идём по всем парам «вершина -> расстояние» из карты результата
        for (Map.Entry<String, Integer> entry
                : result.getDistances().entrySet()) {
            String distance = entry.getValue() == Integer.MAX_VALUE
                    ? "INF"                            // вершина недостижима
                    : String.valueOf(entry.getValue()); // иначе число в виде строки

            System.out.println(entry.getKey() + " = " + distance);
        }

        System.out.println("\nПуть A -> F:");

        if (result.hasPath()) { // если путь существует (список пути не пуст)
            System.out.println(String.join(" -> ", result.getPath()));
            System.out.println("Количество рёбер: " + result.getPathLength());
        } else {
            System.out.println("Путь из A в F отсутствует.");
        }

        // запускаем набор проверок: BFS, основной граф и первый результат
        runTests(bfs, graph, result);
    }

    private static Graph createMainGraph() { //делаю пустой граф 
        Graph graph = new Graph(); //добавляю 6 вершин циклом по списку имен через аррайс

        for (String name : Arrays.asList("A", "B", "C", "D", "E", "F")) {
            graph.addVertex(new Vertex(name));
        }
// Добавляем направленные рёбра с весами
        graph.addEdge("A", "B", 7);
        graph.addEdge("A", "C", 9);
        graph.addEdge("C", "B", -3);
        graph.addEdge("B", "D", -1);
        graph.addEdge("B", "E", -2);
        graph.addEdge("C", "E", 2);
        graph.addEdge("D", "E", 1);
        graph.addEdge("D", "F", 3);
        graph.addEdge("E", "F", 3);

        return graph; // возвращаем заполненный граф в main
    }

    private static void runTests(
            Bfs bfs, Graph mainGraph, BfsResult firstResult) {

        System.out.println("\nПроверки:");

        // --- Тест 1: основной граф ---
        check("Основной граф",
                firstResult.getOrder().equals(
                        Arrays.asList("A", "B", "C", "D", "E", "F"))
                        && firstResult.getPath().equals(
                                Arrays.asList("A", "B", "D", "F"))
                        && firstResult.getPathLength() == 3);

        // --- Тест 2: граф из одной вершины ---

        Graph single = new Graph();
        single.addVertex(new Vertex("A"));

        check("Одна вершина",
                bfs.search(single, "A", "A").getPath() // сравниваем с ожидаемым списком ["A"]
                        .equals(Arrays.asList("A")));

        // --- Тест 3: недостижимая цель ---
        Graph unreachable = new Graph();
        unreachable.addVertex(new Vertex("A"));
        unreachable.addVertex(new Vertex("F")); // вершина F есть, но рёбер к ней нет

        BfsResult unreachableResult = bfs.search(unreachable, "A", "F");

        check("Недостижимая F",
                !unreachableResult.hasPath()  // пути нет, обход посетил только A
                        && unreachableResult.getOrder()
                                .equals(Arrays.asList("A")));

        // --- Тест 4: граф с циклом ---
        Graph cycle = new Graph();
        addVertices(cycle, "A", "B", "F");
        cycle.addEdge("A", "B", 1);
        cycle.addEdge("B", "A", 1);
        cycle.addEdge("B", "F", 1);

        // цикл не должен сломать BFS: порядок обхода ровно [A, B, F]
        check("Граф с циклом",
                bfs.search(cycle, "A", "F").getOrder()
                        .equals(Arrays.asList("A", "B", "F")));

        // --- Тест 5: граф без цикла ---
        Graph noCycle = new Graph();
        addVertices(noCycle, "A", "B", "F");
        noCycle.addEdge("A", "B", 1);
        noCycle.addEdge("B", "F", 1);

        check("Граф без цикла",
                bfs.search(noCycle, "A", "F").getPath()
                        .equals(Arrays.asList("A", "B", "F")));

        // --- Тест 6: повторный запуск (детерминизм) ---
        BfsResult secondResult = bfs.search(mainGraph, "A", "F");
        // оба запуска должны дать одинаковый результат (три условия через &&)
        check("Повторный запуск",
                firstResult.getOrder().equals(secondResult.getOrder())
                        && firstResult.getDistances()
                                .equals(secondResult.getDistances())
                        && firstResult.getPath()
                                .equals(secondResult.getPath()));
    }
    // Приватный статический хелпер — добавляет несколько вершин в граф
    private static void addVertices(Graph graph, String... names) {
        for (String name : names) {
            graph.addVertex(new Vertex(name));
        }
    }
    // name — название теста, passed — прошёл ли тест
    private static void check(String name, boolean passed) {
        System.out.println(name + ": " + (passed ? "PASS" : "FAIL"));
    } // конец метода check
} // конец класса Main
