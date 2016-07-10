package sample;

import java.util.Map;

/**
 * Created by liufengkai on 16/7/10.
 */
public interface DfaCallBack {
    void onMultipleSetBack(DfaState current, Map<Integer, DfaState> states);
}
