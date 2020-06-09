import java.util.Comparator;

/**
 * Comparator for KDNodes.
 * Comparisons are performed by comparing the k-th dimensions of each node.
 *
 * @author Joshua Nathan Mugerwa
 * @version 1.1
 */
public class DimensionalComparator<T extends KDNode> implements Comparator<T> {
    private int comparisonDim;

    /**
     * Constructs the comparator with the given comparison dimension.
     *
     * @param comparisonDim The dimension to compare on.
     */
    public DimensionalComparator(int comparisonDim) {
        this.setComparisonDim(comparisonDim);
    }

    /**
     * Mutator of dimension of comparison.
     *
     * @param comparisonDim The dimension to compare on.
     */
    public void setComparisonDim(int comparisonDim) {
        if (comparisonDim < 0) {
            throw new IllegalArgumentException("Cannot compare negative dimension.");
        }
        this.comparisonDim = comparisonDim;
    }

    /**
     * Compares two nodes based on the comparator's current comparison dimension.
     *
     * @param o1 First node for comparison.
     * @param o2 Second node for comparison.
     * @return 0 if o1 and o2 are equivalent, -1 if o1 is less than o2, 1 else.
     */
    @Override
    public int compare(T o1, T o2) {
        return Double.compare(o1.getCoordinate()[comparisonDim], o2.getCoordinate()[comparisonDim]);
    }
}
