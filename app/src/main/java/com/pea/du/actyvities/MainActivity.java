package com.pea.du.actyvities;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import com.pea.du.R;
import com.pea.du.actyvities.inspect.InspectionActivity;
import com.pea.du.actyvities.defects.Login;
import com.pea.du.tools.gridview.GridViewAdapter;
import com.pea.du.tools.gridview.ImageItem;
import com.pea.du.web.client.Controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.pea.du.web.client.Contract.*;

public class MainActivity extends AppCompatActivity{

    private ListView listView;
    private ArrayAdapter<ImageItem> listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);


        ArrayList<ImageItem> imageItems = new ArrayList();
        listView = (ListView) findViewById(R.id.main_menu_listview);
        imageItems.add(new ImageItem(BitmapFactory.decodeResource(getBaseContext().getResources(), R.mipmap.comission), "Осмотр комиссией общего имущества"));
        imageItems.add(new ImageItem(BitmapFactory.decodeResource(getBaseContext().getResources(), R.mipmap.defect), "Составление акта дефектации"));
        imageItems.add(new ImageItem(BitmapFactory.decodeResource(getBaseContext().getResources(), R.mipmap.new_item), "Раздел в разработке"));
        imageItems.add(new ImageItem(BitmapFactory.decodeResource(getBaseContext().getResources(), R.mipmap.new_item), "Раздел в разработке"));
        listViewAdapter = new GridViewAdapter(this, R.layout.simple_list_view_item, imageItems);
        listView.setAdapter(listViewAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
    }

    public void onInspectionButtonClick() {
        Intent intent = new Intent(MainActivity.this, InspectionActivity.class);
        startActivity(intent);
    }

    public void onDefectButtonClick() {
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
    }

    public void onLoginButtonClick(View view) {

        findViewById(R.id.menuProgressBar).setVisibility(View.VISIBLE);
        //findViewById(R.id.inspection_button).setEnabled(false);
        //findViewById(R.id.defection_button).setEnabled(false);
        findViewById(R.id.main_menu_listview).setEnabled(false);
        findViewById(R.id.sync_button).setEnabled(false);

        Controller controller = new Controller(this, LOAD_STATIC_ADDRESS); // последовательно загружаются все статичные данные
        controller.start();


    }

}
