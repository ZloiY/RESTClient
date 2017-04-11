package sample;


/**
 * Класс служащий для адаптирования {@link sample.PatternGroup} к строковому формату.
 */
public class Adapter {
    /**
     * Метод преобразующий {@link sample.PatternGroup} к строке.
     * @param patternGroup группа к которой принадлежит паттерн
     * @return группу к оторой принадлежит паттерн в формате строки
     */
    public static String fromEnumToStringPatternGroup(PatternGroup patternGroup){
        if (patternGroup != null) {
            switch (patternGroup.getValue()) {
                case 1:
                    return "MV-паттерны";
                case 2:
                    return "Структурные паттерны";
                case 3:
                    return "Порождающие паттерны";
                case 4:
                    return "Поведенчиские паттерны";
                default: return null;
            }
        }else return "Все паттерны";
    }

    /**
     * Метод преобразующий {@link sample.PatternGroup} к строке.
     * @param key значаени {@link sample.PatternGroup} в int.
     * @return группу к оторой принадлежит паттерн в формате строки
     */
    public static String fromEnumToStringPatternGroup(int key){
        switch (key) {
            case 1:
               return "MV-паттерны";
            case 2:
               return "Структурные паттерны";
            case 3:
               return "Порождающие паттерны";
            case 4:
               return "Поведенчиские паттерны";
            default: return  "";}
    }
}
