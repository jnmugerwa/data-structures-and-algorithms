class _SegmentTreeNode:
    """
    A node in a Segment Tree.

    author: Joshua Nathan Mugerwa
    version: 7/10/20
    """

    def __init__(self, start, end):
        self.start = start
        self.end = end
        self.total = 0
        self.left = self.right = None


class SegmentTree:
    """
    A basic Segment Tree. Very efficient for mutable range-sum queries.
    Reference: https://en.wikipedia.org/wiki/Segment_tree

    author: Joshua Nathan Mugerwa
    version: 7/10/20
    """

    def __init__(self, nums: list[int]) -> None:
        """
        Recursively builds the tree.
        :param nums: The list of numbers we will compute range-sums over.
        """

        def build(i=0, j=len(nums) - 1) -> _SegmentTreeNode:
            """
            Recursive helper. Splits the array in half at each level, then propagates range sums from leaves.
            :param i: The left index of the current halve of the array.
            :param j: The right index of the current halve of the array.
            :return: The root of the segment tree.
            """
            if i > j:
                return None
            else:
                node = _SegmentTreeNode(i, j)  # Build out subtree from here
                if i == j:
                    node.total = nums[i]
                else:
                    mid = i + (j - i) // 2
                    node.left, node.right = build(i, mid), build(mid + 1, j)
                    node.total = node.left.total + node.right.total  # Sum of leaves under root; range_sum(l, r)
                return node

        self.root = build()

    def update(self, idx: int, value: int) -> None:
        """
        Updates the value of nums[idx] in the tree.
        :param idx: The index of the value we're updating.
        :param value: The value we're updating to.
        :return: The total sum of the nodes underneath the current node.
        """

        def recurse(node=self.root) -> int:
            """
            Recursively traverses the tree. Once it finds the node to update it propagates the new range sum upwards.
            :param node: The root of the subtree we're searching.
            :return: root.total: The total sum of the nodes beneath root.
            """
            if node.start == node.end:  # Base; actual value is stored in leaf, and total is propagated upwards.
                node.total = value
            else:
                mid = node.start + (node.end - node.start) // 2
                if idx <= mid:  # Value must be in left subtree
                    recurse(node.left)
                else:  # Value must be in right subtree
                    recurse(node.right)
                node.total = node.left.total + node.right.total
            return node.total

        recurse()

    def range_sum(self, start: int, end: int) -> int:
        """
        Calculate the range-sum between (start, end) inclusive.
        :param start: The start of the range.
        :param end: The end of the range.
        :return: The sum of the range between (start, end) inclusive.
        """

        def _recurse(node=self.root, i=start, j=end) -> int:
            """
            Recursively traverses the tree. Propagates the range sum upwards from the first leaves we encounter.
            :param node: The root of the subtree we're traversing.
            :param i: The start of the range.
            :param j: The end of the range.
            :return: The range-sum of the subtree we're traversing.
            """
            if (node.start, node.end) == (i, j):  # Range is contained in this node
                return node.total
            else:
                mid = node.start + (node.end - node.start) // 2
                if j <= mid:  # Range is entirely contained in left subtree
                    return _recurse(node.left, i, j)
                elif i >= mid + 1:  # Range is entirely contained in right subtree
                    return _recurse(node.right, i, j)
                else:  # Range is in both subtrees
                    return _recurse(node.left, i, mid) + _recurse(node.right, mid + 1, j)

        return _recurse()


'''
Example usage:
--------------
    Given nums = [1, 3, 5]
    
    sumRange(0, 2) -> 9
    
    update(1, 2)
    
    sumRange(0, 2) -> 8
'''
