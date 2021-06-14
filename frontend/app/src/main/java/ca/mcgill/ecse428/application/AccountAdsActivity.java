package ca.mcgill.ecse428.application;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import ca.mcgill.ecse428.application.dto.AdDto;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountAdsActivity extends AppCompatActivity {
    private static final String TAG = "AllAdsActivity";
    Info endptInfo = new Info();

    Gson dateParse = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm")
            .create();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(endptInfo.getRoot())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(dateParse))
            .build();

    InterfaceAPI backend = retrofit.create(InterfaceAPI.class);

    private String username;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_ads);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        username = (String) i.getSerializableExtra("USERNAME");
        token = (String) i.getSerializableExtra("TOKEN");

        Log.e(TAG, "onCreate: "+username );
        Log.e(TAG, "onCreate: "+token );
        fetchAds();

    }

    public void fetchAds(){
        backend.getAccountAds(username)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<AdDto>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.e(TAG, "onSubscribe: " );
                    }

                    @Override
                    public void onNext(@NonNull List<AdDto> adDtos) {
                        startRecyclerView(adDtos);
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError: "+e.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete: ");

                    }
                });

    }

    public void startRecyclerView(List<AdDto> adDtos){

        RecyclerView adsRecyclerView = findViewById(R.id.ads_recycler_view);
        AdsRecyclerAdapter adapter = new AdsRecyclerAdapter(adDtos, username,token, AccountAdsActivity.this);
        adsRecyclerView.setAdapter(adapter);
        adsRecyclerView.setLayoutManager(new LinearLayoutManager(AccountAdsActivity.this));
    }

}