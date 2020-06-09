/**
 * A tree node of k dimensions.
 *
 * @author Joshua Nathan Mugerwa
 * @version 1.1
 */
public interface KDNode {
    /**
     * Retrieves the node's left child.
     * @return The node's left child.
     */
    KDNode getLeftChild();

    /**
     * Sets the node's left child node.
     * @param leftChild The right child node.
     */
    void setLeftChild(KDNode leftChild);

    /**
     * Retrieves the node's right child.
     * @return The node's right child.
     */
    KDNode getRightChild();

    /**
     * Sets the node's right child node.
     * @param rightChild The right child node.
     */
    void setRightChild(KDNode rightChild);

    /**
     * Retrieves the node's coordinate.
     * @return The coordinate.
     */
    double[] getCoordinate();

    /**
     * Sets the coordinate (i.e. spatial coordinate) of the node.
     * @param coordinate The coordinate.
     */
    void setCoordinate(double[] coordinate);

    /**
     * Retrieves the node's data.
     * @return The node's data.
     */
    String getData();

    /**
     * Sets the node's data.
     * @param data The data.
     */
    void setData(String data);

    /**
     * Prints the contents (data, dimensions, and other unique information) of this node.
     */
    void printContents();
}
