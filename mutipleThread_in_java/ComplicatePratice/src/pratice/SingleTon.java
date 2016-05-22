package pratice;

/**
 * 单例模式
 * Created by liufengkai on 16/5/21.
 */
public class SingleTon {
    private SingleTon() {

    }

    private static class SingleTonHoler {
        private static SingleTon singleTon = new SingleTon();
    }

    public SingleTon getInstance() {
        return SingleTonHoler.singleTon;
    }
}
