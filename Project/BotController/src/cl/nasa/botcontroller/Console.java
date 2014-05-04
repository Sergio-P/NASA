package cl.nasa.botcontroller;

import android.widget.TextView;

public class Console {
	
	private TextView out;
	private String showText;
	
	public Console(TextView t){
		out=t;
		showText="Bienvenido a BotController\nDesarrollado por N.A.S.A, fcfm - 2014\nConecta un bot para comenzar.";
		out.setText(showText);
	}
	
	public void print(String line){
		String[] parts = showText.split("\n");
		showText = parts[1]+"\n"+parts[2]+"\n"+line;
		out.setText(showText);
	}
	
	public void clear(){
		showText=" \n \n ";
		out.setText(showText);
	}
	
}
