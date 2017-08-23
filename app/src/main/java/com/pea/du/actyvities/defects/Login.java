package com.pea.du.actyvities.defects;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import com.pea.du.R;
import com.pea.du.actyvities.defects.address.AddressActivity;
import com.pea.du.data.User;
import com.pea.du.db.methods.WriteMethods;

public class Login extends AppCompatActivity {

    public static User currentUser;

    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.login_toolbar);
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
        currentUser.isUserInDB(this);
    }

    // Кнопка добавления нового юзера в базу
    public void onNewButtonClick(View view){
        getUserFromFields();

        currentUser.setId(WriteMethods.setUser(this, currentUser));

        //Create intent
        Intent intent = new Intent(Login.this, AddressActivity.class);
        intent.putExtra("User", currentUser);

        startActivity(intent);
    }
}
