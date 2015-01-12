package bs.h2datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.h2.tools.Server;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

/**
 * Single connection datasource to h2 database that enables other processes to connect. Delegates to spring
 * SingleConnectionDataSource. Driver is fixed org.h2.Driver. Starts a tcpServer to connect to. Specify
 * h2TcpPort property to override default 9092.
 * <p>
 * We need to start the tcpServer here if the Application Server manages the dataSource. If you would start
 * the server, for example, in a webapp during startup, then the server would not be able to access the in
 * memory database on the AS level.
 * <p>
 * Example connect string: <code>jdbc:h2:tcp://localhost:9092/mem:demodb;IFEXISTS=TRUE</code>
 *
 */
public class H2DataSource implements DataSource {

    private int h2TcpPort = 9092;

    private final SingleConnectionDataSource target;

    private Server h2Server = null;

    public H2DataSource() {
        super();
        this.target = new SingleConnectionDataSource();
        target.setDriverClassName("org.h2.Driver");
    }

    public void setUrl(String url) {
        target.setUrl(url);
    }

    public String getUrl() {
        return target.getUrl();
    }

    public int getLoginTimeout() throws SQLException {
        return target.getLoginTimeout();
    }

    public void setUsername(String username) {
        target.setUsername(username);
    }

    public void setLoginTimeout(int timeout) throws SQLException {
        target.setLoginTimeout(timeout);
    }

    public String getUsername() {
        return target.getUsername();
    }

    public PrintWriter getLogWriter() {
        return target.getLogWriter();
    }

    public void setPassword(String password) {
        target.setPassword(password);
    }

    public void setLogWriter(PrintWriter pw) throws SQLException {
        target.setLogWriter(pw);
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        return target.unwrap(iface);
    }

    public String getPassword() {
        return target.getPassword();
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return target.isWrapperFor(iface);
    }

    public Properties getConnectionProperties() {
        return target.getConnectionProperties();
    }

    public Logger getParentLogger() {
        return target.getParentLogger();
    }

    public void setSuppressClose(boolean suppressClose) {
        target.setSuppressClose(suppressClose);
    }

    public Connection getConnection() throws SQLException {
        synchronized (this.target) {
            if (this.h2Server == null) {
                startH2Server();
            }
            return target.getConnection();
        }
    }

    private void startH2Server() {
        try {

            h2Server = Server.createTcpServer("-tcpDaemon", "-tcpPort", h2TcpPort + "").start();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Connection getConnection(String username, String password) throws SQLException {
        synchronized (this.target) {
            if (this.h2Server == null) {
                startH2Server();
            }
            return target.getConnection(username, password);
        }
    }

    public boolean shouldClose(Connection con) {
        return target.shouldClose(con);
    }

    public int getH2TcpPort() {
        return h2TcpPort;
    }

    public void setH2TcpPort(int h2TcpPort) {
        this.h2TcpPort = h2TcpPort;
    }

}
