package com.pea.du.actyvities.addresses;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import com.pea.du.R;
import com.pea.du.actyvities.addresses.works.WorksActivity;
import com.pea.du.data.Act;
import com.pea.du.data.StaticValue;
import com.pea.du.data.User;
import com.pea.du.db.methods.ReadMethods;
import com.pea.du.web.client.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static com.pea.du.actyvities.MainActivity.*;
import static com.pea.du.db.data.Contract.GuestEntry.ADDRESS;
import static com.pea.du.db.data.Contract.GuestEntry.ADDRESS_TABLE_NAME;
import static com.pea.du.web.client.Contract.SAVE_ACT;

public class AddressesActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private ListView listView;
    private EditText addressText;

    public static String flag = null;

    private ArrayList<String> currentAddressList = new ArrayList<String>();
    private ArrayList<String> defaultAddressList = new ArrayList<String>();
    private ArrayList<StaticValue> staticValueList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_adress);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.adress_toolbar);
        listView = (ListView)findViewById(R.id.address_listview);
        addressText = (EditText)findViewById(R.id.address_edittext);

        final User user = getIntent().getParcelableExtra("User");
        flag = getIntent().getStringExtra("Work_Type");

        LoadAddresses loadAddresses = new LoadAddresses();
        loadAddresses.execute("");

        toolbar.setTitle("Адреса");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        // Делаем поиск по адресам
        addressText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
                parseList(editable);
                fillList();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });


        // вызываем активити акта при выборе акта
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                // Переводим название в объект адрес
                StaticValue staticValue = new StaticValue(ADDRESS_TABLE_NAME, ADDRESS);
                staticValue.setName(currentAddressList.get(position));
                staticValue.getStaticByName(AddressesActivity.this);

                Act currentAct = new Act(user, staticValue);

                if (flag.equals(DEFECT)) {
                    if (!currentAct.isActInDB(getApplicationContext())) {
                        currentAct.setCreateDate(new Date());
                        Controller controller = new Controller(AddressesActivity.this, SAVE_ACT, currentAct); // последовательно загружаются все статичные данные
                        controller.start();
                        return;
                    }
                }

                Intent intent = new Intent(AddressesActivity.this, WorksActivity.class);
                intent.putExtra("Act", currentAct);
                intent.putExtra("Work_Type", flag);
                startActivity(intent);
            }
        });
    }

    private class LoadAddresses extends AsyncTask<String,String,String>{


        @Override
        protected String doInBackground(String... strings) {
            staticValueList = ReadMethods.getStaticValues(AddressesActivity.this, ADDRESS_TABLE_NAME, null, null);
            for (StaticValue staticValue: staticValueList) {
                currentAddressList.add(staticValue.getName());
            }
            Collections.sort(currentAddressList);
            defaultAddressList.addAll(currentAddressList);

            return null;
        }

        @Override
        protected void onPostExecute(String r)
        {
            fillList();
        }
    }

    private void fillList() {
        final ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, currentAddressList);
        listView.setAdapter(adapter);
    }

    private void parseList(Editable editable){
        currentAddressList = new ArrayList<>();
        currentAddressList.addAll(defaultAddressList);
        ArrayList<String> newAddressList = new ArrayList();
        for (String s: currentAddressList) {
            if (s.contains(editable))
                newAddressList.add(s);
        }
        currentAddressList = newAddressList;
    }

}
