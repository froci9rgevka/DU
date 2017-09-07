package com.pea.du.actyvities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import com.pea.du.R;
import com.pea.du.actyvities.addresses.AddressesActivity;
import com.pea.du.actyvities.inspect.InspectionActivity;
import com.pea.du.data.User;
import com.pea.du.tools.gridview.GridViewAdapter;
import com.pea.du.tools.gridview.ImageItem;
import com.pea.du.web.client.Controller;

import java.util.ArrayList;

import static com.pea.du.web.client.Contract.*;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{

    final static public String DEFECT = "Defect";
    final static public String BEGIN_WORK = "Begin";
    final static public String DURING_WORK = "During";
    final static public String END_WORK = "End";

    private GridView gridView;
    private ArrayAdapter<ImageItem> listViewAdapter;
    private User currentUser;

    private SeekBar seekBar;
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        objInit();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        MainActivity.this.onInspectionButtonClick();
                        break;
                    case 1:
                        MainActivity.this.onDefectButtonClick();
                        break;
                }
            }
        });


        dbConnect();

        currentUser = getIntent().getParcelableExtra("User");
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

        gridView = (GridView) findViewById(R.id.main_menu_gridview);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);

        if (seekBar.getProgress()==0) {
            imageItems.add(new ImageItem(R.mipmap.comission));
            imageItems.add(new ImageItem(R.mipmap.defect));
            imageItems.add(new ImageItem(R.mipmap.new_item));
            imageItems.add(new ImageItem(R.mipmap.new_item));
            imageItems.add(new ImageItem(R.mipmap.new_item));
            imageItems.add(new ImageItem(R.mipmap.new_item));

            gridView.setNumColumns(2);
        }
        else
            if (seekBar.getProgress()==1){
                imageItems.add(new ImageItem(R.mipmap.big_comission_button));
                imageItems.add(new ImageItem(R.mipmap.big_defect_button));
                imageItems.add(new ImageItem(R.mipmap.big_begin_button));
                imageItems.add(new ImageItem(R.mipmap.big_mid_button));
                imageItems.add(new ImageItem(R.mipmap.big_end_button));
                imageItems.add(new ImageItem(R.mipmap.big_new_button));

                gridView.setNumColumns(2);
            }
        else
            if (seekBar.getProgress()==2){
                imageItems.add(new ImageItem(R.mipmap.small_comission_button));
                imageItems.add(new ImageItem(R.mipmap.small_defect_button));
                imageItems.add(new ImageItem(R.mipmap.small_begin_button));
                imageItems.add(new ImageItem(R.mipmap.small_mid_button));
                imageItems.add(new ImageItem(R.mipmap.small_end_button));
                imageItems.add(new ImageItem(R.mipmap.small_new_button));

                gridView.setNumColumns(1);
            }



        listViewAdapter = new GridViewAdapter(this, R.layout.simple_list_view_item, imageItems);
        gridView.setAdapter(listViewAdapter);
    }

    public void onInspectionButtonClick() {
        Intent intent = new Intent(MainActivity.this, InspectionActivity.class);
        startActivity(intent);
    }

    public void onDefectButtonClick() {
        Intent intent = new Intent(MainActivity.this, AddressesActivity.class);
        intent.putExtra("User", currentUser);
        intent.putExtra("Work_Type", DEFECT);
        startActivity(intent);
    }

    public void onLoginButtonClick(View view) {
    }

    public void dbConnect(){
        findViewById(R.id.menuProgressBar).setVisibility(View.VISIBLE);
        //findViewById(R.id.inspection_button).setEnabled(false);
        //findViewById(R.id.defection_button).setEnabled(false);
        findViewById(R.id.main_menu_gridview).setEnabled(false);
        findViewById(R.id.sync_button).setEnabled(false);

        Controller controller = new Controller(this, LOAD_STATIC_ADDRESS); // последовательно загружаются все статичные данные
        controller.start();
    }


}
