public class Building {
	private int		vertexNum;
	private int[]	x;
	private int[]	y;

	public Building(int vertexNum, int[] ordinates) {
		super();
		this.vertexNum = vertexNum;
		this.x = new int[vertexNum];
		this.y = new int[vertexNum];

		int j = -1;
		for (int i = 0; i < ordinates.length; i += 2) {
			j++;
			x[j] = ordinates[i];
		}
		
		j = -1;
		for (int i = 1; i < ordinates.length; i += 2) {
			j++;
			y[j] = ordinates[i];
		}
	}

	public String toString() {
		StringBuilder ordinates = new StringBuilder();
		for (int i = 0; i < this.x.length; i++) {
			ordinates.append(x[i] + "," + y[i] + ", ");
		}
		return vertexNum + ":" + ordinates.toString();
	}

	public int getVertexNum() {
		return vertexNum;
	}

	public int[] getX() {
		return x;
	}

	public int[] getY() {
		return y;
	}
}