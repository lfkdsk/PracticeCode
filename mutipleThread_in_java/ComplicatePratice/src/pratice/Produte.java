package pratice;

/**
 * 不变模式
 * Created by liufengkai on 16/5/21.
 */
public final class Produte {
    private final String no;
    private final String name;
    private final double price;

    public Produte(String no, String name, double price) {
        this.no = no;
        this.name = name;
        this.price = price;
    }
}
