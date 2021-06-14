package ca.mcgill.ecse428.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Base64;

public class BackToMainActivity extends AppCompatActivity {

    TextView promptText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_to_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        promptText = findViewById(R.id.prompt_msg);

        Intent passedIntent = getIntent();
        promptText.setText( (String) passedIntent.getSerializableExtra("MSG"));
    }

    public void backToMain(View view){
        Intent back = new Intent(BackToMainActivity.this, MenuActivity.class);
        startActivity(back);
    }
}