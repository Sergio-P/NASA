package cl.nasa.botcontroller;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ConnectActivity extends Activity{

	private static final int REQUEST_ENABLE_BT = 1;
	
	private ToggleButton toggle;
	private ListView lista;
	private ArrayAdapter<String> adapter;
	private Context context;
	
	private BluetoothAdapter btAdapter;
	
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			
			if(BluetoothDevice.ACTION_FOUND.equals(action)){
				Log.d("BT","New BT device founded");
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				String name = device.getName();
				String address = device.getAddress();
				adapter.add(name+"\n"+address);
				adapter.notifyDataSetChanged();
			}
			
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect);
		context = this;
		
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		
		lista = (ListView) findViewById(R.id.lista);
		adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);
		lista.setAdapter(adapter);
		
		lista.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
				String address = ((TextView) v).getText().toString().split("\n")[1];
				//Log.d("BT",address);
				Intent gotoMain = new Intent(context,MainActivity.class);
				gotoMain.putExtra("ad", address);
				context.startActivity(gotoMain);
			}
			
		});
		
		toggle = (ToggleButton) findViewById(R.id.togglebt);
		toggle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(toggle.isChecked()){
					Intent enableBtIntent = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
					startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
					/*Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
					for(BluetoothDevice bt : pairedDevices){
						adapter.add(bt.getName()+"\n"+bt.getAddress());
					}*/
				}
				else{
					btAdapter.disable();
					adapter.clear();
				}
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
            	btAdapter.startDiscovery();
				context.registerReceiver(mReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            }
		}
	}
	
	
}
