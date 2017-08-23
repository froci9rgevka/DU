package com.pea.du.actyvities.defects.address.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import com.pea.du.R;
import com.pea.du.data.Act;
import com.pea.du.data.Defect;
import com.pea.du.data.StaticValue;
import com.pea.du.db.methods.ReadMethods;
import com.pea.du.db.methods.WriteMethods;
import com.pea.du.web.client.Controller;

import java.util.ArrayList;

import static com.pea.du.db.data.Contract.GuestEntry.*;
import static com.pea.du.web.client.Contract.LOAD_STATIC_ADDRESS;
import static com.pea.du.web.client.Contract.SAVE_ACT;
import static com.pea.du.web.client.Contract.SAVE_DEFECT;

public class AddActActivity extends AppCompatActivity {

    Spinner sDefect_type;
    Spinner sConstructiveElement;
    EditText etPorch;
    EditText etFloor;
    EditText etFlat;
    EditText etDescription;
    EditText etCurrency;
    Spinner sMeasure;

    private Act currentAct;

    private android.support.v7.widget.Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_act);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.add_act_toolbar);
        toolbar.setTitle("Новый акт");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        currentAct = getIntent().getParcelableExtra("Act");

        sDefect_type = (Spinner) findViewById(R.id.activity_defect_type);
        sConstructiveElement = (Spinner) findViewById(R.id.activity_defect_constructive_element);
        etPorch = (EditText) findViewById(R.id.activity_defect_porch);
        etFloor = (EditText) findViewById(R.id.activity_defect_floor);
        etFlat = (EditText) findViewById(R.id.activity_defect_flat);
        etDescription = (EditText) findViewById(R.id.activity_defect_description);
        sMeasure = (Spinner) findViewById(R.id.activity_defect_measure);
        etCurrency = (EditText) findViewById(R.id.activity_defect_currency);

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

    public void onAddButtonClick(View view) {
        StaticValue type = new StaticValue(DEFECT_TYPE_TABLE_NAME, DEFECT_TYPE);
        type.setName(sDefect_type.getSelectedItem().toString());
        type.getStaticByName(this);

        StaticValue constructiveElement = new StaticValue(DEFECT_CONSTRUCTIVE_ELEMENT_TABLE_NAME, DEFECT_CONSTRUCTIVE_ELEMENT);
        constructiveElement.setName(sConstructiveElement.getSelectedItem().toString());
        constructiveElement.getStaticByName(this);

        StaticValue measure = new StaticValue(DEFECT_MEASURE_TABLE_NAME, DEFECT_MEASURE);
        measure.setName(sMeasure.getSelectedItem().toString());
        measure.getStaticByName(this);

        Defect defect = new Defect(
                currentAct,
                type,
                constructiveElement,
                etPorch.getText().toString(),
                etFloor.getText().toString(),
                etFlat.getText().toString(),
                etDescription.getText().toString(),
                measure,
                etCurrency.getText().toString()
        );

        Controller controller = new Controller(this, SAVE_DEFECT, defect); // последовательно загружаются все статичные данные
        controller.start();

    }
}
