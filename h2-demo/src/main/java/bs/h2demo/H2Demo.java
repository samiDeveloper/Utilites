package bs.h2demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.h2.tools.Server;

public class H2Demo {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        
        // H2 start all servers by default, we only need tcp on a specific port
        Server server = Server.createTcpServer("-tcpDaemon", "-tcpPort", "8092").start();
        
        Class.forName("org.h2.Driver");
        
        // This creates the testdb
        Connection c = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
        
        PreparedStatement stm = c.prepareStatement("CREATE TABLE TEST1(ID INT PRIMARY KEY, NAME VARCHAR(255))");
        stm.executeUpdate();
        c.commit();
        
        c.close(); // Hit breakpoint here and make jdbc connection 'jdbc:h2:tcp://localhost:8092/mem:testdb;IFEXISTS=TRUE' using squirrel
        // The IFEXISTS triggers an error if the testdb does not exist yet 
        
        System.out.println("Done.");
    }
}
