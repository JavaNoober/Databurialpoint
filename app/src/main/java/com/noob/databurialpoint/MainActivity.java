package com.noob.databurialpoint;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button btnLogin = findViewById(R.id.btn_login);
		Button btnRegister = findViewById(R.id.btn_register);

		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			@Statistics(function = Function.LOGIN)
			public void onClick(View v) {
				Log.e("Statistics", "登陆");
			}
		});

		btnRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			@Statistics(function = Function.REGISTER)
			public void onClick(View v) {
				Log.e("Statistics", "注册");
			}
		});
	}
}
