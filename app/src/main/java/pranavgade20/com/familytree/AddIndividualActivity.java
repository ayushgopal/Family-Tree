package pranavgade20.com.familytree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pranavgade20.com.familytree.gedcom4j.model.Family;
import pranavgade20.com.familytree.gedcom4j.model.Individual;
import pranavgade20.com.familytree.gedcom4j.model.IndividualReference;
import pranavgade20.com.familytree.gedcom4j.model.PersonalName;

public class AddIndividualActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_individual);
    }

    public void addIndividual(View v) {
        String firstName = ((TextView) findViewById(R.id.editText_fname)).getText().toString();
        String middleName = ((TextView) findViewById(R.id.editText_mname)).getText().toString();
        String lastName = ((TextView) findViewById(R.id.editText_lname)).getText().toString();

        RadioGroup rg = (RadioGroup) findViewById(R.id.gender_radioGroup);
        RadioButton rb = (RadioButton) findViewById(rg.getCheckedRadioButtonId());
        String gender = rb.getText().toString();

        Individual individual = new Individual();
        individual.setSex(gender);
        String individualXref = "@I"+String.format("%04d", (gedcom.data.getIndividuals().size()+1))+"@";
        individual.setXref(individualXref);

        PersonalName name = new PersonalName();
        name.setSurname(lastName);
        name.setGivenName(firstName + " " + middleName);
        List<PersonalName> nameList = new ArrayList<>();
        nameList.add(name);
        individual.setNames(nameList);

        IndividualReference reference = new IndividualReference();
        reference.setIndividual(individual);

        Family family = new Family();
        family.setHusband(reference);

        gedcom.data.addIndividual(individual, individualXref);
        gedcom.data.addFamily(family, "@F"+String.format("%04d", (gedcom.data.getFamilies().size()+1))+"@");

        gedcom.save();
        gedcom.load();

        Intent intent = new Intent(getApplicationContext(), homeActivity.class);
        startActivity(intent);
    }
}
