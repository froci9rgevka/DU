package com.pea.du.actyvities.defects.address.act.photo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.pea.du.R;
import com.pea.du.data.Photo;
import com.pea.du.db.methods.DeleteMethods;
import com.squareup.picasso.Picasso;

import java.io.File;


public class PhotoDetails extends ActionBarActivity {

    private Photo currentPhoto;
    private String title;

    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_photo_details);

        title = getIntent().getStringExtra("title");
        currentPhoto = (Photo) getIntent().getParcelableExtra("image");

        TextView titleTextView = (TextView) findViewById(R.id.photo_details_title);
        titleTextView.setText(title);

        ImageView imageView = (ImageView) findViewById(R.id.photo_details_image);


        if (!(currentPhoto.getUrl()==null))
            Picasso
                    .with(this)
                    .load(currentPhoto.getUrl())
                    .into(imageView);
        else
            Picasso
                    .with(this)
                    .load(new File(currentPhoto.getPath()))
                    //.resize(250, 200)
                    .into(imageView);


        deleteButton = (Button) findViewById(R.id.photo_details_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePhoto(currentPhoto);
                onBackPressed();

            }
        });
    }


    ///////////////////////////////////////////УДАЛЕНИЕ ФОТО//////////////////////////////////////////

    public void deletePhoto(Photo photo){
        File file = new File(photo.getPath());
        if(file.delete()){
            System.out.println(" файл удален");
        }else System.out.println("Файла не обнаружен");

        DeleteMethods.deletePhoto(this, photo);
    }


}
