package com.pea.du.actyvities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import com.pea.du.R;
import com.pea.du.actyvities.inspect.InspectionActivity;
import com.pea.du.actyvities.defects.Login;
import com.pea.du.web.client.Controller;

import static com.pea.du.web.client.Contract.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
    }

    public void onInspectionButtonClick(View view) {
        Intent intent = new Intent(MainActivity.this, InspectionActivity.class);
        startActivity(intent);
    }

    public void onDefectButtonClick(View view) {
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
    }

    public void onLoginButtonClick(View view) {

        findViewById(R.id.menuProgressBar).setVisibility(View.VISIBLE);
        findViewById(R.id.inspection_button).setEnabled(false);
        findViewById(R.id.defection_button).setEnabled(false);
        findViewById(R.id.sync_button).setEnabled(false);

        Controller controller = new Controller(this, LOAD_STATIC_ADDRESS); // последовательно загружаются все статичные данные
        controller.start();


    }


}
