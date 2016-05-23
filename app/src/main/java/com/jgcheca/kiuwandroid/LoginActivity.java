package com.jgcheca.kiuwandroid;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;


import com.dd.processbutton.iml.ActionProcessButton;
import com.google.gson.JsonObject;
import com.jgcheca.kiuwandroid.Utils.Session;
import com.jgcheca.kiuwandroid.rest.RestClient;
import com.jgcheca.kiuwandroid.rest.model.AppKiuwan;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;


import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.mime.TypedInput;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    ActionProcessButton btnSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = LoginActivity.this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(LoginActivity.this.getResources().getColor(R.color.colorPrimaryDark));
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        btnSignIn = (ActionProcessButton) findViewById(R.id.btnLogin);
        //throw new RuntimeException("This is a crash");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void Login(View v){
        btnSignIn.setProgress(50);
        final Intent loginIntent = new Intent(this,MainActivity.class);
        String credentials = ((EditText)findViewById(R.id.txtUser)).getText().toString() + ":" + ((EditText)findViewById(R.id.txtPassword)).getText().toString();
        String string = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        Session.get().setAuthorization(string);
        String appName = "Juegos en Grupo Server";
       /* RestClient.apiService.getAppAnalysis(string,appName,new Callback<List<AppKiuwan>>() {
                                          @Override
                                          public void success(List<AppKiuwan> appKiuwans, Response response) {
                                              TypedInput body = response.getBody();
                                              try {
                                                  BufferedReader reader = new BufferedReader(new InputStreamReader(body.in()));
                                                  StringBuilder out = new StringBuilder();
                                                  String newLine = System.getProperty("line.separator");
                                                  String line;
                                                  while ((line = reader.readLine()) != null) {
                                                      out.append(line);
                                                      out.append(newLine);
                                                  }

                                                  // Prints the correct String representation of body.
                                                  Log.d("RETROFIT response", String.valueOf(out));
                                              } catch (IOException e) {
                                                  e.printStackTrace();
                                              }
                                          }

                                          @Override
                                          public void failure(RetrofitError error) {

                                          }
                                      });*/
        RestClient.apiService.login(Session.get().getAuthorization(), new Callback<JsonObject>() {

                    @Override
                    public void success(JsonObject s, Response response) {
            /*User userData = new User(((EditText)findViewById(R.id.email)).getText().toString(),"user");
            userData.setSessionId(s.get("user").getAsJsonObject().get("sessionId").getAsString());
            loginIntent.putExtra("user", userData);*/

                        String username = s.get("username").getAsString();
                        String organization = s.get("organization").getAsString();

                        List<Header> headers = response.getHeaders();
                        String sessionID="";
                        for(int i= 0 ; i<headers.size();i++){

                            if (headers.get(i).getValue().contains("JSESSIONID=")){
                                int index = headers.get(i).getValue().indexOf("JSESSIONID=");

                                int endIndex = headers.get(i).getValue().indexOf(";", index);

                               sessionID = headers.get(i).getValue().substring(
                                        index + "JSESSIONID=".length(), endIndex);

                                Log.d("HEADER COOKIE ",sessionID);

                                i=headers.size();

                            }

                        }



                        loginIntent.putExtra("username", username);
                        loginIntent.putExtra("organization", organization);
                        loginIntent.putExtra("sessionID",sessionID);

                        Session.get().setSessionId(sessionID);

                        TypedInput body = response.getBody();
                        try {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(body.in()));
                            StringBuilder out = new StringBuilder();
                            String newLine = System.getProperty("line.separator");
                            String line;
                            while ((line = reader.readLine()) != null) {
                                out.append(line);
                                out.append(newLine);
                            }

                            // Prints the correct String representation of body.
                            Log.d("RETROFIT response", String.valueOf(out));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        startActivity(loginIntent);
                        btnSignIn.setProgress(0);
                        Toast.makeText(LoginActivity.this,
                                "Successfully logged in", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("LoginActivity", "FAILURE");
                        Log.d("LoginActivity", error.toString());

                        Log.d("Tipo de error", error.getKind().toString());

                        // Log.d("Tipo de error", error.);
                        //error.isNetworkError()
                        switch (error.getKind()) {
                            case NETWORK:
                                Toast.makeText(LoginActivity.this,
                                        "Network unavailable", Toast.LENGTH_SHORT).show();
                                break;
                            case HTTP:
                                Toast.makeText(LoginActivity.this,
                                        "Credentials wrong", Toast.LENGTH_SHORT).show();
                                break;
                            case UNEXPECTED:
                                Toast.makeText(LoginActivity.this,
                                        "UNEXPECTED", Toast.LENGTH_SHORT).show();
                                break;
                            case CONVERSION:
                                Toast.makeText(LoginActivity.this,
                                        "CONVERSION", Toast.LENGTH_SHORT).show();
                                break;
                        }

                        btnSignIn.setProgress(0);

                    }
                });
    }

}

