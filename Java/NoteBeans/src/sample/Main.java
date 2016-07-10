package sample;

import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    public static String getCurrentString(DfaState currentState) {
        String tempString = "";
        DfaState tempState = currentState;
        while (tempState.getParentState() != null) {
            tempString = (char) tempState.getParentInput() + tempString;
            tempState = tempState.getParentState();
        }
        return tempString;
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Fuck Writing!!!");

        BorderPane borderPane = new BorderPane();

        HBox box = new HBox();

        DfaBuilder builder = new DfaBuilder();

        builder.setDfaCallBack((current, states) -> {

            System.out.println("current list " + getCurrentString(current));

            System.out.println("预测文本 =》");

            ArrayList<DfaState> list = new ArrayList<>();
            for (Integer key : states.keySet()) {
                states.get(key).returnEndList(list);
            }

            for (DfaState state : list) {
                System.out.println("prediction list " + getCurrentString(state));
            }
        });

        TextArea field = new TextArea();
        field.setPrefSize(500, 500);
        field.getParagraphs().addListener((ListChangeListener<CharSequence>) c -> {
            while (c.next()) {
                int size = c.getAddedSubList().get(0).length();
//                System.out.println(size);
//                System.out.println(c);
                if (size >= 1) {
                    System.out.println(c.getAddedSubList().get(0).charAt(size - 1));
                    builder.input(c.getAddedSubList().get(0).charAt(size - 1));
                }
            }
        });

        box.getChildren().addAll(field);


        borderPane.setCenter(box);


        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
