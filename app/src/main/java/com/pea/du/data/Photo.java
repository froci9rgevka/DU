package com.pea.du.data;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import com.pea.du.db.data.Contract;
import com.pea.du.db.methods.ReadMethods;

import java.util.ArrayList;

public class Photo implements Parcelable{
    private Integer id;
    private Integer serverId;
    private Defect defect;
    private String path;
    private Bitmap image;

    public Photo() {
    }

    public Photo(Integer id, Defect defect, String path, Bitmap image) {
        this.id = id;
        this.defect = new Defect(defect);
        this.path = path;
        this.image = image;
    }

    public Photo(Parcel source) {
        final ClassLoader cl = getClass().getClassLoader();

        Object[] data = source.readArray(cl);

        id = (Integer) data[0];
        serverId = (Integer) data[1];
        defect = (Defect) data[2];
        path = (String) data[3];
        image = (Bitmap) data[4];
    }


    public static ArrayList<Photo> getPhotosByDefect(Context context, Defect defect) {
        ArrayList<Photo> photoList = ReadMethods.getDefectPhotos(context,
                Contract.GuestEntry.DEFECT_ID + " = ?",
                new String[]{defect.getServerId().toString()});
        return photoList;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", defect=" + defect +
                ", path='" + path + '\'' +
                ", image=" + image +
                '}';
    }


    public void getImageFromPath() {
        // Get the dimensions of the View
        //int targetW = mImageView.getWidth();
        //int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        //int photoW = bmOptions.outWidth;
        //int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = 1;//Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        image = BitmapFactory.decodeFile(path, bmOptions);
    }


    ///////////////////////////////////////Parcelable/////////////////////////////////////////////////////////

    @Override
    public int describeContents() {
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Object[] array = new Object[5];
        array[0] = id;
        array[1] = serverId;
        array[2] = defect;
        array[3] = path;
        array[4] = image;

        dest.writeArray(array);
    }

    public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {

        @Override
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Defect getDefect() {
        return defect;
    }

    public void setDefect(Defect defect) {
        this.defect = defect;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }
}
