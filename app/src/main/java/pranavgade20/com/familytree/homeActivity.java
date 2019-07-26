package pranavgade20.com.familytree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pranavgade20.com.familytree.gedcom4j.model.Gedcom;
import pranavgade20.com.familytree.gedcom4j.model.Header;
import pranavgade20.com.familytree.gedcom4j.model.NoteRecord;
import pranavgade20.com.familytree.gedcom4j.model.StringWithCustomFacts;
import pranavgade20.com.familytree.gedcom4j.model.Submitter;
import pranavgade20.com.familytree.gedcom4j.model.SubmitterReference;

public class homeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        if (intent.hasExtra("response")) {
            Toast toast = Toast.makeText(getApplicationContext(), intent.getStringExtra("response"), Toast.LENGTH_LONG);
            toast.show();
        }

        try{
            String x = gedcom.data.getHeader().toString();
        } catch (Exception e) {
            Toast toast = Toast.makeText(getApplicationContext(), "Error loading from server. Loading from local storage.", Toast.LENGTH_LONG);
            toast.show();
            gedcom.load();
        }
    }

    public void search(View view) {
        Intent intent = new Intent(getApplicationContext(), SearchResultsActivity.class);
        EditText input = (EditText) findViewById(R.id.search_input);
        String query = input.getText().toString();
        intent.putExtra("query", query);
        startActivity(intent);
    }

    public void addIndividual(View v) {
        Intent intent = new Intent(getApplicationContext(), AddIndividualActivity.class);
        startActivity(intent);
    }

    public void updateServer(View v){
        Toast toast = Toast.makeText(getApplicationContext(), "Please wait while the server is updated.", Toast.LENGTH_LONG);
        toast.show();

        Intent intent = new Intent(getApplicationContext(), UpdateServerActivity.class);
        startActivity(intent);
    }

    public void updateFromInternal(View v) {
        gedcom.load();

        Toast toast = Toast.makeText(getApplicationContext(), "Data loaded from local storage.", Toast.LENGTH_LONG);
        toast.show();
    }
}
