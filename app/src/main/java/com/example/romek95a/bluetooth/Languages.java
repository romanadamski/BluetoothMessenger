package com.example.romek95a.bluetooth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

/**
 * Created by romek95a on 08.07.2018.
 */

public class Languages extends Activity {
    Button polish;
    Button english;
    Button back;
    String languageEnglish="en";
    String languagePolish="pl";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.languages);
        english=(Button) findViewById(R.id.english);
        polish=(Button) findViewById(R.id.polish);
        back=(Button) findViewById(R.id.back);
        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLanguage(languageEnglish);
            }
        });
        polish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLanguage(languagePolish);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Languages.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    public void changeLanguage(String language){
        Configuration config = new Configuration();
        Locale locale = new Locale(language);
        config.locale = locale;
        Context context=getApplicationContext();
        context.getResources().updateConfiguration(config,context.getResources().getDisplayMetrics());
        Intent intent = new Intent(Languages.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
