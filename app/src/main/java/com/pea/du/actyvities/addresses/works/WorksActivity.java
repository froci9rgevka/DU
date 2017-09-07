package com.pea.du.actyvities.addresses.works;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

import com.pea.du.R;
import com.pea.du.actyvities.addresses.works.defectation.AddActActivity;
import com.pea.du.data.Act;
import com.pea.du.data.Defect;


import java.util.ArrayList;

import static com.pea.du.actyvities.addresses.works.defectation.AddActActivity.EXISTING;
import static com.pea.du.actyvities.addresses.works.defectation.AddActActivity.NEW;
import static com.pea.du.data.Defect.getDefectsByAct;


public class WorksActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private ListView listView;

    private ArrayList<Defect> defectsList;

    private Act currentAct;

    private String currentWorkType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_defect);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.defect_toolbar);
        listView = (ListView)findViewById(R.id.defect_acts_list);

        currentAct = getIntent().getParcelableExtra("Act");
        currentWorkType = getIntent().getStringExtra("Work_Type");

        toolbar.setTitle(currentAct.getAddress().getName());
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        LoadActs loadActs = new LoadActs();
        loadActs.execute("");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                Intent intent = new Intent(WorksActivity.this, AddActActivity.class);
                intent.putExtra("Act", currentAct);
                intent.putExtra("Defect", (Defect) defectsList.get(position));
                intent.putExtra("isExist", EXISTING);
                startActivity(intent);
            }
        });
    }


    private class LoadActs extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            defectsList = getDefectsByAct(WorksActivity.this, currentAct);

            return null;
        }

        @Override
        protected void onPostExecute(String r)
        {
            final ArrayAdapter<Defect> adapter = new ArrayAdapter<Defect>(WorksActivity.this,
                    android.R.layout.simple_list_item_1, defectsList);
            listView.setAdapter(adapter);
        }
    }

    public void onAddButtonClick(View view) {
        Intent intent = new Intent(WorksActivity.this, AddActActivity.class);
        intent.putExtra("Act", currentAct);
        intent.putExtra("Flag", NEW);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LoadActs loadActs = new LoadActs();
        loadActs.execute("");
    }


}
