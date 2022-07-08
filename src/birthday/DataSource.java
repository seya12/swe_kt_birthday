package birthday;

import java.sql.*;

public interface DataSource {
  Connection getConnection() throws SQLException;
}
