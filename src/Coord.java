public class Coord {
	public final int x;
	public final int y;

	public Coord(final int arg_x, final int arg_y) {
		this.x = arg_x;
		this.y = arg_y;
	}

	public static int compare(Coord a, Coord b) {
		if (a.x < b.x) {
			return -1;
		} else if (a.x == b.x) {
			return Integer.compare(a.y, b.y);
		} else {
			return 1;
		}
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Coord)) {
			return false;
		} else {
			Coord other = (Coord) obj;
			return this.x == other.x && this.y == other.y;
		}
	}

	public boolean isAdjacent(Coord other) {
		return this.x == other.x && this.y == (other.y + 1) ||
			   this.x == other.x && this.y == (other.y - 1) ||
			   this.x == (other.x + 1) && this.y == other.y ||
			   this.x == (other.x - 1) && this.y == other.y;
	}

	public int hashCode() {
		String repr = String.valueOf(this.x) + " " + String.valueOf(this.y);
		return repr.hashCode();
	}

	public String toString() {
		return "(" + String.valueOf(this.x) + ", " + String.valueOf(this.y) + ")";
	}
}
