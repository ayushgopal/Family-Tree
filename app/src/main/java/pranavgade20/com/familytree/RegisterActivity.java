package pranavgade20.com.familytree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import pranavgade20.com.familytree.gedcom4j.model.ChangeDate;
import pranavgade20.com.familytree.gedcom4j.model.Gedcom;
import pranavgade20.com.familytree.gedcom4j.model.Header;
import pranavgade20.com.familytree.gedcom4j.model.StringWithCustomFacts;
import pranavgade20.com.familytree.gedcom4j.model.Submitter;
import pranavgade20.com.familytree.gedcom4j.model.SubmitterReference;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void onPressNext(View v) {
        EditText name = (EditText) findViewById(R.id.name_input); // TODO check name
        EditText password = (EditText) findViewById(R.id.password_input);
        EditText confirm_password = (EditText) findViewById(R.id.password_confirm_input);

        if (password.getText().toString().equals(confirm_password.getText().toString())) {
            //TODO add password length verif
            gedcom.data = new Gedcom();
            Submitter s = new Submitter();
            s.setXref("@SUBM@");
            s.setName(new StringWithCustomFacts(name.getText().toString()));
            Header h = new Header();
            SubmitterReference ref = new SubmitterReference();
            ref.setSubmitter(s);
            h.setSubmitterReference(ref);
            gedcom.data.setHeader(h);
            gedcom.data.addSubmitter(s, "@SUBM@");

            if(!gedcom.save()) {
                Toast toast = Toast.makeText(getApplicationContext(), "Error! Failed to save file.", Toast.LENGTH_LONG);
                toast.show();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Success.", Toast.LENGTH_LONG); // TODO remove
                toast.show();
            }
            //TODO make network calls

            Intent i = new Intent(getApplicationContext(), homeActivity.class);
            startActivity(i);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Error! Passwords do not match.", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
