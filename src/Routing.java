import java.util.*;

public class Routing {

    /**
     * TODO
     * <p>
     * The findPaths function takes a board and a list of goals that contain
     * endpoints that need to be connected. The function returns a list of
     * Paths that connect the points.
     */
    public static ArrayList<Wire>
    findPaths(Board board, ArrayList<Endpoints> goals) {
        ArrayList<Wire> wireList = new ArrayList<>();
        for (int i = 0; i < goals.size();i++) {
            wireList.add(findPath(board, goals.get(i)));
        }
        return wireList;  // replace this line with your code
    }

    public static Wire findPath(Board board, Endpoints goal) {
        ArrayList<Coord> path = new ArrayList<>();
        if (goal.start == goal.end) {
            path.add(goal.end);
            return new Wire(goal.id, path);
        }

        ArrayList<pathCoord> edges = new ArrayList<>();
        pathCoord start = new pathCoord(goal.start);
        pathCoord end = new pathCoord(goal.end);
        edges.add(start);
        Queue<pathCoord> bfs = new ArrayDeque<>();
        bfs.add(start);

        while (!edges.contains(end)) {
            if (edges.isEmpty()) {
                System.out.println("null");
                return null;
            }
            starOnce(board, edges, goal.end);
        }

        Wire wire = new Wire(goal.id);
        ArrayList<Coord> reverse = new ArrayList<>();
        pathCoord traverse = edges.get(edges.size() - 1);
        while(traverse != null) {
            reverse.add(traverse.coord);
            traverse = traverse.prev;
        }
        Collections.reverse(reverse);
        for (Coord i : reverse) {
            wire.add(i);
        }
        board.placeWire(wire);
        return wire;
    }

    public static void starOnce(Board board, ArrayList<pathCoord> edges, Coord goal) {
        ArrayList<pathCoord> removeable = new ArrayList<>();
        ArrayList<pathCoord> toEdge = new ArrayList<>();
        for (pathCoord i : edges) {
            ArrayList<Coord> validAdj = validAdjacent(board, i, edges, goal);
            if (validAdj.contains(goal)) {
                toEdge.add(new pathCoord(validAdj.get(0), i));
                break;
            }
            if (validAdj.isEmpty()) removeable.add(i);
            for (Coord x : validAdj) {
                toEdge.add(new pathCoord(x, i));
            }
        }
        System.out.println("Size of edge list is " + edges.size() + ", adding " + toEdge.size() + ", removing " + removeable.size());
        edges.addAll(toEdge);
        edges.removeAll(removeable);
    }

    public static ArrayList<Coord> validAdjacent (Board board, Coord coord, ArrayList<pathCoord> edges, Coord goal) {
        ArrayList<Coord> x = new ArrayList<>();
        for (Coord i : board.adj(coord)) {
            if (!board.isObstacle(i) && !board.isOccupied(i) && !edges.contains(i)) {
                //System.out.println(coord + " to " + i);
                x.add(i);
            }
            if (i.equals(goal)) {
                //System.out.println("Found goal: " + coord + " to " + i);
                x.clear();
                x.add(i);
                return x;
            }
        }
        return x;
    }

    static class pathCoord extends Coord {
        public pathCoord prev;
        public Coord coord;

        public pathCoord(Coord current) {
            super(current.row, current.column);
            prev = null;
            coord = current;
        }

        public pathCoord(Coord current, pathCoord previous) {
            super(current.row, current.column);
            prev = previous;
            coord = current;
        }
    }
}
