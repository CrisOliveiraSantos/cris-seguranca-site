package br.com.cris.landing;

import br.com.cris.landing.controller.LandingController;
import br.com.cris.landing.view.LandingView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        LandingController controller = new LandingController(getHostServices());
        LandingView view = new LandingView(controller);

        Scene scene = new Scene(view.create(), 1240, 820);
        scene.getStylesheets().add(getClass().getResource("/styles/app.css").toExternalForm());

        stage.setTitle("Cris Oliveira Santos - Técnica em Segurança do Trabalho");
        stage.setMinWidth(980);
        stage.setMinHeight(700);
        stage.setScene(scene);

        try {
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/styles/icon.png")));
        } catch (Exception ignored) {
            // Icone opcional.
        }

        stage.show();
        view.playEntranceAnimations();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
