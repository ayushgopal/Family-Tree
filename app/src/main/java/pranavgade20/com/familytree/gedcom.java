package pranavgade20.com.familytree;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pranavgade20.com.familytree.gedcom4j.io.reader.GedcomFileReader;
import pranavgade20.com.familytree.gedcom4j.model.Gedcom;
import pranavgade20.com.familytree.gedcom4j.model.Header;
import pranavgade20.com.familytree.gedcom4j.model.Individual;
import pranavgade20.com.familytree.gedcom4j.model.IndividualEvent;
import pranavgade20.com.familytree.gedcom4j.model.NoteRecord;
import pranavgade20.com.familytree.gedcom4j.model.StringWithCustomFacts;
import pranavgade20.com.familytree.gedcom4j.model.Submitter;
import pranavgade20.com.familytree.gedcom4j.model.SubmitterReference;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class gedcom {
    public static Gedcom data = null;
    public static Context context;
    private final static String file_name = "gdata.ged";

    public static String writtenData = "";
    public static String getUrl = "";
    public static String postUrl = "";
    public static String familyCode = "";

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

    public static boolean save(){
        try {
            Validator validator = new Validator(data);
            validator.validate();
            // TODO do some validation stuff

            context.deleteFile(file_name);
            File file = new File(context.getFilesDir(), file_name);

            final GedcomWriter writer = new GedcomWriter(data);
            writer.write(file);

            return true;
        } catch (Exception e) {
            Log.e("ERROR", "writing file failed");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean writeData(){
        try {
            final GedcomWriter writer = new GedcomWriter(data);

            OutputStream outputStream = new OutputStream() {
                @Override
                public void write(int i) throws IOException {
                    writtenData = writtenData + (char) (i & 0xFF);
                }
            };
            writtenData = "";
            writer.write(outputStream);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void load(){
        try {
            InputStream inputStream = context.openFileInput(file_name);

            if ( inputStream != null ) {
                GedcomParser parser = new GedcomParser();
                parser.load(new BufferedInputStream(inputStream));
                data = null;
                data = parser.getGedcom();

                Validator validator = new Validator(data);
                validator.validate();

                inputStream.close();
            } else throw new Exception("input file is null");

            //Try reading the file to see if loaded correctly
            String x = data.getHeader().toString();
        } catch (Exception e) {
            Log.e("ERROR", "reading file failed");
            e.printStackTrace();

            data = new Gedcom();
            Submitter s = new Submitter();
            s.setXref("@SUBM@");
            s.setName(new StringWithCustomFacts("ERROR"));
            Header h = new Header();
            SubmitterReference ref = new SubmitterReference();
            ref.setSubmitter(s);
            h.setSubmitterReference(ref);
            data.setHeader(h);
            data.addSubmitter(s, "@SUBM@");

            NoteRecord record = new NoteRecord("@FAMILIESNOTE@");
            List<String> lines = new ArrayList<>();
            lines.add("0001");
            record.setLines(lines);
            Map<String, NoteRecord> noteRecordMap = new HashMap<String, NoteRecord>();
            noteRecordMap.put("@FAMILIESNOTE@", record);
            data.setNotes(noteRecordMap);
        }
    }
}
