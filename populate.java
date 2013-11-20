import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class populate {
	public static void main(String args[]) {
		if (args.length == 3) {
			insertBuildings(args[0]);
			insertStudents(args[1]);
			insertAnnounceSys(args[2]);
		} else {
			System.out.println("Missing arguments.");
		}
	}

	private static void insertBuildings(String filename) {
		ArrayList<String> records = readFile(filename);
		DatabaseHelper dbHelper = new DatabaseHelper();

		dbHelper.connect();
		dbHelper.clean("BUILDING");

		for (Iterator<String> iterator = records.iterator(); iterator.hasNext();) {
			String record = (String) iterator.next();
			String elements[] = record.split(",", 4);
			String query = "INSERT INTO BUILDING VALUES ('"
					+ elements[0]
					+ "','"
					+ elements[1]
					+ "',"
					+ elements[2]
					+ ", "
					+ "SDO_GEOMETRY(2003,NULL,NULL,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,1),"
					+ "MDSYS.SDO_ORDINATE_ARRAY(" + elements[3] + ")))";
			dbHelper.insert(query);
		}

		dbHelper.disconnect();
	}

	private static void insertStudents(String filename) {
		ArrayList<String> records = readFile(filename);
		DatabaseHelper dbHelper = new DatabaseHelper();

		dbHelper.connect();
		dbHelper.clean("STUDENT");

		for (Iterator<String> iterator = records.iterator(); iterator.hasNext();) {
			String record = (String) iterator.next();
			String elements[] = record.split(",", 2);
			String query = "INSERT INTO STUDENT VALUES ('" + elements[0].trim()
					+ "', " + "SDO_GEOMETRY(2001,NULL,MDSYS.SDO_POINT_TYPE("
					+ elements[1].trim() + ",NULL),NULL,NULL))";
			dbHelper.insert(query);
		}

		dbHelper.disconnect();
	}

	private static void insertAnnounceSys(String filename) {
		ArrayList<String> records = readFile(filename);
		DatabaseHelper dbHelper = new DatabaseHelper();

		dbHelper.connect();
		dbHelper.clean("ANNOUNCE_SYS");

		for (Iterator<String> iterator = records.iterator(); iterator.hasNext();) {
			String record = (String) iterator.next();
			String elements[] = record.split(",");
			StringBuilder threePoints = new StringBuilder();
			threePoints.append(""
					+ (Integer.parseInt(elements[1].trim()) + Integer
							.parseInt(elements[3].trim())) + ","
					+ elements[2].trim());
			threePoints.append(","
					+ (Integer.parseInt(elements[1].trim()) - Integer
							.parseInt(elements[3].trim())) + ","
					+ elements[2].trim());
			threePoints.append(","
					+ Integer.parseInt(elements[1].trim())
					+ ","
					+ (Integer.parseInt(elements[2].trim()) + Integer
							.parseInt(elements[3].trim())));

			String query = "INSERT INTO ANNOUNCE_SYS VALUES ('"
					+ elements[0].trim()
					+ "','"
					+ elements[3].trim()
					+ "', SDO_GEOMETRY(2001,NULL,MDSYS.SDO_POINT_TYPE("
					+ elements[1].trim()
					+ ","
					+ elements[2].trim()
					+ ",NULL),NULL,NULL)"
					+ ", SDO_GEOMETRY(2003,NULL,NULL,SDO_ELEM_INFO_ARRAY(1,1003,4), MDSYS.SDO_ORDINATE_ARRAY("
					+ threePoints.toString() + ")))";
			dbHelper.insert(query);
		}

		dbHelper.disconnect();
	}

	private static ArrayList<String> readFile(String filename) {
		try {
			File file = new File(filename);
			Scanner scanner = new Scanner(file);
			ArrayList<String> records = new ArrayList<String>();

			while (scanner.hasNext()) {
				records.add(scanner.nextLine());
			}

			scanner.close();

			return records;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return null;
	}
}
