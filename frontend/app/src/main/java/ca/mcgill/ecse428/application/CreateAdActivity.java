package ca.mcgill.ecse428.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import ca.mcgill.ecse428.application.dto.AdDto;
import ca.mcgill.ecse428.application.dto.BidDto;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateAdActivity extends AppCompatActivity {
    private static final String TAG = "CreateAdActivity";

    private CheckBox ironBox;
    private CheckBox bleachBox;
    private CheckBox foldBox;

    private TextView zipcodeStr;
    private TextView addressStr;
    private TextView descStr;
    private TextView instStr;
    private TextView weightStr;
    private TextView estimateLink;

    MaterialSpinner pickupDateSpinner;
    MaterialSpinner pickupStartSpinner;
    MaterialSpinner pickupEndSpinner;

    MaterialSpinner dropoffDateSpinner;
    MaterialSpinner dropoffStartSpinner;
    MaterialSpinner dropoffEndSpinner;

    SimpleDateFormat mediumFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH);
    SimpleDateFormat longFormatter = new SimpleDateFormat("yyyy-MMM-dd'T'HH:mm", Locale.ENGLISH);

    SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy");
    SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd");
    SimpleDateFormat hourFormatter = new SimpleDateFormat("HH");

    Date now;
    Calendar calendar;

    List<String> pickupDays = new ArrayList<String>();
    List<String> pickupStartHours = new ArrayList<String>();
    List<String> pickupEndHours = new ArrayList<String>();

    List<String> dropoffDays = new ArrayList<String>();
    List<String> dropoffStartHours = new ArrayList<String>();
    List<String> dropoffEndHours = new ArrayList<String>();


    String yearStr;

    String pickupDateStr;
    String pickupStartStr;
    String pickupEndStr;

    String dropoffDateStr;
    String dropoffStartStr;
    String dropoffEndStr;

    String token;
    String username;

    Gson dateParse = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm")
            .create();

    Info endptInfo = new Info();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(endptInfo.getRoot())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(dateParse))
            .build();

    InterfaceAPI interfaceAPI = retrofit.create(InterfaceAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ad);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        token=(String) i.getSerializableExtra("TOKEN");
        username=(String) i.getSerializableExtra("USERNAME");

        findTextViews();
        findSpinners();
        findCheckboxes();

        now = new Date(System.currentTimeMillis());
        calendar = Calendar.getInstance();
        calendar.setTime(now);
        yearStr = yearFormatter.format(calendar.getTime());

        populateDays();
        populateHours();

        setSpinnerItems();
        setSpinnerListeners();

        pickupDateStr= pickupDays.get(0);
        pickupStartStr=pickupStartHours.get(0);
        pickupEndStr=pickupEndHours.get(0);

        dropoffDateStr=dropoffDays.get(0);
        dropoffStartStr=dropoffStartHours.get(0);
        dropoffEndStr=dropoffEndHours.get(0);

    }

    public void createAd(View view){

        if (weightStr.getText().toString()==null || weightStr.getText().toString().isEmpty()){
            Toast.makeText(CreateAdActivity.this, "please specify a weight", Toast.LENGTH_LONG).show();
            return;
        }
        AdDto dto = new AdDto();
        dto.setWeight(Double.parseDouble(weightStr.getText().toString()));
        dto.setZipcode(zipcodeStr.getText().toString());
        dto.setAddress(addressStr.getText().toString());
        dto.setClothingDesc(descStr.getText().toString());
        dto.setSpecialInst(instStr.getText().toString());
        dto.setBleach(bleachBox.isChecked());
        dto.setIron(ironBox.isChecked());
        dto.setFold(foldBox.isChecked());


        Date pickupStart = longToMediumDate(concatDateStr(yearStr,pickupDateStr,pickupStartStr));
        Date pickupEnd = longToMediumDate(concatDateStr(yearStr,pickupDateStr,pickupEndStr));

        Date dropoffStart = longToMediumDate(concatDateStr(yearStr,dropoffDateStr,dropoffStartStr));
        Date dropoffEnd = longToMediumDate(concatDateStr(yearStr,dropoffDateStr,dropoffEndStr));

        dto.setPickupStart(pickupStart);
        dto.setPickupEnd(pickupEnd);
        dto.setDropoffStart(dropoffStart);
        dto.setDropoffEnd(dropoffEnd);

        dto.setAccount(username);
        dto.setPhoneNum("tbd");

        dto.setId("");
        Log.e(TAG, "createAd: "+dto.getId().toString() );

        Observable<AdDto> call = interfaceAPI.createAd(dto,"Bearer"+token);
        call
        .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AdDto>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull AdDto adDto) {
                        Log.e(TAG, "onNext: "+adDto.toString()+"\n created!");
                        Intent back = new Intent(CreateAdActivity.this,BackToMainActivity.class);
                        back.putExtra("MSG","ad created!");
                        startActivity(back);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (e instanceof HttpException){
                            ResponseBody body = ((HttpException) e).response().errorBody();
                            try {
                                Toast.makeText(CreateAdActivity.this, body.string(), Toast.LENGTH_LONG).show();
                            } catch (IOException ioException) {
                                Log.e(TAG, "onError: "+ioException.getLocalizedMessage());
                            }
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    private String concatDateStr(String yr, String date,String time){
        return yr+"-"+date.replace(" ","-")+"T"+time;
    }
    private Date longToMediumDate(String longFormatDate){
        try {
            Date longDate = longFormatter.parse(longFormatDate);
            String mediumStr = mediumFormatter.format(longDate);
            Date mediumDate=mediumFormatter.parse(mediumStr);
            return mediumDate;
        }
        catch (Exception e){
            Log.e(TAG, "longToMediumDate: "+e.getLocalizedMessage().toString() );
            return null;
        }
    }

    private void populateDays() {
        for (int i = 0; i < 7; i++) {
            calendar.setTime(now);
            calendar.add(Calendar.DATE, i);
            pickupDays.add(dateFormatter.format(calendar.getTime()));
            dropoffDays.add(dateFormatter.format(calendar.getTime()));
        }
    }

    private void populateHours() {

        for (int hourNow = 8; hourNow < 20; hourNow++) {
            pickupStartHours.add(String.valueOf(hourNow) + ":00");
            pickupEndHours.add(String.valueOf(hourNow) + ":00");

            dropoffStartHours.add(String.valueOf(hourNow) + ":00");
            dropoffEndHours.add(String.valueOf(hourNow) + ":00");
        }

    }

    private void findSpinners() {
        pickupDateSpinner = findViewById(R.id.pickup_date_spinner);
        pickupStartSpinner = findViewById(R.id.pickup_start_spinner);
        pickupEndSpinner = findViewById(R.id.pickup_end_spinner);

        dropoffDateSpinner = findViewById(R.id.dropoff_date_spinner);
        dropoffStartSpinner = findViewById(R.id.dropoff_start_spinner);
        dropoffEndSpinner = findViewById(R.id.dropoff_end_spinner);
    }

    private void findTextViews() {
        zipcodeStr = findViewById(R.id.zipcode_input_text);
        addressStr = findViewById(R.id.address_input_text);
        descStr = findViewById(R.id.clothing_desc_text_input);
        instStr = findViewById(R.id.special_inst_text_input);
        weightStr = findViewById(R.id.weight_input_text);
        estimateLink = findViewById(R.id.estimate_text);

        estimateLink.setPaintFlags(estimateLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        weightStr.setText("1.0");
    }

    private void findCheckboxes() {
        ironBox = findViewById(R.id.iron_checkbox);
        bleachBox = findViewById(R.id.bleach_check);
        foldBox = findViewById(R.id.fold_checkbox);
    }

    private void setSpinnerItems() {
        pickupDateSpinner.setItems(pickupDays);
        pickupStartSpinner.setItems(pickupStartHours);
        pickupEndSpinner.setItems(pickupEndHours);

        dropoffDateSpinner.setItems(dropoffDays);
        dropoffStartSpinner.setItems(dropoffStartHours);
        dropoffEndSpinner.setItems(dropoffEndHours);
    }

    private void setSpinnerListeners() {
        pickupDateSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                pickupDateStr = (String) item;
            }
        });

        pickupStartSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                pickupStartStr = (String) item;
            }
        });

        pickupEndSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                pickupEndStr = (String) item;
            }
        });

        dropoffDateSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                dropoffDateStr = (String) item;
            }
        });

        dropoffStartSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                dropoffStartStr = (String) item;
            }
        });

        dropoffEndSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                dropoffEndStr = (String) item;
            }
        });
    }
}