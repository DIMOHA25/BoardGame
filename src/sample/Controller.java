package sample;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public Button start;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void startGame(ActionEvent event) throws IOException {
        try {
            GameController game = new GameController();

        } catch (IOException e) {
            System.out.println("No file");
        }

    }
}
