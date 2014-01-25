Database Systems Course Project - Spatial Database Application
=========

An application that queries a spatial database with Graphical Query Language. The application allows the users to select spatial attributes for spatial queries (e.g. selection from a map).

#### Env:
1. OS: Windows 8.1
2. JDK: 1.7.0_40
3. Oracle: 12c
4. JDBC: 12c specific(download ojdbc7.jar from here: http://www.oracle.com/technetwork/database/features/jdbc/jdbc-drivers-12c-download-1958347.html)

#### Usage:
Compile: 

	javac -cp "ojdbc7.jar;." *.java

Populate data: 

	java -cp "ojdbc7.jar;." populate buildings.xy students.xy announcementSystems.xy

Run Application:

	java -cp "ojdbc7.jar;." hw2
