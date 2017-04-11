package sample;

import com.sun.jersey.api.client.Client;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.window_process.MainWindowPane;

import java.util.Map;

/**
 * Класс служащий для запуска клиента.
 */
public class Main extends Application {
    /**
     * URL-адрес клиента.
     */
    public static String CLIENT_URL;
    /**
     * Точка входа в программу.
     * @param primaryStage главное окно клиента
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        BorderPane mainPane = new BorderPane();
        Parameters parameters = getParameters();
        Map<String, String> namedParametrs =  parameters.getNamed();
        CLIENT_URL = namedParametrs.get("URL");
        Client  client = Client.create();
       MainWindowPane mainWindowPanePane = new MainWindowPane(mainPane,client);
        Scene scene = new Scene(mainPane, 610, 400);
        primaryStage.setTitle("Client connect to "+namedParametrs.get("URL"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
