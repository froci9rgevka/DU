package com.pea.du.data;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import com.pea.du.db.local.data.Contract;
import com.pea.du.db.local.methods.ReadMethods;

import java.util.ArrayList;
import java.util.List;

import static com.pea.du.flags.Flags.*;
import static java.lang.Integer.parseInt;

public class Photo implements Parcelable{
    private Integer id;
    private Integer serverId;
    private Defect defect;
    private Work work;
    private String path;
    private String url;
    private Bitmap image;

    public Photo() {
    }

    public Photo(Photo photo) {
        this.id = photo.getId();
        this.serverId = photo.getServerId();
        this.defect = new Defect(photo.getDefect());
        this.work = new Work(photo.getWork());
        this.path = photo.getPath();
        this.url = photo.getUrl();
        this.image = photo.getImage();
    }

    public Photo(Parcel source) {
        final ClassLoader cl = getClass().getClassLoader();

        Object[] data = source.readArray(cl);

        id = (Integer) data[0];
        serverId = (Integer) data[1];
        defect = (Defect) data[2];
        work = (Work) data[3];
        path = (String) data[4];
        url = (String) data[5];
        image = (Bitmap) data[6];
    }


    public static ArrayList<Photo> getPhotos() {
        ArrayList<Photo> photoList;
        if(workType.equals(DEFECT))
            photoList = ReadMethods.getPhotos(currentContext,
                    Contract.GuestEntry.WORK_ID + " = ? AND "
                            + Contract.GuestEntry.WORKTYPE + " = ?",
                    new String[]{workId.toString(), DEFECT});
        else
            photoList = ReadMethods.getPhotos(currentContext,
                Contract.GuestEntry.WORK_ID + " = ? AND "
                        + Contract.GuestEntry.WORKTYPE + " = ?",
                new String[]{workId.toString(), workStageType});
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


    public static Photo fromStringList(List<String> list, Context context){
        Photo photo = new Photo();

        photo.setServerId(parseInt(list.get(0)));

        Defect defect = new Defect();
        defect.setServerId(parseInt(list.get(1)));
        photo.setDefect(defect);

        photo.setUrl(list.get(2));

        return photo;
    }

    ///////////////////////////////////////Parcelable/////////////////////////////////////////////////////////

    @Override
    public int describeContents() {
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Object[] array = new Object[7];
        array[0] = id;
        array[1] = serverId;
        array[2] = defect;
        array[3] = work;
        array[4] = path;
        array[5] = url;
        array[6] = image;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }
}
