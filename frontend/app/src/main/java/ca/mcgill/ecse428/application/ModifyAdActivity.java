package ca.mcgill.ecse428.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.textservice.SpellCheckerInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
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
import java.util.Random;

import ca.mcgill.ecse428.application.dto.AdDto;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ModifyAdActivity extends AppCompatActivity {

    private static final String TAG = "ModifyAdActivity";

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

    AdDto incomingDto;
    AdDto outgoingDto = new AdDto();

    int adId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_ad);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        adId=(Integer) intent.getSerializableExtra("ID");

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

        fetchAd(adId);

    }

    private void filloutExisting(){

        zipcodeStr.setText(incomingDto.getZipcode());
        addressStr.setText(incomingDto.getAddress());
        descStr.setText(incomingDto.getClothingDesc());
        instStr.setText(incomingDto.getSpecialInst());

        weightStr.setText(String.valueOf(incomingDto.getWeight()));
        ironBox.setChecked(incomingDto.isIron());
        bleachBox.setChecked(incomingDto.isBleach());
        foldBox.setChecked(incomingDto.isFold());


    }


    private void fetchAd(int id){
        Observable<AdDto> call = interfaceAPI.getAd(id);
        call
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AdDto>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull AdDto adDto) {
                        incomingDto=adDto;
                        filloutExisting();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void deleteAd(View view){
        Observable<ResponseBody> call = interfaceAPI.deleteAd(71);
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        try {
                            Log.e(TAG, "onDeleteOK: "+responseBody.string() );
                            Intent back = new Intent (ModifyAdActivity.this, BackToMainActivity.class);
                            back.putExtra("MSG","ad deleted!");
                            startActivity(back);
                        } catch (IOException ioException) {
                            Log.e(TAG, "onError: "+ioException.getLocalizedMessage());
                        }
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

    public void modifyAd(View view){
        outgoingDto.setWeight(Double.parseDouble(weightStr.getText().toString()));
        outgoingDto.setZipcode(zipcodeStr.getText().toString());
        outgoingDto.setAddress(addressStr.getText().toString());
        outgoingDto.setClothingDesc(descStr.getText().toString());
        outgoingDto.setSpecialInst(instStr.getText().toString());
        outgoingDto.setBleach(bleachBox.isChecked());
        outgoingDto.setIron(ironBox.isChecked());
        outgoingDto.setFold(foldBox.isChecked());


        Date pickupStart = longToMediumDate(concatDateStr(yearStr,pickupDateStr,pickupStartStr));
        Date pickupEnd = longToMediumDate(concatDateStr(yearStr,pickupDateStr,pickupEndStr));

        Date dropoffStart = longToMediumDate(concatDateStr(yearStr,dropoffDateStr,dropoffStartStr));
        Date dropoffEnd = longToMediumDate(concatDateStr(yearStr,dropoffDateStr,dropoffEndStr));

        outgoingDto.setPickupStart(pickupStart);
        outgoingDto.setPickupEnd(pickupEnd);
        outgoingDto.setDropoffStart(dropoffStart);
        outgoingDto.setDropoffEnd(dropoffEnd);

        outgoingDto.setId(incomingDto.getId());

        outgoingDto.setAccount(incomingDto.getAccount());
        outgoingDto.setPhoneNum(incomingDto.getPhoneNum());


        Observable<AdDto> call = interfaceAPI.modifyAd(adId,outgoingDto);
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
                        Intent back = new Intent (ModifyAdActivity.this,BackToMainActivity.class);
                        back.putExtra("MSG","ad modified!");
                        startActivity(back);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError 1: "+e.getLocalizedMessage() );
                        if (e instanceof HttpException){
                            ResponseBody body = ((HttpException) e).response().errorBody();
                            try {
                                Toast.makeText(ModifyAdActivity.this, body.string(), Toast.LENGTH_LONG).show();
                                Log.e(TAG, "onError body: "+body.string() );
                            } catch (IOException ioException) {
                                Log.e(TAG, "onError fo real: "+ioException.getLocalizedMessage());
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
                Log.e(TAG, "onItemSelected: "+dropoffStartStr );
            }
        });

        dropoffEndSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                dropoffEndStr = (String) item;
                Log.e(TAG, "onItemSelected: "+dropoffEndStr );
            }
        });
    }
}