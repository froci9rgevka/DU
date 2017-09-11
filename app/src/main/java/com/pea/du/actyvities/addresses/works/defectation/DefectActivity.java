package com.pea.du.actyvities.addresses.works.defectation;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.pea.du.R;
import com.pea.du.actyvities.addresses.works.photo.PhotoGridFragment;
import com.pea.du.data.Act;
import com.pea.du.data.Defect;
import com.pea.du.data.StaticValue;
import com.pea.du.db.local.methods.ReadMethods;
import com.pea.du.db.remote.methods.SaveDefect;
import com.pea.du.flags.Flags;

import java.util.ArrayList;

import static com.pea.du.data.StaticValue.getNameById;
import static com.pea.du.db.local.data.Contract.GuestEntry.*;
import static com.pea.du.flags.Flags.actId;
import static com.pea.du.flags.Flags.addressId;
import static com.pea.du.flags.Flags.workId;

public class DefectActivity extends AppCompatActivity {

    Spinner sDefect_type;
    Spinner sConstructiveElement;
    EditText etPorch;
    EditText etFloor;
    EditText etFlat;
    EditText etDescription;
    EditText etCurrency;
    Spinner sMeasure;

    Button bAddDefect;

    android.support.v4.app.FragmentTransaction fragmentTransaction;
    android.support.v4.app.Fragment photoGridFragment;

    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_defect);

        Flags.currentContext=this;

        objInit();

        setDefectActivityContent();
    }

    private void objInit(){
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.add_defect_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        sDefect_type = (Spinner) findViewById(R.id.activity_defect_type);
        sConstructiveElement = (Spinner) findViewById(R.id.activity_defect_constructive_element);
        etPorch = (EditText) findViewById(R.id.activity_defect_porch);
        etFloor = (EditText) findViewById(R.id.activity_defect_floor);
        etFlat = (EditText) findViewById(R.id.activity_defect_flat);
        etDescription = (EditText) findViewById(R.id.activity_defect_description);
        sMeasure = (Spinner) findViewById(R.id.activity_defect_measure);
        etCurrency = (EditText) findViewById(R.id.activity_defect_currency);

        bAddDefect = (Button) findViewById(R.id.activity_defect_addDefect);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        photoGridFragment = new PhotoGridFragment();

        ArrayList measures = ReadMethods.getStaticValues(this, DEFECT_MEASURE_TABLE_NAME, null, null);
        ArrayList constructiveElements = ReadMethods.getStaticValues(this, DEFECT_CONSTRUCTIVE_ELEMENT_TABLE_NAME, null, null);
        ArrayList types = ReadMethods.getStaticValues(this, DEFECT_TYPE_TABLE_NAME, null, null);

        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,measures);
        ArrayAdapter<String> ceAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,constructiveElements);
        ArrayAdapter<String> tAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,types);

        sMeasure.setAdapter(mAdapter);
        sConstructiveElement.setAdapter(ceAdapter);
        sDefect_type.setAdapter(tAdapter);
    }

    // Наполняем активити контентом
    public void setDefectActivityContent() {
        // Загружаем адрес используя id
        String address = getNameById(DefectActivity.this, ADDRESS_TABLE_NAME, addressId);
        // Отображаем адресс
        toolbar.setTitle(address);

        if (workId==-1) {
            // Если создаём новый дефект
            bAddDefect.setVisibility(View.VISIBLE);
        }
        else {
            // Если редактируем старый дефект
            bAddDefect.setVisibility(View.GONE);
            setPhotoGridFragmentContent();
            LoadData loadData = new LoadData();
            loadData.execute("");
        }
    }

    // Наполняем фрагмент активити с фотофиксацией
    public void setPhotoGridFragmentContent(){
        fragmentTransaction.replace(R.id.photoGrid_fragment, photoGridFragment);
        fragmentTransaction.commit();
    }

    private class LoadData extends AsyncTask<String,String,String> {

        Defect currentDefect = new Defect();

        @Override
        protected String doInBackground(String... strings) {


            // Загружаем дефект используя id
            currentDefect.setServerId(workId);
            currentDefect.getDefectById(DefectActivity.this);

            return null;
        }

        @Override
        protected void onPostExecute(String r)
        {

            // Отображаем дефект
            sDefect_type.setSelection(currentDefect.getType().getServerId()-1);
            sConstructiveElement.setSelection(currentDefect.getConstructiveElement().getServerId()-1);
            etPorch.setText(currentDefect.getPorch());
            etFloor.setText(currentDefect.getFloor());
            etFlat.setText(currentDefect.getFlat());
            etDescription.setText(currentDefect.getDescription());
            sMeasure.setSelection(currentDefect.getMeasure().getServerId()-1);
            etCurrency.setText(currentDefect.getCurrency());
        }
    }

    public void onAddDefectButtonClick(View view) {
        Defect defect = getDefectFromFields();

        SaveDefect saveDefect = new SaveDefect(this, defect);
        saveDefect.execute("");

    }

    // Создаём дефект из полей активити
    private Defect getDefectFromFields(){
        StaticValue type = new StaticValue(DEFECT_TYPE_TABLE_NAME, DEFECT_TYPE);
        type.setName(sDefect_type.getSelectedItem().toString());
        type.getStaticByName(this);

        StaticValue constructiveElement = new StaticValue(DEFECT_CONSTRUCTIVE_ELEMENT_TABLE_NAME, DEFECT_CONSTRUCTIVE_ELEMENT);
        constructiveElement.setName(sConstructiveElement.getSelectedItem().toString());
        constructiveElement.getStaticByName(this);

        StaticValue measure = new StaticValue(DEFECT_MEASURE_TABLE_NAME, DEFECT_MEASURE);
        measure.setName(sMeasure.getSelectedItem().toString());
        measure.getStaticByName(this);

        Act act = new Act();
        act.setServerId(actId);

        Defect defect = new Defect(
                act,
                type,
                constructiveElement,
                etPorch.getText().toString(),
                etFloor.getText().toString(),
                etFlat.getText().toString(),
                etDescription.getText().toString(),
                measure,
                etCurrency.getText().toString()
        );

        return defect;
    }

}
