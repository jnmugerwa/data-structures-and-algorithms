class Node:
    """
    A node.
    """

    def __init__(self, val=None):
        self.val = val
        self.next = None


def detect_cycle(head: Node) -> Node:
    """
    Detects a cycle in a linked list, returning the head of the cycle.
    :param head: The starting node of the linked list
    :return: The head of the list's cycle
    """
    if not head:
        return None

    intersection = None
    slow = fast = head
    while fast and fast.next:  # Find the intersection of hare and pointer
        fast = fast.next.next
        slow = slow.next
        if fast == slow:
            intersection = fast
            break
    if not intersection:  # The list is acyclic
        return None

    p1, p2 = head, intersection
    while p1 != p2:  # Trace from start of list to start of cycle. Where these pointers meet is the start of the cycle
        p1, p2 = p1.next, p2.next
    return p1
