class UnionFind:
    """
    A union-find (also called "disjoint-set") data structure. Very useful for tracking high-level structure within
    a graph like connected components.
    author: Joshua Nathan Mugerwa
    version: 5/23/20
    """

    def __init__(self, iterable):  # This is more fungible... dealing with indexing errors if we use ranges
        '''
        Creates fields where parent/representative and rank values are stored for each element in the universe.
        (universe is cute way of saying all elements in our collection of disjoint sets).
        :param iterable:
        '''
        self.parent = {x: x for x in iterable}
        self.rank = {x: 0 for x in iterable}

    def make_set(self, x):
        '''
        Creates a disjoint set containing x (and only x).
        :param x: An element
        '''
        self.parent[x] = x

    def find(self, x):
        '''
        Path-compressed find routine.
        :param x: The element whose set we're locating
        :return: The parent/representative of the set that x belongs to (a proxy for the set itself)
        '''
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]

    def union(self, x, y):
        '''
        Union-by-rank.
        :param x: Element one
        :param y: Element two
        :return: True if we were able to union their sets else False
        '''
        xr, yr = self.find(x), self.find(y)
        if xr == yr:  # x and y are in the same set; doesn't make sense to try to union
            return False

        elif self.rank[xr] > self.rank[yr]:
            self.parent[yr] = xr

        elif self.rank[xr] < self.rank[yr]:
            self.parent[xr] = yr

        elif self.rank[xr] == self.rank[yr]:  # If ranks are equivalent, we arbitrarily set one set to be the parent
            self.parent[yr] = xr
            self.rank[xr] += 1

        return True

    def is_connected(self, x, y):
        '''
        Tests if two elements are connected (i.e. a path exists between x and y).
        We leverage the fact that UnionFind maintains a collection of disjoint sets, so a path exists iff x and y
        are in the same set.
        :param x: Element one
        :param y: Element 2
        :return: True if the two elements are connected
        '''
        return self.find(x) == self.find(y)
