package sample;

/**
 * Created by ZloiY on 09.04.17.
 */
public class SearchParmatersCreator {
    public static String getParametrs(PatternModel patternModel){
        if (patternModel.getName()==null)
        return Integer.toString(patternModel.getGroup());
        else return patternModel.getName() + "9" +patternModel.getGroup();
    }
}
