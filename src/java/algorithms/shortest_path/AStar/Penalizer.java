/**
 * A penalty term for path-finding algorithms.
 *
 * @param <V> The object being penalized.
 * @author Joshua Nathan Mugerwa
 * @version 1.0
 */
public interface Penalizer<V> {

    /**
     * Computes the penalty for two vertices.
     *
     * @param v1 The first vertex.
     * @param v2 The second vertex.
     * @return The penalty for two vertices.
     */
    double computePenalty(V v1, V v2);
}
