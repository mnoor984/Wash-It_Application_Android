package ca.mcgill.ecse428.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import ca.mcgill.ecse428.application.dto.AccountDto;
import ca.mcgill.ecse428.application.representation.AccountRep;
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

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    TextView usernameView;
    TextView passwordView;
    Button loginButton;

    String usernameStr;
    String passwordStr;

    Info endptInfo = new Info();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(endptInfo.getRoot())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    InterfaceAPI interfaceAPI = retrofit.create(InterfaceAPI.class);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        usernameView=findViewById(R.id.username_login);
        passwordView=findViewById(R.id.password_login);
    }


    public void actLogin (View view){
        usernameStr=usernameView.getText().toString();
        passwordStr=passwordView.getText().toString();

        AccountRep accountRep = new AccountRep();

        accountRep.setUsername(usernameStr);
        accountRep.setPassword(passwordStr);

        Observable<ResponseBody> call = interfaceAPI.login(accountRep);

        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        try {

                            Log.e(TAG, "onLoginOk: " + responseBody.string());
                            Intent back = new Intent(LoginActivity.this, BackToMainActivity.class);
                            back.putExtra("MSG", "account created!");
                            startActivity(back);
                        }
                        catch (IOException ioException) {
                                Log.e(TAG, "onError: " + ioException.getLocalizedMessage());}
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (e instanceof HttpException){
                            ResponseBody body = ((HttpException) e).response().errorBody();
                            try {
                                Toast.makeText(LoginActivity.this, body.string(), Toast.LENGTH_LONG).show();
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
}
