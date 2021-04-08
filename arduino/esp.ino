#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <WiFiClient.h>
#include <Arduino_JSON.h>
int trig=0;
const char* ssid = "TP";
const int trigPin = 16;
const int echoPin = 12;
const int readbut=2;
const char* pass = "1368maman20baba";
String outputsState;
String state="1";
long duration;
int distance;
const char* serverName1 = "http://farsam-co.com/ard-sensor-post.php";
const char* serverName2 = "http://farsam-co.com/ard-sensor-get.php";
void setup() {
  Serial.begin(115200);
  WiFi.disconnect();
  WiFi.begin(ssid,pass);
  while((!(WiFi.status() == WL_CONNECTED))){
  delay(300);
  Serial.print(".");
  }
  Serial.println("Connected to WiFi");
  pinMode(trigPin, OUTPUT); 
  pinMode(echoPin, INPUT);
  pinMode(readbut,INPUT);

}

void loop() {
 int readed=digitalRead(readbut);
 if (readed==HIGH){
  trig=0;
 }Serial.println("readbut:");
 Serial.println(readed);
 if((WiFi.status() == WL_CONNECTED)){
  
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);
  duration = pulseIn(echoPin, HIGH);
  distance = duration * 0.034 / 2;
  Serial.print("Distance: ");
  Serial.println(distance);
  
  int safe=httpGETRequest(serverName2);
  
  if (distance<20){
    trig =1;
  }
  Serial.print("trig=");
  Serial.println(trig);
  if (trig==1&&safe==1)
      state="1";
  else {
      state="0";
  }
  String post=httpPOSTRequest(serverName1,state);
  Serial.println(post);   
    }
   else {
      Serial.println("WiFi Disconnected");
    }



  delay(1000);
}
String httpPOSTRequest(const char* serverName,String state){
      HTTPClient http;
      String s="state="+state;
      Serial.println(s);
      http.begin(serverName);
      http.addHeader("Content-Type", "application/x-www-form-urlencoded");
     int httpResponseCode = http.POST(s);
     Serial.print("HTTP RESPONSE POST: ");
     Serial.println(httpResponseCode);
     String payload = http.getString(); 
     
     http.end();
     return payload;
}
int httpGETRequest(const char* serverName) {
  WiFiClient client;
  HTTPClient http;
    
  // Your IP address with path or Domain name with URL path 
  http.begin(client, serverName);
  
  // Send HTTP POST request and recieve response code
  int httpResponseCode = http.GET();
  
  String payload = "{}"; 
  // code 200 means OK
  if (httpResponseCode>0) {
    Serial.print("HTTP Response code GET: ");
    Serial.println(httpResponseCode);
    payload = http.getString();
    Serial.println(payload);
  }
  else {
    Serial.print("Error code: ");
    Serial.println(httpResponseCode);
  }
  JSONVar myObject = JSON.parse(payload);
  JSONVar keys = myObject.keys();
  JSONVar value = myObject[keys[0]];
  Serial.println(value);
  // Free resources
  http.end();
  
  return atoi(value);
}
