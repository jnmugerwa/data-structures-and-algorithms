class _TrieNode:
    """
    A node in a Trie.

    author: Joshua Nathan Mugerwa
    version: 5/24/20
    """

    def __init__(self):
        self.children = {}
        self.is_word = False


class Trie:
    """
    A Trie. Supports space-efficient storage of strings and searching of words/prefixes.

    author: Joshua Nathan Mugerwa
    version: 5/24/20
    """

    def __init__(self):
        """
        Initializes a the Trie by creating its root.
        """
        self.root = _TrieNode()
        self.size = 1  # Number of nodes in Trie

    def insert(self, word: str) -> None:
        """
        Insert a word into the Trie.
        :param word: The word
        :return: Boolean; if the insertion was successful or not
        """
        self._validate_input(word)
        curr = self.root
        for letter in word:
            if letter not in curr.children:
                curr.children[letter] = _TrieNode()
                self.size += 1
            curr = curr.children[letter]
        curr.is_word = True

    def clear(self) -> None:
        """
        Clears the Trie of all nodes (except a new root).
        """
        self.root = _TrieNode()
        self.size = 1

    def find(self, word: str) -> bool:
        """
        Searches for a word in the Trie.
        :param word: A word
        :return: Boolean; if the word exists in the Trie or not
        """
        self._validate_input(word)
        curr = self.root
        for letter in word:
            if letter not in curr.children:
                return False
            curr = curr.children[letter]
        return curr.is_word

    def is_prefix(self, prefix: str) -> bool:
        """
        Searches for a prefix in the Trie.
        :param prefix: A word
        :return: Boolean; if the prefix exists in the Trie or not
        """
        self._validate_input(prefix)
        curr = self.root
        for letter in prefix:
            if letter not in curr.children:
                return False
            curr = curr.children[letter]
        return True

    def _validate_input(self, input: object):
        """
        Simple helper to validate input for various methods.
        :param input: The value passed into the method
        """
        if not isinstance(input, str):
            raise ValueError("ERROR: Passed in a non-string.")

