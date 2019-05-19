package pranavgade20.com.familytree;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import pranavgade20.com.familytree.gedcom4j.io.reader.GedcomFileReader;
import pranavgade20.com.familytree.gedcom4j.model.Gedcom;
import pranavgade20.com.familytree.gedcom4j.model.Individual;
import pranavgade20.com.familytree.gedcom4j.model.IndividualEvent;
import pranavgade20.com.familytree.gedcom4j.parser.GedcomParser;
import pranavgade20.com.familytree.gedcom4j.writer.GedcomWriter;

import pranavgade20.com.familytree.gedcom4j.validate.Validator;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

public class gedcom {
    public static Gedcom data = null;
    public static Context context;
    private final static String file_name = "gdata.ged";

    public static String writtenData = "";

    public static String getBirth(Individual i) {
        try {
            List<IndividualEvent> events =  i.getEvents(); //TODO
        } catch (Exception e) {
            return "unknown";
        }
        return "unknown";
    }


    //returns M or F
    public static char getGender(Individual i) {
        try {
            return i.getSex().toString().toUpperCase().charAt(0);
        } catch (Exception E) {
            return 'M';
        }
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
            Validator validator = new Validator(gedcom.data);
            validator.validate();
            // TODO do some validation stuff

            context.deleteFile(file_name);
            File file = new File(context.getFilesDir(), file_name);

            final GedcomWriter writer = new GedcomWriter(gedcom.data);
            writer.write(file);

            OutputStream outputStream = new OutputStream() {
                @Override
                public void write(int i) throws IOException {
                    writtenData = writtenData + (char) (i & 0xFF);
                }
            };
//            writer.write(outputStream);

            Log.e("A" ,"B");

            return true;
        } catch (Exception e) {
            Log.e("ERROR", "writing file failed");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean save2(){  //TODO remove
        try {
            Validator validator = new Validator(gedcom.data);
            validator.validate();
            // TODO do some validation stuff
            Log.e("A" ,"B");
            //check family here

//            context.deleteFile(file_name);
//            File file = new File(context.getFilesDir(), file_name);

            final GedcomWriter writer = new GedcomWriter(gedcom.data);
//            writer.write(file);

            OutputStream outputStream = new OutputStream() {
                @Override
                public void write(int i) throws IOException {
                    writtenData = writtenData + (char) (i & 0xFF);
                }
            };
            writtenData = "";
            writer.write(outputStream);

            Log.e("A" ,"B");

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

                Validator validator = new Validator(gedcom.data);
                validator.validate();

                inputStream.close();
            } else throw new Exception("input file is null");
        } catch (Exception e) {
            Log.e("ERROR", "reading file failed");
            e.printStackTrace();
        }
    }
}
