// Set pins [PWM]
int ml1 = 5;
int mr1 = 3;
int ml2 = 9;
int mr2 = 10;

int spd = 192;

char data;

String nums = "012345678";

//Setup
void setup(){
	pinMode(ml1,OUTPUT);
	pinMode(mr1,OUTPUT);
	pinMode(ml2,OUTPUT);
	pinMode(mr2,OUTPUT);
        Serial.begin(9600);
}

//Loop
void loop(){
	if(Serial.available()){
		data = Serial.read();
		//Command Functions
		//Speed Command
		int a = nums.indexOf(data);
		if(a!=-1){
			changeSpeed(a);
		}
		else{
			//Other Commands
			switch(data){
			case 'U':
				avance();
				break;
			case 'D':
				retro();
				break;
			case 'R':
				giroDer();
				break; 
			case 'L':
				giroIzq();
				break;
			case 'S':
				stop();
				break;
			case 'Z':
				int sensor = analogRead(0);
				Serial.write(sensor);
				break;
			}
		}
	}
	delay(100);
}

//Move Functions
void avance(){
	analogWrite(ml1,spd);
	digitalWrite(mr1,0);
	analogWrite(ml2,spd);
	digitalWrite(mr2,0);
}
void retro(){
	digitalWrite(ml1,0);
	analogWrite(mr1,spd);
	digitalWrite(ml2,0);
	analogWrite(mr2,spd);
}
void stop(){
	digitalWrite(ml1,0);
	digitalWrite(mr1,0);
	digitalWrite(ml2,0);
	digitalWrite(mr2,0);
}
void giroDer(){
	analogWrite(ml1,spd);
	digitalWrite(mr1,0);
	digitalWrite(ml2,0);
	analogWrite(mr2,spd);
}
void giroIzq(){
	digitalWrite(ml1,0);
	analogWrite(mr1,spd);
	analogWrite(ml2,spd);
	digitalWrite(mr2,0);
}

void changeSpeed(int vel){
	if(vel==0){
		spd = 0;
	}
	else{
		spd = 32*vel -1;
	}
}
