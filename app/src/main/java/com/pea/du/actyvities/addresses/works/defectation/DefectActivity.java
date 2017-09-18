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
import static com.pea.du.db.local.methods.ReadMethods.getTypesFronConstructiveElement;
import static com.pea.du.flags.Flags.*;

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
    Button bCancel;
    Button bSave;

    /////Flag отвечающий за коректное отображение типа дефекта
    //Заполнено ли активити
    Boolean isActivityFilled = false;


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

    private void objInit() {
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
        bCancel = (Button) findViewById(R.id.activity_defect_cancel);
        bSave = (Button) findViewById(R.id.activity_defect_save);

        ArrayList measures = ReadMethods.getStaticValues(this, DEFECT_MEASURE_TABLE_NAME, null, null);
        ArrayList constructiveElements = ReadMethods.getStaticValues(this, DEFECT_CONSTRUCTIVE_ELEMENT_TABLE_NAME, null, null);
        ArrayList types = ReadMethods.getStaticValues(this, DEFECT_TYPE_TABLE_NAME, null, null);

        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, measures);
        ArrayAdapter<String> ceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, constructiveElements);
        ArrayAdapter<String> tAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, types);

        sMeasure.setAdapter(mAdapter);
        sConstructiveElement.setAdapter(ceAdapter);
        sDefect_type.setAdapter(tAdapter);

        sConstructiveElement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(((!isActivityFilled) && (workId==null)) || (isActivityFilled)){
                    isActivityFilled = true;
                    StaticValue constructiveElement = new StaticValue(DEFECT_CONSTRUCTIVE_ELEMENT_TABLE_NAME, DEFECT_CONSTRUCTIVE_ELEMENT);
                    constructiveElement.setName(sConstructiveElement.getSelectedItem().toString());
                    constructiveElement.getStaticByName(currentContext);

                    setTypesFromConstructiveElement(constructiveElement.getServerId(), -1);
                }
                else
                    isActivityFilled = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    // Выбираем только типы, которые относятся к выбранному конструктивному элементу
    private void setTypesFromConstructiveElement(Integer constructiveElementId, Integer currentTypeId){
        ArrayList<String> typesNames = new ArrayList<>();
        ArrayList<Integer> typesId = getTypesFronConstructiveElement(currentContext, constructiveElementId);
        Integer typeNumber = 0;
        for (int i = 0; i < typesId.size(); i++) {
            Integer typeId = typesId.get(i);
            typesNames.add(getNameById(currentContext, DEFECT_TYPE_TABLE_NAME, typeId));
            if (typeId==currentTypeId) typeNumber=i;
        }
        ArrayAdapter<String> tAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,typesNames);
        sDefect_type.setAdapter(tAdapter);
        sDefect_type.setSelection(typeNumber);
    }

    // Наполняем активити контентом
    public void setDefectActivityContent() {
        // Загружаем адрес используя id
        String address = getNameById(DefectActivity.this, ADDRESS_TABLE_NAME, addressId);
        // Отображаем адресс
        toolbar.setTitle(address);

        setPhotoGridFragmentContent();

        if (workId==-1) {
            // Если создаём новый дефект
            bAddDefect.setVisibility(View.VISIBLE);
            bCancel.setVisibility(View.VISIBLE);
            bSave.setVisibility(View.GONE);
        }
        else {
            // Если редактируем старый дефект
            bAddDefect.setVisibility(View.GONE);
            bCancel.setVisibility(View.GONE);
            bSave.setVisibility(View.VISIBLE);
            LoadData loadData = new LoadData();
            loadData.execute("");
        }
    }

    // Наполняем фрагмент активити с фотофиксацией
    public void setPhotoGridFragmentContent(){
        android.support.v4.app.Fragment photoGridFragment;
        photoGridFragment = new PhotoGridFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction1;
        fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction1.remove(photoGridFragment);
        fragmentTransaction1.commit();


        android.support.v4.app.FragmentTransaction fragmentTransaction2;
        fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction2.replace(R.id.defect_photoGrid_fragment, photoGridFragment);
        fragmentTransaction2.commit();
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
            sConstructiveElement.setSelection(currentDefect.getConstructiveElement().getServerId()-1);
            setTypesFromConstructiveElement(currentDefect.getConstructiveElement().getServerId(), currentDefect.getType().getServerId());
            etPorch.setText(getTextable(currentDefect.getPorch()));
            etFloor.setText(getTextable(currentDefect.getFloor()));
            etFlat.setText(getTextable(currentDefect.getFlat()));
            etDescription.setText(currentDefect.getDescription());
            sMeasure.setSelection(currentDefect.getMeasure().getServerId()-1);
            etCurrency.setText(getTextable(currentDefect.getCurrency()));
        }
    }

    public void onAddDefectButtonClick(View view) {
        Defect defect = getDefectFromFields();

        SaveDefect saveDefect = new SaveDefect(this, defect);
        saveDefect.execute("");

    }

    public void onCancelButtonClick(View view) {
        onBackPressed();
    }

    // Создаём дефект из полей активити
    private Defect getDefectFromFields(){
        Defect defect = new Defect();

        StaticValue type = new StaticValue(DEFECT_TYPE_TABLE_NAME, DEFECT_TYPE);
        type.setName(sDefect_type.getSelectedItem().toString());
        type.getStaticByName(this);
        defect.setType(type);

        StaticValue constructiveElement = new StaticValue(DEFECT_CONSTRUCTIVE_ELEMENT_TABLE_NAME, DEFECT_CONSTRUCTIVE_ELEMENT);
        constructiveElement.setName(sConstructiveElement.getSelectedItem().toString());
        constructiveElement.getStaticByName(this);
        defect.setConstructiveElement(constructiveElement);

        StaticValue measure = new StaticValue(DEFECT_MEASURE_TABLE_NAME, DEFECT_MEASURE);
        measure.setName(sMeasure.getSelectedItem().toString());
        measure.getStaticByName(this);
        defect.setMeasure(measure);

        Act act = new Act();
        act.setServerId(actId);
        defect.setAct(act);

        String porch = getParsable(etPorch.getText().toString());
        defect.setPorch(porch);

        String floor = getParsable(etFloor.getText().toString());
        defect.setFloor(floor);

        String flat = getParsable(etFlat.getText().toString());
        defect.setFlat(flat);

        String currency = getParsable(etCurrency.getText().toString());
        defect.setCurrency(currency);

        defect.setDescription(etDescription.getText().toString());

        return defect;
    }

    public static String getParsable(String s) {
        try {
            Integer.parseInt(s);
            return s;
        }
        catch (Exception ex){
            return null;
        }
    }
    public static String getTextable(String s){
        if (s==null)
            return "";
        else
            return s;
    }
}
