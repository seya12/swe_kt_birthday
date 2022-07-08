package trader;

import java.util.*;

public class TraderService implements Trader {

  private final Map<String, List<Product>> products = new HashMap<>();

  @Override
  public void offer(String name, int count, double askPrice) {
    List<Product> list = products.get(name);

    if (list == null) {
      list = new ArrayList<>();
      list.add(new Product(name, count, askPrice));
      products.put(name, list);
    } else {
      Optional<Product> o = list.stream()
        .filter(p -> p.askPrice == askPrice)
        .findFirst();
      if (o.isPresent()) {
        o.get().count += count;
      } else {
        list.add(new Product(name, count, askPrice));
      }
    }

  }

  @Override
  public boolean buy(String name, int count, double bidPrice) {
    List<Product> list = products.get(name);

    if (list == null) {
      return false;
    }

    Optional<Product> first = list.stream()
      .filter(p -> p.count >= count && p.askPrice <= bidPrice)
      .findFirst();

    if (first.isPresent()) {
      Product p = first.get();
      p.count -= count;
      if (p.count <= 0) list.remove(p);
    } else{
      return false;
    }
    return true;
  }

  private static class Product {
    String name;
    int count;
    double askPrice;

    public Product(String name, int count, double askPrice) {
      this.name = name;
      this.count = count;
      this.askPrice = askPrice;
    }
  }

  public static void main(String[] args) {
    TraderService ts = new TraderService();
    ts.offer("Used Book Java for Dummies", 1, 10.50);
    ts.offer("PC", 1, 105.0);
    ts.offer("Used Book Java for Dummies", 2, 5.22);

    System.out.println(ts.buy("PC", 1, 100));
    System.out.println(ts.buy("PC", 1, 120));
    System.out.println(ts.buy("PC", 1, 120));
  }
}
