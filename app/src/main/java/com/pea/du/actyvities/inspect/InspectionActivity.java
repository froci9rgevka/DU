package com.pea.du.actyvities.inspect;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.ViewFlipper;
import com.pea.du.R;
import com.pea.du.db.local.methods.ReadMethods;

import java.util.ArrayList;

import static com.pea.du.db.local.data.Contract.GuestEntry.ADDRESS_TABLE_NAME;


@TargetApi(Build.VERSION_CODES.N)
public class InspectionActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    Calendar dateAndTime= Calendar.getInstance();

    Button dateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //////////////////////////////////////////////////////////////////////////////////////////////
        // 18.07.2017 PEA add flipper
        ViewFlipper content_flipper = (ViewFlipper) findViewById(R.id.content_flipper);

        // Create a View and adding them to flipper
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int layouts[] = new int[]{R.layout.content_common, R.layout.content_specifications,
                R.layout.content_results, R.layout.content_commission};
        for (int layout : layouts)
            content_flipper.addView(inflater.inflate(layout, null));

        //////////////////////////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////////////////////////
        // 18.07.2017 PEA add spinner
        Spinner spinner = (Spinner) findViewById(R.id.content_common_adress_spinner);


        // Добавление элемента
        //SQLiteDatabase db_write = dbHelper.getWritableDatabase();
        //WriteMethods.setAddress(db_write,"Рижская улица");

        ArrayList addresses = ReadMethods.getStaticValues(this, ADDRESS_TABLE_NAME,null, null);

        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,addresses);
        spinner.setAdapter(adp);

        //////////////////////////////////////////////////////////////////////////////////////////////

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inspection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        ViewFlipper content_flipper = (ViewFlipper) findViewById(R.id.content_flipper);


        if (id == R.id.bar_button_common) {
            content_flipper.setDisplayedChild(0);

        } else if (id == R.id.bar_button_specifications) {
            content_flipper.setDisplayedChild(1);

        } else if (id == R.id.bar_button_results) {
            content_flipper.setDisplayedChild(2);

        } else if (id == R.id.bar_button_commission) {
            content_flipper.setDisplayedChild(3);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.bar_button_main_menu) {
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onResultButtonClick(View view) {
        Intent intent = new Intent(InspectionActivity.this, InspectionDetailsActivity.class);
        startActivity(intent);
    }


    // отображаем диалоговое окно для выбора даты
    public void setDate(View v) {
        switch(v.getId())
        {
            case R.id.dateButton_1:
                dateButton=(Button)findViewById(R.id.dateButton_1);
                break;
            case R.id.dateButton_2:
                dateButton=(Button)findViewById(R.id.dateButton_2);
                break;
            default:
                throw new RuntimeException("Unknown button ID");
        }

        new DatePickerDialog(InspectionActivity.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    // установка начальных даты и времени
    private void setInitialDateTime() {

        dateButton.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
                       // | DateUtils.FORMAT_SHOW_TIME));
    }

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

}