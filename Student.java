public class Student {
	private int	x;
	private int	y;

	public Student(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public String toString() {
		return x + "," + y + ",";
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}