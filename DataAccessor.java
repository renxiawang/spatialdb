import java.util.ArrayList;

import oracle.jdbc.OracleResultSet;

public class DataAccessor {

	public ArrayList<Student> getStudents(String query) {
		DatabaseHelper dbHelper = new DatabaseHelper();
		ArrayList<Student> students = new ArrayList<Student>();

		dbHelper.connect();
		OracleResultSet resultSet = dbHelper.query(query);

		try {
			while (resultSet.next()) {
				int x = resultSet.getInt("POSITION.SDO_POINT.X");
				int y = resultSet.getInt("POSITION.SDO_POINT.y");

				Student student = new Student(x, y);
				students.add(student);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Getting Students Error: " + e.toString());
		}

		dbHelper.disconnect();

		return students;
	}

	public ArrayList<Building> getBuildings(String query) {
		DatabaseHelper dbHelper = new DatabaseHelper();
		ArrayList<Building> buildings = new ArrayList<Building>();

		dbHelper.connect();
		OracleResultSet resultSet = dbHelper.query(query);

		try {
			while (resultSet.next()) {
				int vertexNum = resultSet.getInt("VERTEX_NUM");
				int[] ordinates = resultSet.getARRAY("AREA.SDO_ORDINATES")
						.getIntArray();

				Building building = new Building(vertexNum, ordinates);
				buildings.add(building);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Getting Buildings Error: " + e.toString());
		}

		dbHelper.disconnect();

		return buildings;
	}

	public ArrayList<AnnouncementSys> getAnnouncementSyss(String query) {
		DatabaseHelper dbHelper = new DatabaseHelper();
		ArrayList<AnnouncementSys> announcementSyss = new ArrayList<AnnouncementSys>();

		dbHelper.connect();
		OracleResultSet resultSet = dbHelper.query(query);

		try {
			while (resultSet.next()) {
				String id = resultSet.getString("ID");
				int radius = resultSet.getInt("RADIUS");
				int x = resultSet.getInt("CENTER.SDO_POINT.X");
				int y = resultSet.getInt("CENTER.SDO_POINT.Y");

				AnnouncementSys announcementSys = new AnnouncementSys(id,
						radius, x, y);
				announcementSyss.add(announcementSys);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Getting AnnouncementSyss Error: "
					+ e.toString());
		}

		dbHelper.disconnect();

		return announcementSyss;
	}
}
