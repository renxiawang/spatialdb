import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

public class hw2 {

	private JFrame	frmRenxiaWang;
	private JLabel lblMap;
	private JPanel panel;
	private JPanel panel_1;
	private GroupLayout groupLayout;
	private GroupLayout gl_panel;
	private JLabel lblNewLabel;
	private JPanel panel_2;
	private JLabel lblQuery;
	private JPanel panel_3;
	private JButton btnSubmit;
	private GroupLayout gl_panel_1;
	private JRadioButton rdbtnWholeRegion;
	private JRadioButton rdbtnPointQuery;
	private JRadioButton rdbtnRangeQuery;
	private JRadioButton rdbtnSoundingStudents;
	private JRadioButton rdbtnEmegencyQuery;
	private GroupLayout gl_panel_3;
	private JCheckBox chckbxAS;
	private JCheckBox chckbxBuilding;
	private JCheckBox chckbxStudents;
	private GroupLayout gl_panel_2;
	private JTextArea txtrQuery;
	private JScrollPane scrollPane;
	private JTextField textField;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	
	// variables for queries
	private Point point = new Point();
	
	private ArrayList<Point> rangePoints = new ArrayList<Point>();
	
	private AnnouncementSys nearestAnnounceSys; 
	
	private int queryCount; 
	
	private DataAccessor dataAccessor = new DataAccessor();
	private QueryBuilder queryBuilder = new QueryBuilder();
	
	private boolean isReadyToSubmit = false;
	private boolean isMapClean = true;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					hw2 window = new hw2();
					window.frmRenxiaWang.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void updateQueryBox(String query) {
		String previous = txtrQuery.getText() + "\n";
		String now = "Query " + (++queryCount) + ":" + query;
		txtrQuery.setText(previous + now);
	}
	
	public void drawSelection(MouseEvent arg0) {
		if (isMapClean == false) {
			lblMap.repaint();
			isMapClean = true;
		} else if (rdbtnPointQuery.isSelected()
				&& arg0.getButton() == MouseEvent.BUTTON1) {
			drawPoint(arg0);
		} else if (rdbtnRangeQuery.isSelected()) {
			drawRange(arg0);
		} else if (rdbtnSoundingStudents.isSelected()
				&& arg0.getButton() == MouseEvent.BUTTON1) {
			drawNearestAnnounceSys(arg0);
		} else if (rdbtnEmegencyQuery.isSelected()
				&& arg0.getButton() == MouseEvent.BUTTON1) {
			drawNearestAnnounceSys(arg0);
		}
	}
	
	public void drawPoint(MouseEvent arg0) {
		if (isReadyToSubmit) {
			lblMap.repaint(0);
			isReadyToSubmit = false;
			isMapClean = true;
			point = new Point();
		} else {
			Graphics2D graphics = (Graphics2D) lblMap.getGraphics();
			graphics.setColor(Color.RED);
	
			point.setX(arg0.getX());
			point.setY(arg0.getY());
	
			Shape square = new Rectangle.Double(point.getX() - 2.5,
					point.getY() - 2.5, 5, 5);
			Shape circle = new Ellipse2D.Float(point.getX() - 50,
					point.getY() - 50, 100, 100);
			
			graphics.fill(square);
			graphics.draw(circle);
			graphics.dispose();
			
			isReadyToSubmit = true;
		}
	}
	
