package ca.mcgill.ecse428.application;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class DeleteAccountActivity extends AppCompatActivity {

    private static final String TAG = "DeleteAccountActivity";

    TextView usernameView;
    TextView passwordView;
    TextView confirmPasswordView;
    Button deleteButton;

    String usernameStr;
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
        setContentView(R.layout.activity_delete_account);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();

        usernameView=findViewById(R.id.delete_account_username);
        passwordView=findViewById(R.id.delete_account_password);
        confirmPasswordView=findViewById(R.id.delete_account_confirmpassword);
        deleteButton=findViewById(R.id.delete_act_button);

    }
    public void showAlertDialogButtonClicked(View view) {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Account Deletion Confirmation");
        builder.setMessage("Are you sure you wish to proceed with the deletion of your account?");

        // add the buttons
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener(){
             @Override
            public void onClick(DialogInterface dialog, int which) {

                deleteAccount();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {

                deleteFail();
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void deleteAccount(){
        usernameStr=usernameView.getText().toString();
        passwordStr=passwordView.getText().toString();

        if (!passwordStr.equals(confirmPasswordView.getText().toString())){
            Toast.makeText(DeleteAccountActivity.this,"passwords do not match, try again",Toast.LENGTH_SHORT).show();
            return;
        }

            Observable<ResponseBody> call = interfaceAPI.deleteAccount("RegularCustomer", true);
            call.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResponseBody>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }
                        @Override
                        public void onNext(@NonNull ResponseBody responseBody) {
                            try {

                                Log.e(TAG, "onDeleteOK: " + responseBody.string());
                                Intent back = new Intent(DeleteAccountActivity.this, BackToMainActivity.class);
                                back.putExtra("MSG", "account deleted!");
                                startActivity(back);
                            } catch (IOException ioException) {
                                Log.e(TAG, "onError: " + ioException.getLocalizedMessage());
                            }
                        }
                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.e(TAG, "onError: " + e.getLocalizedMessage());
                        }
                        @Override
                        public void onComplete() {

                        }
                    });
        }

     public void deleteFail(){
        usernameStr=usernameView.getText().toString();
        passwordStr=passwordView.getText().toString();

        if (!passwordStr.equals(confirmPasswordView.getText().toString())) {
            Toast.makeText(DeleteAccountActivity.this, "passwords do not match, try again", Toast.LENGTH_SHORT).show();
            return;
        }

            Observable<ResponseBody> call = interfaceAPI.deleteAccount("RegularCustomer", false);
            call.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResponseBody>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }
                        @Override
                        public void onNext(@NonNull ResponseBody responseBody) {
                            try {

                                Log.e(TAG, "onDeleteOK: " + responseBody.string());
                                Intent back = new Intent(DeleteAccountActivity.this, BackToMainActivity.class);
                                back.putExtra("MSG", "account not deleted!");
                                startActivity(back);
                            } catch (IOException ioException) {
                                Log.e(TAG, "onError: " + ioException.getLocalizedMessage());
                            }
                        }
                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.e(TAG, "onError: " + e.getLocalizedMessage());
                        }
                        @Override
                        public void onComplete() {

                        }
                    });
    }

}