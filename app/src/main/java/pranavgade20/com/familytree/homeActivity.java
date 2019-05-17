package pranavgade20.com.familytree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class homeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
}
