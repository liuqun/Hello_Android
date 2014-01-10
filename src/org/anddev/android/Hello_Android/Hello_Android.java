package org.anddev.android.Hello_Android;

import android.app.Activity;
import android.os.Bundle;

public class Hello_Android extends Activity{
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		this.setContentView(R.layout.main);
	}
}