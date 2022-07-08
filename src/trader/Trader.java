package trader;

import java.rmi.*;

public interface Trader extends Remote {
  void offer(String name, int count, double askPrice);
  boolean buy(String name, int count, double bidPrice);
}
