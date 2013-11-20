public class AnnouncementSys {
	private String	id;
	private int		radius;
	private int		x;
	private int		y;

	public AnnouncementSys(String id, int radius, int x, int y) {
		super();
		this.id = id;
		this.radius = radius;
		this.x = x;
		this.y = y;
	}

	public String toString() {
		return id.hashCode() + "," + id + "," + radius + "," + x + "," + y
				+ ",";
	}

	public int hashCode() {
		return id.hashCode();
	}

	public boolean equals(Object o) {
		if (!(o instanceof AnnouncementSys)) {
			return false;
		}

		AnnouncementSys a = (AnnouncementSys) o;
		return a.id.hashCode() == id.hashCode();
	}

	public String getId() {
		return id;
	}

	public int getRadius() {
		return radius;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}