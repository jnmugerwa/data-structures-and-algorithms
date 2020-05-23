import java.util.*;

/**
 * An object-encapsulated shortest-path finder. Specifically, runs A* algorithm
 * to find, print, and return the shortest path between two vertices.
 *
 * @param <V> The vertex that this finder will process.
 * @param <E> The edge that this finder will process.
 * @author Joshua Nathan Mugerwa
 * @version 1.2
 */
public class AStar<V extends Vertex<E>, E extends Edge<V>> implements ShortestPathFinder<V, E> {

    private Map<V, Double> distMap;
    private Map<V, Edge<V>> prevEdgeMap;
    private V mostRecentStart;
    private V mostRecentEnd;
    private Comparator<V> distanceComparator;
    private Penalizer<V> penalizer;

    /**
     * Initializes the source and target nodes of the A* routine.
     *
     * @param penalizer penalizes nodes depending on their likelihood of being in
     *                  shortest path
     */
    public AStar(Penalizer<V> penalizer) {
        this.distanceComparator = new DistanceComparator();
        this.penalizer = penalizer;
    }

    /**
     * Conducts the A* routine.
     *
     * @param start vertex of path
     * @param end   vertex of path
     */
    @Override
    public void findShortestPath(V start, V end) {
        this.distMap = new HashMap<>();
        this.prevEdgeMap = new HashMap<>();
        this.mostRecentStart = start;
        this.mostRecentEnd = end;

        boolean endFlag = false;
        PriorityQueue<V> frontier = new PriorityQueue<>((a, b) -> distanceComparator.compare(a, b));
        HashSet<V> visited = new HashSet<>();

        distMap.put(start, 0.0);
        frontier.add(start);
        while (!frontier.isEmpty() && !endFlag) {
            V closest = frontier.poll();
            visited.add(closest);
            List<E> edges = closest.getEdges();
            double currDist = distMap.get(closest);
            for (E edge : edges) {
                V neighbor = edge.getEnd();
                distMap.putIfAbsent(neighbor, Double.MAX_VALUE);
                double newDist = currDist + edge.getWeight() + penalizer.computePenalty(neighbor,
                        mostRecentEnd);
                if (newDist < distMap.get(neighbor) && !visited.contains(neighbor)) {
                    distMap.put(neighbor, newDist);
                    prevEdgeMap.put(neighbor, edge);
                    frontier.add(neighbor);
                    if (neighbor.equals(end)) {
                        // If you don't do this, we never update end -- the currV we encounter in our
                        // search is not the
                        // same object as end, even if they have the same field values.
                        mostRecentEnd = neighbor;
                        // A flag helps end the outer loop; else, we just break from this edge loop but
                        // continue
                        // the broader loop.
                        endFlag = true;
                        break;
                    }
                }
            }
        }
    }

    /**
     * Retrieves set of vertices on shortest path from source to target.
     *
     * @return A path representation of the shortest-path.
     */
    public List<V> getShortestPath() {
        if (prevEdgeMap.get(mostRecentEnd) == null) {
            System.out.println("ERROR: No path could be formed.");
            return Collections.emptyList();
        }
        return new LinkedList<>(getPathHelper(mostRecentEnd));
    }

    /**
     * Helper for retrieving shortest path; recursively traces path, printing from
     * start to finish.
     *
     * @param curr The current vertex in the path.
     * @return A representation of the path.
     */
    private List<V> getPathHelper(V curr) {
        if (prevEdgeMap.get(curr) == null) {
            return new LinkedList<>();
        }
        List<V> recursiveList = getPathHelper(prevEdgeMap.get(curr).getOpposite(curr));
        recursiveList.add(curr);
        return recursiveList;
    }

    /**
     * Prints shortest-path, starting from first in path. If no connection was
     * possible, prints an error string.
     *
     * @return A string representation of the shortest-path.
     */
    @Override
    public String printShortestPath() {
        return prevEdgeMap.get(mostRecentEnd) != null ? printHelper(mostRecentEnd).trim()
                : mostRecentStart.getName() + " -/- " + mostRecentEnd.getName();
    }

    /**
     * Helper for printing shortest path; recursively traces path, printing from
     * start to finish.
     *
     * @param curr The current vertex in the path.
     * @return A string representation of the path.
     */
    private String printHelper(V curr) {
        if (prevEdgeMap.get(curr) == null) {
            return "";
        }
        return printHelper(prevEdgeMap.get(curr).getOpposite(curr)) + prevEdgeMap.get(curr).getOpposite(
                curr).getName() + " -> " + curr.getName() + " : " + prevEdgeMap.get(curr).getEdgeLabel()
                + "\n";
    }

    /**
     * Comparator for distances between vertices. Comparisons are performed by
     * comparing the distances between the two nodes and some shared source node.
     *
     * @author Kalli Feinberg and Joshua Nathan Mugerwa
     * @version 1.0
     */
    private class DistanceComparator implements Comparator<V> {
        /**
         * Compares the distances (from some source) of two vertices.
         *
         * @param v1 The first vertex.
         * @param v2 The second vertex.
         * @return 0 if v1 and v2 are distance- equivalent, 1 if v1 is less than v2, -1
         * else.
         */
        @Override
        public int compare(V v1, V v2) {
            if (distMap.containsKey(v1) && distMap.containsKey(v2)) {
                return distMap.get(v1).compareTo(distMap.get(v2));
            }
            return -1;
        }
    }
}
