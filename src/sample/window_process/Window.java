package sample.window_process;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.Adapter;
import sample.PatternGroup;
import sample.PatternModel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;

/**
 * Класс служащий для отображения информации о паттерне,
 * редактирования этой информации, а также для добаления новых паттернов.
 */
public class Window {
    /**
     * id паттерна.
     */
    private int patternID;
    /**
     * Отображаемое название паттерна.
     */
    private Label patternName;
    /**
     * Отображаемая группа паттерна.
     */
    private Label patternGroup;
    /**
     * Редактируемая группа паттерна.
     */
    private ComboBox<String> newPatternGroup;
    /**
     * Отображаемое описание паттерна.
     */
    private Text patternDescription;
    /**
     * Изображение паттерна.
     */
    private ImageView patternSchemaImage;
    /**
     * Главная разметка окна.
     */
    private BorderPane borderPane;
    /**
     * Редактироемое название паттерна.
     */
    private TextField newPatternName;
    /**
     * Редактироемое описание паттерна.
     */
    private TextArea newPatternDescription;
    /**
     * Новое изображение паттерна.
     */
    private ImageView newPatternSchema;
    /**
     * Паттерн.
     */
    private PatternModel patternModel;
    /**
     * Вызывает окно для отображения паттерна.
     */
    private Button schemaViewer;
    /**
     * Вызывает окно для отображения выбора изображения паттерна.
     */
    private Button schemaChooseBtn;
    /**
     * Принятияе изменений.
     */
    private Button applyBtn;
    /**
     * Отмена изменений.
     */
    private Button cnclBtn;
    /**
     * Хранит элементы для описания паттерна.
     */
    private VBox descripitionBox;
    /**
     * Удаляеи паттерн.
     */
    private Button delBtn;
    /**
     * Открывает окно редактирования паттерна.
     */
    private Button editBtn;
    /**
     * Хранит кнопки удаления, редактировани, отображения схемы паттерна.
     */
    private HBox delEditBox;
    /**
     * Хранит соответсвия id группы паттерна и названия этой группы.
     */
    private HashMap<String , Integer> patternsMap;

