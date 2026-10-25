 while (current != null) {
                path.add(current);

                if (current.equals(start)) {
                    break;
                }

                current = predecessors.get(current);
            }

            if (!path.get(path.size() - 1).equals(start)) {
                path.clear();
            } else {
                Collections.reverse(path);
            }
        }

        return new BfsResult(
                order, distances, predecessors, path);
    }
}