	public void drawRange(MouseEvent arg0) {
		Graphics2D graphics = (Graphics2D) lblMap.getGraphics();
		graphics.setColor(Color.RED);

		if (arg0.getButton() == MouseEvent.BUTTON1) {
			Point p = new Point(arg0.getX(), arg0.getY());
			rangePoints.add(p);

			if (rangePoints.size() > 1) {
				int len = rangePoints.size() - 1;
				graphics.drawLine(p.getX(), p.getY(), rangePoints.get(len - 1)
						.getX(), rangePoints.get(len - 1).getY());
			} else {
				graphics.drawLine(p.getX(), p.getY(), p.getX(), p.getY());
			}
		} else if (arg0.getButton() == MouseEvent.BUTTON3) {
			if (rangePoints.size() > 2) {
				int len = rangePoints.size() - 1;
				graphics.drawLine(rangePoints.get(len).getX(),
						rangePoints.get(len).getY(), rangePoints.get(0).getX(),
						rangePoints.get(0).getY());
				isReadyToSubmit = true;
			}
		}
	}
	
	public void drawNearestAnnounceSys(MouseEvent arg0) {
		if (isReadyToSubmit) {
			lblMap.repaint(0);
			isReadyToSubmit = false;
			isMapClean = true;
			nearestAnnounceSys = null;
		} else {
			int x = arg0.getX();
			int y = arg0.getY();
	
			String query = queryBuilder.getNearestAnnounceSysQuery(x, y);
			ArrayList<AnnouncementSys> announcementSyss = dataAccessor
					.getAnnouncementSyss(query);
			nearestAnnounceSys = announcementSyss.get(0);
			updateQueryBox(query);
			drawAnnouncementSyss(announcementSyss, Color.RED, false);
			isReadyToSubmit = true;
		}
	}
	
	public ArrayList<Color> generateColors() {
		ArrayList<Color> colors = new ArrayList<Color>();
		for (int i = 0; i < 100; i++) {
			Color c = new Color((int) (Math.random() * 0xFFFFFF));
			if (!colors.contains(c)) {
				colors.add(c);
			}
		}
		return colors;
	}
	
	public void drawNearestAnnounceSysAndStudents(ArrayList<Student> students) {
		ArrayList<Color> uniqueColors = generateColors();
		final String id = nearestAnnounceSys.getId();

		HashMap<AnnouncementSys, ArrayList<Student>> map = new HashMap<>();

		for (int i = 0; i < students.size(); i++) {
			Student student = students.get(i);

			String query = queryBuilder.getSecondNearestAnnounceSysQuery(id,
					student.getX(), student.getY());
			updateQueryBox(query);
			AnnouncementSys announcementSys = dataAccessor.getAnnouncementSyss(
					query).get(0);

			if (map.containsKey(announcementSys)) {
				ArrayList<Student> students2 = map.get(announcementSys);
				students2.add(student);
				map.put(announcementSys, students2);
			} else {
				ArrayList<Student> students2 = new ArrayList<>();
				students2.add(student);
				map.put(announcementSys, students2);
			}
		}

		int colorIndex = 0;
		for (Entry<AnnouncementSys, ArrayList<Student>> entry : map.entrySet()) {
			AnnouncementSys key = entry.getKey();
			ArrayList<Student> value = entry.getValue();

			ArrayList<AnnouncementSys> temp = new ArrayList<>();
			temp.add(key);
			drawAnnouncementSyss(temp, uniqueColors.get(colorIndex), false);
			drawStudents(value, uniqueColors.get(colorIndex), false);

			colorIndex++;
		}
	}
	
	
	public int[] getActiveFeatures() {
		int[] features = { 0, 0, 0 }; // building, student, announcementSys

		if (chckbxBuilding.isSelected()) {
			features[0] = 1;
		}

		if (chckbxStudents.isSelected()) {
			features[1] = 1;
		}

		if (chckbxAS.isSelected()) {
			features[2] = 1;
		}

		return features;
	}
	
	public void submitQuery() {
		int[] features = getActiveFeatures();

		if (rdbtnWholeRegion.isSelected()) {
			wholeRange(features);
			isMapClean = false;
		} else if (rdbtnPointQuery.isSelected() && isReadyToSubmit) {
			pointQuery(features);
			isMapClean = false;
		} else if (rdbtnRangeQuery.isSelected() && isReadyToSubmit) {
			rangeQuery(features);
			isMapClean = false;
		} else if (rdbtnSoundingStudents.isSelected() && isReadyToSubmit) {
			surroundingQuery(features);
			isMapClean = false;
		} else if (rdbtnEmegencyQuery.isSelected() && isReadyToSubmit) {
			emergencyQuery(features);
			isMapClean = false;
		}
		isReadyToSubmit = false;
	}
	
