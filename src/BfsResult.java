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
