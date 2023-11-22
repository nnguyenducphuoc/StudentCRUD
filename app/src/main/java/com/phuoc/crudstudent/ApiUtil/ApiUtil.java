package com.phuoc.crudstudent.ApiUtil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.phuoc.crudstudent.model.Student;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiUtil {
    //   http://localhost/crud_students/db_read.php

    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient.Builder okBuilder = new OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(loggingInterceptor);

    ApiUtil apiutil = new Retrofit.Builder()
            .baseUrl("https://spaceofme.000webhostapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okBuilder.build())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(ApiUtil.class);

    @GET("db_read.php")
    Observable<List<Student>> readStudents();

    @FormUrlEncoded
    @POST("db_update.php")
    Call<Student> updateStudent(@Field("id") String id, @Field("first_name") String first_name, @Field("last_name") String last_name, @Field("email") String email);

    @FormUrlEncoded
    @POST("db_delete.php")
    Call<Student> deleteStudent(@Field("id") String id);

    @FormUrlEncoded
    @POST("db_insert.php")
    Call<Student> insertStudent(@Field("first_name") String first_name, @Field("last_name") String last_name, @Field("email") String email);
}
