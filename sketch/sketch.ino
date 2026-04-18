#include <Servo.h>

const int LED_ROJO = 4;
const int LED_VERDE = 5;
const int LED_AMARILLO = 6;
const int SERVO_PIN = 9;

Servo miServo;

void setup() {
  Serial.begin(9600);
  miServo.attach(SERVO_PIN);
  pinMode(LED_ROJO, OUTPUT);
  pinMode(LED_VERDE, OUTPUT);
  pinMode(LED_AMARILLO, OUTPUT);
  // estado inicial: puerta cerrada (servo 90), rojo encendido
  digitalWrite(LED_ROJO, HIGH);
  digitalWrite(LED_VERDE, LOW);
  miServo.write(90);
  Serial.println("SYSTEM:READY");
}

String readLine() {
  static String line = "";
  while (Serial.available()) {
    char c = Serial.read();
    if (c == '\n') {
      String out = line;
      line = "";
      return out;
    } else if (c != '\r') {
      line += c;
    }
  }
  return "";
}

void handleCommand(String cmd) {
  cmd.trim();
  if (cmd.length() == 0) return;

  Serial.print("RECV:"); Serial.println(cmd); // para debug

  if (cmd == "LIGHT:ON") {
    digitalWrite(LED_AMARILLO, HIGH);
    Serial.println("LIGHT:ON:OK");
  } else if (cmd == "LIGHT:OFF") {
    digitalWrite(LED_AMARILLO, LOW);
    Serial.println("LIGHT:OFF:OK");
  } else if (cmd == "DOOR:OPEN") {
  miServo.write(90); // abre (antes estaba 0)
  digitalWrite(LED_VERDE, HIGH);
  digitalWrite(LED_ROJO, LOW);
  Serial.println("DOOR:OPEN:OK");
  } else if (cmd == "DOOR:CLOSE") {
    miServo.write(0); // cierra (antes estaba 90)
    digitalWrite(LED_ROJO, HIGH);
    digitalWrite(LED_VERDE, LOW);
    Serial.println("DOOR:CLOSE:OK");
  } else if (cmd == "STATUS?") {
    String s = "STATUS:LIGHT=";
    s += (digitalRead(LED_VERDE) == HIGH ? "ON" : "OFF");
    s += ";DOOR=";
    s += (digitalRead(LED_ROJO) == HIGH ? "CLOSED" : "OPEN");
    Serial.println(s);
  } else {
    Serial.println("ERR:UNKNOWN_CMD");
  }
}

void loop() {
  String ln = readLine();
  if (ln.length() > 0) handleCommand(ln);
}

