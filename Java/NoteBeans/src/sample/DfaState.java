package sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liufengkai on 16/7/10.
 */
public class DfaState implements Comparable<DfaState> {

    private static int DFA_ID_COUNT = 0;
    /**
     * state id
     */
    private int stateId;
    /**
     * transition set
     * char / set of dfaState
     */
    private Map<Integer, DfaState> transitionSet;

    private DfaState parentState;

    private Integer parentInput;

    public DfaState(Integer input, DfaState parentState) {
        this.parentInput = input;
        this.parentState = parentState;
        this.stateId = DFA_ID_COUNT++;
        this.transitionSet = new HashMap<>();
    }

    /**
     * 添加一条转移语句
     *
     * @param input 输入字符
     * @param state 下一个状态
     * @return 返回添加状态
     */
    public DfaState addTransition(int input, DfaState state) {

        if (!transitionSet.containsKey(input)) {
            transitionSet.put(input, state);
        }

        return state;
    }

    public DfaState getTransitionInput(int input) {
        return getTransitionSet().get(input);
    }

    public int getStateId() {
        return stateId;
    }

    public static int getTotalNumber() {
        return DFA_ID_COUNT;
    }

    public Map<Integer, DfaState> getTransitionSet() {
        return transitionSet;
    }

    public DfaState getParentState() {
        return parentState;
    }

    @Override
    public int compareTo(DfaState o) {
        return 0;
    }

    public int getParentInput() {
        return parentInput;
    }

    public void printState() {
        System.out.println("state : " + getStateId());
        for (Integer integer : transitionSet.keySet()) {
            System.out.println("symbol: " +
                    (char) integer.intValue() + " to :" +
                    transitionSet.get(integer).getStateId());
            transitionSet.get(integer).printState();
        }
    }

    public void returnEndList(ArrayList<DfaState> list) {
        for (Integer key : transitionSet.keySet()) {
            DfaState cur = transitionSet.get(key);
            if (cur.getTransitionSet().isEmpty()) {
                list.add(cur);
            } else {
                cur.returnEndList(list);
            }
        }
    }
}
