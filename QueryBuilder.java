
public class QueryBuilder {
	
	public String getNearestAnnounceSysQuery(int x, int y) {
		return "SELECT A.ID, A.RADIUS, A.CENTER.SDO_POINT.X, A.CENTER.SDO_POINT.Y " +
				"FROM ANNOUNCE_SYS A " +
				"WHERE SDO_NN(" +
					"A.AREA, " +
					"SDO_GEOMETRY(" +
					"2001," +
					"NULL," +
					"SDO_POINT_TYPE("+ x +", "+ y+", NULL)," +
					"NULL," +
					"NULL))='TRUE' " +
				"AND ROWNUM = 1";
	}
	
	public String getSecondNearestAnnounceSysQuery(String nearestId, int x, int y) {
		return "SELECT A.ID, A.RADIUS, A.CENTER.SDO_POINT.X, A.CENTER.SDO_POINT.Y " +
				"FROM ANNOUNCE_SYS A " +
				"WHERE A.ID != '" + nearestId + "' " +
				"AND SDO_NN(" +
					"A.AREA, " +
					"SDO_GEOMETRY(" +
					"2001," +
					"NULL," +
					"SDO_POINT_TYPE("+ x +", "+ y +", NULL)," +
					"NULL," +
					"NULL))='TRUE' " +
				"AND ROWNUM = 1";
	}
	
	public String getAllBuildingsQuery() {
		return "SELECT B.VERTEX_NUM,B.AREA.SDO_ORDINATES FROM BUILDING B";
	}
	
	public String getPointBuildingsQuery(String threePoints) {
		return "SELECT B.VERTEX_NUM,B.AREA.SDO_ORDINATES " +
				"FROM BUILDING B " +
				"WHERE SDO_ANYINTERACT(" +
				"B.AREA,SDO_GEOMETRY(" +
					"2003," +
					"NULL," +
					"NULL," +
					"SDO_ELEM_INFO_ARRAY(1,1003,4)," +
					"SDO_ORDINATE_ARRAY("+ threePoints +")))='TRUE'";
	}
	
	public String getPointBUildingsOrderByDisQuery(String threePoints, int x, int y) {
		return "SELECT /*+ INDEX(B BUILDING_AREA) */ B.VERTEX_NUM,B.AREA.SDO_ORDINATES,SDO_NN_DISTANCE(1) dist " +
				"FROM BUILDING B " +
				"WHERE SDO_ANYINTERACT(" +
				"B.AREA,SDO_GEOMETRY(" +
					"2003," +
					"NULL," +
					"NULL," +
					"SDO_ELEM_INFO_ARRAY(1,1003,4)," +
					"SDO_ORDINATE_ARRAY("+ threePoints +")))='TRUE' " + 
				"AND SDO_NN(" +
					"B.AREA, " +
					"SDO_GEOMETRY(" +
					"2001," +
					"NULL," +
					"SDO_POINT_TYPE("+ x +", "+ y +", NULL)," +
					"NULL," +
					"NULL), 1)='TRUE' " +
				"ORDER BY dist";
	}
	
	public String getRangeBuildingQuery(String ordinates) {
		return "SELECT B.VERTEX_NUM,B.AREA.SDO_ORDINATES " +
				"FROM BUILDING B " +
				"WHERE SDO_ANYINTERACT(" +
				"B.AREA,SDO_GEOMETRY(" +
					"2003," +
					"NULL," +
					"NULL," +
					"SDO_ELEM_INFO_ARRAY(1,1003,1)," +
					"SDO_ORDINATE_ARRAY("+ ordinates +")))='TRUE'";
	}
	
	public String getAllStudentsQuery() {
		return "SELECT S.POSITION.SDO_POINT.X, S.POSITION.SDO_POINT.Y FROM STUDENT S";
	}
	
	public String getPointStudentsQuery(String threePoints) {
		return "SELECT S.POSITION.SDO_POINT.X, S.POSITION.SDO_POINT.Y " +
				"FROM STUDENT S " +
				"WHERE SDO_ANYINTERACT(" +
				"S.POSITION,SDO_GEOMETRY(" +
					"2003," +
					"NULL," +
					"NULL," +
					"SDO_ELEM_INFO_ARRAY(1,1003,4)," +
					"SDO_ORDINATE_ARRAY("+ threePoints +")))='TRUE'";
	}
	
