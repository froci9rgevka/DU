package com.pea.du.actyvities.addresses.works.stagework;

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

import static com.pea.du.actyvities.addresses.works.defectation.DefectActivity.getParsable;
import static com.pea.du.actyvities.addresses.works.defectation.DefectActivity.getTextable;
import static com.pea.du.data.StaticValue.getNameById;
import static com.pea.du.data.Work.getWorkById;
import static com.pea.du.db.local.data.Contract.GuestEntry.*;
import static com.pea.du.flags.Flags.*;

public class StageActivity extends AppCompatActivity {

    Spinner sStage;
    Spinner sWorkName;
    Spinner sMeasure;
    Spinner sContractor;
    EditText etCurrency;
    EditText etDescription;

    LinearLayout llContractor;

    Switch tbSubContractor;
    Button bAddStage;
    Button bCancel;
    Button bSave;

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

        sStage = (Spinner) findViewById(R.id.activity_stage_stage);
        sWorkName = (Spinner) findViewById(R.id.activity_stage_workName);
        sMeasure = (Spinner) findViewById(R.id.activity_stage_measure);
        sContractor = (Spinner) findViewById(R.id.activity_stage_contractor_s);
        etCurrency = (EditText) findViewById(R.id.activity_stage_currency);
        etDescription = (EditText) findViewById(R.id.activity_stage_description);

        llContractor = (LinearLayout) findViewById(R.id.activity_stage_contractorLayout);

        tbSubContractor = (Switch) findViewById(R.id.activity_stage_subContractorButton);
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
        bCancel = (Button) findViewById(R.id.activity_stage_cancel);
        bSave = (Button) findViewById(R.id.activity_stage_save);

        photoGridFragment = new PhotoGridFragment();

        ArrayList stages = ReadMethods.getStaticValues(this, STAGE_TABLE_NAME, null, null);
        ArrayList measures = ReadMethods.getStaticValues(this, DEFECT_MEASURE_TABLE_NAME, null, null);
        ArrayList workNames = ReadMethods.getStaticValues(this, WORK_NAME_TABLE_NAME, null, null);
        ArrayList contractors = ReadMethods.getStaticValues(this, CONTRACTOR_TABLE_NAME, null, null);

        ArrayAdapter<String> sAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, stages);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, measures);
        ArrayAdapter<String> wnAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, workNames);
        ArrayAdapter<String> cAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, contractors);

        sStage.setAdapter(sAdapter);
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
            bCancel.setVisibility(View.VISIBLE);
            bSave.setVisibility(View.GONE);

            if(!(workStageType==null)) {
                StaticValue stage = new StaticValue(STAGE_TABLE_NAME, STAGE);
                stage.setName(workStageType);
                stage.getStaticByName(this);

                sStage.setSelection(stage.getServerId() - 1);
            }
        } else {
            // Если редактируем старый дефект
            bAddStage.setVisibility(View.GONE);
            bCancel.setVisibility(View.GONE);
            bSave.setVisibility(View.VISIBLE);
            setFragmentContent();
            StageActivity.LoadData loadData = new StageActivity.LoadData();
            loadData.execute("");
        }
    }

    // Наполняем фрагмент активити с фотофиксацией
    private void setFragmentContent() {
        android.support.v4.app.Fragment photoGridFragment;
        photoGridFragment = new PhotoGridFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction1;
        fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction1.remove(photoGridFragment);
        fragmentTransaction1.commit();


        android.support.v4.app.FragmentTransaction fragmentTransaction2;
        fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction2.replace(R.id.stage_photoGrid_fragment, photoGridFragment);
        fragmentTransaction2.commit();
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
            sStage.setSelection(work.getStage().getServerId() - 1);
            sWorkName.setSelection(work.getName().getServerId() - 1);
            sMeasure.setSelection(work.getMeasure().getServerId() - 1);
            tbSubContractor.setChecked(work.getSubcontract());
            sContractor.setSelection(work.getContractor().getServerId()-1);
            etCurrency.setText(getTextable(work.getCnt()));
            etDescription.setText(work.getDescr());
            tbSubContractor.setChecked(work.getSubcontract());
        }
    }

    public void onAddWorkButtonClick(View view) {
        Work work = getWorkFromFields();

        SaveWork saveWork = new SaveWork(work);
        saveWork.execute("");
    }

    public void onCancelButtonClick(View view) {
        onBackPressed();
    }

    // Создаём дефект из полей активити
    private Work getWorkFromFields() {
        Work work = new Work();
        StaticValue contractor = null;

        StaticValue name = new StaticValue(WORK_NAME_TABLE_NAME, WORK_NAME);
        name.setName(sWorkName.getSelectedItem().toString());
        name.getStaticByName(this);
        work.setName(name);

        StaticValue measure = new StaticValue(DEFECT_MEASURE_TABLE_NAME, DEFECT_MEASURE);
        measure.setName(sMeasure.getSelectedItem().toString());
        measure.getStaticByName(this);
        work.setMeasure(measure);

        StaticValue stage = new StaticValue(STAGE_TABLE_NAME, STAGE);
        stage.setName(sStage.getSelectedItem().toString());
        stage.getStaticByName(this);
        work.setStage(stage);

        User user = new User(authorId);
        work.setUser(user);

        StaticValue address = new StaticValue(addressId);
        work.setAddress(address);

        Boolean subcontract = false;
        if (tbSubContractor.isChecked()) {
            subcontract = true;

            contractor = new StaticValue(CONTRACTOR_TABLE_NAME, CONTRACTOR);
            contractor.setName(sContractor.getSelectedItem().toString());
            contractor.getStaticByName(this);
        }

        work.setSubcontract(subcontract);
        work.setContractor(contractor);

        String currency = getParsable(etCurrency.getText().toString());
        work.setCnt(currency);

        work.setDescr(etDescription.getText().toString());

        return work;
    }
}