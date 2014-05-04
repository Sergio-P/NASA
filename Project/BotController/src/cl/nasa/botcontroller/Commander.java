package cl.nasa.botcontroller;

import android.content.Context;

public class Commander {

	public enum Command {CON,TEST,SERIAL,READ1,READ2,DISCON,SPD,
			UP,UPR,DOWN,DOWNR,LEFT,LEFTR,RIGHT,RIGHTR,ATK1,ATK2};
	
	private Console con;
	private Context context;
			
	public Commander(Context cx, Console c){
		con=c;
		context=cx;
	}
	
	public void sendCommand(Command cmd){
		con.print("CMD: "+cmd.toString());
	}
	
	public void sendCommand(Command cmd, int value){
		con.print("CMD: "+cmd.toString()+" "+String.valueOf(value));
	}
	
}
