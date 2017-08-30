package com.pea.du.actyvities.defects.address.act.photo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.pea.du.data.Defect;
import com.pea.du.data.Photo;
import com.pea.du.tools.gridview.GridViewAdapter;
import com.pea.du.tools.gridview.ImageItem;
import com.pea.du.R;
import com.pea.du.db.methods.WriteMethods;
import com.pea.du.web.client.Controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.pea.du.data.Photo.getPhotosByDefect;
import static com.pea.du.web.client.Contract.LOAD_STATIC_ADDRESS;
import static com.pea.du.web.client.Contract.SAVE_PHOTO;


public class PhotoLibrary extends AppCompatActivity {

    private Defect currentDefect;

    private GridView gridView;
    private GridViewAdapter gridViewAdapter;
    private android.support.v7.widget.Toolbar toolbar;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_REFRESH_PHOTO = 2;
    static final int REQUEST_CHECK_CAMERA = 3;



    // Список фото
    ArrayList photoList;

    // Новое фото
    private Photo newPhoto = new Photo();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_photo_library);

        currentDefect = (Defect) getIntent().getParcelableExtra("Defect");

        //Устанавливаем тайтл
        this.setTitle(currentDefect.toString());


        //mImageView = (ImageView) findViewById(R.id.imageView);

        gridView = (GridView) findViewById(R.id.gridView);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.photo_library_toolbar);

        toolbar.setTitle("Фото дефектов");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loadPhotosInGrid();


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Photo currentPhoto = (Photo) photoList.get(position);

                //Большой размер нельзя передать через Extra
                currentPhoto.setImage(null);

                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                //Create intent
                Intent intent = new Intent(PhotoLibrary.this, PhotoDetails.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("image", currentPhoto); //item.getImage()

                //Start details activity
                startActivityForResult(intent, REQUEST_REFRESH_PHOTO);

            }
        });

    }


    public void loadPhotosInGrid() {
        loadPhotos();
        gridViewAdapter = new GridViewAdapter(this, R.layout.activity_photo_library_item, getData());
        gridView.setAdapter(gridViewAdapter);
    }

    // Подготовка картинок с текстом для gridView
    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        //TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        for (int i = 0; i < photoList.size(); i++) { //img.length
            //Drawable d = getResources().getDrawable(imgs.getResourceId(i, -1));
            //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
           // Bitmap bitmap = drawableToBitmap(d);
            Photo photo = (Photo) photoList.get(i);
            photo.getImageFromPath();
            imageItems.add(new ImageItem(photo.getImage(), "Image#" + i));
        }
        return imageItems;
    }

    // Конвертация Drawable в Bitmap
    public static Bitmap drawableToBitmap (Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }



    /////////////////////////////////////РАБОТА С БД////////////////////////////////////////////

    public void savePhoto() {
        WriteMethods.setDefectPhoto(this, newPhoto);
    }


    public void loadPhotos () {

        // Массив для хранения списка актов, загружаем в него из базы акты
        photoList = getPhotosByDefect(this, currentDefect);
    }


        ////////////////////////////////////////РАБОТА С КМЕРОЙ//////////////////////////////////////

    // Кнопка добавления фото
    public void onAddClick(View v) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            galleryAddPic();

            newPhoto.setDefect(currentDefect);
            newPhoto.getImageFromPath();

            //savePhoto();
            //loadPhotosInGrid();

            Controller controller = new Controller(this, SAVE_PHOTO, newPhoto); // последовательно загружаются все статичные данные
            controller.start();

        }

        if (requestCode == REQUEST_REFRESH_PHOTO){
            loadPhotosInGrid();
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

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(newPhoto.getPath());
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

}
