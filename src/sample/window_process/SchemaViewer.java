package sample.window_process;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Класс для отображения схе мы паттерна в отдельном окне.
 * Изображение можно увеличивать или уменьшать. С помощью клавиши Ctrl+колесико мыши вверх - увеличиать,
 * Ctrl+колёсико мыши вниз - уменьшать. Двигать изображени по окну зажимая левую кнопку мыши и перемещая мышь.
 */
public class SchemaViewer {
    /**
     * Окно для отображения схемы паттерна и интерфейса.
     */
    private Stage window;
    /**
     * ImageView для хранения схемы паттерна и отображеиня.
     */
    private ImageView patternSchema;
    /**
     * Разметка окна.
     */
    private BorderPane mainPane;
    /**
     * Хранит кнопки зума и отображает текущий зум.
     */
    private HBox btnBox;
    /**
     * Кнопка для увеличения изображения.
     */
    private Button zoomIn;
    /**
     * Кнопка для уменьшения изображения.
     */
    private Button zoomOut;
    /**
     * Служит для перемещения изображения по окну.
     */
    private ScrollPane scrollPane;
    /**
     * Показывает текущее увеличение.
     */
    private Label zoomLabel;
    /**
     * Используется для изменения размеров изоброажения.
     */
    private DoubleProperty zoomProperty;
    /**
     * Текущее увеличение.
     */
    private double currentZoom;

    /**
     * Создаёт окно для отображения схемы паттерна.
     * @param schema схема паттерна
     */
    public SchemaViewer(ImageView schema){
        window = new Stage();
        patternSchema = schema;
        patternSchema.preserveRatioProperty().set(true);
        zoomLabel = new Label();
        scrollPane = new ScrollPane(patternSchema);
        scrollPane.setPrefSize(window.getWidth(), window.getHeight());
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPannable(true);
        mainPane = new BorderPane();
        zoomProperty = new SimpleDoubleProperty(200);
        scrollPane.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaY() > 0 && event.isControlDown()){
                    currentZoom+=0.05;
                    setZoomLabel();
                    zoomProperty.set(zoomProperty.get()*1.05);
                }
                if (event.getDeltaY() < 0 && event.isControlDown() && currentZoom > 0){
                    currentZoom-=0.05;
                    setZoomLabel();
                    zoomProperty.set(zoomProperty.get()/1.05);
                }
            }
        });
        zoomProperty.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                patternSchema.setFitHeight(zoomProperty.get()*4);
                patternSchema.setFitWidth(zoomProperty.get()*3);
            }
        });
        zoomIn = new Button("+");
        zoomIn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                zoomInAction();
            }
        });
        zoomOut = new Button("-");
        zoomOut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                zoomOutAction();
            }
        });
        mainPane.setCenter(scrollPane);
        currentZoom = 1;
        setZoomLabel();
        btnBox = new HBox(5);
        zoomLabel.setAlignment(Pos.BASELINE_LEFT);
        btnBox.getChildren().addAll(zoomIn,zoomOut,zoomLabel);
        mainPane.setBottom(btnBox);
        window.setTitle("Schema Viewer");
        window.setScene(new Scene(mainPane, 500, 400));
    }

    /**
     * Вызывается при нажатии {@code zoomIn}.
     * Увеличает иззображение.
     */
    private void zoomInAction(){
        currentZoom+=0.05;
        zoomProperty.set(zoomProperty.get()*1.05);
        setZoomLabel();
    }

    /**
     * Вызывается при нажатии {@code zoomOut}.
     * Уменьшает изображение.
     */
    private void zoomOutAction(){
        if (!(currentZoom < 0)) {
            currentZoom -= 0.05;
            zoomProperty.set(zoomProperty.get()/1.05);
            setZoomLabel();
        }
    }

    /**
     * Отображает окно.
     */
    public void showViewer(){
        window.show();
    }

    /**
     * Обнавляет текщий зум окна.
     */
    private void setZoomLabel(){
        zoomLabel.setText(currentZoom*100+"%");
    }
}
