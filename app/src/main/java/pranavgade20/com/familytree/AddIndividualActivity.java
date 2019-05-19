package pranavgade20.com.familytree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pranavgade20.com.familytree.gedcom4j.model.Family;
import pranavgade20.com.familytree.gedcom4j.model.FamilySpouse;
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

        int errors = 0;
        if (lastName == null || lastName.isEmpty()) {
            errors ++;
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter last name.", Toast.LENGTH_LONG);
            toast.show();
        }
        if (errors == 0) {
            Individual individual = new Individual();
            individual.setSex(gender);
            String individualXref = "@I" + String.format("%04d", (gedcom.data.getIndividuals().size() + 1)) + "@";
            individual.setXref(individualXref);

            PersonalName name = new PersonalName();
            name.setSurname(lastName);
            name.setGivenName(firstName + " " + middleName);
            name.setBasic(firstName);
            List<PersonalName> nameList = new ArrayList<>();
            nameList.add(name);
            individual.setNames(nameList);

            IndividualReference reference = new IndividualReference();
            reference.setIndividual(individual);

            Family family = new Family();
            if (gedcom.getGender(individual) == 'M') family.setHusband(reference);
            else family.setWife(reference);


            String familyXref = "@F" + gedcom.data.getNotes().get("@FAMILIESNOTE@").getLines().get(0) + "@";
            family.setXref(familyXref);
            gedcom.data.getNotes().get("@FAMILIESNOTE@").getLines().set(0, String.format("%04d", Integer.parseInt(gedcom.data.getNotes().get("@FAMILIESNOTE@").getLines().get(0)) + 1));

            List<FamilySpouse> familySpouseList = new ArrayList<>();
            FamilySpouse familySpouse = new FamilySpouse();
            familySpouse.setFamily(family);
            familySpouseList.add(familySpouse);
            individual.setFamiliesWhereSpouse(familySpouseList);

            gedcom.data.addIndividual(individual, individualXref);
            gedcom.data.addFamily(family, familyXref);

            gedcom.save();
            gedcom.load();

            Intent intent = new Intent(getApplicationContext(), homeActivity.class);
            startActivity(intent);
        }
    }
}
