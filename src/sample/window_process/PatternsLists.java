package sample.window_process;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.ListView;
import sample.PatternModel;

import java.util.List;

/**
 * Класс для отображения групп паттернов.
 * Всего четыре группы MV-паттерны, структурные, порождающие и поведенчискеи.
 * Для каждой группы есть свой {@link List} и {@link ListView}.
 */
public class PatternsLists {
    /**
     * Хранит всю информацию о паттерне {@link sample.PatternModel}.
     */
    private List<PatternModel> patternsLists;
    /**
     * Отображает названия всех патарнов находящихся в {@code patternSList}.
     */
    private ListView<String> patternsView;
    /**
     * Ширина {@code patternsView}.
     */
    private double listsWidth = 100.0;

    /**
     * Создаёт {@code patternsView}.
     */
    public PatternsLists(){
        patternsView = new ListView<>();
        patternsView.setPrefWidth(listsWidth);
    }

    /**
     * Получает список паттернов и добавляет их в {@code patternsView}.
     * @param patterns список паттернов
     */
    public void setPatternsLists(List<PatternModel> patterns){
        patternsLists = patterns;
        patternsView.getItems().clear();
        for (PatternModel patternModel : patternsLists)
            patternsView.getItems().add(patternModel.getName());
    }

    /**
     * Возварщает {@link ListView} с названиями паттернов.
     * @return список с названиями паттенов
     */
    public ListView<String> getPatternsView(){ return patternsView; }

    /**
     * Возвращает id выбранного паттерна в {@link ListView}
     * @return id выбранного паттерна
     */
    public PatternModel getSelectedPattern(){
        return patternsLists.get(patternsView.getSelectionModel().getSelectedIndex());
    }

    /**
     * Устанавливает листнер для {@link ListView}.
     * @param listner листнер
     */
    public void setListViewListner(ChangeListener<String> listner){
        patternsView.getSelectionModel().selectedItemProperty().addListener(listner);
    }

    /**
     * Уддаляет паттерн из {@code patternsView}.
     */
    public void deleteFromPatternsView() {
        if (patternsView.getSelectionModel().getSelectedIndex()!=0) {
            int selectedId = patternsView.getSelectionModel().getSelectedIndex();
            patternsView.getSelectionModel().select( selectedId- 1);
            patternsView.getItems().remove(selectedId);
        }else{ patternsView.getSelectionModel().clearSelection();
            patternsView.getItems().remove(0);
        }
    }
}
