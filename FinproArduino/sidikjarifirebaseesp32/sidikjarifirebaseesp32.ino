#include <Adafruit_Fingerprint.h>
#include <WiFi.h>
#include <FirebaseESP32.h>

#define RELAY_PIN 5

#if (defined(__AVR__) || defined(ESP8266)) && !defined(__AVR_ATmega2560__)
#include <SoftwareSerial.h>
SoftwareSerial mySerial(2, 3);
#else
#define mySerial Serial2
#endif

Adafruit_Fingerprint finger = Adafruit_Fingerprint(&mySerial);

uint8_t id;
bool isLEDOn = false;

/* WiFi credentials */
#define WIFI_SSID "abdillah"
#define WIFI_PASSWORD "bangnabil02"

/* Firebase credentials */
#define API_KEY "AIzaSyAscWKfMvRuvgq-K8gu526odqXqEG_ig1k"
#define DATABASE_URL "https://iotkeyless-default-rtdb.asia-southeast1.firebasedatabase.app/"
#define USER_EMAIL "tes@gmail.com"
#define USER_PASSWORD "nabiltes123"

FirebaseData fbdo;
FirebaseConfig config; // Declare FirebaseConfig object
FirebaseAuth auth;    

void setup() {
        digitalWrite(RELAY_PIN, HIGH);
  Serial.begin(115200);
  delay(100);

  /* Initialize WiFi */
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.println("Connecting to WiFi...");
  }
  Serial.println("Connected to WiFi");
  
    /* Initialize FirebaseConfig and FirebaseAuth */
  config.api_key = API_KEY;
  config.database_url = DATABASE_URL;

  auth.user.email = USER_EMAIL;
  auth.user.password = USER_PASSWORD;


  /* Initialize Firebase */
  Firebase.begin(&config, &auth);
  while (!Firebase.ready()) {
    delay(500);
    Serial.println("Connecting to Firebase...");
  }
  Serial.println("Connected to Firebase");

  pinMode(RELAY_PIN, OUTPUT);
  Serial.println("Program Dasar Akses Sidik Jari Arduino");
  Serial.println("https://www.cronyos.com");

  finger.begin(57600);

  if (finger.verifyPassword()) {
    Serial.println("Found fingerprint sensor!");
  } else {
    Serial.println("Did not find fingerprint sensor :(");
    while (1) { delay(1); }
  }
  Serial.println("Waiting for valid finger...");
}



void loop() {

  uint8_t fingerprintID = getFingerprintID();

  // Check if a fingerprint is detected
  if (fingerprintID != FINGERPRINT_NOFINGER && fingerprintID !=FINGERPRINT_NOTFOUND) {
    // Assuming "fingerprint/id" and "fingerprint/status" are the paths in your Firebase database
    Firebase.setInt(fbdo, "Fingerprint/fingerprint/fingerprintId", fingerprintID);
    Firebase.setBool(fbdo, "Fingerprint/fingerprint/fingerprintStatus", isLEDOn);
    Firebase.setBool(fbdo, "Fingerprint/fingerprint/fingerprintCheck", false);


  

    // Add a delay to avoid multiple readings in a short period
    delay(5000); // You can adjust the delay as needed
  }
}



uint8_t getFingerprintID() {
    pinMode(RELAY_PIN, OUTPUT);

  uint8_t p = finger.getImage();
  switch (p) {
    case FINGERPRINT_OK:
      Serial.println("Image taken");
      break;
    case FINGERPRINT_NOFINGER:
    
      return p;
    case FINGERPRINT_PACKETRECIEVEERR:
      Serial.println("Communication error");
      return p;
    case FINGERPRINT_IMAGEFAIL:
      Serial.println("Imaging error");
      return p;
    default:
      Serial.println("Unknown error");
      return p;
  }

  p = finger.image2Tz();
  switch (p) {
    case FINGERPRINT_OK:
      Serial.println("Image converted");
      break;
    case FINGERPRINT_IMAGEMESS:
      Serial.println("Image too messy");
      return p;
    case FINGERPRINT_PACKETRECIEVEERR:
      Serial.println("Communication error");
      return p;
    case FINGERPRINT_FEATUREFAIL:
      Serial.println("Could not find fingerprint features");
      return p;
    case FINGERPRINT_INVALIDIMAGE:
      Serial.println("Could not find fingerprint features");
      return p;
    default:
      Serial.println("Unknown error");
      return p;
  }

  p = finger.fingerFastSearch();
  if (p == FINGERPRINT_OK) {
    if (!isLEDOn) {
      Serial.println("Found a print match! Turning LED ON");
      digitalWrite(RELAY_PIN, LOW);
      isLEDOn = true;
    } else{
      Serial.println("Found a print match! Turning LED OFF");
      digitalWrite(RELAY_PIN, HIGH);
      isLEDOn = false;
    }
  } else if (p == FINGERPRINT_PACKETRECIEVEERR) {
    Serial.println("Communication error");
    return p;
  } else if (p == FINGERPRINT_NOTFOUND) {
    // Handle the case when fingerprint is not found if needed
          Firebase.setBool(fbdo, "Fingerprint/fingerprint/fingerprintCheck", true);

          Serial.println("Fingerprint not found in database");
    return p;
  } else {
    Serial.println("Unknown error");
    return p;
  }

  Serial.print("Found ID #");
  Serial.print(finger.fingerID);
  Serial.print(" with confidence of ");
  Serial.println(finger.confidence);

  // Add a delay to avoid multiple readings in a short period

  return finger.fingerID;
}


