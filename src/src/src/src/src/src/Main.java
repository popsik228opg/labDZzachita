check("Повторный запуск",
                firstResult.getOrder().equals(secondResult.getOrder())
                        && firstResult.getDistances()
                                .equals(secondResult.getDistances())
                        && firstResult.getPath()
                                .equals(secondResult.getPath()));
    }

    private static void addVertices(
            Graph graph, String... names) {
        for (String name : names) {
            graph.addVertex(new Vertex(name));
        }
    }

    private static void check(String name, boolean passed) {
        System.out.println(
                name + ": " + (passed ? "PASS" : "FAIL"));
    }
}
