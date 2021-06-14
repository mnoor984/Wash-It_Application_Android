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

public class CreateAccountActivity extends AppCompatActivity {

    private static final String TAG = "CreateAccountActivity";

    TextView usernameView;
    TextView fullnameView;
    TextView emailView;
    TextView passwordView;
    TextView confirmPasswordView;
    Button createButton;

    String usernameStr;
    String fullnameStr;
    String emailStr;
    String passwordStr;


    Info endptInfo = new Info();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(endptInfo.getRoot())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    InterfaceAPI interfaceAPI = retrofit.create(InterfaceAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        usernameView=findViewById(R.id.username_input);
        fullnameView=findViewById(R.id.full_name_input);
        emailView=findViewById(R.id.email_input);
        passwordView=findViewById(R.id.password_input);
        confirmPasswordView=findViewById(R.id.confirm_password_input);
        createButton=findViewById(R.id.create_acct_btn);
    }

    public void createAcct(View view){
        usernameStr=usernameView.getText().toString();
        fullnameStr=fullnameView.getText().toString();
        emailStr=emailView.getText().toString();
        passwordStr=passwordView.getText().toString();

        if (!passwordStr.equals(confirmPasswordView.getText().toString())){
            Toast.makeText(CreateAccountActivity.this,"passwords do not match, try again",Toast.LENGTH_SHORT).show();
            return;
        }

        AccountDto dto = new AccountDto();

        dto.setId(usernameStr);
        dto.setUsername(usernameStr);
        dto.setFullName(fullnameStr);
        dto.setEmail(emailStr);
        dto.setPassword(passwordStr);
        dto.setLoggedIn(false);

        Observable<AccountDto> call = interfaceAPI.createAccount(dto);

        call
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AccountDto>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull AccountDto accountDto) {
                        Log.e(TAG, "onNext: "+accountDto.toString()+"\n created!");
                        Intent back = new Intent (CreateAccountActivity.this, BackToMainActivity.class);
                        back.putExtra("MSG","account created!");
                        startActivity(back);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (e instanceof HttpException){
                            ResponseBody body = ((HttpException) e).response().errorBody();
                            try {
                                Toast.makeText(CreateAccountActivity.this, body.string(), Toast.LENGTH_LONG).show();
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