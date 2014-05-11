// Set pins
int ml1 = 2;
int mr1 = 3;
int ml2 = 4;
int mr2 = 5;

char data;

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
	if(Serial.aviable()){
		data = Serial.read();
		//Command Functions
		switch(data){
		case "1":
			avance();
			break;
		case "2":
			giroDer();
			break;
		case "3":
			giroIzq();
			break;
		case "4":
			stop();
			break;
		default:
			stop();
			break;
		}
	}
	delay(100);
}

//Move Functions
void avance(){
	digitalWrite(ml1,1);
	digitalWrite(mr1,0);
	digitalWrite(ml2,1);
	digitalWrite(mr2,0);
}
void stop(){
	digitalWrite(ml1,0);
	digitalWrite(mr1,0);
	digitalWrite(ml2,0);
	digitalWrite(mr2,0);
}
void giroDer(){
	digitalWrite(ml1,1);
	digitalWrite(mr1,0);
	digitalWrite(ml2,0);
	digitalWrite(mr2,1);
}
void giroIzq(){
	digitalWrite(ml1,0);
	digitalWrite(mr1,1);
	digitalWrite(ml2,1);
	digitalWrite(mr2,0);
}