    /**
     * Служит для создания окна отображения паттерна и возможности его дальнеёшего редактирования.
     * @param pattern отображаемый паттерн.
     */
    public Window(PatternModel pattern){
        patternModel = pattern;
        patternID = pattern.getId();
        patternGroup = new Label(Adapter.fromEnumToStringPatternGroup(pattern.getGroup()));
        patternName = new Label(pattern.getName());
        patternsMap = new HashMap<>();
        patternDescription = new Text(pattern.getDescription());
        patternDescription.setWrappingWidth(380);
        patternSchemaImage = new ImageView();
        if (pattern.getImage()!= null) {
            Image image = new Image(new ByteArrayInputStream(pattern.getImage()));
            patternSchemaImage.setImage(image);
        }
        newPatternName = new TextField();
        newPatternDescription = new TextArea();
        newPatternDescription.setWrapText(true);
        newPatternGroup = new ComboBox<>();
        for (int i = 1; i <= 4; i++) {
            patternsMap.put(Adapter.fromEnumToStringPatternGroup(PatternGroup.findByValue(i)),i);
            newPatternGroup.getItems().add(Adapter.fromEnumToStringPatternGroup(PatternGroup.findByValue(i)));
        }
        newPatternSchema = new ImageView();
        applyBtn = new Button("Add");
        cnclBtn = new Button("Cancel");
        editBtn = new Button("Edit");
        delBtn = new Button("Delete");
        schemaViewer = new Button("View schema");
        schemaViewer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SchemaViewer schemaViewer = new SchemaViewer(patternSchemaImage);
                schemaViewer.showViewer();
            }
        });
        schemaChooseBtn = new Button("Choose schema");
        borderPane = new BorderPane();
        descripitionBox = new VBox(5);
        delEditBox = new HBox(5);
        delEditBox.getChildren().addAll(editBtn, delBtn, schemaViewer);
        borderPane.setCenter(descripitionBox);
        borderPane.setBottom(delEditBox);
        borderPane.setPadding(new Insets(5,15,5,5));

    }

    /**
     * СОздаёт окно для добавления нового паттерна.
     */
    public Window(){
        newPatternName = new TextField();
        newPatternDescription = new TextArea();
        newPatternDescription.setWrapText(true);
        newPatternSchema = new ImageView();
        newPatternGroup = new ComboBox<>();
        patternGroup = new Label();
        patternsMap = new HashMap<>();
        for (int i = 1; i <= 4; i++) {
            patternsMap.put(Adapter.fromEnumToStringPatternGroup(PatternGroup.findByValue(i)),i);
            newPatternGroup.getItems().add(Adapter.fromEnumToStringPatternGroup(PatternGroup.findByValue(i)));
        }
        schemaViewer = new Button("Schema viewer");
        schemaViewer.setOnAction(e->{
            if (newPatternSchema!=null) {
                SchemaViewer schemaViewer = new SchemaViewer(newPatternSchema);
                schemaViewer.showViewer();
            }
        });
        applyBtn = new Button("Add");
        cnclBtn = new Button("Cancel");
        schemaChooseBtn = new Button("Choose schema");
        borderPane = new BorderPane();
        borderPane.setPadding(new Insets(5,15,5,5));
        descripitionBox = new VBox(5);
        borderPane.setCenter(descripitionBox);
    }

    /**
     * Возвращает название паттерна.
     * @return название
     */
    public Label getPatternName() {
        return patternName;
    }

    /**
     * Возвращает описание паттерна.
     * @return описание паттерна
     */
    public Text getPatternDescription() {
        return patternDescription;
    }

    /**
     * Возвращает схему паттерна.
     * @return схему паттерна.
     */
    public ImageView getPatternSchemaImage() {
        return patternSchemaImage;
    }

    /**
     * Устанавливает id паттерна.
     * @param patternID id паттерна
     */
    public void setPatternID(int patternID) {
        this.patternID = patternID;
    }

    /**
     * Устанавливает новую схему паттерна.
     * @param newPatternSchema новая схема паттерна.
     */
    public void setNewPatternSchema(ImageView newPatternSchema) {
        this.newPatternSchema = newPatternSchema;
    }

    /**
     * Возвращает новое название паттерна.
     * @return название паттерна
     */
    public TextField getNewPatternName() {
        return newPatternName;
    }

    /**
     * Возвращает новое описание паттерна.
     * @return описание паттерниа
     */
    public TextArea getNewPatternDescription() {
        return newPatternDescription;
    }

    /**
     * Возвращает новую схему паттерна.
     * @return схему паттерна
     */
    public ImageView getNewPatternSchema() {
        return newPatternSchema;
    }

    /**
     * Возвращает кнопуку принятия изменений.
     * @return кнопку принятия изменений
     */
    public Button getApplyBtn() {
        return applyBtn;
    }

    /**
     * Возвращает кнопку отмены изменений.
     * @return кнопку отмены изменений
     */
    public Button getCnclBtn() {
        return cnclBtn;
    }

    /**
     * Возвращает кнопку удаления паттерна.
     * @return кнопку удаления паттерна
     */
    public Button getDelBtn() {
        return delBtn;
    }

    /**
     * Возвращет кнопку редактирования паттерна.
     * @return кнопку редактирования
     */
    public Button getEditBtn() {
        return editBtn;
    }

    /**
     * Возвращает id паттерна.
     * @return id паттерна.
     */
    public int getPatternID(){return patternID;}

    /**
     * Возврашет разметку окна.
     * @return разметку окна.
     */
    public BorderPane getBorderPane() {
        return borderPane;
    }

    /**
     * Устанавливает элементы дял ортображения информации о паттене.
     */
    public void showLayout(){
        descripitionBox.getChildren().clear();
        descripitionBox.getChildren().addAll(patternGroup,patternName,patternDescription);
    }

    /***
     * Возвращает паттерн.
     * @return паттерн
     */
    public PatternModel getPatternModel(){
        return patternModel;
    }

    /**
     * Возвращает новую группу паттерна.
     * @return новую группу паттерна.
     */
    public ComboBox<String> getNewPatternGroup() {
        return newPatternGroup;
    }

    /**
     * Возвращает id новой группы паттерна.
     * @return id новой группы паттерна
     */
    public int getPatternGroupId(){
        return patternsMap.get(newPatternGroup.getValue());
    }

    /**
     * Возвращает id группы паттерна.
     * @return id группы паттерна.
     */
    public int getPatternGroup(){
        return patternModel.getGroup();
    }

    /**
     * Удаляет все элементы отображения и редактирования информации о паттерне.
     */
    public void blankWindow(){
        descripitionBox.getChildren().clear();
        borderPane.setBottom(null);
        borderPane.setCenter(null);
    }

    /**
     * Устанавливает эелементы для редактирования/добавлениие информации о паттерне.
     */
    public void editLayout(){
        descripitionBox.getChildren().clear();
        descripitionBox.getChildren().addAll(newPatternGroup, newPatternName, newPatternDescription);
        HBox btnBox = new HBox(10);
        btnBox.getChildren().addAll(applyBtn, cnclBtn, schemaChooseBtn, schemaViewer);
        btnBox.setPadding(new Insets(0,5,0,0));
        descripitionBox.getChildren().add(btnBox);
        if (delEditBox!=null)
            delEditBox.visibleProperty().set(false);
        cnclBtn.setOnAction(e -> {
            showLayout();
            delEditBox.visibleProperty().set(true);
            delEditBox.getChildren().add(schemaViewer);
        });
        schemaChooseBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage schemaChooseWindow = new Stage();
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
                fileChooser.setTitle("Pattern schema chooser");
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("png","*.png"),
                        new FileChooser.ExtensionFilter("bmp","*.bmp"),
                        new FileChooser.ExtensionFilter("jpg", "*.jpg"));
                File file = fileChooser.showOpenDialog(schemaChooseWindow);
                if(file != null)
                    newPatternSchema.setImage(new Image(file.toURI().toString()));
            }
        });
    }
}


