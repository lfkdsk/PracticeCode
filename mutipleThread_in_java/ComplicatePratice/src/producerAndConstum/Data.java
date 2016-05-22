package producerAndConstum;

/**
 * 不变模式的数据类
 * Created by liufengkai on 16/5/22.
 */
public final class Data {
    private final int intData;


    public Data(int intData) {
        this.intData = intData;
    }

    public Data(String data) {
        this.intData = Integer.valueOf(data);
    }

    public int getIntData() {
        return intData;
    }

    public String toString() {
        return "data" + intData;
    }
}
