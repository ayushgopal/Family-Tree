package pranavgade20.com.familytree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdateServerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_server);

        if (gedcom.writeData()) {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("familyCode", gedcom.familyCode)
                    .add("data", gedcom.writtenData)
                    .build();
            Request request = new Request.Builder()
                    .url(gedcom.postUrl)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("Error!", "exception", e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        Log.e("Error!", response.toString());
//                        Toast toast = Toast.makeText(getApplicationContext(), "Error! Please try again later.", Toast.LENGTH_LONG);
//                        toast.show();
                        Intent intent = new Intent(getApplicationContext(), homeActivity.class);
                        startActivity(intent);
                    } else {
                        String str = response.body().string();
//                        Toast toast = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG);
//                        toast.show();
                        Intent intent = new Intent(getApplicationContext(), homeActivity.class);
                        intent.putExtra("response", str);
                        startActivity(intent);
                    }
                }
            });
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Error! Please try again later.", Toast.LENGTH_LONG);
            toast.show();
            Intent intent = new Intent(getApplicationContext(), homeActivity.class);
            startActivity(intent);
        }
    }
}
