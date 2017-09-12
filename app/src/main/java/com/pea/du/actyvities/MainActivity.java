package com.pea.du.actyvities;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import com.pea.du.R;
import com.pea.du.actyvities.addresses.AddressesActivity;
import com.pea.du.actyvities.inspect.InspectionActivity;
import com.pea.du.db.remote.methods.LoadData;
import com.pea.du.flags.Flags;
import com.pea.du.tools.gridview.GridViewAdapter;
import com.pea.du.tools.gridview.ImageItem;

import java.util.ArrayList;

import static com.pea.du.flags.Flags.*;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{

    public static ProgressBar mainProgressBar = null;
    public static GridView mainGridView = null;
    private ArrayAdapter<ImageItem> listViewAdapter;

    private SeekBar seekBar;
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        Flags.currentContext=this;

        objInit();

        mainGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        MainActivity.this.onInspectionButtonClick();
                        break;
                    case 1:
                        workType = DEFECT;
                        workStageType = null;
                        MainActivity.this.onDefectButtonClick();
                        break;
                    case 2:
                        workType = STAGE_WORK;
                        workStageType = BEGIN_WORK;
                        MainActivity.this.onDefectButtonClick();
                        break;
                    case 3:
                        workType = STAGE_WORK;
                        workStageType = DURING_WORK;
                        MainActivity.this.onDefectButtonClick();
                        break;
                    case 4:
                        workType = STAGE_WORK;
                        workStageType = END_WORK;
                        MainActivity.this.onDefectButtonClick();
                        break;
                    case 5:
                        workType = STAGE_WORK;
                        workStageType = null;
                        MainActivity.this.onDefectButtonClick();
                        break;
                }
            }
        });


        dbConnect();

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        objInit();
    }

    public void objInit(){
        ArrayList<ImageItem> imageItems = new ArrayList();


        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle("Меню");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        mainProgressBar = (ProgressBar) findViewById(R.id.menuProgressBar);
        mainGridView = (GridView) findViewById(R.id.main_menu_gridview);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);

        if (seekBar.getProgress()==0) {
            imageItems.add(new ImageItem(R.mipmap.comission));
            imageItems.add(new ImageItem(R.mipmap.defect));
            imageItems.add(new ImageItem(R.mipmap.work_start));
            imageItems.add(new ImageItem(R.mipmap.work_progress));
            imageItems.add(new ImageItem(R.mipmap.work_finish));
            imageItems.add(new ImageItem(R.mipmap.new_item));

            mainGridView.setNumColumns(2);
        }
        else
            if (seekBar.getProgress()==1){
                imageItems.add(new ImageItem(R.mipmap.big_comission_button));
                imageItems.add(new ImageItem(R.mipmap.big_defect_button));
                imageItems.add(new ImageItem(R.mipmap.big_begin_button));
                imageItems.add(new ImageItem(R.mipmap.big_mid_button));
                imageItems.add(new ImageItem(R.mipmap.big_end_button));
                imageItems.add(new ImageItem(R.mipmap.big_new_button));

                mainGridView.setNumColumns(2);
            }
        else
            if (seekBar.getProgress()==2){
                imageItems.add(new ImageItem(R.mipmap.small_comission_button));
                imageItems.add(new ImageItem(R.mipmap.small_defect_button));
                imageItems.add(new ImageItem(R.mipmap.small_begin_button));
                imageItems.add(new ImageItem(R.mipmap.small_mid_button));
                imageItems.add(new ImageItem(R.mipmap.small_end_button));
                imageItems.add(new ImageItem(R.mipmap.small_new_button));

                mainGridView.setNumColumns(1);
            }



        listViewAdapter = new GridViewAdapter(this, R.layout.simple_list_view_item, imageItems);
        mainGridView.setAdapter(listViewAdapter);
    }

    public void onInspectionButtonClick() {
        Intent intent = new Intent(MainActivity.this, InspectionActivity.class);
        startActivity(intent);
    }

    public void onDefectButtonClick() {
        Intent intent = new Intent(MainActivity.this, AddressesActivity.class);
        startActivity(intent);
    }

    public void onLoginButtonClick(View view) {
    }

    public static void lockMainActivity(){
        mainProgressBar.setVisibility(View.VISIBLE);
        mainGridView.setEnabled(false);
    }

    public static void unlockMainActivity(){
        mainProgressBar.setVisibility(View.GONE);
        mainGridView.setEnabled(true);
    }

    public void dbConnect(){


        LoadData loadData = new LoadData(this);
        loadData.execute("");

        //lockMainActivity();

        //Controller controller = new Controller(this, LOAD_STATIC_ADDRESS); // последовательно загружаются все статичные данные
        //controller.start();

    }


}
