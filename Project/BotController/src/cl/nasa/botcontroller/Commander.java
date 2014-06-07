package cl.nasa.botcontroller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Commander {

	public enum Command {CON,TEST,SERIAL,READ1,READ2,DISCON,SPD,
			UP,UPR,DOWN,DOWNR,LEFT,LEFTR,RIGHT,RIGHTR,ATK1,ATK2};
	
	private Console con;
	private Context context;
	
	private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private static String address = "20:13:07:15:00:79";
	
	private static final int REQUEST_ENABLE_BT = 1;
	private BluetoothAdapter btAdapter = null;
	private BluetoothSocket btSocket = null;
	private OutputStream outStream = null;
		
	public Commander(Context cx, Console c){
		con=c;
		context=cx;
		
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		checkBT();
	}
	
	private void checkBT() {
		if(btAdapter==null) { 
				con.print("No hay soporte de BT");
		}
		else{
			if (btAdapter.isEnabled()) {
				con.print("Bluetooth conectado");
			}else {
				Intent enableBtIntent = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
				((Activity) context).startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
		}
	}
	
	public void startBT(){
		con.print("Empezando a conectar...");
		BluetoothDevice device = btAdapter.getRemoteDevice(address);
		
		try {
			btSocket = device.createRfcommSocketToServiceRecord(uuid);
		} catch (IOException e) {
			con.print("No se encuentra el dispositivo");
		}
		
		btAdapter.cancelDiscovery();
		
		try {
			btSocket.connect();
		} catch (IOException e) {
			try {
				btSocket.close();
				con.print("No se puede conectar al dispositivo");
			} catch (IOException e1) {
				con.print("No se puede conectar al dispositivo");
			}
		}
		
		try {
			outStream = btSocket.getOutputStream();
			/*String test = "1";
			outStream.write(test.getBytes());*/
		} catch (IOException e) {
			con.print("No se pueden transmitir datos");
		}
		
	}
	
	public void stopBT(){
		if(outStream!=null){
			try {
				outStream.flush();
			} catch (IOException e) {
				con.print("No se puede cerrar la conexion");
			}
		}
		try {
			btSocket.close();
		} catch (IOException e) {
			con.print("No se puede desconectar el dispositivo");
		}
	}
	
	private String convertSerial(Command cmd){
		switch(cmd){
		case ATK1:
			return "Z";
		case ATK2:
			return "X";
		case CON:
			return "0";
		case DISCON:
			return "0";
		case DOWN:
			return "D";
		case DOWNR:
			return "S";
		case LEFT:
			return "L";
		case LEFTR:
			return "S";
		case READ1:
			return "0";
		case READ2:
			return "0";
		case RIGHT:
			return "R";
		case RIGHTR:
			return "S";
		case SERIAL:
			return "0";
		case SPD:
			return "0";
		case TEST:
			return "0";
		case UP:
			return "U";
		case UPR:
			return "S";
		default:
			return "0";
		
		}
	}

	public void sendCommand(Command cmd){	
		/*String msg="0";
		if(cmd.equals(Command.DOWN)){
			msg="1";
		}
		else{
			msg="0";
		}*/
		String msg = convertSerial(cmd);
		
		byte[] buffer = msg.getBytes();
		try {
			outStream.write(buffer);
			con.print("CMD: "+msg);
		} catch (IOException e) {
			con.print("No se pueden enviar datos, revise la direccion MAC");
		}
	}
	
	public void sendCommand(Command cmd, int value){
		con.print("CMD: "+cmd.toString()+" "+String.valueOf(value));
	}
	
	
	/*
	private static final int REQUEST_ENABLE_BT = 1;
	private BluetoothAdapter myBluetoothAdapter;
	private Set<BluetoothDevice> pairedDevices;
	private ListView myListView;
	private ArrayAdapter<String> BTArrayAdapter;
   
	private OutputStream mmOutputStream;	
	private InputStream mmInputStream;
	private BluetoothDevice mmDevice;
	private BluetoothSocket mmSocket;
	private String chosenOne; // Device Name that has been chosen
   
	private volatile boolean stopWorker;
	private Thread workerThread;
	private byte[] readBuffer;
	private int readBufferPosition;
	
	final BroadcastReceiver bReceiver = new BroadcastReceiver() {
	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        String name;
	        Integer index;
	        // When discovery finds a device
	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	        	con.print("Hola");
	             // Get the BluetoothDevice object from the Intent
	        	 BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	        	 // add the name and the MAC address of the object to the arrayAdapter
	             BTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
	             BTArrayAdapter.notifyDataSetChanged();
	             
	        	 if ((chosenOne != null) && (mmDevice == null)) {	        		 
	        		 index = chosenOne.indexOf("\n");
	        		 if (index > -1) {	        			 
   	        		    name = chosenOne.substring(0,index);
	        		    if (name.compareTo(device.getName()) == 0) {
   	        	            mmDevice = device;
   	        	            con.print("Device ready to begin");
   	       	   		        myBluetoothAdapter.cancelDiscovery();
   	 	                    try {
   		                       openBT();
   		                    }
   		                    catch (IOException ex) { }
   	 	                    BTArrayAdapter.clear();
   	 	                    BTArrayAdapter.notifyDataSetChanged();
	        		    }   
	           		    else {
	           		    	con.print("Device failed");
	        		    }
	        		 } 
	        		 else
	        			 con.print("Device failed");
	        	 }	 

	        }
	    }
	};
			
	public Commander(Context cx, Console c){
		con=c;
		context=cx;
		
		mmDevice = null;
		chosenOne = null;
		
		myListView = (ListView)((Activity) context).findViewById(R.id.listView1);
	  	
	      // create the arrayAdapter that contains the BTDevices, and set it to the ListView
	      BTArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);
	      myListView.setAdapter(BTArrayAdapter);
	      
	      myListView.setOnItemClickListener(new OnItemClickListener() {
	    	  public void onItemClick (AdapterView<?> parent, View view,
	    			                   int position, long id) {
	    		  // Set mmDevice
	    		  
	    		  String adapter = ((TextView) view).getText().toString();
	    		  mmDevice = null;
	    		  chosenOne = adapter;
	   		      myBluetoothAdapter.cancelDiscovery();
	   		      
  		 	  BTArrayAdapter.clear();
				  myBluetoothAdapter.startDiscovery();
				  context.registerReceiver(bReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));	
	   		      
	    	  }
	      });
		
	    myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	      if(myBluetoothAdapter == null) {
	    	  con.print("No Bluetooth supported");
	      }
	      else {
	    	 onBT();
	      }
	      
	      
	}
	
	private void onBT() {
		if(!myBluetoothAdapter.isEnabled()) {
	         Intent turnOnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	         ((Activity) context).startActivityForResult(turnOnIntent, REQUEST_ENABLE_BT);
	    }
		BTArrayAdapter.clear();
		myBluetoothAdapter.startDiscovery();
		con.print("Searching BT Arduino Devices");
		context.registerReceiver(bReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
	}
	
	void openBT() throws IOException{
	       UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
	       mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);        
	       mmSocket.connect();
	       mmOutputStream = mmSocket.getOutputStream();
	       mmInputStream  = mmSocket.getInputStream();
	       
	       String msg = "Hello from Android\n";
	       mmOutputStream.write(msg.getBytes());
	       
	       beginListenForData();       
	   }
	
	void beginListenForData(){
	       final Handler handler = new Handler(); 
	       final byte delimiter = 10; //This is the newline character
	       
	       stopWorker = false;
	       readBufferPosition = 0;
	       readBuffer = new byte[1024];
	       workerThread = new Thread(new Runnable()
	       {
	           public void run()
	           {                
	              while(!Thread.currentThread().isInterrupted() && !stopWorker)
	              {
	                   try 
	                   {
	                       int bytesAvailable = mmInputStream.available();                        
	                       if(bytesAvailable > 0)
	                       {
	                           byte[] packetBytes = new byte[bytesAvailable];
	                           mmInputStream.read(packetBytes);
	                           for(int i=0;i<bytesAvailable;i++)
	                           {
	                               byte b = packetBytes[i];
	                               if(b == delimiter)
	                               {
	                                   byte[] encodedBytes = new byte[readBufferPosition];
	                                   System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
	                                   final String data = new String(encodedBytes, "US-ASCII");
	                                   readBufferPosition = 0;
	                                   
	                                   handler.post(new Runnable()
	                                   {
	                                       public void run()
	                                       {
	                                           con.print(data);
	                                       }
	                                   });
	                               }
	                               else
	                               {
	                                   readBuffer[readBufferPosition++] = b;
	                               }
	                           }
	                       }
	                   } 
	                   catch (IOException ex) 
	                   {
	                       stopWorker = true;
	                   }
	              }
	           }
	       });

	       workerThread.start();
	   }

	public void sendCommand(Command cmd){		
		if(mmDevice == null){
			con.print("No hay conexion Bluetooth");
		}
		else{    	   
			try {
	        	  String msg = cmd.toString();
	        	  mmOutputStream.write(msg.getBytes());
			}
			catch (IOException ex) {
	        	  con.print("Fallo el envio de datos");
			}
	          con.print("CMD: "+cmd.toString());
	       }
	}
	
	public void sendCommand(Command cmd, int value){
		con.print("CMD: "+cmd.toString()+" "+String.valueOf(value));
	}
	
*/
	
	
}
