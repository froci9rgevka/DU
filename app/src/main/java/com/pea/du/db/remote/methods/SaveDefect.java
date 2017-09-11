package com.pea.du.db.remote.methods;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import com.pea.du.actyvities.addresses.works.WorksActivity;
import com.pea.du.actyvities.addresses.works.defectation.DefectActivity;
import com.pea.du.data.Act;
import com.pea.du.data.Defect;
import com.pea.du.db.local.methods.WriteMethods;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static com.pea.du.db.remote.MysqlConnection.getConnection;
import static com.pea.du.flags.Flags.actId;
import static com.pea.du.flags.Flags.addressId;
import static com.pea.du.flags.Flags.workId;

/*
        Сохраняет в MySQL, и сохраняет в локальную бд. Хранит объект private defect.
 */
public class SaveDefect extends AsyncTask<String,String,String> {

    Context context = null;
    String z = "";
    Boolean isSuccess = false;
    Connection con;

    private Defect defect = new Defect();

    public SaveDefect(Context context, Defect defect){
        this.context = context;
        this.defect = defect;
    }

    @Override
    protected void onPostExecute(String r)
    {
        Toast.makeText(context, r, Toast.LENGTH_SHORT).show();

        if (isSuccess) {
            defect.setId(WriteMethods.setDefect(context, defect));

            workId = defect.getServerId();

            ((DefectActivity) context).setDefectActivityContent();
        }
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
                        "insert into zkh_actofdefectdetail (Header, Name, DefectType, Grand, Floor, Flat, Text, Measure, CntDefect) values ("
                                + defect.getAct().getServerId() + ","
                                + defect.getConstructiveElement().getServerId() + ","
                                + defect.getType().getServerId() + ","
                                + defect.getPorch() + ","
                                + defect.getFloor() + ","
                                + defect.getFlat() + ",'"
                                + defect.getDescription() + "',"
                                + defect.getMeasure().getServerId() + ","
                                + defect.getCurrency() + ")";
                Statement stmt = con.createStatement();
                stmt.executeUpdate(query);

                query = "SELECT MAX(id) from zkh_actofdefectdetail";
                ResultSet rs = stmt.executeQuery(query);

                if(rs.next())
                {
                    defect.setServerId(rs.getInt(1));
                    z = "Новый дефект создан!";
                    isSuccess=true;
                    con.close();
                }
                else
                {
                    z = "Ошибка создания дефекта!";
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
