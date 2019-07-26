package pranavgade20.com.familytree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gedcom.setContext(getApplicationContext());
    }

    public void signIn(View V) {
        EditText editText = (EditText) findViewById(R.id.editText_familyID);
        String familyID = editText.getText().toString();

        Intent intent = new Intent(getApplicationContext(), intermediateActivity.class);

//        intent.putExtra("url", "https://webtreeprint.com/tp_downloader.php?path=famous_gedcoms/royal92.ged&file=royal92.ged");
        gedcom.getUrl = "https://familytree-back.herokuapp.com/get?familyCode="+familyID;
        gedcom.postUrl = "https://familytree-back.herokuapp.com/send";
        gedcom.familyCode = familyID;
        intent.putExtra("url", gedcom.getUrl);
        startActivity(intent);
    }

    public void register(View v){
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }
}
