package data_structures.kd_tree;

import java.util.*;

/**
 * A K-D Tree (see: https://en.wikipedia.org/wiki/K-d_tree).
 * A useful data structure for multi-dimensional spacial search, among other things.
 *
 * @author Joshua Nathan Mugerwa
 * @version 1.1
 */
public class KDTree<T extends KDNode> {

    private final int lastDimension; // Zero-based; i.e. If five dimensions, then lastDimension is 4.
    private DimensionalComparator<T> dimensionalComparator;
    private EuclideanComparator<T> euclideanComparator;
    private T root;
    private HashMap<String, T> nameToCoordinateMap;
    private int size;

    /**
     * Constructs a null tree where each node will have (lastDimension + 1) dimensions.
     *
     * @param lastDimension The last dimension in each each node.
     */
    public KDTree(int lastDimension) {
        if (lastDimension < 0) {
            throw new IllegalArgumentException("A tree's nodes must have at least one dimension.");
        }
        this.lastDimension = lastDimension;
        nameToCoordinateMap = new HashMap<>();
        dimensionalComparator = new DimensionalComparator<T>(0);
    }

    /**
     * Constructs a tree with nodes from parsed CSV data, where each node will have
     * (lastDimension + 1) dimensions.
     *
     * @param lastDimension The last dimension in each each node.
     * @param nodes         A list of nodes.
     */
    public KDTree(List<T> nodes, int lastDimension) {
        if (lastDimension < 1) {
            throw new IllegalArgumentException("Nodes must have at least one dimension.");
        }
        this.lastDimension = lastDimension;
        nameToCoordinateMap = new HashMap<>();
        root = this.build(nodes, 0);
        dimensionalComparator = new DimensionalComparator<T>(0);
    }

    /**
     * Helper -- determines dimension to split children upon.
     *
     * @param currentDimension The current dimension of the traversal.
     * @return The next dimension to travel to in our traversal, looping through the available
     * dimensions.
     */
    public int nextDimension(int currentDimension) {
        if (currentDimension == lastDimension) {
            return 0;
        }
        return currentDimension + 1;
    }

    /**
     * Recursively builds a KD Tree, starting from the root. Uses median algorithm.
     *
     * @param nodes         A List of the nodes to be placed in the tree.
     * @param currDimension The dimension of the node we're creating in the current call.
     * @return node A node in the KD Tree. Will be returned after all of its children have been built.
     */
    public T build(List<T> nodes, int currDimension) {
        if (nodes == null || nodes.size() == 0) {
            return null;
        }

        // We must re-sort on each level, based on the new dimension of comparison.
        dimensionalComparator.setComparisonDim(currDimension);
        nodes.sort(dimensionalComparator);

        int medianIndex = nodes.size() / 2;
        T node = nodes.get(medianIndex);

        List<T> leftSubList = nodes.subList(0, medianIndex);
        T leftChild = this.build(leftSubList, this.nextDimension(currDimension));
        if (leftChild != null) {
            node.setLeftChild(leftChild);
        }

        List<T> rightSubList = nodes.subList(medianIndex + 1, nodes.size());
        T rightChild = this.build(rightSubList, this.nextDimension(currDimension));
        if (rightChild != null) {
            node.setRightChild(rightChild);
        }
        size++;
        return node;
    }

    /**
     * K-Nearest-Neighbors (KNN) Search via coordinate.
     *
     * @param target The node to search around.
     * @param k      The max number of neighbors to return.
     */
    public List<KDNode> findKNearestNeighbors(T target, int k) {
        if (k < 1) {
            return Collections.emptyList();
        }
        EuclideanComparator<T> maxComparator = new EuclideanComparator<T>(target);
        PriorityQueue<T> priorityQueue = new PriorityQueue<>(k, maxComparator);
        KNNTraverse(target, k, root, 0, priorityQueue);

        /*
         * The priority queue is sorted descending but we want ascending.
         * So, we reverse it then print its contents.
         */
        List<KDNode> pQueueAsList = new ArrayList<>();
        while (priorityQueue.size() > 0) {
            KDNode curr = priorityQueue.poll();
            pQueueAsList.add(curr);
        }
        return pQueueAsList;
    }

    /**
     * KNN via name. Simply looks up coordinate in nameToCoordinate map then uses coordinate-based
     * KNN method.
     *
     * @param name The name of the target coordinate.
     * @param k    The max number of neighbors to return.
     */
    public void findKNearestNeighbors(String name, int k) {
        findKNearestNeighbors(nameToCoordinateMap.get(name), k);
    }

