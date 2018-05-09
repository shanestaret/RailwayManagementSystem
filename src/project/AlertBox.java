package project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

//class to create popup windows
public class AlertBox {
    private static Stage alertWindow;
    private static Label label;
    private static Button closeButton;
    private static Scene scene;
    private static Image alertWindowIcon; //icon that will be used for Alert Boxes

    public static void display(String title, int width, int height, String message) {
        alertWindow = new Stage();

        //setting up icon within alertWindow
        alertWindowIcon = new Image(ConfirmBox.class.getResourceAsStream("AlertBoxIcon.png"));
        alertWindow.getIcons().add(alertWindowIcon);


        //forces user to interact with popup window before interacting with window below it
        alertWindow.initModality(Modality.APPLICATION_MODAL);

        //assign attributes to window based on parameters
        alertWindow.setTitle(title);
        alertWindow.setWidth(width);
        alertWindow.setHeight(height);
        label = new Label(message);

        //create button to close out of popup window
        closeButton = new Button("OK");
        closeButton.setOnAction(e -> alertWindow.close());

        //layout for this Scene
        VBox alertLayout = new VBox(10);
        alertLayout.setPadding(new Insets(20, 50, 20, 50));
        alertLayout.getChildren().addAll(label, closeButton);
        alertLayout.setAlignment(Pos.CENTER);

        scene = new Scene(alertLayout);
        alertWindow.setScene(scene);

        //gets rid of extra whitespace
        alertWindow.sizeToScene();
        alertWindow.setResizable(false);

        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        alertWindow.setX(primScreenBounds.getWidth() - (primScreenBounds.getWidth() / 1.7));
        alertWindow.setY(primScreenBounds.getHeight() - (primScreenBounds.getHeight() / 1.75));

        //doesn't return until user closes the window
        alertWindow.showAndWait();
    }
}
