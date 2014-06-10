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
	
	
	
}
