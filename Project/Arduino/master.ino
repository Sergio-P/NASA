// Fijar los pines
int ml1 = 2;
int mr1 = 3;
int ml2 = 4;
int mr2 = 5;

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
	if(Serial.read()){

	}
}