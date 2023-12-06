#include <TinyGPS++.h>
#include <SoftwareSerial.h>
#include <ESP8266WiFi.h>
#include <Firebase_ESP_Client.h>

TinyGPSPlus gps;
SoftwareSerial SerialGPS(4, 5);

const char* ssid = "abdillah";
const char* password = "bangnabil02";

double Latitude, Longitude;  // Change data type to double
String LatitudeString, LongitudeString;

WiFiServer server(80);

// Firebase configuration
#define WIFI_SSID "abdillah"
#define WIFI_PASSWORD "bangnabil02"
#define API_KEY "AIzaSyAscWKfMvRuvgq-K8gu526odqXqEG_ig1k"
#define DATABASE_URL "https://iotkeyless-default-rtdb.asia-southeast1.firebasedatabase.app/"

FirebaseData fbdo;
FirebaseAuth auth;
FirebaseConfig config;

bool getLocationAgain = false;

void setup() {
  Serial.begin(9600);
  SerialGPS.begin(9600);

  // Connect to WiFi
  WiFi.begin(ssid, password);
  Serial.print("Connecting");
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.println("WiFi connected");

  // Initialize Firebase
  config.api_key = API_KEY;
  config.database_url = DATABASE_URL;

  if (Firebase.signUp(&config, &auth, "", "")) {
    Serial.println("Firebase authentication successful");
  } else {
    Serial.println("Firebase authentication failed");
    Serial.printf("%s\n", config.signer.signupError.message.c_str());
  }

  Firebase.begin(&config, &auth);
  Firebase.reconnectWiFi(true);

  server.begin();
  Serial.println("Server started");
  Serial.println(WiFi.localIP());
}

void loop() {
  while (SerialGPS.available() > 0) {
    if (gps.encode(SerialGPS.read())) {
      if (gps.location.isValid()) {
        Latitude = gps.location.lat();
        LatitudeString = String(Latitude, 6);
        Longitude = gps.location.lng();
        LongitudeString = String(Longitude, 6);

        // Get Location ID (replace with your logic to get the ID)
        int locationID = 1;

        // Send latitude and longitude to Firebase with ID
        String locationPath = "Location/locationid" + String(locationID);
        if (Firebase.RTDB.setString(&fbdo, locationPath + "/lat", LatitudeString) &&
            Firebase.RTDB.setString(&fbdo, locationPath + "/long", LongitudeString)) {
          Serial.println("Latitude and Longitude sent to Firebase with ID: " + String(locationID));
        } else {
          Serial.println("Failed to send Latitude and Longitude to Firebase");
          Serial.println("Reason: " + fbdo.errorReason());
        }
      }
    }
  }

  WiFiClient client = server.available();
  if (!client) {
    return;
  }

  if (client.available()) {
    String request = client.readStringUntil('\r');
    if (request.indexOf("/getLocation") != -1) {
      getLocationAgain = true;
    }
  }

  // Response
  String s = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n <!DOCTYPE html> <html> <head> <title>NEO-6M GPS Readings</title> <style>";
  // ... (your existing HTML code)

  if (getLocationAgain && gps.location.isValid()) {
    s += "<p align=center><a style=""color:RED;font-size:125%;"" href=""http://maps.google.com/maps?&z=15&mrt=yp&t=k&q=";
    s += LatitudeString;
    s += "+";
    s += LongitudeString;
    s += """ target=""_top"">Click here</a> to open the location in Google Maps.</p>";
    s += "<p align=center><a href=\"/getLocation\">Get Location Again</a></p>";
    getLocationAgain = false;
  }

  s += "</body> </html> \n";

  client.print(s);
  delay(100);
}