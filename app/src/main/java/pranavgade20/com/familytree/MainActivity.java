package pranavgade20.com.familytree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void signIn(View V) {
        Intent intent = new Intent(getApplicationContext(), intermediateActivity.class);

        intent.putExtra("url", "https://webtreeprint.com/tp_downloader.php?path=famous_gedcoms/royal92.ged&file=royal92.ged");
        startActivity(intent);
    }
}