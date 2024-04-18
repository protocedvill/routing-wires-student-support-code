Routing Wires Louison Savarese

1.
    The algorithm uses three different techniques.

    To store the path of each wire, a subclass of Coord is used, which keeps track of the previous coordinate of each Coordinate.
    When the ending is found, to generate the wire is as simple as traversing backwards through the coordinates.

    In order to find a wire between two end points, it uses a priority queue in order to determine which edges should be checked next.
    The priority is determined by a lambda function which checks the distance of the two coordinates from the endpoint.
    The coordinate with the lowest distance to the endpoint will always be checked next.

    The algorithm will generate wires using this, between all the different coordinate sets.
    If there is an end-point that can't be reached, the wire with the longest length will temporarily be removed, and connecting the wire will be retried.
    If this fails, then the next longest wire will be retried, and so on until there are no more wires to remove.
    If this happens, we can assume it is because of an obstruction, and we put all the other wires back where they were.
    If a wire is successfully removed and a new wire installed, then we will attempt to regenerate paths for all the other wires as well.

    The longest wire, which can span across the entire board, tends to cause the most issues with cutting in half shorter wires spans.
    example:
        1 0 0 0 0
        1 1 0 2 0
        0 1 1 0 0
        0 2 1 1 0
        0 0 0 1 1

    I target this problem by removing the longest wires first, when a wire can't be connected.
    If it works then we regenerate the longer wire, avoiding the new wire.
    fix:
       1 0 0 0 0
       1 0 0 2 0
       1 0 2 0 0
       1 2 0 0 0
       1 1 1 1 1

2.
    see example above.

    This board has a mix of long and short conflicting paths if BFS was used.
    |  1   1   1   1   5   5   2 |
    |  0   3   0   1   1   5   2 |
    |  0   3   0   0   1   5   2 |
    |  0   3   4   0   1   1   2 |
    |  0   0   4   0   0   1   2 |
    |  0   0   4   4   4   1   2 |
    |  2   2   2   2   2   2   2 |

    This board has a mix of long and short and blockers.
    |  1   1   1   1   1   1   2 |
    |  3   3   5   5   5   1   2 |
    |  3   5   5  -1   5   1   2 |
    |  3   3   4  -1  -1   1   2 |
    |  0   0   4  -1  -1   1   2 |
    |  0   0   4   4   4   1   2 |
    |  2   2   2   2   2   2   2 |

3.
    This algorithm tends to use as little wire as possible. Because it generates wires based on distance,
    only any extremely maze-like block would cause it to waste wire, where it might get tricked into following a path towards the goal,
    but then have to backtrack once it realized it can't actually reach the goal.

    A more complex algorithm could realize it's backtracking and as such create shortcuts in the path back, using some sort of DFS.

4.
    The time complexity of this algorithm is okay. Best case of generating a wire is based on the distance between the goals.
    Worst case it must evaluate the entire board and then decide it is impossible to reach the goal. Therefore generating a wire (findPath) is O(N) where N is board area.

    Finding several wires tends to get complicated. Worst case, a significant amount of wires have no possible connection, and must delete the entire queue and search the entire board multiple times.
    findPaths O(N^k) where k is number of wires, n is board size.

    Wall clock analysis paints a better picture of real world performance, as rarely are we dealing with worst case scenarios.

    An example of a best case scenario, where b-lining directly for the goal is ideal is as follows:
        30000
        30000
        0
        2
        0 0 100 100
        1 1 29999 29999
    the program finishes in 1664 milliseconds. If you want to recreate this test, you may need to increase the java heap space.

    A bad case scenario, where the longest wires need to be rerouted multiple times, is as follows:
        1000
        1000
        0
        3
        0 0 999 999
        1 0 1 998
        3 1 3 999
    the program finished in 1660 milliseconds, however the board size is about 30 times smaller.

5.
    This code passes all tests on auto-grader. It also is extremely time efficient in most scenarios, and is unlikely to waste cable.
    The program can handle most situations when the wires get in it's own way.