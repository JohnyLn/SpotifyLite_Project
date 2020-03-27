package fr.esilv.spotifylite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class About extends AppCompatActivity {

    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        btnBack = findViewById(R.id.backButton);
        btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(About.this, MenuActivity.class));

            }
        });
    }
}
