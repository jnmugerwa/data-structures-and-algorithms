import java.util.Comparator;

/**
 * Comparator for KDNodes.
 * Comparisons are performed by comparing the Euclidean distances between the two nodes and a target coordinate.
 *
 * @author Joshua Nathan Mugerwa
 * @version 1.1
 */
public class EuclideanComparator<T extends KDNode> implements Comparator<T> {
    private T target;

    /**
     * Constructs a comparator about the euclidean distance between a target coordinate and node.
     *
     * @param target The target coordinate.
     */
    public EuclideanComparator(T target) {
        this.target = target;
    }

    /**
     * Computes the Euclidean distance between two coordinates.
     *
     * @param o1 The first node.
     * @param o2 The second node.
     * @return The distance between two coordinates.
     */
    public double getEuclideanDistance(T o1, T o2) {
        double[] c1 = o1.getCoordinate();
        double[] c2 = o2.getCoordinate();
        if (c1.length != c2.length) {
            throw new IllegalArgumentException("ERROR: Mismatched number of dimensions.");
        }
        double euclideanDist = 0.0;
        for (int i = 0; i < c1.length; i++) {
            euclideanDist += Math.pow(c1[i] - c2[i], 2);
        }
        return Math.sqrt(euclideanDist);
    }

    /**
     * Compares the Euclidean distances between two nodes and a target coordinate -- multiplied by -1 to allow
     * for max-heap / max-priority-queue functionality (i.e. sorting from largest to smallest).
     *
     * @param o1 The first node.
     * @param o2 The second node.
     * @return 0 if o1 and o2 are distance- equivalent, -1 if o1 is less than o2, 1 else.
     */
    @Override
    public int compare(T o1, T o2) {
        return -1 * Double.compare(getEuclideanDistance(o1, target), getEuclideanDistance(o2, target));
    }

}
