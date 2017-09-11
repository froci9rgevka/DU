package com.pea.du.actyvities.addresses.works.photo;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import com.pea.du.R;
import com.pea.du.actyvities.addresses.works.defectation.DefectActivity;
import com.pea.du.data.Defect;
import com.pea.du.data.Photo;
import com.pea.du.db.local.methods.WriteMethods;
import com.pea.du.db.remote.methods.SavePhoto;
import com.pea.du.tools.gridview.GridViewAdapter;
import com.pea.du.tools.gridview.ImageItem;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.pea.du.data.Photo.getPhotosByDefect;
import static com.pea.du.data.StaticValue.getNameById;
import static com.pea.du.db.local.data.Contract.GuestEntry.ADDRESS_TABLE_NAME;
import static com.pea.du.flags.Flags.addressId;
import static com.pea.du.flags.Flags.workId;
import static com.pea.du.tools.gridview.ImageItem.ITEM_PATH;
import static com.pea.du.tools.gridview.ImageItem.ITEM_URL;

public class PhotoGrid extends AppCompatActivity {

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_REFRESH_PHOTO = 2;

    // Список фото
    ArrayList photoList;

    // Новое фото
    private Photo newPhoto = new Photo();

    Button bAddPhoto;
    private android.support.v7.widget.Toolbar toolbar;
    private GridView gridView;
    private GridViewAdapter gridViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_grid);
    }

    private class LoadData extends AsyncTask<String,String,String> {

        private String address = null;


        @Override
        protected void onPreExecute()
        {
        }

        @Override
        protected String doInBackground(String... strings) {

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

            // Наполняем активити загруженным контентом
            if (workId==-1) {
            }
            else {
                gridViewAdapter = new GridViewAdapter(PhotoGrid.this, R.layout.simple_list_view_item, getImageItems());
                gridView.setAdapter(gridViewAdapter);
            }
        }
    }


    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Photo currentPhoto = (Photo) photoList.get(position);

            //Большой размер нельзя передать через Extra
            currentPhoto.setImage(null);

            ImageItem item = (ImageItem) parent.getItemAtPosition(position);
            //Create intent
            Intent intent = new Intent(PhotoGrid.this, PhotoDetails.class);
            intent.putExtra("title", item.getTitle());
            intent.putExtra("image", currentPhoto); //item.getImage()

            //Start details activity
            startActivityForResult(intent, REQUEST_REFRESH_PHOTO);
        }
    };

    // Подготовка картинок с текстом для mainGridView
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
            PhotoGrid.LoadData loadData = new PhotoGrid.LoadData();
            loadData.execute("");


            bAddPhoto.setEnabled(false);

            //Controller controller = new Controller(this, SAVE_PHOTO, newPhoto); // последовательно загружаются все статичные данные
            //controller.start();

            SavePhoto savePhoto = new SavePhoto(this, newPhoto);
            savePhoto.execute("");

        }

        if (requestCode == REQUEST_REFRESH_PHOTO){
            PhotoGrid.LoadData loadData = new PhotoGrid.LoadData();
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
