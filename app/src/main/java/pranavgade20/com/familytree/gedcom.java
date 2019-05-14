package pranavgade20.com.familytree;

import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualEvent;

import java.util.List;

public class gedcom {
    public static Gedcom data = null;

    public static String getBirth(Individual i) {
        try {
            List<IndividualEvent> events =  i.getEvents();
        } catch (Exception e) {
            return "unknown";
        }
        return "unknown";
    }
}