	public void wholeRange(int[] features) {
		if (features[0] == 1) {
			String query = queryBuilder.getAllBuildingsQuery();
			updateQueryBox(query);
			drawBuildings(dataAccessor.getBuildings(query), Color.YELLOW, false);
		}
		if (features[1] == 1) {
			String query = queryBuilder.getAllStudentsQuery();
			updateQueryBox(query);
			drawStudents(dataAccessor.getStudents(query), Color.GREEN, false);
		}
		if (features[2] == 1) {
			String query = queryBuilder.getAllAnnounceSysQuery();
			updateQueryBox(query);
			drawAnnouncementSyss(dataAccessor.getAnnouncementSyss(query),
					Color.RED, false);
		}
	}
	
	public void pointQuery(int[] features) {
		StringBuilder threePoints = new StringBuilder();
		threePoints.append("" + (point.getX() + 50) + "," + point.getY());
		threePoints.append("," + (point.getX() - 50) + "," + point.getY());
		threePoints.append("," + point.getX() + "," + (point.getY() + 50));

		if (features[0] == 1) {
			String query = queryBuilder.getPointBUildingsOrderByDisQuery(
					threePoints.toString(), point.getX(), point.getY());
			updateQueryBox(query);
			drawBuildings(dataAccessor.getBuildings(query), Color.GREEN, true);
		}

		if (features[1] == 1) {
			String query = queryBuilder.getPointStudentsOrderByDisQuery(
					threePoints.toString(), point.getX(), point.getY());
			updateQueryBox(query);
			drawStudents(dataAccessor.getStudents(query), Color.GREEN, true);
		}

		if (features[2] == 1) {
			String query = queryBuilder.getPointAnnounceSysOrderByDisQuery(
					threePoints.toString(), point.getX(), point.getY());
			updateQueryBox(query);
			drawAnnouncementSyss(dataAccessor.getAnnouncementSyss(query),
					Color.GREEN, true);
		}
	}
	
	public void rangeQuery(int[] features) {
		StringBuilder ordinates = new StringBuilder();
		for (Iterator<Point> iterator = rangePoints.iterator(); iterator
				.hasNext();) {
			Point point = (Point) iterator.next();
			ordinates.append(point.toString());
		}
		ordinates.append(rangePoints.get(0).getX());
		ordinates.append(",");
		ordinates.append(rangePoints.get(0).getY());

		// System.out.println(ordinates.toString());

		if (features[0] == 1) {
			String query = queryBuilder.getRangeBuildingQuery(ordinates
					.toString());
			updateQueryBox(query);
			drawBuildings(dataAccessor.getBuildings(query), Color.YELLOW, false);
		}

		if (features[1] == 1) {
			String query = queryBuilder.getRangeStudentsQuery(ordinates
					.toString());
			updateQueryBox(query);
			drawStudents(dataAccessor.getStudents(query), Color.GREEN, false);
		}

		if (features[2] == 1) {
			String query = queryBuilder.getRangeAnnounceSysQuery(ordinates
					.toString());
			updateQueryBox(query);
			drawAnnouncementSyss(dataAccessor.getAnnouncementSyss(query),
					Color.RED, false);
		}
		rangePoints.clear();
	}
	
