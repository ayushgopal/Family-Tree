package pranavgade20.com.familytree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.Individual;
import org.gedcom4j.parser.GedcomParser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class intermediateActivity extends AppCompatActivity {


    String urlLoc = "http://heiner-eichmann.de/gedcom/simple.ged";
    OkHttpClient client = new OkHttpClient();

    public void getHttpResponse() {
        // this gets the data and stores it in the gedcom class
        Request request = new Request.Builder().url(urlLoc).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Error!", "exception", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("Error!", response.toString());
                } else {
                    String str = response.body().string();
                    parseGedcom(str);
                }
            }
        });
    }

    public void parseGedcom(final String gedcomData) {
        GedcomParser gp = new GedcomParser();
        try {
            gp.load(new BufferedInputStream(new StringBufferInputStream(gedcomData)));
            gedcom.data = gp.getGedcom();

//            Intent intent = new Intent(getApplicationContext(), baseActivity.class);
//            Map<String, Individual> indivisuals = gedcom.data.getIndividuals();
//            Map.Entry<String, Individual> entry = indivisuals.entrySet().iterator().next();
//            String key= entry.getKey();
//            intent.putExtra("id", key);
//            startActivity(intent);

            Intent intent = new Intent(getApplicationContext(), homeActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();

            showError();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermediate);

        Intent intent = getIntent();
        if (intent.hasExtra("url")){
            urlLoc = intent.getStringExtra("url");
        }

        getHttpResponse();
    }

    public void showError(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView t = (TextView) findViewById(R.id.intermediate_text);
                t.setText("An error occured. Please try again.");
            }
        });
    }
}
