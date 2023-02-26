package com.example.irontextapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.irontextapp.R;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (getSupportActionBar() != null) getSupportActionBar().hide();
	}
}