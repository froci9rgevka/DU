package com.pea.du.actyvities.addresses.works.defectation;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.pea.du.R;
import com.pea.du.actyvities.addresses.works.photo.PhotoDetails;
import com.pea.du.data.Act;
import com.pea.du.data.Defect;
import com.pea.du.data.Photo;
import com.pea.du.data.StaticValue;
import com.pea.du.db.methods.ReadMethods;
import com.pea.du.db.methods.WriteMethods;
import com.pea.du.tools.gridview.GridViewAdapter;
import com.pea.du.tools.gridview.ImageItem;
import com.pea.du.web.client.Controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.pea.du.data.Photo.getPhotosByDefect;
import static com.pea.du.data.StaticValue.getNameById;
import static com.pea.du.db.data.Contract.GuestEntry.*;
import static com.pea.du.flags.Flags.actId;
import static com.pea.du.flags.Flags.addressId;
import static com.pea.du.flags.Flags.workId;
import static com.pea.du.tools.gridview.ImageItem.ITEM_PATH;
import static com.pea.du.tools.gridview.ImageItem.ITEM_URL;
import static com.pea.du.web.client.Contract.*;

public class DefectActivity extends AppCompatActivity {

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_REFRESH_PHOTO = 2;

    public static final String EXISTING = "EXISTING";
    public static final String NEW = "NEW";

    Spinner sDefect_type;
    Spinner sConstructiveElement;
    EditText etPorch;
    EditText etFloor;
    EditText etFlat;
    EditText etDescription;
    EditText etCurrency;
    Spinner sMeasure;

    Button bAddAct;
    Button bAddPhoto;

    private android.support.v7.widget.Toolbar toolbar;
    private GridView gridView;
    private GridViewAdapter gridViewAdapter;

    // Список фото
    ArrayList photoList;

