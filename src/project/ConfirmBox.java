package project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

//class to create popup windows
public class ConfirmBox {
    private static Stage confirmWindow;
    private static Label label;
    private static Button yesButton, noButton;
    private static Scene scene;
    private static boolean userAnswer;
    private static Image confirmWindowIcon; //icon that will be used for Confirm Boxes

    public static boolean display(String title, int width, int height, String question) {
        confirmWindow = new Stage();

        //setting up icon within confirmWindow
        confirmWindowIcon = new Image(ConfirmBox.class.getResourceAsStream("ConfirmBoxIcon.png"));
        confirmWindow.getIcons().add(confirmWindowIcon);

        //forces user to interact with confirmation window before interacting with window below it
        confirmWindow.initModality(Modality.APPLICATION_MODAL);

        //assign attributes to window based on parameters
        confirmWindow.setTitle(title);
        confirmWindow.setWidth(width);
        confirmWindow.setHeight(height);
        label = new Label(question);

        //create buttons so user can confirm or cancel
        yesButton = new Button("Yes");
        yesButton.setOnAction(e -> {
            userAnswer = true;
            confirmWindow.close();
        });
        noButton = new Button("No");
        noButton.setOnAction(e -> {
            userAnswer = false;
            confirmWindow.close();
        });

        //layout for this Scene
        VBox outerLayout = new VBox(10);
        HBox innerLayout = new HBox(10);
        outerLayout.setPadding(new Insets(20, 30, 20, 30)); //pads out border so nothing is directly against the frame
        innerLayout.setPadding(new Insets(10, 10, 10, 10)); //pads out border so nothing is directly against the frame
        innerLayout.getChildren().addAll(yesButton, noButton);
        innerLayout.setAlignment(Pos.CENTER);
        outerLayout.getChildren().addAll(label, innerLayout);
        outerLayout.setAlignment(Pos.CENTER);

        scene = new Scene(outerLayout);
        confirmWindow.setScene(scene);

        //gets rid of extra whitespace
        confirmWindow.sizeToScene();
        confirmWindow.setResizable(false);

        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        confirmWindow.setX(primScreenBounds.getWidth() - (primScreenBounds.getWidth() / 1.7));
        confirmWindow.setY(primScreenBounds.getHeight() - (primScreenBounds.getHeight() / 1.75));

        //doesn't return until user closes the window
        confirmWindow.showAndWait();

        return userAnswer;
    }
}
