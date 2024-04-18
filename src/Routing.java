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
            Wire wire = findPath(board, goals.get(i));
            wireList.add(wire);
            if (wire != null) board.placeWire(wire);
        }
        return wireList;  // replace this line with your code
    }

    public static Wire findPath(Board board, Endpoints goal) {
        pathCoord start = new pathCoord(goal.start);
        pathCoord end = new pathCoord(goal.end);
        PriorityQueue<pathCoord> bfs = new PriorityQueue<>((pathCoord c1, pathCoord c2) -> (c1.column - c2.column + c1.row - c2.column));
        bfs.add(start);
        HashMap<Coord, Boolean> visited = new HashMap<>();
        visited.put(start.coord, true);

        while (true) {
            if (bfs.isEmpty()) {
                System.out.println("queue empty!");
                return null;
            }

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
