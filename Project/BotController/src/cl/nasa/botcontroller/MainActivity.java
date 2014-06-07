package cl.nasa.botcontroller;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import cl.nasa.botcontroller.Commander.Command;

public class MainActivity extends Activity {

	private Console con;
	private Commander cmd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Controllers
		con = new Console((TextView) this.findViewById(R.id.consoletext));
		cmd = new Commander(this,con);
		
		//Buttons
		Button bup = (Button) this.findViewById(R.id.cmdup);
		bup.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					cmd.sendCommand(Command.UP);
				}
				else if(event.getAction()==MotionEvent.ACTION_UP){
					cmd.sendCommand(Command.UPR);
				}
				return false;
			}
		});
		
		Button bdown = (Button) this.findViewById(R.id.cmddown);
		bdown.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					cmd.sendCommand(Command.DOWN);
				}
				else if(event.getAction()==MotionEvent.ACTION_UP){
					cmd.sendCommand(Command.DOWNR);
				}
				return false;
			}
		});
		
		Button bleft = (Button) this.findViewById(R.id.cmdleft);
		bleft.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					cmd.sendCommand(Command.LEFT);
				}
				else if(event.getAction()==MotionEvent.ACTION_UP){
					cmd.sendCommand(Command.LEFTR);
				}
				return false;
			}
		});
		
		Button bright = (Button) this.findViewById(R.id.cmdRight);
		bright.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					cmd.sendCommand(Command.RIGHT);
				}
				else if(event.getAction()==MotionEvent.ACTION_UP){
					cmd.sendCommand(Command.RIGHTR);
				}
				return false;
			}
		});
		
		Button batk1 = (Button) this.findViewById(R.id.cmdatk1);
		batk1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cmd.sendCommand(Command.ATK1);
			}
		});
		Button batk2 = (Button) this.findViewById(R.id.cmdatk2);
		batk2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cmd.sendCommand(Command.ATK2);
			}
		});
		
		SeekBar spdseek = (SeekBar) this.findViewById(R.id.spdseek);
		spdseek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			int p = 4;
			public void onStopTrackingTouch(SeekBar seekBar) {
				cmd.sendCommand(Command.SPD,p);
			}
			public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
				p =progress;
			}
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
		});
		
	}
	
	@Override
	public void onResume(){
		super.onResume();
		cmd.startBT();
	}
	
	@Override
	public void onPause(){
		super.onPause();
		cmd.stopBT();
	}

}
