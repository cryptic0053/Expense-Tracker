package com.example.expensetracker;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    private Button buttonLetsGo;

    ImageView image;
    TextView textView;
    Animation topAnim,bottomAnim;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        topAnim=AnimationUtils.loadAnimation(this,R.anim.top_anim);
        bottomAnim=AnimationUtils.loadAnimation(this,R.anim.bottom_anim);

        image=findViewById(R.id.imageViewExpense);
        buttonLetsGo=findViewById(R.id.btnLetsGo);
        textView=findViewById(R.id.textViewExpense);

        image.setAnimation(topAnim);
        buttonLetsGo.setAnimation(bottomAnim);
        textView.setAnimation(bottomAnim);

        buttonLetsGo = findViewById(R.id.btnLetsGo);
        buttonLetsGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
