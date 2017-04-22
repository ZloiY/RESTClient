package sample.window_process;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.istack.internal.Nullable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.glassfish.jersey.client.ClientResponse;
import org.glassfish.jersey.client.JerseyWebTarget;
import sample.Adapter;
import sample.Main;
import sample.PatternGroup;
import sample.PatternModel;

import javax.imageio.ImageIO;
import javax.sound.midi.Soundbank;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Класс являющийся главным окном программы и реализующий навигацию пользователя между списками паттернов.
 */
public class MainWindowPane {
    /**
     * Списки всех группы паттернов.
     */
    private PatternsLists allPattern, mvPatterns, createPatterns, structPatterns, behavePatterns;
    /**
     * Выбор группы паттернов.
     */
    private ComboBox<String> patternsGroups;
    /**
     * Jersey объект для RPC взаимодействия.
     */
    private Client client;
    /**
     *
     */
    Gson gson;
    /**
     *
     */
    Type listType;
    /**
     * Хранит соответсвия id группы паттерна и названия этой группы.
     */
    private HashMap<String, Integer> patternsMap;
    /**
     * Главаня разметка окна.
     */
    private BorderPane pane;
    /**
     * Хранит {@link ComboBox} для отображения группы паттернов,
     * {@link ListView} для отображения списка названий паттернов,
     * кнопку для добавления паттернов.
     */
    private VBox leftBox;
    /**
     * Добавляет новые паттерны.
     */
    private Button addPatternBtn;
    /**
     * Поле поиска паттернов.
     */
    private TextField searchField;

