DROP TABLE BUILDING;
DROP TABLE STUDENT;
DROP TABLE ANNOUNCE_SYS;
DELETE FROM USER_SDO_GEOM_METADATA 
WHERE TABLE_NAME = 'ANNOUNCE_SYS' 
	OR TABLE_NAME = 'STUDENT' 
	OR TABLE_NAME = 'BUILDING';
COMMIT;