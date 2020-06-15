package algorithms;

import data_structures.graph.Vertex;
import data_structures.graph.Edge;
import java.util.*;

/**
 * Tarjan's bridge-finding algorithm; finds edges connected to articulation points in a connected, undirected graph.
 * Reference: https://en.wikipedia.org/wiki/Bridge_(graph_theory)#Tarjan's_bridge-finding_algorithm
 *
 * @param <V> The type of vertex.
 * @param <E> The type of edge.
 */
public class tarjansBridgeAlgorithm<V extends Vertex<E>, E extends Edge<V>> {
    private static final int initialDepth = 0;
    private final V dummy = null;  // Dummy source for the first call of DFS; Static variable for readability.
    private Map<V, List<E>> graph;
    private Map<V, Integer> disc;  // Discovery time of each vertex.
    private Map<V, Integer> low;  // "Lowest" reachable depth from this node.
    private Set<E> bridges;
    private V entryVertex;

    /**
     * Initializes the bridge-finder.
     *
     * @param edges The set of edges to create the graph from.
     */
    public tarjansBridgeAlgorithm(List<E> edges) {
        graph = new HashMap<>();
        disc = new HashMap<>();
        low = new HashMap<>();
        bridges = new HashSet<>();
        populateGraph(edges);
        entryVertex = graph.keySet().iterator().next(); // Grabs the first vertex in the graph to be entry; Arbitrary.
    }

    /**
     * Creates the graph data structure using the given edges.
     *
     * @param edges The set of edges passed into the constructor.
     */
    private void populateGraph(List<E> edges) {
        for (E edge : edges) {
            graph.putIfAbsent(edge.getStart(), new ArrayList<>());
            graph.putIfAbsent(edge.getEnd(), new ArrayList<>());
            graph.get(edge.getStart()).add(edge);
            graph.get(edge.getEnd()).add(edge);
        }
    }

    /**
     * Finds and returns the set of bridge edges in the graph.
     *
     * @return The set of bridge edges.
     */
    public Set<E> findBridges() {
        dfsHelper(dummy, entryVertex, initialDepth);
        return bridges;
    }

    /**
     * Performs DFS, finding bridges along the way.
     *
     * @param source The source (outgoing) vertex of the current call.
     * @param target The target (incoming) vertex of the current call.
     * @param depth  The depth of the current call (same idea as in level-order traversal of a tree).
     */
    private void dfsHelper(V source, V target, int depth) {
        disc.put(target, depth + 1);
        low.put(target, depth + 1);
        for (E edge : graph.get(target)) {
            V nei = edge.getOpposite(target);
            if (nei == source) continue;
            else if (!disc.containsKey(nei)) {
                dfsHelper(target, nei, depth + 1);
                low.replace(target, Math.min(low.get(target), low.get(nei)));
                if (low.get(nei) > disc.get(target)) {
                    bridges.add(edge);
                }
            } else {
                low.replace(target, Math.min(low.get(target), disc.get(nei)));
            }
        }
    }

}
