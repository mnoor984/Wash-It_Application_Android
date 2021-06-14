package ca.mcgill.ecse428.application;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;


public class MenuActivity extends AppCompatActivity {

    private static final String TAG = "MenuActivity";
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        username = (String) i.getSerializableExtra("USERNAME");
    }

    public void viewAllAds(View view){
        Intent allAdsIntent = new Intent (MenuActivity.this, AllAdsActivity.class);
        SharedPreferences p = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();

        //FOR TESTING PURPOSES ONLY. Retrieve token if successful login has been detected.
        String token = p.getString("token", "");
//        Toast.makeText(MenuActivity.this, token, Toast.LENGTH_LONG).show();

        allAdsIntent.putExtra("USERNAME",username);
        allAdsIntent.putExtra("TOKEN",token);
        startActivity(allAdsIntent);
    }

    public void viewMyAds(View view){
        Intent myAdsIntent = new Intent(MenuActivity.this, AccountAdsActivity.class);
        myAdsIntent.putExtra("USERNAME",username);
        startActivity(myAdsIntent);
    }

    public void createAd(View view){

        SharedPreferences p = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String token = p.getString("token", "");

        Intent createAdIntent = new Intent(MenuActivity.this, CreateAdActivity.class);
        createAdIntent.putExtra("TOKEN",token);
        createAdIntent.putExtra("USERNAME",username);
        startActivity(createAdIntent);
    }

    public void modifyAd(View view) {
        Intent modifyIntent = new Intent(MenuActivity.this, ModifyAdActivity.class);
        modifyIntent.putExtra("ID", 71);
        startActivity(modifyIntent);
    }

    public void modifyAccount(View view) {
        Intent modifyIntent = new Intent(MenuActivity.this, ModifyAccountActivity.class);
        modifyIntent.putExtra("ID", username);
        startActivity(modifyIntent);
    }
    public void deleteAccount(View view) {
        Intent deleteIntent = new Intent(MenuActivity.this, DeleteAccountActivity.class);
        startActivity(deleteIntent);
    }
}