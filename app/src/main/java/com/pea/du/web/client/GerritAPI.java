package com.pea.du.web.client;


import com.pea.du.data.Act;
import com.pea.du.data.Defect;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.*;


public interface GerritAPI {

    @GET("message/addresses/")
    Call<Object> loadAddresses(@Query("q") String status);
    @GET("message/measures/")
    Call<Object> loadMeasures(@Query("q") String status);
    @GET("message/constructiveElements/")
    Call<Object> loadConstructiveElements(@Query("q") String status);
    @GET("message/types/")
    Call<Object> loadTypes(@Query("q") String status);
    @GET("message/acts/")
    Call<Object> loadActs(@Query("q") String status);
    @GET("message/defects/")
    Call<Object> loadDefects(@Query("q") String status);
    @GET("message/photos/")
    Call<Object> loadPhotos(@Query("q") String status);

    @GET("message/checkUser/")
    Call<Object> checkUser(@Query("username") String username);


    @POST("message/act/")
    Call<Object> saveAct(@Query("query") String query);

    @POST("message/defect/")
    Call<Object> saveDefect(@Query("query") String query);

    @POST("message/photoInfo/")
    Call<Object> savePhotoInfo(@Query("length") Integer length, @Query("defectId") Integer defectId);

    @POST("message/photo/")
    Call<Object> savePhoto(@Query("encodedPart") String encodedPart);;

    @Multipart
    @POST("message/fastphoto/")
    Call<Object> fastSave(@Part("description") RequestBody description,
                            @Part MultipartBody.Part file);
}