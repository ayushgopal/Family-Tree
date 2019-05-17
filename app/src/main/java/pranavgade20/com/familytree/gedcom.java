package pranavgade20.com.familytree;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import pranavgade20.com.familytree.gedcom4j.io.reader.GedcomFileReader;
import pranavgade20.com.familytree.gedcom4j.model.Gedcom;
import pranavgade20.com.familytree.gedcom4j.model.Individual;
import pranavgade20.com.familytree.gedcom4j.model.IndividualEvent;
import pranavgade20.com.familytree.gedcom4j.parser.GedcomParser;
import pranavgade20.com.familytree.gedcom4j.writer.GedcomWriter;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class gedcom {
    public static Gedcom data = null;
    public static Context context;
    private final static String file_name = "gdata";

    public static String getBirth(Individual i) {
        try {
            List<IndividualEvent> events =  i.getEvents(); //TODO
        } catch (Exception e) {
            return "unknown";
        }
        return "unknown";
    }

    public static void setContext(Context c) {
        context = c;
    }

//    public static boolean save(Context c) {
////        //get name from shared pref,
////        SharedPreferences pref = c.getSharedPreferences("familytree.file", Context.MODE_PRIVATE);
////        SharedPreferences.Editor editor = pref.edit();
////        //save in internal as name from shared prefs
////        if (pref.getString("file_name", null) == null) {
////            editor.putString("file_name", file_name);
////        }
////
////        String fileName = pref.getString("file_name", file_name);
//
//        context = c;
//        try {
//            context.deleteFile(file_name);
//            File file = new File(context.getFilesDir(), file_name);
//
//            GedcomWriter writer = new GedcomWriter(gedcom.data);
//            writer.write(file);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    public static boolean save(){
        try {
            context.deleteFile(file_name);
            File file = new File(context.getFilesDir(), file_name);

            GedcomWriter writer = new GedcomWriter(gedcom.data);
            writer.write(file);
            return true;
        } catch (Exception e) {
            Log.e("ERROR", "writing file failed");
            e.printStackTrace();
            return false;
        }
    }

    public static void load(){
        try {
//            FileInputStream inputStream = context.openFileInput(file_name);
//            GedcomParser parser = new GedcomParser();
//            parser.load(new BufferedInputStream(inputStream));
//            data = null;
//            data = parser.getGedcom();
            InputStream inputStream = context.openFileInput(file_name);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                GedcomParser parser = new GedcomParser();
                parser.load(new BufferedInputStream(inputStream));
                data = null;
                data = parser.getGedcom();
                inputStream.close();
            } else throw new Exception("input file is null");
        } catch (Exception e) {
            Log.e("ERROR", "reading file failed");
            e.printStackTrace();
        }
    }
}
