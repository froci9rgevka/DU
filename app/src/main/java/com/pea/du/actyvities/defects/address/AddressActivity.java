package com.pea.du.actyvities.defects.address;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.pea.du.R;
import com.pea.du.actyvities.defects.address.act.DefectActivity;
import com.pea.du.data.Act;
import com.pea.du.data.StaticValue;
import com.pea.du.data.User;
import com.pea.du.db.methods.ReadMethods;
import com.pea.du.db.methods.WriteMethods;
import com.pea.du.web.client.Controller;

import java.util.ArrayList;
import java.util.Date;

import static com.pea.du.db.data.Contract.GuestEntry.ADDRESS_TABLE_NAME;
import static com.pea.du.web.client.Contract.SAVE_ACT;

public class AddressActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private ListView listView;

    private ArrayList<StaticValue> staticValueList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_adress);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.adress_toolbar);
        listView = (ListView)findViewById(R.id.adress_list);

        final User user = getIntent().getParcelableExtra("User");

        toolbar.setTitle("Адреса");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fillList();

        // вызываем активити акта при выборе акта
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                Act currentAct = new Act(user, staticValueList.get(position));

                if (!currentAct.isActInDB(getApplicationContext())) {
                    currentAct.setCreateDate(new Date());
                    Controller controller = new Controller(getBaseContext(), SAVE_ACT, currentAct); // последовательно загружаются все статичные данные
                    controller.start();
                    return;
                }

                Intent intent = new Intent(AddressActivity.this, DefectActivity.class);
                intent.putExtra("Act", currentAct);
                startActivity(intent);
            }
        });
    }

    private void fillList() {
        staticValueList = ReadMethods.getStaticValues(this, ADDRESS_TABLE_NAME, null, null);

        final ArrayAdapter<StaticValue> adapter =
                new ArrayAdapter<StaticValue>(this, android.R.layout.simple_list_item_1, staticValueList);
        listView.setAdapter(adapter);

    }


}
