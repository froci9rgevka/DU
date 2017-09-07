package com.pea.du.web.client;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pea.du.R;
import com.pea.du.actyvities.MainActivity;
import com.pea.du.actyvities.addresses.works.defectation.DefectActivity;
import com.pea.du.actyvities.addresses.works.WorksActivity;
import com.pea.du.data.*;
import com.pea.du.db.methods.DeleteMethods;
import com.pea.du.db.methods.WriteMethods;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


import static com.pea.du.actyvities.Login.currentUser;
import static com.pea.du.db.data.Contract.GuestEntry.*;
import static com.pea.du.web.client.Contract.*;

public class Controller implements Callback<Object> {

    Context context;
    String typeOfObject;
    Object argument;

    ArrayList<String> imageParts = new ArrayList<String>();
    Integer packagesCount = 0;

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

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        GerritAPI gerritAPI = retrofit.create(GerritAPI.class);

        Call<Object> call = gerritAPI.loadAddresses("status:open");
        if (typeOfObject.equals(LOAD_STATIC_ADDRESS)) {
            call = gerritAPI.loadAddresses("status:open");

        } else if (typeOfObject.equals(LOAD_STATIC_MEASURE)) {
            call = gerritAPI.loadMeasures("status:open");

        } else if (typeOfObject.equals(LOAD_STATIC_CONSTRUCTIVE_ELEMENT)) {
            call = gerritAPI.loadConstructiveElements("status:open");

        } else if (typeOfObject.equals(LOAD_STATIC_TYPE)) {
            call = gerritAPI.loadTypes("status:open");

        } else if (typeOfObject.equals(LOAD_ACTS)) {
            call = gerritAPI.loadActs("status:open");

        } else if (typeOfObject.equals(LOAD_DEFECTS)) {
            call = gerritAPI.loadDefects("status:open");

        } else if (typeOfObject.equals(LOAD_PHOTOS)) {
            call = gerritAPI.loadPhotos("status:open");

        } else if (typeOfObject.equals(CHECK_USER)) {
            call = gerritAPI.checkUser((String) argument);

        } else if (typeOfObject.equals(SAVE_ACT)) {
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

        } else if (typeOfObject.equals(SAVE_DEFECT)) {
            String query;
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

        } else if (typeOfObject.equals("ASDASD")) {
            Photo photo = new Photo((Photo) argument);
            File file = new File(photo.getPath());

            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            // MultipartBody.Part используется, чтобы передать имя файла
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

            // Добавляем описание
            String descriptionString = "hello, this is description speaking";
            RequestBody description =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), descriptionString);

            // Выполняем запрос
            call = gerritAPI.fastSave(description, body);


        } else if (typeOfObject.equals(SAVE_PHOTO)) {
            Photo photo;
            photo = new Photo((Photo) argument);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            options.inPurgeable = true;

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            Bitmap bitmap = photo.getImage();

            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2, false);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);


            String encodedImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

            Integer len = encodedImage.length();
            Integer packager = 5000;
            packagesCount = len / packager + 1;
            Integer mod = len % packager;
            for (int i = 0; i < packagesCount - 1; i++) {
                imageParts.add(encodedImage.substring(i * packager, (i + 1) * packager));
            }
            imageParts.add(encodedImage.substring(len - mod, len));

            call = gerritAPI.savePhotoInfo(len, photo.getDefect().getServerId());

        } else if (typeOfObject.equals(SAVE_PHOTO_PART)) {
            String string = imageParts.remove(0);
            call = gerritAPI.savePhoto(string);

        }
        call.enqueue(this);

    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        if(response.isSuccessful()) {
            if (typeOfObject.equals(LOAD_STATIC_ADDRESS)) {
                ArrayList<StaticValue> staticValueArrayList = onGetStaticValues((List<List<String>>) response.body());
                DeleteMethods.deleteAdresses(context);
                WriteMethods.setAddresses(context, staticValueArrayList);

                typeOfObject = LOAD_STATIC_MEASURE;
                start();

            } else if (typeOfObject.equals(LOAD_STATIC_MEASURE)) {
                ArrayList<StaticValue> staticValueArrayList;
                staticValueArrayList = onGetStaticValues((List<List<String>>) response.body());
                DeleteMethods.deleteMeasures(context);
                WriteMethods.setMeasures(context, staticValueArrayList);

                typeOfObject = LOAD_STATIC_CONSTRUCTIVE_ELEMENT;
                start();

            } else if (typeOfObject.equals(LOAD_STATIC_CONSTRUCTIVE_ELEMENT)) {
                ArrayList<StaticValue> staticValueArrayList;
                staticValueArrayList = onGetStaticValues((List<List<String>>) response.body());
                DeleteMethods.deleteConstructiveElements(context);
                WriteMethods.setConstructiveElements(context, staticValueArrayList);

                typeOfObject = LOAD_STATIC_TYPE;
                start();

            } else if (typeOfObject.equals(LOAD_STATIC_TYPE)) {
                ArrayList<StaticValue> staticValueArrayList;
                staticValueArrayList = onGetStaticValues((List<List<String>>) response.body());
                DeleteMethods.deleteTypes(context);
                WriteMethods.setTypes(context, staticValueArrayList);

                typeOfObject = LOAD_ACTS;
                start();

            } else if (typeOfObject.equals(LOAD_ACTS)) {
                ArrayList<Act> actArrayList = onGetActs((List<List<String>>) response.body());
                DeleteMethods.deleteActs(context);
                WriteMethods.setActs(context, actArrayList);

                typeOfObject = LOAD_DEFECTS;
                start();

            } else if (typeOfObject.equals(LOAD_DEFECTS)) {
                ArrayList<Defect> defectArrayList = onGetDefects((List<List<String>>) response.body());
                DeleteMethods.deleteDefects(context);
                WriteMethods.setDefects(context, defectArrayList);

                typeOfObject = LOAD_PHOTOS;
                start();

            } else if (typeOfObject.equals(LOAD_PHOTOS)) {
                ArrayList<Photo> photoArrayList = onGetPhotos((List<List<String>>) response.body());
                DeleteMethods.deletePhotos(context);
                WriteMethods.setDefectPhotos(context, photoArrayList);

                //Скрываем прогрес бар и делаем кнопки активными
                AppCompatActivity appCompatActivity = (AppCompatActivity) context;
                //appCompatActivity.findViewById(R.id.inspection_button).setEnabled(true);
                //appCompatActivity.findViewById(R.id.defection_button).setEnabled(true);
                appCompatActivity.findViewById(R.id.main_menu_gridview).setEnabled(true);
                appCompatActivity.findViewById(R.id.sync_button).setEnabled(true);
                appCompatActivity.findViewById(R.id.menuProgressBar).setVisibility(View.GONE);

            } else if (typeOfObject.equals(SAVE_ACT)) {
                onActSaved(((Double) response.body()).intValue());

            } else if (typeOfObject.equals(SAVE_DEFECT)) {
                onDefectSaved(((Double) response.body()).intValue());

            } else if (typeOfObject.equals(SAVE_PHOTO)) {
                onPhotoSavedStart(((Double) response.body()).intValue());

            } else if (typeOfObject.equals(SAVE_PHOTO_PART)) {
                onPhotoSaved(((Double) response.body()).intValue());

            } else if (typeOfObject.equals(CHECK_USER)) {
                onCheckUser(((Double) response.body()).intValue());

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
        ArrayList<StaticValue> staticValueArrayList = new ArrayList<StaticValue>();
        for (List<String> list:response) {
            if (typeOfObject.equals(LOAD_STATIC_ADDRESS)) {
                staticValue = new StaticValue(list.get(1), Integer.parseInt(list.get(0)), ADDRESS_TABLE_NAME, ADDRESS);

            } else if (typeOfObject.equals(LOAD_STATIC_CONSTRUCTIVE_ELEMENT)) {
                staticValue = new StaticValue(list.get(1), Integer.parseInt(list.get(0)), DEFECT_CONSTRUCTIVE_ELEMENT_TABLE_NAME, DEFECT_CONSTRUCTIVE_ELEMENT);

            } else if (typeOfObject.equals(LOAD_STATIC_MEASURE)) {
                staticValue = new StaticValue(list.get(1), Integer.parseInt(list.get(0)), DEFECT_MEASURE_TABLE_NAME, DEFECT_MEASURE);

            } else if (typeOfObject.equals(LOAD_STATIC_TYPE)) {
                staticValue = new StaticValue(list.get(1), Integer.parseInt(list.get(0)), DEFECT_TYPE_TABLE_NAME, DEFECT_TYPE);

            }
            staticValueArrayList.add(staticValue);
        }
        return staticValueArrayList;
    }

    public ArrayList<Act> onGetActs(List<List<String>> response) {
        ArrayList<Act> actArrayList = new ArrayList<Act>();
        for (List<String> list:response) {
            actArrayList.add(Act.fromStringList(list,context));
        }
        return actArrayList;
    }

    public ArrayList<Defect> onGetDefects(List<List<String>> response) {
        ArrayList<Defect> defectArrayList = new ArrayList<Defect>();
        for (List<String> list:response) {
            defectArrayList.add(Defect.fromStringList(list, context));
        }
        return defectArrayList;
    }

    public ArrayList<Photo> onGetPhotos(List<List<String>> response) {
        ArrayList<Photo> photoArrayList = new ArrayList<Photo>();
        for (List<String> list:response) {
            photoArrayList.add(Photo.fromStringList(list, context));
        }
        return photoArrayList;
    }

    public void onActSaved(Integer response){
        Act act = new Act((Act) argument);
        act.setServerId(response);

        act.setId(WriteMethods.setAct(context, act));
        Intent intent = new Intent(context, WorksActivity.class);
        intent.putExtra("Act", act);
        context.startActivity(intent);
    }

    public void onDefectSaved(Integer response){
        ((Defect) argument).setServerId(response);
        WriteMethods.setDefect(context, (Defect)argument);
    }

    public void onPhotoSavedStart(Integer response){
       // ((Photo) argument).setServerId(response);
       // WriteMethods.setDefectPhoto(context, (Photo)argument);

       // ((PhotoLibrary) context).loadPhotosInGrid();

        typeOfObject = SAVE_PHOTO_PART; start();
    }

    public void onPhotoSaved(Integer response){
        TextView tvPocketsCount = (TextView) ((DefectActivity) context).findViewById(R.id.tvPocketsCount);
        tvPocketsCount.setText("Количество отправленных пакетов: " + (packagesCount - imageParts.size()) + "из" + packagesCount);
        ProgressBar pbSendPhoto = (ProgressBar) ((DefectActivity) context).findViewById(R.id.pbSendPhoto);
        pbSendPhoto.setMax(packagesCount);
        pbSendPhoto.setProgress(packagesCount - imageParts.size());

        if (response==0) {

            start();
        }
        else{
            Button bAddPhoto = (Button) ((DefectActivity) context).findViewById(R.id.activity_defect_addPhoto);
            bAddPhoto.setEnabled(true);
        }
    }

    public void onCheckUser(Integer response){
        if(response == -1) {
            Toast.makeText(context, "Неверный логин или пароль!", Toast.LENGTH_SHORT).show();
            return;
        }

        currentUser.setServerId(response);

        currentUser.setId(WriteMethods.setUser(context, currentUser));

        // Если юзер есть в базе, то запускаем активити
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("User", currentUser);

        context.startActivity(intent);
    }
}