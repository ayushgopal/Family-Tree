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
                        String id = relatives.get(i).getAge();
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

        Family parentsFamily;
        try {
            parentsFamily = individual.getFamiliesWhereChild().get(0).getFamily();
        } catch (Exception E) {
            parentsFamily = null;
        }
        if (parentsFamily == null) {
            ListDetails listDetails = new ListDetails();
            listDetails.setRelation("Parents");
            listDetails.setName("N/A");
            listDetails.setAge("details not available");
            ret.add(listDetails);
        } else {
            Individual dad = parentsFamily.getHusband().getIndividual();
            Individual mom = parentsFamily.getWife().getIndividual();
            List<IndividualReference> siblings = parentsFamily.getChildren();
            if (dad != null) {
                ListDetails listDetails = new ListDetails();
                listDetails.setRelation("Father");
                listDetails.setName(dad.getFormattedName());
                listDetails.setAge(getIndividualAge(dad));
                ret.add(listDetails);
            }
            if (mom != null) {
                ListDetails listDetails = new ListDetails();
                listDetails.setRelation("Mother");
                listDetails.setName(mom.getFormattedName());
                listDetails.setAge(getIndividualAge(mom));
                ret.add(listDetails);
            }
            if (siblings.size() > 0) { // TODO check if the sibling is the person itself
                for (IndividualReference r : siblings) {
                    Individual i = r.getIndividual();

                    ListDetails listDetails = new ListDetails();
                    listDetails.setRelation("Sibling"); // TODO add sibling gender ie sis/bro
                    listDetails.setName(i.getFormattedName());
                    listDetails.setAge(getIndividualAge(i));
                    ret.add(listDetails);
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
            if(individual.getSex().toString().toLowerCase().charAt(0) == 'M') {
                Individual wife = currentFamily.getWife().getIndividual();
                ListDetails listDetails = new ListDetails();
                listDetails.setRelation("Wife");
                listDetails.setName(wife.getFormattedName());
                listDetails.setAge(getIndividualAge(wife));
                ret.add(listDetails);
            } else {
                Individual husband = currentFamily.getHusband().getIndividual();
                ListDetails listDetails = new ListDetails();
                listDetails.setRelation("Husband");
                listDetails.setName(husband.getFormattedName());
                listDetails.setAge(getIndividualAge(husband));
                ret.add(listDetails);
            }

            List<IndividualReference> children = currentFamily.getChildren();
            if (children.size() > 0) {
                for (IndividualReference r : children) {
                    Individual i = r.getIndividual();

                    ListDetails listDetails = new ListDetails();
                    listDetails.setRelation("Child"); // TODO add child gender ie son/daughter
                    listDetails.setName(i.getFormattedName());
                    listDetails.setAge(getIndividualAge(i));
                    ret.add(listDetails);
                }
            }
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
        } else {
            //TODO error
            Toast toast = Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG);
            toast.show();
        }

        setListElements();
    }

    public void onFabPress(View v){
        Intent i = new Intent(getApplicationContext(), homeActivity.class);
        startActivity(i);
    }
}
