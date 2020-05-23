/**
 * An edge between two vertices.
 *
 * @param <V> The type of vertex that this edge connects.
 * @author Joshua Nathan Mugerwa
 * @version 1.1
 */
public interface Edge<V> {

    /**
     * Accessor of edge's weight.
     *
     * @return The edge's weight.
     */
    double getWeight();

    /**
     * Getter of the starting (i.e. outgoing) vertex in the edge.
     *
     * @return The outgoing vertex in the edge.
     */
    V getStart();

    /**
     * Getter of the ending (i.e. incoming) vertex in the edge.
     *
     * @return The incoming vertex in the edge.
     */
    V getEnd();

    /**
     * Accessor of the vertex that is directionally-opposite in the edge of the one given.
     *
     * @param curr The vertex we want the opposite of.
     * @return The vertex opposite of curr.
     */
    V getOpposite(V curr);

    /**
     * Getter for the edge's label.
     *
     * @return The edge's label.
     */
    String getEdgeLabel();

}
