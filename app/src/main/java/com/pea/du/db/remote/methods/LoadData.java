package com.pea.du.db.remote.methods;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import com.pea.du.data.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static com.pea.du.actyvities.MainActivity.lockMainActivity;
import static com.pea.du.actyvities.MainActivity.unlockMainActivity;
import static com.pea.du.db.local.data.Contract.GuestEntry.*;
import static com.pea.du.db.local.methods.DeleteMethods.deleteAnything;
import static com.pea.du.db.local.methods.WriteMethods.*;
import static com.pea.du.db.remote.MysqlConnection.getConnection;
import static com.pea.du.flags.Flags.DEFECT;

/*
        И загружает из MySQL, и сохраняет в локальную бд.
 */
public class LoadData extends AsyncTask<String,String,String> {

    Context context = null;
    String z = "";
    Boolean isSuccess = false;
    Connection con;

    private ArrayList<StaticValue> staticValues = new ArrayList<>();
    private ArrayList<Integer[]> syncCeT = new ArrayList<>();
    private ArrayList<Act> acts = new ArrayList<>();
    private ArrayList<Defect> defects = new ArrayList<>();
    private ArrayList<Work> works = new ArrayList<>();
    private ArrayList<Photo> photos = new ArrayList<>();

    // Массив сопоставляющий таблицы из удалённой базы с таблицами и колонками локальной
    private final String[][] syncDBStaticTables = new String[][]
            {{"zkh_house", ADDRESS_TABLE_NAME,ADDRESS}
            ,{"zkh__measure", DEFECT_MEASURE_TABLE_NAME,DEFECT_MEASURE}
            ,{"zkh__constructiveelements", DEFECT_CONSTRUCTIVE_ELEMENT_TABLE_NAME,DEFECT_CONSTRUCTIVE_ELEMENT}
            ,{"zkh__defecttype", DEFECT_TYPE_TABLE_NAME,DEFECT_TYPE}
            ,{"zkh_work", WORK_NAME_TABLE_NAME,WORK_NAME}
            ,{"hsg_dirWorkStage", STAGE_TABLE_NAME,STAGE}
            ,{"hsg_dirContractor", CONTRACTOR_TABLE_NAME, CONTRACTOR}};

    public LoadData(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute()
    {
        lockMainActivity();
        Toast.makeText(context , "Синхронизация" , Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostExecute(String r)
    {
        Toast.makeText(context, r, Toast.LENGTH_SHORT).show();

        deleteAnything(context);
        setStaticValues(context,staticValues);
        setSyncCeT(context,syncCeT);
        setActs(context,acts);
        setDefects(context,defects);
        setWorks(context,works);
        setPhotos(context, photos);

        if(isSuccess)
        {
            Toast.makeText(context , "Данные синхронизированы" , Toast.LENGTH_LONG).show();
        }
        unlockMainActivity();
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
                for (String[] syncDBTable : syncDBStaticTables) {
                    LoadStaticTable(syncDBTable);
                }

                LoadSyncCeTTable();
                LoadActTable();
                LoadDefectTable();
                LoadWorkTable();
                LoadDefectPhotoTable();
                LoadStagesPhotoTable();

                if(!staticValues.isEmpty())
                {
                    z = "Таблицы загружены, идет запись данных...";
                    isSuccess=true;
                    con.close();
                }
                else
                {
                    z = "Таблицы пусты!";
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

    private void LoadStaticTable(String[] syncDBStaticTable) throws SQLException {
        String query = "select Id, Name from " + syncDBStaticTable[0];
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            StaticValue staticValue = new StaticValue(syncDBStaticTable[1], syncDBStaticTable[2]);
            staticValue.setServerId(rs.getInt("Id"));
            staticValue.setName(rs.getString("Name"));
            staticValues.add(staticValue);
        }
    }

    private void LoadSyncCeTTable() throws SQLException {
        String query = "select Header, Defect from zkh__constructiveelementsdetail";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            Integer[] arr = {rs.getInt("Header"),rs.getInt("Defect")};
            syncCeT.add(arr);
        }
    }

    private void LoadActTable() throws SQLException {
        String query = "select Id, Author, Adress from zkh_actofdefect";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            Act act = new Act();
            act.setServerId(rs.getInt("Id"));
            act.setUser(new User(rs.getInt("Author")));
            act.setAddress(new StaticValue(rs.getInt("Adress"), ADDRESS_TABLE_NAME, ADDRESS));
            acts.add(act);
        }
    }

    private void LoadDefectTable() throws SQLException {
        String query = "select Id, Header, Name, DefectType, Grand, Floor, Flat, Text, Measure, CntDefect " +
                "from zkh_actofdefectdetail";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            Defect defect = new Defect();
            defect.setServerId(rs.getInt("Id"));
            defect.setAct(new Act(rs.getInt("Header")));
            defect.setConstructiveElement(new StaticValue(rs.getInt("Name"), DEFECT_CONSTRUCTIVE_ELEMENT_TABLE_NAME, DEFECT_CONSTRUCTIVE_ELEMENT));
            defect.setType(new StaticValue(rs.getInt("DefectType"), DEFECT_TYPE_TABLE_NAME, DEFECT_TYPE));
            defect.setPorch(rs.getString("Grand"));
            defect.setFloor(rs.getString("Floor"));
            defect.setFlat(rs.getString("Flat"));
            defect.setDescription(rs.getString("Text"));
            defect.setMeasure(new StaticValue(rs.getInt("Measure"), DEFECT_MEASURE_TABLE_NAME, DEFECT_MEASURE));
            defect.setCurrency(rs.getString("CntDefect"));
            defects.add(defect);
        }
    }

    private void LoadWorkTable() throws SQLException {
        String query = "select Id, Author, House, Work, WorkStage, Cnt, MeasureC, Descr, Subcontract, Contractor " +
                "from hsg_DataWorkTape";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            Work work = new Work();
            work.setServerId(rs.getInt("Id"));
            work.setUser(new User(rs.getInt("Author")));
            work.setAddress(new StaticValue(rs.getInt("House"), ADDRESS_TABLE_NAME, ADDRESS));
            work.setName(new StaticValue(rs.getInt("Work"), WORK_NAME_TABLE_NAME, WORK_NAME));
            work.setStage(new StaticValue(rs.getInt("WorkStage"), STAGE_TABLE_NAME, STAGE));
            work.setCnt(rs.getString("Cnt"));
            work.setMeasure(new StaticValue(rs.getInt("MeasureC"), DEFECT_MEASURE_TABLE_NAME, DEFECT_MEASURE));
            work.setDescr(rs.getString("Descr"));
            work.setSubcontract(Boolean.parseBoolean(rs.getString("Subcontract")));
            work.setContractor(new StaticValue(rs.getInt("Contractor"), CONTRACTOR_TABLE_NAME, CONTRACTOR));
            works.add(work);
        }
    }

    private void LoadDefectPhotoTable() throws SQLException {
        String query = "select Id, Header, DefectFileLink from zkh_actofdefectphoto";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            Photo photo = new Photo();
            photo.setServerId(rs.getInt("Id"));
            photo.setDefect(new Defect(rs.getInt("Header")));
            photo.setUrl(rs.getString("DefectFileLink"));
            photos.add(photo);
        }
    }

    private void LoadStagesPhotoTable() throws SQLException {
        String query = "select Id, Header, Name, ServiceName from hsg_dataworktapefile";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            Photo photo = new Photo();
            photo.setServerId(rs.getInt("Id"));
            photo.setWork(new Work(rs.getInt("Header")));
            photo.setUrl(rs.getString("Name"));
            photos.add(photo);
        }
    }
}
