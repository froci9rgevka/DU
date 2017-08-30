package com.pea.du.web.client;


import com.pea.du.data.Act;
import com.pea.du.data.Defect;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


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

    @GET("message/checkUser/")
    Call<Object> checkUser(@Query("username") String username);


    @POST("message/act/")
    Call<Object> saveAct(@Query("query") String query);

    @POST("message/defect/")
    Call<Object> saveDefect(@Query("query") String query);

    @POST("message/photo/")
    Call<Object> savePhoto(@Query("encodedPart") String encodedPart);

    @POST("message/photoInfo/")
    Call<Object> savePhotoInfo(@Query("length") Integer length);
}