package com.pea.du.actyvities.addresses.works;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

import com.pea.du.R;
import com.pea.du.actyvities.addresses.works.defectation.DefectActivity;
import com.pea.du.actyvities.addresses.works.stagework.StageActivity;
import com.pea.du.data.Defect;


import java.util.ArrayList;

import static com.pea.du.data.Defect.getDefectsByAct;
import static com.pea.du.data.StaticValue.getNameById;
import static com.pea.du.db.local.data.Contract.GuestEntry.ADDRESS_TABLE_NAME;
import static com.pea.du.flags.Flags.*;


public class WorksActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private ListView listView;

    private ArrayList<String> content;
    private ArrayList<Defect> defectsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_works);

        objInit();

        LoadActs loadActs = new LoadActs();
        loadActs.execute("");

    }

    private void objInit(){
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.defect_toolbar);
        listView = (ListView)findViewById(R.id.defect_acts_list);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                onClick(position);
            }
        });
    }

    //Действие при нажатии на элемент
    private void onClick(int position){

        if (workType.equals(DEFECT)) {
            workId = defectsList.get(position).getServerId();

            Intent intent = new Intent(WorksActivity.this, DefectActivity.class);
            startActivity(intent);
        }
        else {





            Intent intent = new Intent(WorksActivity.this, StageActivity.class);
            startActivity(intent);
        }


    }


    // Загрузка контента на активити
    private class LoadActs extends AsyncTask<String,String,String> {

        private String address = null;


        @Override
        protected String doInBackground(String... strings) {
            // Загружаем адрес используя id
            address = getNameById(WorksActivity.this, ADDRESS_TABLE_NAME, addressId);

            // Закружаем листинг работ
            if(workType.equals(DEFECT))
                defectsList = getDefectsByAct(WorksActivity.this, actId);
            else
            {





            }

            return null;
        }

        @Override
        protected void onPostExecute(String r)
        {
            // Отображаем адресс
            toolbar.setTitle(address);

            // Отображаем листинг работ
            if(workType.equals(DEFECT)){
                ArrayAdapter<Defect> adapter = new ArrayAdapter<Defect>(WorksActivity.this,
                        android.R.layout.simple_list_item_1, defectsList);
                listView.setAdapter(adapter);
            }
            else
            {






            }
        }
    }

    public void onAddButtonClick(View view) {
        workId = -1;

        Intent intent = new Intent(WorksActivity.this, DefectActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LoadActs loadActs = new LoadActs();
        loadActs.execute("");
    }


}
