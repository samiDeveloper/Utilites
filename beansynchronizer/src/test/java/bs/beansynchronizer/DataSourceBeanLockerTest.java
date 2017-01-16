package bs.beansynchronizer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;

import org.apache.commons.dbcp2.BasicDataSource;
import org.h2.Driver;
import org.h2.tools.Server;
import org.junit.Assert;
import org.junit.Test;

public class DataSourceBeanLockerTest
{
    @Test
    public void testAcquireLock() throws SQLException
    {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(Driver.class.getName());
        ds.setUsername("SA");
        ds.setPassword("");
        ds.setUrl("jdbc:h2:mem:testdb");

        // for debugging
        Server server = Server.createTcpServer("-tcpDaemon", "-tcpPort", "8092");
        server.start();

        Connection con = ds.getConnection();
        Statement updateStm = con.createStatement();
        updateStm.execute("CREATE TABLE BEAN_LOCK (BEANID VARCHAR, CLIENTID VARCHAR, ISSUED_TIMESTAMP TIMESTAMP)");

        PreparedStatement insertStm = con
                .prepareStatement("INSERT INTO BEAN_LOCK (BEANID, CLIENTID, ISSUED_TIMESTAMP) VALUES (?, ?, ?)");
        insertStm.setString(1, "asdf");
        insertStm.setString(2, "qwer");
        Timestamp timestamp = new Timestamp(Instant.now().toEpochMilli());
        insertStm.setTimestamp(3, timestamp);
        
        insertStm.execute();


        server.stop();
        Assert.fail("Not Yet Implemented");

    }
}
