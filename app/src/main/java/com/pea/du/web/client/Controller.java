package com.pea.du.web.client;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pea.du.R;
import com.pea.du.actyvities.defects.address.AddressActivity;
import com.pea.du.actyvities.defects.address.act.AddActActivity;
import com.pea.du.actyvities.defects.address.act.DefectActivity;
import com.pea.du.data.Act;
import com.pea.du.data.Defect;
import com.pea.du.data.StaticValue;
import com.pea.du.data.User;
import com.pea.du.db.methods.DeleteMethods;
import com.pea.du.db.methods.WriteMethods;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import static com.pea.du.actyvities.defects.Login.currentUser;
import static com.pea.du.data.Defect.fromStringList;
import static com.pea.du.db.data.Contract.GuestEntry.*;
import static com.pea.du.web.client.Contract.*;
import static java.lang.Integer.parseInt;

public class Controller implements Callback<Object> {

    Context context;
    String typeOfObject;
    Object argument;

    public Controller(Context context, String typeOfObject) {
        super();
        this.context = context;
        this.typeOfObject = typeOfObject;
    }

    public Controller(Context context, String typeOfObject, Object argument) {
        super();
        this.context = context;
        this.typeOfObject = typeOfObject;
        this.argument = argument;
    }

    static final String BASE_URL = "http://148.251.48.87:8080/";

    public void start() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        GerritAPI gerritAPI = retrofit.create(GerritAPI.class);