    // Новое фото
    private Photo newPhoto = new Photo();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_act);


        objInit();


        LoadData loadData = new LoadData();
        loadData.execute("");
    }

    private void objInit(){
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.add_act_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        sDefect_type = (Spinner) findViewById(R.id.activity_defect_type);
        sConstructiveElement = (Spinner) findViewById(R.id.activity_defect_constructive_element);
        etPorch = (EditText) findViewById(R.id.activity_defect_porch);
        etFloor = (EditText) findViewById(R.id.activity_defect_floor);
        etFlat = (EditText) findViewById(R.id.activity_defect_flat);
        etDescription = (EditText) findViewById(R.id.activity_defect_description);
        sMeasure = (Spinner) findViewById(R.id.activity_defect_measure);
        etCurrency = (EditText) findViewById(R.id.activity_defect_currency);
        bAddAct = (Button) findViewById(R.id.activity_defect_addAct);
        bAddPhoto = (Button) findViewById(R.id.activity_defect_addPhoto);

        ArrayList measures = ReadMethods.getStaticValues(this, DEFECT_MEASURE_TABLE_NAME, null, null);
        ArrayList constructiveElements = ReadMethods.getStaticValues(this, DEFECT_CONSTRUCTIVE_ELEMENT_TABLE_NAME, null, null);
        ArrayList types = ReadMethods.getStaticValues(this, DEFECT_TYPE_TABLE_NAME, null, null);

        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,measures);
        ArrayAdapter<String> ceAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,constructiveElements);
        ArrayAdapter<String> tAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,types);

        sMeasure.setAdapter(mAdapter);
        sConstructiveElement.setAdapter(ceAdapter);
        sDefect_type.setAdapter(tAdapter);

        gridView = (GridView) findViewById(R.id.activity_defect_gridView);
        gridView.setOnItemClickListener(onItemClickListener);
    }

    private class LoadData extends AsyncTask<String,String,String> {

        private String address = null;


        @Override
        protected void onPreExecute()
        {
            // Наполняем активити контентом
            if (workId==-1) {
                // Если создаём новый дефект
                bAddPhoto.setVisibility(View.GONE);
                bAddAct.setVisibility(View.VISIBLE);
            }
            else {
                // Если редактируем старый дефект
                bAddPhoto.setVisibility(View.VISIBLE);
                bAddAct.setVisibility(View.GONE);
                // Заполняем поля в активити полями редактируемого дефекта
                fillFields();
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            // Загружаем адрес используя id
            address = getNameById(DefectActivity.this, ADDRESS_TABLE_NAME, addressId);

            // Загружаем данные
            if (workId==-1) {
            } else {
                // Загружаем фотографии
                loadPhotos();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String r)
        {
            // Отображаем адресс
            toolbar.setTitle(address);

            // Наполняем активити загруженным контентом
            if (workId==-1) {
            }
            else {
                gridViewAdapter = new GridViewAdapter(DefectActivity.this, R.layout.simple_list_view_item, getImageItems());
                gridView.setAdapter(gridViewAdapter);
            }
        }
    }


    private void fillFields(){
        Defect currentDefect = new Defect();

        currentDefect.setServerId(workId);
        currentDefect.getDefectById(this);

        sDefect_type.setSelection(currentDefect.getType().getServerId()-1);
        sConstructiveElement.setSelection(currentDefect.getConstructiveElement().getServerId()-1);
        etPorch.setText(currentDefect.getPorch());
        etFloor.setText(currentDefect.getFloor());
        etFlat.setText(currentDefect.getFlat());
        etDescription.setText(currentDefect.getDescription());
        sMeasure.setSelection(currentDefect.getMeasure().getServerId());
        etCurrency.setText(currentDefect.getCurrency());
    }


    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Photo currentPhoto = (Photo) photoList.get(position);

            //Большой размер нельзя передать через Extra
            currentPhoto.setImage(null);

            ImageItem item = (ImageItem) parent.getItemAtPosition(position);
            //Create intent
            Intent intent = new Intent(DefectActivity.this, PhotoDetails.class);
            intent.putExtra("title", item.getTitle());
            intent.putExtra("image", currentPhoto); //item.getImage()

            //Start details activity
            startActivityForResult(intent, REQUEST_REFRESH_PHOTO);
        }
    };


    public void onAddActButtonClick(View view) {
        StaticValue type = new StaticValue(DEFECT_TYPE_TABLE_NAME, DEFECT_TYPE);
        type.setName(sDefect_type.getSelectedItem().toString());
        type.getStaticByName(this);

        StaticValue constructiveElement = new StaticValue(DEFECT_CONSTRUCTIVE_ELEMENT_TABLE_NAME, DEFECT_CONSTRUCTIVE_ELEMENT);
        constructiveElement.setName(sConstructiveElement.getSelectedItem().toString());
        constructiveElement.getStaticByName(this);

        StaticValue measure = new StaticValue(DEFECT_MEASURE_TABLE_NAME, DEFECT_MEASURE);
        measure.setName(sMeasure.getSelectedItem().toString());
        measure.getStaticByName(this);

        Act act = new Act();
        act.setServerId(actId);

        Defect defect = new Defect(
                act,
                type,
                constructiveElement,
                etPorch.getText().toString(),
                etFloor.getText().toString(),
                etFlat.getText().toString(),
                etDescription.getText().toString(),
                measure,
                etCurrency.getText().toString()
        );

        Controller controller = new Controller(this, SAVE_DEFECT, defect); // последовательно загружаются все статичные данные
        controller.start();

        bAddPhoto.setVisibility(View.VISIBLE);
        bAddAct.setVisibility(View.GONE);

    }

    // Подготовка картинок с текстом для gridView
    private ArrayList<ImageItem> getImageItems() {
        final ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();
        for (int i = 0; i < photoList.size(); i++) {
            Photo photo = (Photo) photoList.get(i);
            if (!(photo.getPath() == null))
                imageItems.add(new ImageItem(photo.getPath(), ITEM_PATH));
            else
                imageItems.add(new ImageItem(photo.getUrl(), ITEM_URL));
        }
        return imageItems;
    }

    /////////////////////////////////////РАБОТА С БД////////////////////////////////////////////

    public void savePhoto() {
        WriteMethods.setDefectPhoto(this, newPhoto);
    }


    public void loadPhotos () {

        // Массив для хранения списка актов, загружаем в него из базы акты
        photoList = getPhotosByDefect(this, workId);
    }

    ////////////////////////////////////////РАБОТА С КМЕРОЙ//////////////////////////////////////

    // Кнопка добавления фото
    public void onAddPhotoClick(View v) {
        dispatchTakePictureIntent();
    }

    //Запускаем камеру
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                // Выдаем уникальный путь к фото
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.pea.du.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            }

            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }

    // Выдаем уникальный путь к фото
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        newPhoto.setPath(image.getAbsolutePath());
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            galleryAddPic();

            Defect defect = new Defect();
            defect.setServerId(workId);
            newPhoto.setDefect(defect);
            newPhoto.getImageFromPath();

            savePhoto();
            LoadData loadData = new LoadData();
            loadData.execute("");


            bAddPhoto.setEnabled(false);

            Controller controller = new Controller(this, SAVE_PHOTO, newPhoto); // последовательно загружаются все статичные данные
            controller.start();

        }

        if (requestCode == REQUEST_REFRESH_PHOTO){
            LoadData loadData = new LoadData();
            loadData.execute("");
        }
    }


    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(newPhoto.getPath());
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

}
