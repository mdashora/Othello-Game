Follow the below instructions to run project.

1. Open the project in NetBeans 7.1 or higher.
2. Install Apache Tomcat 7.0.69 or higher in NetBeans.
3. Create Java Derby DB named othello with username/password othello using Netbeans javadb-Services. Place below configuration in Tomcat’s Server.Xml and start the java DB

        <Resource name="jdbc/othellodb" auth="Container"
          type="javax.sql.DataSource" username="othello" password="othello"
          driverClassName="org.apache.derby.jdbc.ClientDriver" 
          url="jdbc:derby://localhost:1527/othello"
          maxActive="50" maxIdle="50"/>

4. Create the Table Structure in database as mentioned in TableStructure.txt
5. Configure the SSL in Tomcat as described in Section 3.1.1 of Project Report
6. Build and deploy the application in NetBeans. Maven will download all required dependent libraries.
7. Run the Application on HTTPS and port 8443.
