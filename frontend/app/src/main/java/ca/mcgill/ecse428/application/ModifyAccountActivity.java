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
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import io.reactivex.Observable;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class ModifyAccountActivity extends AppCompatActivity {

    private static final String TAG = "ModifyAccountActivity";

    TextView usernameView;
    TextView fullnameView;
    TextView emailView;
    TextView passwordView;
    TextView confirmPasswordView;
    Button updateButton;

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
        setContentView(R.layout.activity_modify_account);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        usernameStr=(String) intent.getSerializableExtra("ID");

        usernameView=findViewById(R.id.update_username_input);
        fullnameView=findViewById(R.id.update_full_name_input);
        emailView=findViewById(R.id.update_email_input);
        passwordView=findViewById(R.id.update_password_input);
        confirmPasswordView=findViewById(R.id.update_confirm_password_input);
        updateButton=findViewById(R.id.update_acct_btn);

        usernameView.setEnabled(false);

        fetchAccount(usernameStr);

    }

    public void modifyAccount(View view){
        usernameStr=usernameView.getText().toString();
        fullnameStr=fullnameView.getText().toString();
        emailStr=emailView.getText().toString();
        passwordStr=passwordView.getText().toString();

        if (!passwordStr.equals(confirmPasswordView.getText().toString())){
            Toast.makeText(ModifyAccountActivity.this,"passwords do not match, try again",Toast.LENGTH_SHORT).show();
        }

        else {

            AccountDto dto = new AccountDto();

            dto.setId(usernameStr);
            dto.setUsername(usernameStr);
            dto.setFullName(fullnameStr);
            dto.setEmail(emailStr);
            dto.setPassword(passwordStr);
            dto.setLoggedIn(true);

            Observable<AccountDto> call = interfaceAPI.modifyAccount(usernameStr, dto);

            call
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<AccountDto>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull AccountDto accountDto) {
                            Log.e(TAG, "onNext: " + accountDto.toString() + "\n modified!");
                            Intent back = new Intent (ModifyAccountActivity.this, BackToMainActivity.class);
                            back.putExtra("MSG","account updated!");
                            startActivity(back);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            if (e instanceof HttpException){
                                ResponseBody body = ((HttpException) e).response().errorBody();
                                try {
                                    Toast.makeText(ModifyAccountActivity.this, body.string(), Toast.LENGTH_LONG).show();
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

    private void filloutExisting(AccountDto dto){
        usernameView.setText(dto.getUsername());
        fullnameView.setText(dto.getFullName());
        emailView.setText(dto.getEmail());
        passwordView.setText(dto.getPassword());
        confirmPasswordView.setText(dto.getPassword());
    }

    private void fetchAccount(String username){
        Observable<AccountDto> call = interfaceAPI.getAccount(username);
        call
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AccountDto>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull AccountDto accountDto) {
                        filloutExisting(accountDto);
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