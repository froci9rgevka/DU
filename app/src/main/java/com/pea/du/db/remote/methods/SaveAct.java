package com.pea.du.db.remote.methods;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import com.pea.du.actyvities.addresses.works.WorksActivity;
import com.pea.du.data.Act;
import com.pea.du.db.local.methods.WriteMethods;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static com.pea.du.db.remote.MysqlConnection.getConnection;
import static com.pea.du.flags.Flags.actId;
import static com.pea.du.flags.Flags.addressId;

/*
        Сохраняет в MySQL, и сохраняет в локальную бд. Хранит объект private act.
 */
public class SaveAct extends AsyncTask<String,String,String>{

    Context context = null;
    String z = "";
    Boolean isSuccess = false;
    Connection con;

    private Act act = new Act();

    public SaveAct(Context context, Act act){
        this.context = context;
        this.act = act;
    }

    @Override
    protected void onPreExecute()
    {
        Toast.makeText(context , "Создание нового акта..." , Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostExecute(String r)
    {
        if (!isSuccess)
            Toast.makeText(context, r, Toast.LENGTH_SHORT).show();

        act.setId(WriteMethods.setAct(context, act));

        addressId = act.getAddress().getServerId();
        actId = act.getServerId();

        Intent intent = new Intent(context, WorksActivity.class);
        context.startActivity(intent);

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
                String query =
                        "insert into zkh_actofdefect (CreateDate, Author, Adress, Status) values ("
                                + "Now()" + ","
                                + act.getUser().getServerId() + ","
                                + act.getAddress().getServerId() + "," +
                                "1" + ")";
                Statement stmt = con.createStatement();
                stmt.executeUpdate(query);

                query = "SELECT MAX(id) from zkh_actofdefect";
                ResultSet rs = stmt.executeQuery(query);

                if(rs.next())
                {
                    act.setServerId(rs.getInt(1));
                    z = "Новый акт создан!";
                    isSuccess=true;
                    con.close();
                }
                else
                {
                    z = "Ошибка создания акта!";
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
