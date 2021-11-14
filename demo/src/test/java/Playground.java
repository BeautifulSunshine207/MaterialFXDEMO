import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXRadioButton;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import io.github.palexdev.materialfx.factories.InsetsFactory;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.scenicview.ScenicView;

public class Playground extends Application {

    @Override
    public void start(Stage primaryStage) {
        StackPane stackPane = new StackPane();

        HBox box = new HBox(20);
        box.setAlignment(Pos.CENTER_LEFT);

        MFXToggleButton toggle = new MFXToggleButton("Toggle Button");
        toggle.setAlignment(Pos.CENTER_RIGHT);

        MFXRadioButton radioButton = new MFXRadioButton("Radio Button");
        radioButton.setContentDisposition(ContentDisplay.TOP);
        radioButton.setTextExpand(true);
        radioButton.setAlignment(Pos.BOTTOM_CENTER);
        radioButton.setPrefHeight(120);
        radioButton.setPadding(InsetsFactory.all(5));

        MFXCheckbox checkbox = new MFXCheckbox("Checkbox");

        box.getChildren().addAll(toggle, radioButton, checkbox);
        stackPane.getChildren().add(box);
        Scene scene = new Scene(stackPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        ScenicView.show(scene);
    }
}
