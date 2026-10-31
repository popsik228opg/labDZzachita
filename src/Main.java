import java.util.Arrays;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        Graph graph = createMainGraph(); // Создаём объект алгоритма BFS
        Bfs bfs = new Bfs();// Запускаем BFS: ищем путь из "A" в "F", сохраняем полный результат

        BfsResult result = bfs.search(graph, "A", "F");

        System.out.println("Вариант: A11");/ Печатаем шапку: номер варианта
        System.out.println("Алгоритм: BFS");// Печатаем название алгоритма
        System.out.println("Старт: A");// Печатаем стартовую вершину
        System.out.println("Цель: F");// Печатаем целевую вершину

        System.out.println("\nПорядок посещения:");
        System.out.println(String.join(" -> ", result.getOrder()));

        System.out.println("\nРасстояния от A:");// Идём по всем парам «вершина → расстояние» из карты результата

        for (Map.Entry<String, Integer> entry
                : result.getDistances().entrySet()) {
            String distance = entry.getValue() == Integer.MAX_VALUE
                    ? "INF"     // Иначе — число в виде строки (String.valueOf превращает int в String)
                    : String.valueOf(entry.getValue());   // Печатаем строку вида "A = 0" или "F = INF"

            System.out.println(entry.getKey() + " = " + distance);
        }

        System.out.println("\nПуть A -> F:");

        if (result.hasPath()) { // Если путь существует (список пути не пуст
            System.out.println(String.join(" -> ", result.getPath()));
            System.out.println("Количество рёбер: " + result.getPathLength());
        } else {
            System.out.println("Путь из A в F отсутствует."); //// Запускаем набор проверок, передавая BFS, основной граф и первый результат
        }

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

        return graph; // Возвращаем заполненный граф в main
    }

    private static void runTests(
            Bfs bfs, Graph mainGraph, BfsResult firstResult) {

        System.out.println("\nПроверки:");
        // --- Тест 1: граф из одной вершины ---

        Graph single = new Graph();
        single.addVertex(new Vertex("A"));

        check("Одна вершина",
                bfs.search(single, "A", "A").getPath()  // Сравниваем с ожидаемым списком ["A"] по содержимому
                        .equals(Arrays.asList("A")));

        Graph unreachable = new Graph(); // --- Тест 2: недостижимая цель ---
        unreachable.addVertex(new Vertex("A"));
        unreachable.addVertex(new Vertex("F")); // Вершина F, но без рёбер к ней

        BfsResult unreachableResult = bfs.search(unreachable, "A", "F");

        check("Недостижимая F",
                !unreachableResult.hasPath()  // 2) обход посетил только A
                        && unreachableResult.getOrder()
                                .equals(Arrays.asList("A")));
// --- Тест 3: граф с циклом ---
        Graph cycle = new Graph();
        addVertices(cycle, "A", "B", "F");// Добавляем вершины A, B, F 
        cycle.addEdge("A", "B", 1);
        cycle.addEdge("B", "A", 1);
        cycle.addEdge("B", "F", 1);
 // Проверяем: цикл не должен сломать BFS, порядок обхода ровно [A, B, F]
        check("Граф с циклом",
                bfs.search(cycle, "A", "F").getOrder()
                        .equals(Arrays.asList("A", "B", "F")));
// --- Тест 4: граф без цикла ---
        Graph noCycle = new Graph();
        addVertices(noCycle, "A", "B", "F");
        noCycle.addEdge("A", "B", 1);
        noCycle.addEdge("B", "F", 1);

        check("Граф без цикла",
                bfs.search(noCycle, "A", "F").getPath()
                        .equals(Arrays.asList("A", "B", "F")));
  // --- Тест 5: повторный запуск (детерминизм) ---
        BfsResult secondResult = bfs.search(mainGraph, "A", "F");
 // Проверяем, что оба запуска дали одинаковый результат (три условия через &&)
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
    }/ Конец метода check
} // Конец класса Main
