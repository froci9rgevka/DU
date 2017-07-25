package com.pea.du;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

public class InspectionDetails extends Activity {
    private final int CAMERA_RESULT = 0;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_details);

        mImageView = (ImageView) findViewById(R.id.inspection_details_imagView);

    }

    public void onInspectionDetailsPhotoClick(View v) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_RESULT) {
            Bitmap thumbnailBitmap = (Bitmap) data.getExtras().get("data");
            mImageView.setImageBitmap(thumbnailBitmap);
        }
    }
}
