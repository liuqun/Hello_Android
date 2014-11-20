package com.example.hello;

import com.homer.remote.IMusicControlService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;


public class MainActivity extends Activity implements View.OnClickListener {
	private boolean mLogIsEnabled = true;

//	private Button playBtn;
//	private Button stopBtn;
//	private Button pauseBtn;
//	private Button exitBtn;

	private IMusicControlService mRemoteMusicControlService;
	private ServiceConnection sc = new MusicServiceConnection();

	private class MusicServiceConnection implements ServiceConnection {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// initialize member variables when this service is connected
			mRemoteMusicControlService = IMusicControlService.Stub.asInterface(service);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// clean-up after the service is disconnected
			mRemoteMusicControlService = null;
		}
	}


////////////////////////////////////////////////////////////////////
	/*(non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		if(null != sc){
			unbindService(sc);
		}
		super.onDestroy();
	}
	/*(non-Javadoc)
	 * @param savedInstanceState
	 * @see android.app.Activity#onCreate()
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
		Intent intent = new Intent("com.homer.remote.remoteMusicReceiver");
		bindService(intent, sc, Context.BIND_AUTO_CREATE);
	}

////////////////////////////////////////////////////////////////////

	/*(non-Javadoc)
	 * @param v
	 * @see android.view.View.OnClickListener#onClick()
	 */
	@Override
	public void onClick(View v) {
		String widget = null;
		int id;

		if (mLogIsEnabled) {
			id = v.getId();
			switch (id) {
			case R.id.btn_play:
			case R.id.btn_stop:
			case R.id.btn_pause:
				widget = ((android.widget.Button) v).getText().toString();
				android.util.Log.d("MainActivity.onClick(View)", "[" + widget + "] has been clicked");
				break;
			default:
				android.util.Log.d("MainActivity.onClick(View)", "Something has been clicked, and you should check it");
				break;
			}
		}
		id = v.getId();
		try {
			switch (id) {
			case R.id.btn_play:
				mRemoteMusicControlService.play();
				break;
			case R.id.btn_stop:
				if (null != mRemoteMusicControlService) {
					mRemoteMusicControlService.stop();
				}
				break;
			case R.id.btn_pause:
				if (null != mRemoteMusicControlService) {
					mRemoteMusicControlService.pause();
				}
				break;
			default:
				break;
			}
		} catch (RemoteException e) {
			if (mLogIsEnabled) {
				android.util.Log.d("MainActivity.onClick(View)", "Got a RemoteException from remote music service");
			}
		}
	}
}