class _SegmentTreeNode:
    """
    A node in a Segment Tree.

    author: Joshua Nathan Mugerwa
    version: 6/15/20
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
    version: 6/15/20
    """

    def __init__(self, nums: list[int]) -> None:
        """
        Recursively builds the tree.
        :param nums: The list of numbers we will compute range-sums over.
        """

        def _build(l=0, r=len(nums) - 1) -> _SegmentTreeNode:
            """
            Recursive helper. Splits the array in half at each level, then propagates range sums from leaves.
            :param l: The left index of the current halve of the array.
            :param r: The right index of the current halve of the array.
            :return: The root of the segment tree.
            """
            if l > r:
                return None
            elif l == r:
                n = _SegmentTreeNode(l, r)
                n.total = nums[l]
                return n
            else:
                mid = (l + r) // 2
                root = _SegmentTreeNode(l, r)  # Build out subtree from here
                root.left, root.right = _build(l, mid), _build(mid + 1, r)
                root.total = root.left.total + root.right.total  # Sum of leaves under root; range_sum(l, r)
                return root

        self.root = _build()

    def update(self, idx: int, value: int) -> None:
        """
        Updates the value of nums[idx] in the tree.
        :param idx: The index of the value we're updating.
        :param value: The value we're updating to.
        :return: The total sum of the nodes underneath the current node.
        """

        def _recurse(root=self.root, i=idx, val=value) -> int:
            """
            Recursively traverses the tree. Once it finds the node to update it propagates the new range sum upwards.
            :param root: The root of the subtree we're searching.
            :param i: The index of the value we're updating.
            :param val: The value we're updating to.
            :return: root.total: The total sum of the nodes beneath root.
            """
            # Base; actual value is stored in leaf, and total is propagated upwards.
            if root.start == root.end:
                root.total = val
                return root.total
            mid = (root.start + root.end) // 2
            if i <= mid:  # Value must be in left subtree
                _recurse(root.left, i, val)
            else:  # Value must be in right subtree
                _recurse(root.right, i, val)
            root.total = root.left.total + root.right.total
            return root.total

        _recurse()

    def range_sum(self, start: int, end: int) -> int:
        """
        Calculate the range-sum between (start, end) inclusive.
        :param start: The start of the range.
        :param end: The end of the range.
        :return: The sum of the range between (start, end) inclusive.
        """

        def _recurse(root=self.root, i=start, j=end) -> int:
            """
            Recursively traverses the tree. Propagates the range sum upwards from the first leaves we encounter.
            :param root: The root of the subtree we're traversing.
            :param i: The start of the range.
            :param j: The end of the range.
            :return: The range-sum of the subtree we're traversing.
            """
            if (root.start, root.end) == (i, j):  # Range is contained in this node
                return root.total
            else:
                mid = (root.start + root.end) // 2
                if j <= mid:  # Range is entirely contained in left subtree
                    return _recurse(root.left, i, j)
                elif i >= mid + 1:  # Range is entirely contained in right subtree
                    return _recurse(root.right, i, j)
                else:  # Range is in both subtrees
                    return _recurse(root.left, i, mid) + _recurse(root.right, mid + 1, j)

        return _recurse()


'''
Example usage:
--------------
    Given nums = [1, 3, 5]
    
    sumRange(0, 2) -> 9
    
    update(1, 2)
    
    sumRange(0, 2) -> 8
'''