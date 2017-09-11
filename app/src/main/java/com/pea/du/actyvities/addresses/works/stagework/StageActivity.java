package com.pea.du.actyvities.addresses.works.stagework;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.pea.du.R;
import com.pea.du.actyvities.addresses.works.photo.PhotoGridFragment;
import com.pea.du.data.*;
import com.pea.du.db.local.methods.ReadMethods;
import com.pea.du.db.remote.methods.SaveWork;
import com.pea.du.flags.Flags;

import java.util.ArrayList;

import static com.pea.du.data.StaticValue.getNameById;
import static com.pea.du.data.Work.getWorkById;
import static com.pea.du.db.local.data.Contract.GuestEntry.*;
import static com.pea.du.flags.Flags.*;

public class StageActivity extends AppCompatActivity {

    Spinner sWorkName;
    Spinner sMeasure;
    Spinner sContractor;
    EditText etCurrency;
    EditText etDescription;

    LinearLayout llContractor;

    ToggleButton tbSubContractor;
    Button bAddStage;

    android.support.v4.app.FragmentTransaction fragmentTransaction;
    android.support.v4.app.Fragment photoGridFragment;

    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_stage);

        Flags.currentContext = this;

        objInit();

        setStageActivityContent();
    }

    private void objInit() {
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.add_stage_work_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        sWorkName = (Spinner) findViewById(R.id.activity_stage_workName);
        sMeasure = (Spinner) findViewById(R.id.activity_stage_measure);
        sContractor = (Spinner) findViewById(R.id.activity_stage_contractor_s);
        etCurrency = (EditText) findViewById(R.id.activity_stage_currency);
        etDescription = (EditText) findViewById(R.id.activity_stage_description);

        llContractor = (LinearLayout) findViewById(R.id.activity_stage_contractorLayout);

        tbSubContractor = (ToggleButton) findViewById(R.id.activity_stage_subContractorButton);
        tbSubContractor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    llContractor.setVisibility(View.VISIBLE);
                else
                    llContractor.setVisibility(View.GONE);
            }
        });

        bAddStage = (Button) findViewById(R.id.activity_stage_addWork);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        photoGridFragment = new PhotoGridFragment();

        ArrayList measures = ReadMethods.getStaticValues(this, DEFECT_MEASURE_TABLE_NAME, null, null);
        ArrayList workNames = ReadMethods.getStaticValues(this, WORK_NAME_TABLE_NAME, null, null);
        ArrayList contractors = ReadMethods.getStaticValues(this, CONTRACTOR_TABLE_NAME, null, null);

        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, measures);
        ArrayAdapter<String> wnAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, workNames);
        ArrayAdapter<String> cAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, contractors);

        sWorkName.setAdapter(wnAdapter);
        sMeasure.setAdapter(mAdapter);
        sContractor.setAdapter(cAdapter);
    }

    // Наполняем активити контентом
    public void setStageActivityContent() {
        // Загружаем адрес используя id
        String address = getNameById(StageActivity.this, ADDRESS_TABLE_NAME, addressId);
        // Отображаем адресс
        toolbar.setTitle(address);

        if (workId == -1) {
            // Если создаём новый дефект
            bAddStage.setVisibility(View.VISIBLE);
        } else {
            // Если редактируем старый дефект
            bAddStage.setVisibility(View.GONE);
            setFragmentContent();
            StageActivity.LoadData loadData = new StageActivity.LoadData();
            loadData.execute("");
        }
    }

    // Наполняем фрагмент активити с фотофиксацией
    private void setFragmentContent() {
        fragmentTransaction.replace(R.id.photoGrid_fragment, photoGridFragment);
        fragmentTransaction.commit();
    }

    private class LoadData extends AsyncTask<String, String, String> {

        Work work = new Work();

        @Override
        protected String doInBackground(String... strings) {
            // Загружаем дефект используя id
            work = getWorkById(workId);

            return null;
        }

        @Override
        protected void onPostExecute(String r) {
            // Отображаем дефект
            sWorkName.setSelection(work.getName().getServerId() - 1);
            sMeasure.setSelection(work.getMeasure().getServerId() - 1);
            sContractor.setSelection(work.getContractor().getServerId()-1);
            etCurrency.setText(work.getCnt());
            etDescription.setText(work.getDescr());
            tbSubContractor.setChecked(work.getSubcontract());
        }
    }

    public void onAddWorkButtonClick(View view) {
        Work work = getWorkFromFields();

        SaveWork saveWork = new SaveWork(work);
        saveWork.execute("");
    }

    // Создаём дефект из полей активити
    private Work getWorkFromFields() {
        StaticValue contractor = null;

        StaticValue name = new StaticValue(WORK_NAME_TABLE_NAME, WORK_NAME);
        name.setName(sWorkName.getSelectedItem().toString());
        name.getStaticByName(this);

        StaticValue measure = new StaticValue(DEFECT_MEASURE_TABLE_NAME, DEFECT_MEASURE);
        measure.setName(sMeasure.getSelectedItem().toString());
        measure.getStaticByName(this);

        StaticValue stage = new StaticValue(STAGE_TABLE_NAME, STAGE);
        stage.setName(workType);
        stage.getStaticByName(this);

        User user = new User(authorId);
        StaticValue address = new StaticValue(addressId);

        Boolean subcontract = false;
        if (tbSubContractor.isChecked()) {
            subcontract = true;

            contractor = new StaticValue(CONTRACTOR_TABLE_NAME, CONTRACTOR);
            contractor.setName(sContractor.getSelectedItem().toString());
            contractor.getStaticByName(this);
        }

        Work work = new Work(
                user,
                address,
                name,
                stage,
                etCurrency.getText().toString(),
                measure,
                etDescription.getText().toString(),
                subcontract,
                contractor
        );

        return work;
    }
}