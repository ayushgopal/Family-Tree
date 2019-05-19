package pranavgade20.com.familytree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pranavgade20.com.familytree.gedcom4j.model.Family;
import pranavgade20.com.familytree.gedcom4j.model.FamilyChild;
import pranavgade20.com.familytree.gedcom4j.model.FamilySpouse;
import pranavgade20.com.familytree.gedcom4j.model.Gedcom;
import pranavgade20.com.familytree.gedcom4j.model.Individual;
import pranavgade20.com.familytree.gedcom4j.model.IndividualReference;

public class EditIndividualActivity extends AppCompatActivity {
    String xref = "";
    Individual individual = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_individual);

        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            xref = intent.getStringExtra("id");;
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG);
            toast.show();

            Intent i = new Intent(getApplicationContext(), homeActivity.class);
            startActivity(i);
        }

        individual = gedcom.data.getIndividuals().get(xref);

        if (individual == null) {
            Toast toast = Toast.makeText(getApplicationContext(), "Error! Null individual", Toast.LENGTH_LONG);
            toast.show();
            Intent i = new Intent(getApplicationContext(), homeActivity.class);
            startActivity(i);
        }
    }

    public void onSave(View v) {
//        int errors = 0;
        String parentID = ((EditText) findViewById(R.id.editText_parentID)).getText().toString();
        //TODO Verify data

        if (!parentID.isEmpty() && parentID != null) {
            Individual parent = gedcom.data.getIndividuals().get("@I" + parentID + "@");
            if (parent == null) {
                Toast toast = Toast.makeText(getApplicationContext(), "Error! Parent not found, so no changes were made.", Toast.LENGTH_LONG);
                toast.show();

//            errors++;
            }
            if (individual.getFamiliesWhereChild() != null && individual.getFamiliesWhereChild().size() != 0) {
                Toast toast = Toast.makeText(getApplicationContext(), "A parent was already set. Removing the parent to add new one.", Toast.LENGTH_LONG);
                toast.show();
            }
            Family parentFamily = parent.getFamiliesWhereSpouse().get(0).getFamily();
            parentFamily.addChild(new IndividualReference(individual));

            List<FamilyChild> familyChildList = new ArrayList<>();
            FamilyChild familyChild = new FamilyChild();
            familyChild.setFamily(parent.getFamiliesWhereSpouse().get(0).getFamily());
            familyChildList.add(familyChild);
            individual.setFamiliesWhereChild(familyChildList);
        }

        ////////////////////

        String spouseID = ((EditText) findViewById(R.id.editText_spouseID)).getText().toString();

        if (!spouseID.isEmpty() && spouseID != null) {
            Individual spouse = gedcom.data.getIndividuals().get("@I" + spouseID + "@");
            if (spouse == null) {
                Toast toast = Toast.makeText(getApplicationContext(), "Error! Spouse not found, so no changes were made.", Toast.LENGTH_LONG);
                toast.show();

//            errors++;
            }
            if (gedcom.getGender(individual) == gedcom.getGender(spouse)) {
                Toast toast = Toast.makeText(getApplicationContext(), "Fatal Error! Spouse has same gender.", Toast.LENGTH_LONG);
                toast.show();

                Intent intent = new Intent(getApplicationContext(), homeActivity.class);
                startActivity(intent);
            }

            if (!individual.getSpouses().isEmpty()) {
                Toast toast = Toast.makeText(getApplicationContext(), "A spouse was already set. Removing the spouse to add new one.", Toast.LENGTH_LONG);
                toast.show();
            }
            if (!spouse.getSpouses().isEmpty()) {
                Toast toast = Toast.makeText(getApplicationContext(), "A spouse to spouse was already set. Removing the spouse to add new one.", Toast.LENGTH_LONG);
                toast.show();
            }

            if (gedcom.getGender(individual) == 'M') {
                //spouse is wife, delete her family, add here
                FamilySpouse husbandFamily = individual.getFamiliesWhereSpouse().get(0);
                Family wifeFamily = spouse.getFamiliesWhereSpouse().get(0).getFamily();
                if (wifeFamily.getChildren() != null) {
                    for (IndividualReference i : wifeFamily.getChildren()) {
                        husbandFamily.getFamily().addChild(i);
                    }
                }
                husbandFamily.getFamily().setWife(wifeFamily.getWife());
                List<FamilySpouse> spouseList = new ArrayList<>();
                spouseList.add(husbandFamily);
                spouse.setFamiliesWhereSpouse(spouseList);
                gedcom.data.removeFamily(wifeFamily.getXref());
            } else {
                FamilySpouse husbandFamily = spouse.getFamiliesWhereSpouse().get(0);
                Family wifeFamily = individual.getFamiliesWhereSpouse().get(0).getFamily();
                if (wifeFamily.getChildren() != null) {
                    for (IndividualReference i : wifeFamily.getChildren()) {
                        husbandFamily.getFamily().addChild(i);
                    }
                }
                husbandFamily.getFamily().setWife(wifeFamily.getWife());
                List<FamilySpouse> list = new ArrayList<>();
                list.add(husbandFamily);
                individual.setFamiliesWhereSpouse(list);
                gedcom.data.removeFamily(wifeFamily.getXref());
            }
        }

        gedcom.data.addIndividual(individual, xref);
        gedcom.save();
        gedcom.load();

        Intent intent = new Intent(getApplicationContext(), homeActivity.class);
        startActivity(intent);

    }
}
