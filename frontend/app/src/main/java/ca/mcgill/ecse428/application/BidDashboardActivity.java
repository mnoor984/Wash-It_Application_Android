package ca.mcgill.ecse428.application;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.DashPathEffect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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

public class BidDashboardActivity extends AppCompatActivity {

    private static final String TAG = "BidDashboardActivity";

    Info endptInfo = new Info();

    Gson dateParse = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(endptInfo.getRoot())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(dateParse))
            .build();

    InterfaceAPI backend = retrofit.create(InterfaceAPI.class);

    private String adId;
    private String bidderUsername;
    private String token;
    List<BidDto> bids;

    private TextView bidAmountText;
    private String bidAmountStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_dashboard);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        adId=(String) i.getSerializableExtra("ADID");
        bidderUsername=(String) i.getSerializableExtra("USERNAME");
        token=(String) i.getSerializableExtra("TOKEN");

        bidAmountText=findViewById(R.id.bidAmountText);
        bidAmountText.setText("1.0");

        Log.e(TAG, "onCreate: "+adId );
        Log.e(TAG, "onCreate: "+bidderUsername );
        fetchBids();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void makeBid(View view){
        bidAmountStr=bidAmountText.getText().toString();
        BidDto bidDto = new BidDto();
        bidDto.setAdId(Integer.valueOf(adId));
        bidDto.setAmount(Float.valueOf(bidAmountStr));
        bidDto.setUsername(bidderUsername);
        bidDto.setAccepted(false);


        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        bidDto.setDateTimeCreated(now.toString());

        Log.e(TAG, "makeBid: "+dateParse.toJson(bidDto) );

        Observable<BidDto> call = backend.createBid(bidDto, "Bearer "+token);
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BidDto>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull BidDto bidDto) {
                        Log.e(TAG, "onNext: "+bidDto.toString() );
                        Intent i = new Intent(BidDashboardActivity.this, BidDashboardActivity.class);
                        i.putExtra("ADID",adId);
                        i.putExtra("USERNAME",bidderUsername);
                        i.putExtra("TOKEN",token);
                        startActivity(i);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError: "+e.getLocalizedMessage() );
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void fetchBids(){
        Observable<List<BidDto>> call = backend.getBids(adId);
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BidDto>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<BidDto> bidDtos) {
                        for (BidDto bid: bidDtos){
                            Log.e(TAG, bid.toString());
                        }
                        bids=bidDtos;
                        startRecyclerView();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void startRecyclerView(){
        RecyclerView bidsRecyclerView = findViewById(R.id.bid_recycler_view);
        BidsRecyclerAdapter adapter = new BidsRecyclerAdapter(bids,BidDashboardActivity.this);
        bidsRecyclerView.setAdapter(adapter);
        bidsRecyclerView.setLayoutManager(new LinearLayoutManager(BidDashboardActivity.this));
    }
}