package birthday;

import java.sql.*;
import java.util.*;

public class BirthdayNotifier {

  private final DataSource dataSource;
  private List<BirthdayListener> listeners = new ArrayList<>();

  public BirthdayNotifier(DataSource dataSource){
    this.dataSource = dataSource;
  }

  public void attachBirthdayListener(BirthdayListener listener){
    listeners.add(listener);
  }

  private void fireBirthdayEvent(int month, int day) throws SQLException{
    List<Person> personList = new ArrayList<>();

    try(Connection con = dataSource.getConnection();
        PreparedStatement p = con.prepareStatement("select * from tbl_person where day_of_birth = ? and month_of_birth = ?")){
      p.setInt(1, day);
      p.setInt(2, month);

      try(ResultSet rs = p.executeQuery()){
        while(rs.next()){
          personList.add(new Person(rs.getString(1), rs.getInt(2), rs.getInt(3)));
        }
      }
    }
    for(BirthdayListener listener : listeners) {
      for (Person p : personList) {
        listener.notify(p);
      }
    }

  }

  public void checkForBirthday() throws SQLException {
    fireBirthdayEvent(7,8);
  }

  public static void main(String[] args) throws SQLException {
    DataSource ds = new DataSource() {
      @Override
      public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mariadb://localhost/test", "root", "");
      }
    };
    BirthdayNotifier notifier = new BirthdayNotifier(ds);

    BirthdayListener listener = new BirthdayListener() {
      @Override
      public void notify(Person person) {
        System.out.println(person.name);
      }
    };

    notifier.attachBirthdayListener(listener);
    notifier.checkForBirthday();
  }
}