    /**
     * Создаёт главное окно программы  с элементами поиска и навигации пользователя по программе.
     * @param mainPane главна разметка.
     * @param client
     */
    public MainWindowPane(BorderPane mainPane, Client client){
        pane = mainPane;
        this.client = client;
        gson = new Gson();
        listType = new TypeToken<List<PatternModel>>() {}.getType();
        allPattern = new PatternsLists();
        mvPatterns = new PatternsLists();
        createPatterns = new PatternsLists();
        structPatterns = new PatternsLists();
        behavePatterns = new PatternsLists();
        patternsGroups = new ComboBox<>();
        patternsGroups.setPrefWidth(190.0);
        patternsMap = new HashMap<>();
        searchField = new TextField();
        for (int i = 0; i <= 4; i++) {
            patternsMap.put(Adapter.fromEnumToStringPatternGroup(PatternGroup.findByValue(i)),i);
            patternsGroups.getItems().add(Adapter.fromEnumToStringPatternGroup(PatternGroup.findByValue(i)));
        }
        patternsGroups.getSelectionModel().select(0);
        getPatterns(null, allPattern);
        addPatternBtn = new Button("+");
        patternsGroups.setOnAction(event -> {
           refreshPatternLists();
        });
        Button refreshBtn = new Button();
        refreshBtn.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResource("image/refresh.png").toString())));
        HBox topBox = new HBox(5);
        topBox.getChildren().addAll(searchField, refreshBtn);
        topBox.setPadding(new Insets(5,5,0,5));
        searchField.setOnKeyReleased(searchFieldEvent());
        searchField.setPrefWidth(590-refreshBtn.getWidth());
        refreshBtn.setOnAction(refreshBtnAction());
        leftBox = new VBox(5);
        VBox functionBtnBox = new VBox();
        addPatternBtn.setPrefWidth(190.0);
        pane.setTop(topBox);
        pane.setRight(functionBtnBox);
        leftBox.getChildren().addAll(patternsGroups, allPattern.getPatternsView(),addPatternBtn);
        leftBox.setPadding(new Insets(5,10,5,5));
        pane.setLeft(leftBox);
        addPatternBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addPatternWindow();
            }
        });
    }

    /**
     * Обнавляет список группы паттернов взависимости от выбранной группы.
     */
    private void refreshPatternLists(){
        if (PatternGroup.findByValue(patternsMap.get(patternsGroups.getValue()))!= null)
            switch (PatternGroup.findByValue(patternsMap.get(patternsGroups.getValue()))) {
                case MV_PATTERNS:
                    getPatterns(PatternGroup.MV_PATTERNS, mvPatterns);
                    leftBox.getChildren().clear();
                    leftBox.getChildren().addAll(patternsGroups, mvPatterns.getPatternsView(), addPatternBtn);
                    break;
                case BEHAVE_PATTERNS:
                    getPatterns(PatternGroup.BEHAVE_PATTERNS, behavePatterns);
                    leftBox.getChildren().clear();
                    leftBox.getChildren().addAll(patternsGroups, behavePatterns.getPatternsView(), addPatternBtn);
                    break;
                case CREAT_PATTERNS:
                    getPatterns(PatternGroup.CREAT_PATTERNS, createPatterns);
                    leftBox.getChildren().clear();
                    leftBox.getChildren().addAll(patternsGroups, createPatterns.getPatternsView(), addPatternBtn);
                    break;
                case STRUCT_PATTERNS:
                    getPatterns(PatternGroup.STRUCT_PATTERNS, structPatterns);
                    leftBox.getChildren().clear();
                    leftBox.getChildren().addAll(patternsGroups, structPatterns.getPatternsView(), addPatternBtn);
                    break;
            }
        else{
            getPatterns(null, allPattern);
            leftBox.getChildren().clear();
            leftBox.getChildren().addAll(patternsGroups, allPattern.getPatternsView(), addPatternBtn);
        }
    }

    /**
     * Хендлер для кнопки обновления группы паттернов.
     * @return
     */
    private EventHandler<ActionEvent> refreshBtnAction(){
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                refreshPatternLists();
            }
        };
    }

    /**
     * Хендлер для строки поиска паттернов.
     * @return хендлер строки поиска
     */
    private EventHandler<KeyEvent> searchFieldEvent(){
        EventHandler<KeyEvent> searchEvent = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ESCAPE))
                    searchField.clear();
                if (event.getCode().equals(KeyCode.ENTER)){
                    if (searchField.getText().isEmpty()){
                        refreshPatternLists();
                    }else {
                        PatternModel searchModel = new PatternModel();
                        searchModel.setName(searchField.getText());
                        searchModel.setGroup(patternsMap.get(patternsGroups.getValue()));
                        PatternsLists findList = new PatternsLists();
                        WebTarget resource = client.target(Main.CLIENT_URL).path("group/"+searchModel.getGroup()+"/name/"+searchModel.getName());
                        String searchRes= resource.request(MediaType.APPLICATION_JSON).get(String.class);
                        findList.setPatternsLists(gson.fromJson(searchRes, listType));
                        leftBox.getChildren().clear();
                        leftBox.getChildren().addAll(patternsGroups, findList.getPatternsView(), addPatternBtn);
                        findList.setListViewListner(setListSelectEvent(findList));
                    }
                }
            }
        };
        return searchEvent;
    }

    /**
     * Устанавливает в главноё разметке {@code pane} окно добавления паттерна.
     */
    private void addPatternWindow(){
        Window addWindow = new Window();
        addWindow.editLayout();
        addWindow.getNewPatternDescription().setMinSize(300, 270);
        addWindow.getNewPatternDescription().setEditable(true);
        addWindow.getCnclBtn().setVisible(false);
        pane.setCenter(addWindow.getBorderPane());
        addWindow.getApplyBtn().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PatternModel newPattern = new PatternModel();
                if (addWindow.getNewPatternName().getText().length() > 50 || addWindow.getNewPatternName().getText().isEmpty()) {
                    new Alert(Alert.AlertType.WARNING,"Your pattern name more than 10 characters or it's empty").show();
                    return;
                }else newPattern.setName(addWindow.getNewPatternName().getText());
                if (addWindow.getNewPatternDescription().getText().length() > 500 || addWindow.getNewPatternDescription().getText().isEmpty()){
                    new Alert(Alert.AlertType.WARNING,"Your pattern description more than 500 characters or it's empty").show();
                    return;
                }else newPattern.setDescription(addWindow.getNewPatternDescription().getText());
                if (addWindow.getNewPatternGroup().getSelectionModel().isEmpty()){
                    new Alert(Alert.AlertType.WARNING, "Your must choose pattern group").show();
                    return;
                }else newPattern.setGroup(addWindow.getPatternGroupId());
                if (addWindow.getNewPatternSchema().getImage() != null){
                    BufferedImage bufferedImage = SwingFXUtils.fromFXImage(addWindow.getNewPatternSchema().getImage(), null);
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    try {
                        ImageIO.write(bufferedImage,"png",outStream);
                        newPattern.setImage(outStream.toByteArray());
                        outStream.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                 WebTarget resource = client.target(Main.CLIENT_URL).path("pattern/");
                 String jsonStr = gson.toJson(newPattern);
                 System.out.println(jsonStr);
                 resource.request(MediaType.APPLICATION_JSON).post(Entity.entity(jsonStr,MediaType.WILDCARD_TYPE));
                 addWindow.blankWindow();
                 refreshPatternLists();
                //new Alert(Alert.AlertType.ERROR,"Service is offline try again latter.").show();
            }
        });
    }

    /**
     * Формирует поиск запрос к серверу и принимает ответ сервера.
     * @param patternGroup группа паттерна.
     * @return список наёденных паттернов.
     */
    private ArrayList<PatternModel> searchAllPatterns(@Nullable PatternGroup patternGroup){
        PatternModel pattern = new PatternModel();
        if (patternGroup !=  null)
            pattern.setGroup(patternGroup.getValue());
            WebTarget resource = client.target(Main.CLIENT_URL).path("group/"+ pattern.getGroup());
            String requestRes = resource.request(MediaType.APPLICATION_JSON).get(String.class);
            return gson.fromJson(requestRes,listType);
            //new Alert(Alert.AlertType.ERROR,"Service is offline try again later.").show();
    }

    /**
     * Устанавливает для переданного списка паттерна в соответсвии с их группой.
     * @param patternGroup группа паттернов
     * @param patternsList список группы паттернов
     */
    private void getPatterns(@Nullable PatternGroup patternGroup, PatternsLists patternsList){
        patternsList.setPatternsLists(searchAllPatterns(patternGroup));
        patternsList.setListViewListner(setListSelectEvent(patternsList));
    }

    /**
     * Листнер для списка выбора паттернов.
     * @param lists список паттернов.
     * @return листнер для списка выбора паттернов.
     */
    private ChangeListener<String> setListSelectEvent(PatternsLists lists){
        ChangeListener<String> changeListener = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (lists.getPatternsView().getSelectionModel().getSelectedIndex() >= 0) {
                    Window patternWindow = new Window(lists.getSelectedPattern());
                    patternWindow.showLayout();
                    patternWindow.getEditBtn().setOnAction(setEditEvent(patternWindow));
                    patternWindow.getDelBtn().setOnAction(setDelEvent(patternWindow));
                    pane.setCenter(patternWindow.getBorderPane());
                }
            }
        };
        return changeListener;
    }

    /**
     * Хандлер изменяющий разметку окна отображения информации о паттерне,
     * на разметку редактирования информации о паттерне.
     * @param window окно сожержащее информацию о паттерне.
     * @return хандлер изменения разметки окна
     */
    private EventHandler<ActionEvent> setEditEvent(Window window){
        EventHandler<ActionEvent> editEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                window.editLayout();
                window.getApplyBtn().setText("Edit");
                window.setPatternID(window.getPatternID());
                window.getNewPatternName().setText(window.getPatternName().getText());
                window.getNewPatternDescription().setText(window.getPatternDescription().getText());
                window.getNewPatternGroup().getSelectionModel().select(Adapter.fromEnumToStringPatternGroup(window.getPatternGroup()));
                window.setNewPatternSchema(window.getPatternSchemaImage());
                window.getApplyBtn().setOnAction(e -> {
                    PatternModel newPattern = new PatternModel();
                    PatternModel oldPattern = new PatternModel();
                    oldPattern.setId(window.getPatternID());
                    oldPattern.setName(window.getPatternName().getText());
                    newPattern.setId(window.getPatternID());
                    newPattern.setName(window.getNewPatternName().getText());
                    newPattern.setGroup(window.getPatternGroup());
                    if(newPattern.getName().length() > 50 || newPattern.getName().isEmpty()) {
                        new Alert(Alert.AlertType.WARNING, "Your pattern name more than 10 characters or it's empty").show();
                        return;
                    }
                    newPattern.setDescription(window.getNewPatternDescription().getText());
                    if (newPattern.getDescription().length() > 500 || newPattern.getDescription().isEmpty()) {
                        new Alert(Alert.AlertType.WARNING, "Your pattern description more than 500 characters or it's empty").show();
                        return;
                    }
                    if (window.getNewPatternSchema().getImage() != null) {
                        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(window.getNewPatternSchema().getImage(), null);
                        ByteArrayOutputStream output = new ByteArrayOutputStream();
                        try {
                            ImageIO.write(bufferedImage, "png", output);
                            newPattern.setImage(output.toByteArray());
                            output.close();
                        }catch (IOException ioException){
                            ioException.printStackTrace();
                        }
                    }
                    WebTarget resource =client.target(Main.CLIENT_URL).path(Integer.toString(oldPattern.getId()));
                    String replaceString = gson.toJson(newPattern);
                    resource.request(MediaType.APPLICATION_JSON).put(Entity.entity(replaceString,MediaType.WILDCARD_TYPE));
                    Window newWindow = new Window(newPattern);
                    newWindow.showLayout();
                    newWindow.getDelBtn().setOnAction(setDelEvent(newWindow));
                    newWindow.getEditBtn().setOnAction(setEditEvent(newWindow));
                    pane.setCenter(newWindow.getBorderPane());
                    refreshPatternLists();
                });
            }
        };
        return editEvent;
    }

    /**
     * Хандлер удаляющий паттерн.
     * @param window окно сожержащее информацию о паттерне
     * @return хандлер удаляющий паттерн
     */
    private EventHandler<ActionEvent> setDelEvent(Window window){
        EventHandler<ActionEvent> delEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PatternModel deletePattern = new PatternModel();
                deletePattern.setId(window.getPatternID());
                WebTarget resource = client.target(Main.CLIENT_URL).path("pattern/"+String.valueOf(deletePattern.getId()));
                resource.request(MediaType.APPLICATION_JSON).delete();
                //new Alert(Alert.AlertType.ERROR, "Service is offline try again later.").show();
                if (PatternGroup.findByValue(patternsMap.get(patternsGroups.getValue())) != null)
                switch (PatternGroup.findByValue(patternsMap.get(patternsGroups.getValue()))){
                    case MV_PATTERNS:
                        mvPatterns.deleteFromPatternsView();
                        getPatterns(PatternGroup.MV_PATTERNS, mvPatterns);
                        break;
                    case BEHAVE_PATTERNS:
                        behavePatterns.deleteFromPatternsView();
                        getPatterns(PatternGroup.BEHAVE_PATTERNS, behavePatterns);
                        break;
                    case CREAT_PATTERNS:
                        createPatterns.deleteFromPatternsView();
                        getPatterns(PatternGroup.CREAT_PATTERNS, createPatterns);
                        break;
                    case STRUCT_PATTERNS:
                        structPatterns.deleteFromPatternsView();
                        getPatterns(PatternGroup.STRUCT_PATTERNS, structPatterns);
                        break;
                }
                else getPatterns(null, allPattern);
                window.blankWindow();
            }
        };
        return delEvent;
    }

}