    /**
     * Search routine for KNN. Recursively traverses the tree, pruning paths/regions/hypercubes on
     * Euclidean distance.
     *
     * @param target        The node to search around.
     * @param k             The max number of neighbors to return.
     * @param currNode      The current node we're visiting in our traversal.
     * @param currDimension The dimension that the current node was sorted on.
     * @param priorityQueue A max-queue holding at most k neighbors of minimal distance to target.
     */
    public void KNNTraverse(T target, int k, T currNode, int currDimension,
                            PriorityQueue<T> priorityQueue) {
        if (currNode == null) {
            return;
        }
        double distFromCurrNodeToTarget = euclideanComparator.getEuclideanDistance(currNode, target);
        if (distFromCurrNodeToTarget != 0) {
            if (priorityQueue.size() < k) {
                priorityQueue.add(currNode);
            } else if (euclideanComparator.getEuclideanDistance(priorityQueue.peek(), target)
                    > distFromCurrNodeToTarget) {
                // Remove head.
                priorityQueue.poll();
                priorityQueue.add(currNode);
            }
        }

        int nextDimension = this.nextDimension(currDimension);

        /*
         * If the queue isn't full, we need to search the entire space -- we can't prune yet.
         */
        if (priorityQueue.size() < k) {
            if (currNode.getLeftChild() != null) {
                KNNTraverse(target, k, (T) currNode.getLeftChild(), nextDimension, priorityQueue);
            }

            if (currNode.getRightChild() != null) {
                KNNTraverse(target, k, (T) currNode.getRightChild(), nextDimension, priorityQueue);
            }
        }
        /*

         * The queue is full -- we can start pruning paths ("path" = subtree from current node).
         */
        else {
            double axisDist = target.getCoordinate()[currDimension] - currNode.getCoordinate()[currDimension];
            /*
             * Ambiguous -- can't prune either path.
             */
            if (euclideanComparator.getEuclideanDistance(priorityQueue.peek(), target) > axisDist) {
                if (currNode.getLeftChild() != null) {
                    KNNTraverse(target, k, (T) currNode.getLeftChild(), nextDimension, priorityQueue);
                }

                if (currNode.getRightChild() != null) {
                    KNNTraverse(target, k, (T) currNode.getRightChild(), nextDimension, priorityQueue);
                }
            }

            /*
             * Pruning the right path.
             */
            else if (currNode.getLeftChild() != null && axisDist >= 0) {
                KNNTraverse(target, k, (T) currNode.getLeftChild(), nextDimension, priorityQueue);
            }

            /*
             * Pruning the left path.
             */
            else if (currNode.getRightChild() != null && axisDist < 0) {
                KNNTraverse(target, k, (T) currNode.getRightChild(), nextDimension, priorityQueue);
            }

        }
    }

    /**
     * Searches for all nodes within a radius centered at a target coordinate.
     *
     * @param target The node to search around.
     * @param radius The radius of search.
     */
    public List<KDNode> radiusSearch(T target, double radius) {
        EuclideanComparator<T> maxComparator = new EuclideanComparator<T>(target);
        PriorityQueue<T> priorityQueue = new PriorityQueue<>(size, maxComparator);
        radiusTraverse(target, radius, root, 0, priorityQueue);

        List<KDNode> pQueueAsList = new ArrayList<>();
        while (priorityQueue.size() > 0) {
            KDNode curr = priorityQueue.poll();
            pQueueAsList.add(curr);
        }
        return pQueueAsList;
    }

    /**
     * Searches for all nodes within a radius centered at a target node (by name).
     *
     * @param name   The name of the node.
     * @param radius The radius of search.
     */
    public void radiusSearch(String name, double radius) {
        radiusSearch(nameToCoordinateMap.get(name), radius);
    }

    /**
     * Search routine for Radius Search.
     *
     * @param target        The node to search around.
     * @param radius        The radius of search.
     * @param currNode      The current node we're visiting in our traversal.
     * @param currDimension The dimension that the current node was sorted on.
     * @param priorityQueue A max-queue holding at most k neighbors of minimal distance to target.
     */
    private void radiusTraverse(T target, double radius, T currNode,
                                int currDimension, PriorityQueue<T> priorityQueue) {
        if (currNode == null) {
            return;
        }

        double distFromCurrNodeToTarget = euclideanComparator.getEuclideanDistance(currNode, target);
        boolean isWithinRadius = (distFromCurrNodeToTarget <= radius &&
                !priorityQueue.contains(currNode));

        if (isWithinRadius) {
            priorityQueue.add(currNode);
        }

        int nextDimension = this.nextDimension(currDimension);

        if (currNode.getLeftChild() != null) {
            radiusTraverse(target, radius, (T) currNode.getLeftChild(), nextDimension, priorityQueue);
        }
        if (currNode.getRightChild() != null) {
            radiusTraverse(target, radius, (T) currNode.getRightChild(), nextDimension, priorityQueue);
        }
    }

    /**
     * Prints current node then attempts to print its children in pre-order.
     *
     * @param node The node we're currently at in the traversal.
     */
    public void printTree(T node) {
        if (node == null) {
            return;
        }
        node.printContents();
        if (node.getLeftChild() != null) {
            printTree((T) node.getLeftChild());
        }
        if (node.getRightChild() != null) {
            printTree((T) node.getRightChild());
        }
    }

    /**
     * Accessor for size (number of nodes) of tree.
     *
     * @return The number of nodes in the tree.
     */
    public int getSize() {
        return size;
    }

    /**
     * Accessor for the root of the tree.
     *
     * @return The root of the tree.
     */
    public T getRoot() {
        return root;
    }

    /**
     * Mutator for the root of the tree.
     *
     * @param root The root of the tree.
     */
    public void setRoot(T root) {
        this.root = root;
    }

    /**
     * Accessor of name-to-coordinate map.
     *
     * @param name The name to index with.
     * @return The node associated with name.
     */
    public T getCoordinateFromName(String name) {
        return nameToCoordinateMap.get(name);
    }
}


