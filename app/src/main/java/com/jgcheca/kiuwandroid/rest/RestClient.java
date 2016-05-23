package com.jgcheca.kiuwandroid.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jgcheca.kiuwandroid.rest.service.ApiService;
import com.jgcheca.kiuwandroid.rest.service.SessionRequestInterceptor;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by asus on 03/06/2015.
 */
public class RestClient {
    private static final String BASE_URL = "https://api.kiuwan.com";


        public static Gson gson = new GsonBuilder()
                //.registerTypeAdapterFactory(new ItemTypeAdapterFactory()) // This is the important line ;)
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();

        public static RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(BASE_URL)
                .setConverter(new GsonConverter(gson))
                .setRequestInterceptor(new SessionRequestInterceptor())
                .build();

        public static ApiService apiService = restAdapter.create(ApiService.class);


}