        Call<Object> call = gerritAPI.loadAddresses("status:open");
        switch (typeOfObject)
        {
            case LOAD_STATIC_ADDRESS:
                call = gerritAPI.loadAddresses("status:open");
                break;
            case LOAD_STATIC_MEASURE:
                call = gerritAPI.loadMeasures("status:open");
                break;
            case LOAD_STATIC_CONSTRUCTIVE_ELEMENT:
                call = gerritAPI.loadConstructiveElements("status:open");
                break;
            case LOAD_STATIC_TYPE:
                call = gerritAPI.loadTypes("status:open");
                break;
            case LOAD_ACTS:
                call = gerritAPI.loadActs("status:open");
                break;
            case LOAD_DEFECTS:
                call = gerritAPI.loadDefects("status:open");
                break;
            case CHECK_USER:
                call = gerritAPI.checkUser((String) argument);
                break;
            case SAVE_ACT:
                Act act = new Act((Act) argument);

                //java.text.SimpleDateFormat sdf =
                //        new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //String сreateDate = sdf.format(act.getCreateDate());

                String query =
                        "insert into zkh_actofdefect (CreateDate, Author, Adress) values ("
                                + "Now()" + ","
                                + act.getUser().getServerId() + ","
                                + act.getAddress().getServerId() + ")";
                call = gerritAPI.saveAct(query);
                break;
            case SAVE_DEFECT:
                Defect defect = new Defect((Defect) argument);
                query =
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
                call = gerritAPI.saveDefect(query);
                break;
        }
        call.enqueue(this);

    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        if(response.isSuccessful()) {
            switch (typeOfObject) {
                case LOAD_STATIC_ADDRESS:
                    ArrayList<StaticValue> staticValueArrayList = onGetStaticValues((List<List<String>>)response.body());
                    DeleteMethods.deleteAdresses(context);
                    WriteMethods.setAddresses(context, staticValueArrayList);

                    typeOfObject = LOAD_STATIC_MEASURE; start();
                    break;
                case LOAD_STATIC_MEASURE:
                    staticValueArrayList = onGetStaticValues((List<List<String>>)response.body());
                    DeleteMethods.deleteMeasures(context);
                    WriteMethods.setMeasures(context, staticValueArrayList);

                    typeOfObject = LOAD_STATIC_CONSTRUCTIVE_ELEMENT; start();
                    break;
                case LOAD_STATIC_CONSTRUCTIVE_ELEMENT:
                    staticValueArrayList = onGetStaticValues((List<List<String>>)response.body());
                    DeleteMethods.deleteConstructiveElements(context);
                    WriteMethods.setConstructiveElements(context, staticValueArrayList);

                    typeOfObject = LOAD_STATIC_TYPE; start();
                    break;
                case LOAD_STATIC_TYPE:
                    staticValueArrayList = onGetStaticValues((List<List<String>>)response.body());
                    DeleteMethods.deleteTypes(context);
                    WriteMethods.setTypes(context, staticValueArrayList);

                    typeOfObject = LOAD_ACTS; start();
                    break;
                case LOAD_ACTS:
                    ArrayList<Act> actArrayList = onGetActs((List<List<String>>)response.body());
                    DeleteMethods.deleteActs(context);
                    WriteMethods.setActs(context, actArrayList);

                    typeOfObject = LOAD_DEFECTS; start();
                    break;
                case LOAD_DEFECTS:
                    ArrayList<Defect> defectArrayList = onGetDefects((List<List<String>>)response.body());
                    DeleteMethods.deleteDefects(context);
                    WriteMethods.setDefects(context, defectArrayList);

                    //Скрываем прогрес бар и делаем кнопки активными
                    AppCompatActivity appCompatActivity = (AppCompatActivity) context;
                    appCompatActivity.findViewById(R.id.inspection_button).setEnabled(true);
                    appCompatActivity.findViewById(R.id.defection_button).setEnabled(true);
                    appCompatActivity.findViewById(R.id.sync_button).setEnabled(true);
                    appCompatActivity.findViewById(R.id.menuProgressBar).setVisibility(View.GONE);
                    break;
                case SAVE_ACT:
                    onActSaved(((Double)response.body()).intValue());
                    break;
                case SAVE_DEFECT:
                    onDefectSaved(((Double)response.body()).intValue());
                    break;
                case CHECK_USER:
                    onCheckUser(((Double)response.body()).intValue());
                    break;
            }

        } else {
        }
    }


    @Override
    public void onFailure(Call<Object> call, Throwable t) {
        t.printStackTrace();
    }

    public ArrayList<StaticValue> onGetStaticValues(List<List<String>> response){
        StaticValue staticValue = null;
        ArrayList<StaticValue> staticValueArrayList = new ArrayList<>();
        for (List<String> list:response) {
            switch (typeOfObject) {
                case LOAD_STATIC_ADDRESS:
                    staticValue = new StaticValue(list.get(1), Integer.parseInt(list.get(0)), ADDRESS_TABLE_NAME, ADDRESS);
                    break;
                case LOAD_STATIC_CONSTRUCTIVE_ELEMENT:
                    staticValue = new StaticValue(list.get(1), Integer.parseInt(list.get(0)), DEFECT_CONSTRUCTIVE_ELEMENT_TABLE_NAME, DEFECT_CONSTRUCTIVE_ELEMENT);
                    break;
                case LOAD_STATIC_MEASURE:
                    staticValue = new StaticValue(list.get(1), Integer.parseInt(list.get(0)), DEFECT_MEASURE_TABLE_NAME, DEFECT_MEASURE);
                    break;
                case LOAD_STATIC_TYPE:
                    staticValue = new StaticValue(list.get(1), Integer.parseInt(list.get(0)), DEFECT_TYPE_TABLE_NAME, DEFECT_TYPE);
                    break;
            }
            staticValueArrayList.add(staticValue);
        }
        return staticValueArrayList;
    }


    public ArrayList<Act> onGetActs(List<List<String>> response) {
        ArrayList<Act> actArrayList = new ArrayList<>();
        for (List<String> list:response) {
            actArrayList.add(Act.fromStringList(list,context));
        }
        return actArrayList;
    }

    public ArrayList<Defect> onGetDefects(List<List<String>> response) {
        ArrayList<Defect> defectArrayList = new ArrayList<>();
        for (List<String> list:response) {
            defectArrayList.add(Defect.fromStringList(list, context));
        }
        return defectArrayList;
    }

    public void onActSaved(Integer response){
        Act act = new Act((Act) argument);
        act.setServerId(response);

        act.setId(WriteMethods.setAct(context, act));
        Intent intent = new Intent(context, DefectActivity.class);
        intent.putExtra("Act", act);
        context.startActivity(intent);
    }

    public void onDefectSaved(Integer response){
        ((Defect) argument).setServerId(response);
        WriteMethods.setDefect(context, (Defect)argument);

        ((AddActActivity) context).onBackPressed();
        //DO SNTHING addressArrayList
    }

    public void onCheckUser(Integer response){
        if(response == -1) {
            Toast.makeText(context, "Неверный логин или пароль!", Toast.LENGTH_SHORT).show();
            return;
        }

        currentUser.setServerId(response);

        currentUser.setId(WriteMethods.setUser(context, currentUser));

        // Если юзер есть в базе, то запускаем активити
        Intent intent = new Intent(context, AddressActivity.class);
        intent.putExtra("User", currentUser);

        context.startActivity(intent);
    }
}