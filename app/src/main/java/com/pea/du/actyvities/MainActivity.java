package com.pea.du.actyvities;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
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

    private android.support.v7.widget.Toolbar toolbar;

    Button bDefect;
    Button bStage_Work;
    Button bBegin_Stage;
    Button bDuring_Stage;
    Button bEnd_Stage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        Flags.currentContext=this;

        objInit();

        setContent();
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

    private void objInit(){
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

        bDefect = (Button) findViewById(R.id.activity_main_bDefect);
        bStage_Work = (Button) findViewById(R.id.activity_main_bStage_Work);
        bBegin_Stage = (Button) findViewById(R.id.activity_main_bBegin_Stage);
        bDuring_Stage = (Button) findViewById(R.id.activity_main_bDuring_Stage);
        bEnd_Stage = (Button) findViewById(R.id.activity_main_bEnd_Stage);
    }

    private void setContent(){
        bDefect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workType = DEFECT;
                workStageType = null;
                Intent intent = new Intent(MainActivity.this, AddressesActivity.class);
                startActivity(intent);
            }
        });
        bStage_Work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workType = STAGE_WORK;
                workStageType = null;
                Intent intent = new Intent(MainActivity.this, AddressesActivity.class);
                startActivity(intent);
            }
        });
        bBegin_Stage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workType = STAGE_WORK;
                workStageType = BEGIN_WORK;
                Intent intent = new Intent(MainActivity.this, AddressesActivity.class);
                startActivity(intent);
            }
        });
        bDuring_Stage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workType = STAGE_WORK;
                workStageType = DURING_WORK;
                Intent intent = new Intent(MainActivity.this, AddressesActivity.class);
                startActivity(intent);
            }
        });
        bEnd_Stage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workType = STAGE_WORK;
                workStageType = END_WORK;
                Intent intent = new Intent(MainActivity.this, AddressesActivity.class);
                startActivity(intent);
            }
        });

        dbConnect();
    }

    public void onLoginButtonClick(View view) {
    }

    public static void lockMainActivity(){
        mainProgressBar.setVisibility(View.VISIBLE);
    }

    public static void unlockMainActivity(){
        mainProgressBar.setVisibility(View.GONE);
    }

    public void dbConnect(){
        LoadData loadData = new LoadData(this);
        loadData.execute("");
    }


}
