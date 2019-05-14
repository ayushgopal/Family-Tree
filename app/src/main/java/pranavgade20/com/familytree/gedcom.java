package pranavgade20.com.familytree;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.writer.GedcomWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class gedcom {
    public static Gedcom data = null;
    private final static String file_name = "g_data.prg";

    public static String getBirth(Individual i) {
        try {
            List<IndividualEvent> events =  i.getEvents(); //TODO
        } catch (Exception e) {
            return "unknown";
        }
        return "unknown";
    }

    public static boolean save(Context c) {
//        //get name from shared pref,
//        SharedPreferences pref = c.getSharedPreferences("familytree.file", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//        //save in internal as name from shared prefs
//        if (pref.getString("file_name", null) == null) {
//            editor.putString("file_name", file_name);
//        }
//
//        String fileName = pref.getString("file_name", file_name);

        try {
            c.deleteFile(file_name);
            File file = new File(c.getFilesDir(), file_name);

            GedcomWriter writer = new GedcomWriter(gedcom.data);
            writer.write(file);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
