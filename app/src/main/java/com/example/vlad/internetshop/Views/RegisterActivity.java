package com.example.vlad.internetshop.Views;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vlad.internetshop.Data.ShopAPI;
import com.example.vlad.internetshop.Enteties.User;
import com.example.vlad.internetshop.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private EditText mEtName;
    private EditText mEtEmail;
    private EditText mEtPassword;
    private EditText mEtConfirmPassword;
    private EditText mEtUsername;
    private String mName;
    private String mUsername;
    private String mEmail;
    private String mPassword;
    private String mConfirmPassword;
    private String url = "http://multiflexersshop.azurewebsites.net";
    private Gson gson = new GsonBuilder().create();

    private SendRequest request;
    private Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(url).build();

    private ShopAPI intrf = retrofit.create(ShopAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEtName = findViewById(R.id.name);
        mEtUsername = findViewById(R.id.username);
        mEtEmail = findViewById(R.id.email);
        mEtPassword = findViewById(R.id.password);
        mEtConfirmPassword = findViewById(R.id.confirmPassword);
    }

    public void btnSignupClick(View view){
        initialize();
        if(!validate()){
            Toast.makeText(this, "Sign up has failed", Toast.LENGTH_SHORT).show();
        }
        else{
            onSignupSuccess();
        }
    }

    private boolean validate(){
        boolean valid = true;
        if(mName.isEmpty()|| mName.length() > 32){
            mEtName.setError("Please enter valid name");
            valid = false;
        }

        if(mUsername.isEmpty()|| mUsername.length() > 32){
            mEtUsername.setError("Please enter valid username");
            valid = false;
        }

        if(mEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()){
            mEtEmail.setError("Please enter valid email");
            valid = false;
        }

        if(mPassword.isEmpty()){
            mEtPassword.setError("Please enter Password");
            valid = false;
        }

        if(mConfirmPassword.isEmpty()){
            mEtConfirmPassword.setError("Please confirm your password");
            valid = false;
        }

        if(!mPassword.equals(mConfirmPassword)){
            mEtConfirmPassword.setError("Passwords does not match");
            valid = false;
        }

        return valid;
    }

    private void onSignupSuccess(){
        request = new SendRequest();
        request.execute(intrf);
    }

    private void initialize(){
        mName = mEtName.getText().toString().trim();
        mUsername = mEtUsername.getText().toString().trim();
        mEmail = mEtEmail.getText().toString().trim();
        mPassword = mEtPassword.getText().toString().trim();
        mConfirmPassword = mEtConfirmPassword.getText().toString().trim();
    }

    public class SendRequest extends AsyncTask<ShopAPI, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(ShopAPI... links) {

            Boolean result;

            try{
                User user = new User ();
                user.userName = mUsername;
                user.password = mPassword;
                Call<Boolean> call = links[0].registerUser(user);
                Response<Boolean> response = call.execute();
                result = response.body();

            } catch (Exception e){
                return false;
            }

            return result;
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
            if(aVoid || aVoid == null) {
                finish();
            }
            else
                Toast.makeText(RegisterActivity.this, "You were not registered\n" +
                        "Password require upper case,\nlower case and special characters\n" +
                        "and also digits", Toast.LENGTH_LONG).show();
        }
    }
}
