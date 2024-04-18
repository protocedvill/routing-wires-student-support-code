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
        Queue<Endpoints> goalQueue = new ArrayDeque<>();
        goalQueue.addAll(goals);

        ArrayList<Wire> wireList = new ArrayList<>();
        PriorityQueue<Wire> wirePriorityQueue = new PriorityQueue<>((Wire w1, Wire w2) -> (w1.length() < w2.length() ? 1 : -1));
        while (!goalQueue.isEmpty()) {
            Endpoints goal = goalQueue.remove();
            Wire wire = findPath(board, goal);

            ArrayList<Wire> removedWireList = new ArrayList<>();
            while(!wirePriorityQueue.isEmpty() && wire == null) {
                Wire longestWire = wirePriorityQueue.remove();
                removedWireList.add(longestWire);
                board.removeWire(longestWire);
                wire = findPath(board, goal);
            }

            if (wire == null) {
                for (Wire w : removedWireList) {
                    board.placeWire(w);
                    wirePriorityQueue.add(w);
                }
            }

            if (wire != null) {
                wirePriorityQueue.add(wire);
                board.placeWire(wire);
                for (Wire w : removedWireList) {
                    wire = findPath(board, new Endpoints(w.id, w.start(),w.end()));
                    if (wire != null) {
                        wirePriorityQueue.add(wire);
                        board.placeWire(wire);
                    }
                }
            }

        }
        wireList.addAll(wirePriorityQueue);
        return wireList;
    }

    public static Wire findPath(Board board, Endpoints goal) {
        pathCoord start = new pathCoord(goal.start);
        pathCoord end = new pathCoord(goal.end);
        PriorityQueue<pathCoord> bfs = new PriorityQueue<>((pathCoord c1, pathCoord c2) -> (
                Math.abs(end.column - c1.column) + Math.abs(end.row - c1.row) > Math.abs(end.column - c2.column) + Math.abs(end.row - c2.row) ? 1 : -1));
        bfs.add(start);
        HashMap<Coord, Boolean> visited = new HashMap<>();
        visited.put(start.coord, true);

        while (true) {
            if (bfs.isEmpty()) return null;

            pathCoord parent = bfs.remove();
            ArrayList<Coord> valid = validAdjacent(board, parent.coord, goal.end, visited);
            for (Coord c : valid) {
                if (c.equals(end)) return pathCoordToWire(new pathCoord(c, parent), goal.id);
                bfs.add(new pathCoord(c, parent));
            }
        }
    }

    public static Wire pathCoordToWire(pathCoord pcoord, int id) {
        Wire wire = new Wire(id);
        ArrayList<Coord> reverse = new ArrayList<>();
        while (pcoord != null) {
            reverse.add(pcoord.coord);
            pcoord = pcoord.prev;
        }
        Collections.reverse(reverse);
        for (Coord i : reverse) wire.add(i);
        return wire;
    }

    public static ArrayList<Coord> validAdjacent (Board board, Coord coord, Coord goal, HashMap<Coord, Boolean> visited) {
        ArrayList<Coord> x = new ArrayList<>();
        for (Coord i : board.adj(coord)) {
            if (!board.isObstacle(i) && !board.isOccupied(i) && !visited.containsKey(i) || i.equals(goal)) {
                visited.put(i, true);
                x.add(i);
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
