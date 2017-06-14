package com.expreso.androidapp.androidapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.expreso.androidapp.androidapp.Models.TokenRequest;
import com.expreso.androidapp.androidapp.Models.TokenResponse;
import com.expreso.androidapp.androidapp.Util.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    String BASE_URL = "http://54.94.135.100/";

    //Declaring our ImageView
    private ImageView imageView;

    // 1.- Declaramos
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Switch switchRemember;
    private Button buttonLogin;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        bindUI();

        // 7.- Creamos una instancia donde almacenaremos el email y la clave
        // esta en modo lectura
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setCredentialsIfExist();
        //**************
        // 5.- codigo del boton
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if (login(email, password)) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    IStockService service = retrofit.create(IStockService.class);

                    TokenRequest tokenRequest = new TokenRequest();
                    tokenRequest.setUsername(email);
                    tokenRequest.setPassword(password);
                    Call<TokenResponse> tokenResponse = service.getLoginDetails(tokenRequest);

                    tokenResponse.enqueue(new Callback<TokenResponse>() {
                        @Override
                        public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {


                            TokenResponse tokenResponse = response.body();

                            if(tokenResponse.getUser().is_active() && tokenResponse.getToken()!=null) {
                                goToMain();
                                Log.d("Status", "Login Pass");
                            }   else {
                                Log.d("Status", "error de usuario o password");
                            }


                        }

                        @Override
                        public void onFailure(Call<TokenResponse> call, Throwable t) {
                            Log.d("Status","Login Failed");
                        }
                    });

                    //goToMain();
                 //   saveOnPreferences(email,password);



                }

            }



        });




    }
    //*******************
    // 4.- Validar los campos email y password
    private boolean login(String email, String password) {
        // si correo no es valido
        if (!isValidEmail(email))
        {
            Toast.makeText(this,"Email is no valid, please try again", Toast.LENGTH_LONG).show();
            return false;
            // si password no es valido
        } else if (!isValidPassword(password)) {
            Toast.makeText(this,"Password is no valid, 8 characters o more, please try again", Toast.LENGTH_LONG).show();
            return  false;
        } else
            return true;
    }
    // 3.- Validamos el Email
    private boolean isValidEmail(String email){
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // 4.- Validamos el Password

    private boolean isValidPassword(String password){
        return password.length() > 8;
    }

    // 6.-
    private void goToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    // 8.-
    private void saveOnPreferences(String email, String password){
        if (switchRemember.isChecked() ) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("email",email);
            editor.putString("pass",password);
            //    editor.commit();
            editor.apply();
        }
    }

    //********************
    // 2.-Enlazar
    // meotodo bindUI
    private void bindUI() {
        //Initializing the ImageView
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonLogin = (Button) findViewById(R.id.logIn_butGetStarted);
    }
    //*******---------------------
    private void setCredentialsIfExist(){
        String email = Util.getUserMailPrefs(prefs);
        String password = Util.getUserPassPrefs(prefs);
        // si no esta vacio
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            editTextEmail.setText(email);
            editTextPassword.setText(password);
            switchRemember.setChecked(true);
        }
    }
    //*******---------------------



}
