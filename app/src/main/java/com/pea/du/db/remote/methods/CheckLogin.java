package com.pea.du.db.remote.methods;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import com.pea.du.actyvities.MainActivity;
import com.pea.du.data.User;
import com.pea.du.db.local.methods.WriteMethods;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static com.pea.du.db.remote.MysqlConnection.getConnection;
import static com.pea.du.flags.Flags.authorId;
import static com.pea.du.flags.Flags.currentContext;

public class CheckLogin extends AsyncTask<String,String,String> {

        String z = "";
        Boolean isSuccess = false;
        Connection con;

        private User user = new User();

    public CheckLogin(String nickname, String password){
        user.setNickname(nickname);
        user.setPassword(password);
    }

    @Override
    protected void onPreExecute(){
        Toast.makeText(currentContext, "Выполняется вход...", Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onPostExecute(String r)
    {
        if(isSuccess) {
            authorId = user.getServerId();
            Intent intent = new Intent(currentContext, MainActivity.class);
            currentContext.startActivity(intent);
        }
        else
            Toast.makeText(currentContext, r, Toast.LENGTH_SHORT).show();

    }

    @Override
    protected String doInBackground(String... params)
    {
        try
        {
            con = getConnection();        // Connect to database
            if (con == null)
            {
                z = "Проверьте своё подключение к интернету!";
            }
            else
            {
                String query = "SELECT Id, Login from tmausers where Login='" + user.getNickname() + "'";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                if (rs.next()) {
                    user.setServerId(rs.getInt("Id"));
                    WriteMethods.setUser(currentContext, user);
                    isSuccess = true;
                }
                else{
                    z = "Некоректный логин или пароль!";
                    isSuccess = false;
                }
            }
        }
        catch (Exception ex)
        {
            isSuccess = false;
            z = ex.getMessage();
        }
        return z;
    }

}
