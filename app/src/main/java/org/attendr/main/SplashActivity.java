package org.attendr.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.attendr.R;
import org.attendr.classes.ClassDetailsActivity;

public class SplashActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
        Intent i = new Intent(this, ClassDetailsActivity.class);
        startActivity(i);
	}
}
