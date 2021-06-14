package ca.mcgill.ecse428.application;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import ca.mcgill.ecse428.application.dto.AccountDto;
import ca.mcgill.ecse428.application.dto.BidDto;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewProfileActivity extends AppCompatActivity {

    private static final String TAG = "ViewProfileActivity";
    Info endptInfo = new Info();
    private String username;
    private String fullName;
    private String email;
    public TextView usernameBox;
    public TextView emailBox;
    public TextView fullNameBox;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(endptInfo.getRoot())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    InterfaceAPI backend = retrofit.create(InterfaceAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        username=(String) i.getSerializableExtra("USERNAME");
        usernameBox = findViewById(R.id.view_profile_username);
        emailBox = findViewById(R.id.view_profile_email);
        fullNameBox = findViewById(R.id.view_profile_name);

        Log.e(TAG, "onCreate: "+username );
        fetchProfileInfo();
    }

    public void viewProfileAds(View view){
        Intent profileAdsIntent = new Intent(ViewProfileActivity.this, AccountAdsActivity.class);
        profileAdsIntent.putExtra("USERNAME",username);
        startActivity(profileAdsIntent);
    }


    private void fetchProfileInfo(){
        Observable<AccountDto> call = backend.getAccount(username);
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AccountDto>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull AccountDto dto) {
                       email = dto.getEmail();
                       fullName = dto.getFullName();

                       usernameBox.setText(username);
                       emailBox.setText(email);
                       fullNameBox.setText(fullName);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
