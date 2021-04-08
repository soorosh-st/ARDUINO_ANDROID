package com.example.ardu;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APICLIENT {
    public static Retrofit retrofit=null;
    public static Retrofit getapiclient(String url){
        if (retrofit==null){
            retrofit=new  Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
