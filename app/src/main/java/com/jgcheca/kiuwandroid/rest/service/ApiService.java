package com.jgcheca.kiuwandroid.rest.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jgcheca.kiuwandroid.rest.model.User;
import com.jgcheca.kiuwandroid.rest.model.AppKiuwan;

import java.util.List;


import retrofit.Callback;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by asus on 03/06/2015.
 */
public interface ApiService {

    public static final String BASE_URL = "https://api.kiuwan.com";

    /**
     * Login
     * @param hash
     * @param session
     */

        @GET("/info")
        void login(@Header("Authorization")String hash, Callback<JsonObject> session);

    /**Management
     *
     * GET & POST & PUT & DELETE
     *
     * @param cb
     */

        //List Users
        @GET("/users")
        void getUsers(Callback<List<User>> cb);

        //
        @POST("/user")
        void postUser();

        @POST("/application")
        void postApplication();

        @PUT("/user")
        void putUser();

        @PUT("/application")
        void putApplication();


        @DELETE("/user")
        void deleteUser();


        @DELETE("/application")
        void deleteApplication();





    //Get application list
    @GET("/apps/list")
    void getApps(@Header("Cookie")String hash, Callback<JsonArray> cb);

    //Get all application analysis code
    @GET("/apps/{appName}/analyses")
    void getAppAnalysisCodes(@Header("Authorization")String hash,@Path("appName") String appName, Callback<List<AppKiuwan>> cb);

    //Get the last application analysis
    @GET("/apps/{appName}")
    void getAppAnalysis(@Header("Authorization")String hash,@Path("appName") String appName, Callback<List<AppKiuwan>> cb);

    //Get the application analysis given by analysis code
    @GET("/apps/analysis/:analysisCode")
    void getAppAnalysisAnyVersion(Callback<List<AppKiuwan>> cb);

    //Get application defects
    @GET("/apps/:appName/defects")
    void getAppDefects(Callback<List<AppKiuwan>> cb);

    @GET("/apps/analysis/:analysisCode/defects")
    void getAppDefectsAnyVersion(Callback<List<AppKiuwan>> cb);

    //Get comparison of defects of two different analysis
    @GET("/apps/analysis/:code/defects/compare/:previouscode")
    void getAppsComparisonDefects(Callback<List<AppKiuwan>> cb);

    //Get list of new defects in a comparison
    @GET("/apps/analysis/:code/defects/compare/:previouscode/new")
    void getAppNewDefectsFromPreviousAnalysis(Callback<List<AppKiuwan>> cb);

    //Get the list of removed defects in a comparison
    @GET("/apps/analysis/:code/defects/compare/:previouscode/removed")
    void getAppRemovedDefectsFromPreviousAnalysis(Callback<List<AppKiuwan>> cb);

    //List files with metric values and defects
    @GET("/apps/:appName/files")
    void getAppFilesWithMetricDefects(Callback<List<AppKiuwan>> cb);

    @GET("/apps/analysis/:analysisCode/files")
    void getAppFilesWithMetricDefectsAnyVersion(Callback<List<AppKiuwan>> cb);



}
