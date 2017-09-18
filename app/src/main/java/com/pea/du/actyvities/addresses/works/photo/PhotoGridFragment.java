package com.pea.du.actyvities.addresses.works.photo;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.pea.du.R;
import com.pea.du.data.Defect;
import com.pea.du.data.Photo;
import com.pea.du.data.Work;
import com.pea.du.db.local.methods.WriteMethods;
import com.pea.du.db.remote.methods.SavePhoto;
import com.pea.du.tools.gridview.GridViewAdapter;
import com.pea.du.tools.gridview.ImageItem;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static com.pea.du.data.Photo.getPhotos;
import static com.pea.du.flags.Flags.*;
import static com.pea.du.tools.gridview.ImageItem.ITEM_PATH;
import static com.pea.du.tools.gridview.ImageItem.ITEM_URL;

public class PhotoGridFragment extends android.support.v4.app.Fragment {

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_REFRESH_PHOTO = 2;

    // Список фото
    ArrayList photoList;

    // Новое фото
    private Photo newPhoto = new Photo();

    View view;
    Button bAddPhoto;
    ProgressBar progressBar;
    private GridView gridView;
    private GridViewAdapter gridViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {

        if(workId == -1){
            view = inflater.inflate(R.layout.empty_layout,
                    container, false);
            return view;}

        view = inflater.inflate(R.layout.activity_photo_grid,
                container, false);

        objInit();

        setContent();

        return view;

    }

    private void objInit(){
        bAddPhoto = (Button) view.findViewById(R.id.addPhoto_button);

        progressBar = (ProgressBar) view.findViewById(R.id.photoProgressBar);

        gridView = (GridView) view.findViewById(R.id.photo_gridView);
        gridView.setOnItemClickListener(onItemClickListener);
    }

    private void setContent(){
        bAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        bAddPhoto.setEnabled(!isPhotoSending);

        LoadPhotoInGrid loadPhotoInGrid = new LoadPhotoInGrid();
        loadPhotoInGrid.execute("");
    }


    private class LoadPhotoInGrid extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings)
        {
            loadPhotos();

            return null;
        }

        @Override
        protected void onPostExecute(String r)
        {
            progressBar.setVisibility(View.VISIBLE);
            gridViewAdapter = new GridViewAdapter(currentContext, R.layout.simple_list_view_item, getImageItems());
            gridView.setAdapter(gridViewAdapter);
            progressBar.setVisibility(View.GONE);
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
            Intent intent = new Intent(currentContext, PhotoDetails.class);
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
        WriteMethods.setPhoto(currentContext, newPhoto);
    }


    public void loadPhotos () {
        // Массив для хранения списка актов, загружаем в него из базы акты
        photoList = getPhotos();
    }

    ////////////////////////////////////////РАБОТА С КМЕРОЙ//////////////////////////////////////

    //Запускаем камеру
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(currentContext.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                // Выдаем уникальный путь к фото
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(currentContext, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(currentContext,
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
        File storageDir = currentContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            galleryAddPic();

            if (workType.equals(DEFECT))
                newPhoto.setDefect(new Defect(workId));
            else
                newPhoto.setWork(new Work(workId));

            savePhoto();
            PhotoGridFragment.LoadPhotoInGrid loadPhotoInGrid = new PhotoGridFragment.LoadPhotoInGrid();
            loadPhotoInGrid.execute("");


            //Controller controller = new Controller(this, SAVE_PHOTO, newPhoto); // последовательно загружаются все статичные данные
            //controller.start();

            isPhotoSending = true;

            SavePhoto savePhoto = new SavePhoto(currentContext, newPhoto);
            savePhoto.execute("");

        }

        if (requestCode == REQUEST_REFRESH_PHOTO){
            PhotoGridFragment.LoadPhotoInGrid loadPhotoInGrid = new PhotoGridFragment.LoadPhotoInGrid();
            loadPhotoInGrid.execute("");
        }
    }


    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(newPhoto.getPath());
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        currentContext.sendBroadcast(mediaScanIntent);
    }

}
