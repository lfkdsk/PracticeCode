package sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by liufengkai on 16/7/10.
 */
public class DfaBuilder {
    /**
     * NFA 状态机的起始状态
     */
    public DfaState startState = null;
    /**
     * 状态机的当前状态
     */
    public DfaState currentState = null;
    /**
     * 接受状态
     */
    public HashMap<Integer, DfaState> acceptState;

    public Set<DfaState> endState;

    private static final int RETURN_ID = 13;

    private static final int CHANGE_LINE_ID = 10;

    private static final int TAB_ID = 9;

    private static final int SPACE_ID = 32;

    private ArrayList<Integer> endIdList;

    private DfaCallBack dfaCallBack = null;

    public DfaBuilder() {
        // parent is null
        this(new DfaState(null, null));
    }

    public DfaBuilder(DfaState startState) {
        this.startState = startState;
        this.currentState = startState;
        initial();
    }

    /**
     * 添加接受状态
     */
    public void addAcceptState(int input, DfaState accept) {
        if (!acceptState.containsKey(input)) {
            acceptState.put(input, accept);
        }
    }

    private void initial() {
        this.acceptState = new HashMap<>();
        this.endIdList = new ArrayList<>();
        this.endState = new HashSet<>();
        initialEndIdList();
    }


    private void initialEndIdList() {
        endIdList.add(RETURN_ID);
        endIdList.add(CHANGE_LINE_ID);
        endIdList.add(TAB_ID);
        endIdList.add(SPACE_ID);
    }

    public DfaState input(int input) {
        // parser 了所有特殊情况 对于单词的提示
        // 一个单词内是不会出现空格制表符和换行的
//        System.out.println(input + "sss");
        if (endIdList.contains(input)) {
            this.currentState = startState;
            return null;
        }


        // 处理了当输入串还在起始状态的情况
        if (currentState.getStateId() == startState.getStateId()) {
            return startInput(input);
        }

        // 说明状态不在起始状态
        DfaState tempCurrent = currentState.getTransitionInput(input);
        if (tempCurrent == null) {
            tempCurrent = new DfaState(input, currentState);
            currentState.addTransition(input, tempCurrent);
        } else {
            if (dfaCallBack != null) dfaCallBack.onMultipleSetBack(tempCurrent, tempCurrent.getTransitionSet());
        }
        currentState = tempCurrent;
        return currentState;
    }

    /**
     * 处理还在输入串起始状态的情况
     *
     * @param input 输入
     * @return current状态
     */
    public DfaState startInput(int input) {
        DfaState current;
        // 转入第一个起始状态
        if (!acceptState.containsKey(input)) {
            current = new DfaState(input, currentState);
            this.addAcceptState(input, current);
        } else {
            current = acceptState.get(input);
            if (dfaCallBack != null) dfaCallBack.onMultipleSetBack(current, current.getTransitionSet());
        }
        this.currentState = current;
        return current;
    }

    public void setDfaCallBack(DfaCallBack dfaCallBack) {
        this.dfaCallBack = dfaCallBack;
    }

    public void printDfa() {
        for (Integer integer : acceptState.keySet()) {
            System.out.println("接受状态 " + acceptState.get(integer).getStateId());
            acceptState.get(integer).printState();
        }
    }

    public void resetStartState() {
        this.currentState = startState;
    }
}
