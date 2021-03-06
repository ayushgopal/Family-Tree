package pranavgade20.com.familytree;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import pranavgade20.com.familytree.gedcom4j.exception.GedcomParserException;
import pranavgade20.com.familytree.gedcom4j.model.Family;
import pranavgade20.com.familytree.gedcom4j.model.Gedcom;
import pranavgade20.com.familytree.gedcom4j.model.Individual;
import pranavgade20.com.familytree.gedcom4j.model.IndividualReference;
import pranavgade20.com.familytree.gedcom4j.parser.GedcomParser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class baseActivity extends AppCompatActivity {

    private Individual individual;
    ArrayList<ListDetails> relatives = new ArrayList<ListDetails>();

    public void setListElements(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final ListView listView = (ListView) findViewById(R.id.relatives_list);
                ArrayList<ListDetails> list_details = getListViewElements();
                listView.setAdapter(new ListBaseAdapter(getApplicationContext(), list_details));

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String id = "@I" + relatives.get(i).getAge() + "@";
                        Intent intent;
                        if(id != null && !id.isEmpty()){
                            intent = new Intent(getApplicationContext(), baseActivity.class);
                            intent.putExtra("id", id);
                        } else {
                            intent = new Intent(getApplicationContext(), homeActivity.class);
                        }
                        startActivity(intent);
                    }
                });
            }
        });
    }

    public ArrayList<ListDetails> getListViewElements() {
        ArrayList<ListDetails> ret = new ArrayList<ListDetails>();

        Gedcom g = gedcom.data;
        Family parentsFamily;
        try {
            parentsFamily = individual.getFamiliesWhereChild().get(0).getFamily();
        } catch (Exception E) {
            parentsFamily = null;
        }
        if (parentsFamily == null) {
//            ListDetails listDetails = new ListDetails();
//            listDetails.setRelation("Details not available");
//            listDetails.setName("Tap to return to home");
//            listDetails.setAge("");
//            ret.add(listDetails);
        } else {
            IndividualReference dadRef = parentsFamily.getHusband();
            IndividualReference momRef = parentsFamily.getWife();
            Individual dad = null;
            Individual mom = null;
            if (dadRef != null) dad = dadRef.getIndividual();
            if (momRef != null) mom = momRef.getIndividual();
            List<IndividualReference> siblings = parentsFamily.getChildren();
            if (dad != null) {
                ListDetails listDetails = new ListDetails();
                listDetails.setRelation("Father");
                listDetails.setName(dad.getFormattedName());
                listDetails.setAge(getIndividualAge(dad).substring(2, 6));
                ret.add(listDetails);
            }
            if (mom != null) {
                ListDetails listDetails = new ListDetails();
                listDetails.setRelation("Mother");
                listDetails.setName(mom.getFormattedName());
                listDetails.setAge(getIndividualAge(mom).substring(2, 6));
                ret.add(listDetails);
            }
            if (siblings.size() > 0) {
                for (IndividualReference r : siblings) {
                    Individual i = r.getIndividual();

                    if (!i.getXref().equals(individual.getXref())) {
                        ListDetails listDetails = new ListDetails();
                        listDetails.setRelation("Sibling"); // TODO add sibling gender ie sis/bro
                        listDetails.setName(i.getFormattedName());
                        listDetails.setAge(getIndividualAge(i).substring(2, 6));
                        ret.add(listDetails);
                    }
                }
            }
        }

        Family currentFamily;
        try {
            currentFamily = individual.getFamiliesWhereSpouse().get(0).getFamily();
        } catch (Exception E) {
            currentFamily = null;
        }
        if(currentFamily != null){
            if(gedcom.getGender(individual) == 'M') {
                if (currentFamily.getWife() != null) {
                    Individual wife = currentFamily.getWife().getIndividual();
                    if (!wife.getXref().equals(individual.getXref())) {
                        ListDetails listDetails = new ListDetails();
                        listDetails.setRelation("Wife");
                        listDetails.setName(wife.getFormattedName());
                        listDetails.setAge(getIndividualAge(wife).substring(2, 6));
                        ret.add(listDetails);
                    }
                }
            } else {
                Log.e("A", "B");
                if (currentFamily.getHusband() != null) {
                    Individual husband = currentFamily.getHusband().getIndividual();
                    if (!husband.getXref().equals(individual.getXref())) {
                        ListDetails listDetails = new ListDetails();
                        listDetails.setRelation("Husband");
                        listDetails.setName(husband.getFormattedName());
                        listDetails.setAge(getIndividualAge(husband).substring(2, 6));
                        ret.add(listDetails);
                    }
                }
            }

            List<IndividualReference> children = currentFamily.getChildren();
            if (children != null && children.size() > 0) {
                for (IndividualReference r : children) {
                    Individual i = r.getIndividual();

                    ListDetails listDetails = new ListDetails();
                    listDetails.setRelation("Child"); // TODO add child gender ie son/daughter
                    listDetails.setName(i.getFormattedName());
                    listDetails.setAge(getIndividualAge(i).substring(2, 6));
                    ret.add(listDetails);
                }
            }
        }

        if (ret.size() == 0) {
            ListDetails listDetails = new ListDetails();
            listDetails.setRelation("Details not available");
            listDetails.setName("Tap to return to home");
            listDetails.setAge("");
            ret.add(listDetails);
        }
        relatives = ret;
        return ret;
    }

    public String getIndividualAge(Individual i) {
        // TODO Add age functionality
        return i.getXref();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Intent intent = getIntent();
        if (intent.hasExtra("id")){
            String indivisualId = intent.getStringExtra("id");
            individual = gedcom.data.getIndividuals().get(indivisualId);

            TextView textView = (TextView) findViewById(R.id.textView_name);
            textView.setText(individual.getFormattedName());
        } else {
            //TODO error
            Toast toast = Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG);
            toast.show();

            Intent i = new Intent(getApplicationContext(), homeActivity.class);
            startActivity(i);
        }

        setListElements();
    }

    public void onFabPress(View v){
        Intent i = new Intent(getApplicationContext(), homeActivity.class);
        startActivity(i);
    }

    public void onEditPress(View v) {
        Intent intent = new Intent(getApplicationContext(), EditIndividualActivity.class);
        intent.putExtra("id", individual.getXref());
        startActivity(intent);
    }
}