	public String getPointStudentsOrderByDisQuery(String threePoints, int x, int y) {
		return "SELECT /*+ INDEX(S STUDENT_POS) */ S.POSITION.SDO_POINT.X, S.POSITION.SDO_POINT.Y,SDO_NN_DISTANCE(1) dist " +
				"FROM STUDENT S " +
				"WHERE SDO_ANYINTERACT(" +
				"S.POSITION,SDO_GEOMETRY(" +
					"2003," +
					"NULL," +
					"NULL," +
					"SDO_ELEM_INFO_ARRAY(1,1003,4)," +
					"SDO_ORDINATE_ARRAY("+ threePoints +")))='TRUE' " + 
				"AND SDO_NN(" +
					"S.POSITION, " +
					"SDO_GEOMETRY(" +
					"2001," +
					"NULL," +
					"SDO_POINT_TYPE("+ x +", "+ y +", NULL)," +
					"NULL," +
					"NULL), 1)='TRUE' " +
				"ORDER BY dist";
	}
	
	public String getRangeStudentsQuery(String ordinates) {
		return "SELECT S.POSITION.SDO_POINT.X, S.POSITION.SDO_POINT.Y " +
				"FROM STUDENT S " +
				"WHERE SDO_ANYINTERACT(" +
				"S.POSITION,SDO_GEOMETRY(" +
					"2003," +
					"NULL," +
					"NULL," +
					"SDO_ELEM_INFO_ARRAY(1,1003,1)," +
					"SDO_ORDINATE_ARRAY("+ ordinates +")))='TRUE'";
	}
	
	public String getAllAnnounceSysQuery() {
		return "SELECT A.ID, A.RADIUS, A.CENTER.SDO_POINT.X, A.CENTER.SDO_POINT.Y FROM ANNOUNCE_SYS A";
	}
	
	public String getPointAnnounceSysQuery(String threePoints) {
		return "SELECT A.ID, A.RADIUS, A.CENTER.SDO_POINT.X, A.CENTER.SDO_POINT.Y " +
				"FROM ANNOUNCE_SYS A " +
				"WHERE SDO_ANYINTERACT(" +
				"A.AREA,SDO_GEOMETRY(" +
					"2003," +
					"NULL," +
					"NULL," +
					"SDO_ELEM_INFO_ARRAY(1,1003,4)," +
					"SDO_ORDINATE_ARRAY("+ threePoints +")))='TRUE'";
	}
	
	public String getPointAnnounceSysOrderByDisQuery(String threePoints, int x, int y) {
		return "SELECT /*+ INDEX(A ANNOUNCE_SYS_CENTER) */ A.ID, A.RADIUS, A.CENTER.SDO_POINT.X, A.CENTER.SDO_POINT.Y,SDO_NN_DISTANCE(1) dist " +
				"FROM ANNOUNCE_SYS A " +
				"WHERE SDO_ANYINTERACT(" +
				"A.AREA,SDO_GEOMETRY(" +
					"2003," +
					"NULL," +
					"NULL," +
					"SDO_ELEM_INFO_ARRAY(1,1003,4)," +
					"SDO_ORDINATE_ARRAY("+ threePoints +")))='TRUE' " + 
				"AND SDO_NN(" +
					"A.CENTER, " +
					"SDO_GEOMETRY(" +
					"2001," +
					"NULL," +
					"SDO_POINT_TYPE("+ x +", "+ y +", NULL)," +
					"NULL," +
					"NULL), 1)='TRUE' " +
				"ORDER BY dist";
	}
	
	public String getRangeAnnounceSysQuery(String ordinates) {
		return "SELECT A.ID, A.RADIUS, A.CENTER.SDO_POINT.X, A.CENTER.SDO_POINT.Y " +
				"FROM ANNOUNCE_SYS A " +
				"WHERE SDO_ANYINTERACT(" +
				"A.AREA,SDO_GEOMETRY(" +
					"2003," +
					"NULL," +
					"NULL," +
					"SDO_ELEM_INFO_ARRAY(1,1003,1)," +
					"SDO_ORDINATE_ARRAY("+ ordinates +")))='TRUE'";
	}
}