	public void surroundingQuery(int[] features) {
		StringBuilder threePoints = new StringBuilder();
		threePoints.append(""
				+ (nearestAnnounceSys.getX() + nearestAnnounceSys.getRadius())
				+ "," + nearestAnnounceSys.getY());
		threePoints.append(","
				+ (nearestAnnounceSys.getX() - nearestAnnounceSys.getRadius())
				+ "," + nearestAnnounceSys.getY());
		threePoints.append("," + nearestAnnounceSys.getX() + ","
				+ (nearestAnnounceSys.getY() + nearestAnnounceSys.getRadius()));

		String query = queryBuilder.getPointStudentsQuery(threePoints
				.toString());
		updateQueryBox(query);
		drawStudents(dataAccessor.getStudents(query), Color.GREEN, false);
	}

	public void emergencyQuery(int[] features) {
		StringBuilder threePoints = new StringBuilder();
		threePoints.append(""
				+ (nearestAnnounceSys.getX() + nearestAnnounceSys.getRadius())
				+ "," + nearestAnnounceSys.getY());
		threePoints.append(","
				+ (nearestAnnounceSys.getX() - nearestAnnounceSys.getRadius())
				+ "," + nearestAnnounceSys.getY());
		threePoints.append("," + nearestAnnounceSys.getX() + ","
				+ (nearestAnnounceSys.getY() + nearestAnnounceSys.getRadius()));

		String query = queryBuilder.getPointStudentsQuery(threePoints
				.toString());
		updateQueryBox(query);

		ArrayList<Student> students = dataAccessor.getStudents(query);

		drawNearestAnnounceSysAndStudents(students);
	}
	
	public void drawBuildings(ArrayList<Building> buildings, Color color,
			boolean isPointQuery) {
		for (Iterator<Building> iterator = buildings.iterator(); iterator
				.hasNext();) {
			Building building = (Building) iterator.next();

			if (isPointQuery && building == buildings.get(0)) {
				Graphics2D graphics = (Graphics2D) lblMap.getGraphics();
				graphics.setColor(Color.YELLOW);
				graphics.drawPolygon(buildings.get(0).getX(), buildings.get(0)
						.getY(), buildings.get(0).getVertexNum());
				graphics.dispose();

				isPointQuery = false;

				continue;
			}

			Graphics2D graphics = (Graphics2D) lblMap.getGraphics();
			graphics.setColor(color);
			graphics.drawPolygon(building.getX(), building.getY(),
					building.getVertexNum());
			graphics.dispose();
		}
	}
	
	public void drawStudents(ArrayList<Student> students, Color color,
			boolean isPointQuery) {
		for (Iterator<Student> iterator = students.iterator(); iterator
				.hasNext();) {
			Student student = (Student) iterator.next();

			if (isPointQuery && student == students.get(0)) {
				Graphics2D graphics = (Graphics2D) lblMap.getGraphics();
				graphics.setColor(Color.YELLOW);
				graphics.fillRect(student.getX() - 5, student.getY() - 5, 10,
						10);
				graphics.dispose();

				isPointQuery = false;

				continue;
			}

			Graphics2D graphics = (Graphics2D) lblMap.getGraphics();
			graphics.setColor(color);
			graphics.fillRect(student.getX() - 5, student.getY() - 5, 10, 10);
			graphics.dispose();
		}
	}
	
