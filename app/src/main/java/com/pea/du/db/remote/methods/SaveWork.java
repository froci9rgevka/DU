package com.pea.du.db.remote.methods;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import com.pea.du.actyvities.addresses.works.defectation.DefectActivity;
import com.pea.du.actyvities.addresses.works.stagework.StageActivity;
import com.pea.du.data.Defect;
import com.pea.du.data.Work;
import com.pea.du.db.local.methods.WriteMethods;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static com.pea.du.db.remote.MysqlConnection.getConnection;
import static com.pea.du.flags.Flags.currentContext;
import static com.pea.du.flags.Flags.workId;

public class SaveWork extends AsyncTask<String,String,String>{

    String z = "";
    Boolean isSuccess = false;
    Connection con;

    private Work work = new Work();

    public SaveWork(Work work){
        this.work = work;
    }

    @Override
    protected void onPostExecute(String r)
    {
        Toast.makeText(currentContext, r, Toast.LENGTH_SHORT).show();

        if (isSuccess) {
            work.setId(WriteMethods.setWork(currentContext, work));

            workId = work.getServerId();

            ((StageActivity) currentContext).setStageActivityContent();
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
                Integer contractor = null;
                if(work.getSubcontract()) contractor = work.getContractor().getServerId();

                String query =
                        "insert into hsg_dataworktape (House, Work, Cnt, MeasureC, Descr, Subcontract, Docdate, Contractor, WorkStage, Author) values ("
                                + work.getAddress().getServerId() + ","
                                + work.getName().getServerId() + ","
                                + work.getCnt() + ","
                                + work.getMeasure().getServerId() + ",'"
                                + work.getDescr() + "',"
                                + work.getSubcontract() + ","
                                + "NOW()" + ","
                                + contractor + ","
                                + work.getStage().getServerId() + ","
                                + work.getUser().getServerId() + ")";
                Statement stmt = con.createStatement();
                stmt.executeUpdate(query);

                query = "SELECT MAX(id) from hsg_dataworktape";
                ResultSet rs = stmt.executeQuery(query);

                if(rs.next())
                {
                    work.setServerId(rs.getInt(1));
                    z = "Создана новая работа!";
                    isSuccess=true;
                    con.close();
                }
                else
                {
                    z = "Ошибка создания работы!";
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
