package com.pea.du.actyvities.defects.address.act;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

import com.pea.du.R;
import com.pea.du.actyvities.defects.address.act.photo.PhotoLibrary;
import com.pea.du.data.Act;
import com.pea.du.data.Defect;


import java.util.ArrayList;

import static com.pea.du.actyvities.defects.address.act.AddActActivity.EXISTING;
import static com.pea.du.actyvities.defects.address.act.AddActActivity.NEW;
import static com.pea.du.data.Defect.getDefectsByAct;


public class DefectActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private ListView listView;

    private ArrayList<Defect> defectsList;

    private Act currentAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_defect);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.defect_toolbar);
        listView = (ListView)findViewById(R.id.defect_acts_list);

        currentAct = getIntent().getParcelableExtra("Act");

        toolbar.setTitle(currentAct.getAddress().getName());
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fillList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                Intent intent = new Intent(DefectActivity.this, AddActActivity.class);
                intent.putExtra("Act", currentAct);
                intent.putExtra("Defect", (Defect) defectsList.get(position));
                intent.putExtra("Flag", EXISTING);
                startActivity(intent);
            }
        });
    }

    public void fillList () {
        defectsList = getDefectsByAct(this, currentAct);

        final ArrayAdapter<Defect> adapter = new ArrayAdapter<Defect>(this,
                android.R.layout.simple_list_item_1, defectsList);
        listView.setAdapter(adapter);
    }

    public void onAddButtonClick(View view) {
        Intent intent = new Intent(DefectActivity.this, AddActActivity.class);
        intent.putExtra("Act", currentAct);
        intent.putExtra("Flag", NEW);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        fillList();
    }

}