	public void drawAnnouncementSyss(
			ArrayList<AnnouncementSys> announcementSyss, Color color,
			boolean isPointQuery) {
		for (Iterator<AnnouncementSys> iterator = announcementSyss.iterator(); iterator
				.hasNext();) {
			AnnouncementSys announcementSys = (AnnouncementSys) iterator.next();

			if (isPointQuery && announcementSys == announcementSyss.get(0)) {
				Graphics2D graphics = (Graphics2D) lblMap.getGraphics();
				graphics.setColor(Color.YELLOW);

				Shape square = new Rectangle.Double(
						announcementSys.getX() - 7.5,
						announcementSys.getY() - 7.5, 15, 15);
				Shape circle = new Ellipse2D.Float(announcementSys.getX()
						- announcementSys.getRadius(), announcementSys.getY()
						- announcementSys.getRadius(),
						announcementSys.getRadius() * 2,
						announcementSys.getRadius() * 2);
				graphics.fill(square);
				graphics.draw(circle);
				graphics.dispose();

				isPointQuery = false;

				continue;
			}

			Graphics2D graphics = (Graphics2D) lblMap.getGraphics();
			graphics.setColor(color);

			Shape square = new Rectangle.Double(announcementSys.getX() - 7.5,
					announcementSys.getY() - 7.5, 15, 15);
			Shape circle = new Ellipse2D.Float(announcementSys.getX()
					- announcementSys.getRadius(), announcementSys.getY()
					- announcementSys.getRadius(),
					announcementSys.getRadius() * 2,
					announcementSys.getRadius() * 2);
			graphics.fill(square);
			// graphics.draw(square);
			graphics.draw(circle);
			graphics.dispose();
		}
	}
	
	/**
	 * Create the application.
	 */
	public hw2() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmRenxiaWang = new JFrame();
		frmRenxiaWang.setTitle("Renxia Wang 5312373736");
		frmRenxiaWang.setResizable(false);
		frmRenxiaWang.setBounds(100, 100, 1162, 714);
		frmRenxiaWang.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new JPanel();
		
		panel_1 = new JPanel();
		
		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		groupLayout = new GroupLayout(frmRenxiaWang.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 1132, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(panel, GroupLayout.PREFERRED_SIZE, 841, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 284, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 604, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 616, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
					.addGap(14))
		);
		
