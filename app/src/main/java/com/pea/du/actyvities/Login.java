package com.pea.du.actyvities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import com.pea.du.R;
import com.pea.du.data.User;
import com.pea.du.flags.Flags;

public class Login extends AppCompatActivity {

    private static User currentUser;

    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        Flags.currentContext=this;

        toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        toolbar.setTitle("Вход");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getUserFromFields(){
        currentUser = new User(((TextView) findViewById(R.id.login_nickname)).getText().toString(),
                ((TextView) findViewById(R.id.login_password)).getText().toString());
    }

    // Кнопка входа. Проверяет есть ли юзер в базе.
    public void onLoginButtonClick(View view) {
        getUserFromFields();

        // Отправляем запрос
        currentUser.isUserInDB();
    }
}