		txtrQuery = new JTextArea();
		txtrQuery.setLineWrap(true);
		txtrQuery.setTabSize(4);
		scrollPane.setViewportView(txtrQuery);
		
		
		lblMap = new JLabel();
		lblMap.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent arg0) {
				textField.setText(arg0.getX() + "," + arg0.getY());
			}
		});
		lblMap.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				drawSelection(arg0);
			}
		});
		lblMap.setIcon(new ImageIcon("map.jpg"));
		gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(lblMap, GroupLayout.PREFERRED_SIZE, 820, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblMap, GroupLayout.PREFERRED_SIZE, 580, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		
		lblNewLabel = new JLabel("Active Feature Type");
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 20));
		
		panel_2 = new JPanel();
		
		lblQuery = new JLabel("Query");
		lblQuery.setFont(new Font("Arial", Font.BOLD, 20));
		
		panel_3 = new JPanel();
		
		btnSubmit = new JButton("Submit Query");
		btnSubmit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				submitQuery();
			}
		});
		btnSubmit.setFont(new Font("Tahoma", Font.BOLD, 15));
		
		textField = new JTextField();
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setFont(new Font("Tahoma", Font.BOLD, 16));
		textField.setEditable(false);
		textField.setColumns(10);
		
		JTextArea txtrClickOn = new JTextArea();
		txtrClickOn.setLineWrap(true);
		txtrClickOn.setText("* Click on the map to draw selection, click agian to clean the map ");
		gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(lblQuery, Alignment.LEADING)
								.addComponent(panel_2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(panel_3, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnSubmit, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
								.addComponent(lblNewLabel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING, gl_panel_1.createSequentialGroup()
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(50))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(txtrClickOn, GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
							.addContainerGap())))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(17)
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblQuery)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(8)
					.addComponent(txtrClickOn, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
					.addComponent(btnSubmit, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		
		rdbtnWholeRegion = new JRadioButton("Whole Region");
		rdbtnWholeRegion.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lblMap.repaint();
				rangePoints.clear();
				isMapClean = true;
			}
		});
		buttonGroup.add(rdbtnWholeRegion);
		rdbtnWholeRegion.setFont(new Font("Arial", Font.BOLD, 18));
		
		rdbtnPointQuery = new JRadioButton("Point Query*");
		rdbtnPointQuery.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lblMap.repaint();
				rangePoints.clear();
				isMapClean = true;
			}
		});
		buttonGroup.add(rdbtnPointQuery);
		rdbtnPointQuery.setFont(new Font("Arial", Font.BOLD, 18));
		
		rdbtnRangeQuery = new JRadioButton("Range Query");
		rdbtnRangeQuery.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lblMap.repaint();
				rangePoints.clear();
				isMapClean = true;
			}
		});
		buttonGroup.add(rdbtnRangeQuery);
		rdbtnRangeQuery.setFont(new Font("Arial", Font.BOLD, 18));
		
		rdbtnSoundingStudents = new JRadioButton("Surrounding Students*");
		rdbtnSoundingStudents.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lblMap.repaint();
				rangePoints.clear();
				isMapClean = true;
			}
		});
		buttonGroup.add(rdbtnSoundingStudents);
		rdbtnSoundingStudents.setFont(new Font("Arial", Font.BOLD, 18));
		
		rdbtnEmegencyQuery = new JRadioButton("Emergency Query*");
		rdbtnEmegencyQuery.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lblMap.repaint();
				rangePoints.clear();
				isMapClean = true;
			}
		});
		buttonGroup.add(rdbtnEmegencyQuery);
		rdbtnEmegencyQuery.setFont(new Font("Arial", Font.BOLD, 18));
		gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_3.createSequentialGroup()
							.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
								.addComponent(rdbtnEmegencyQuery)
								.addComponent(rdbtnSoundingStudents))
							.addContainerGap(35, Short.MAX_VALUE))
						.addGroup(gl_panel_3.createSequentialGroup()
							.addComponent(rdbtnRangeQuery, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGap(71))
						.addGroup(gl_panel_3.createSequentialGroup()
							.addComponent(rdbtnPointQuery)
							.addContainerGap(85, Short.MAX_VALUE))
						.addGroup(gl_panel_3.createSequentialGroup()
							.addComponent(rdbtnWholeRegion)
							.addContainerGap(71, Short.MAX_VALUE))))
		);
		gl_panel_3.setVerticalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addGap(8)
					.addComponent(rdbtnWholeRegion)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(rdbtnPointQuery)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(rdbtnRangeQuery)
					.addPreferredGap(ComponentPlacement.RELATED, 6, Short.MAX_VALUE)
					.addComponent(rdbtnSoundingStudents)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(rdbtnEmegencyQuery)
					.addContainerGap())
		);
		panel_3.setLayout(gl_panel_3);
		
		chckbxAS = new JCheckBox("AS");
		chckbxAS.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lblMap.repaint();
				rangePoints.clear();
				isMapClean = true;
			}
		});
		chckbxAS.setFont(new Font("Arial", Font.BOLD, 18));
		
		chckbxBuilding = new JCheckBox("Buildings");
		chckbxBuilding.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lblMap.repaint();
				rangePoints.clear();
				isMapClean = true;
			}
		});
		chckbxBuilding.setFont(new Font("Arial", Font.BOLD, 18));
		
		chckbxStudents = new JCheckBox("Students");
		chckbxStudents.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lblMap.repaint();
				rangePoints.clear();
				isMapClean = true;
			}
		});
		chckbxStudents.setFont(new Font("Arial", Font.BOLD, 18));
		gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addGap(8)
							.addComponent(chckbxAS, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_2.createSequentialGroup()
							.addContainerGap()
							.addComponent(chckbxStudents))
						.addGroup(gl_panel_2.createSequentialGroup()
							.addContainerGap()
							.addComponent(chckbxBuilding, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(80, Short.MAX_VALUE))
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGap(7)
					.addComponent(chckbxAS)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chckbxBuilding)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chckbxStudents)
					.addGap(17))
		);
		panel_2.setLayout(gl_panel_2);
		panel_1.setLayout(gl_panel_1);
		frmRenxiaWang.getContentPane().setLayout(groupLayout);
	}
}

