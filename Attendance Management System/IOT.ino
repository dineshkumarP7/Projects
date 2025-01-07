#include <SoftwareSerial.h>
#include <Adafruit_Fingerprint.h>
#include <Wire.h>
#include <Adafruit_SSD1306.h>
#include <Adafruit_GFX.h>

// Define the RX and TX pins for the fingerprint sensor
SoftwareSerial fingerSerial(2, 3); // RX, TX for communication with the fingerprint sensor
// Define the RX and TX pins for the SIM800L GSM module
SoftwareSerial sim800lSerial(10, 11); // RX, TX for communication with SIM800L GSM module

// Define Ultrasonic sensor pins
const int trigPin = 4;  // Trigger pin for the ultrasonic sensor
const int echoPin = 5;  // Echo pin for the ultrasonic sensor

// Define Relay control pin
const int relayPin = 8;  // Relay control pin

// Define OLED display dimensions
#define SCREEN_WIDTH 128
#define SCREEN_HEIGHT 32
#define OLED_ADDR 0x3C  // I2C address for the OLED display

Adafruit_SSD1306 display(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire, -1);

Adafruit_Fingerprint finger = Adafruit_Fingerprint(&fingerSerial);

void setup() {
  Serial.begin(9600);  // Serial monitor communication
  fingerSerial.begin(9600);  // Fingerprint sensor communication
  sim800lSerial.begin(9600);  // SIM800L GSM module communication
  
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
  pinMode(relayPin, OUTPUT);
  
  digitalWrite(relayPin, LOW);  // Start with relay OFF

  // Initialize OLED display
  if (!display.begin(SSD1306_SWITCHCAPVCC, OLED_ADDR)) {
    Serial.println(F("OLED display not found"));
    while (1); // Infinite loop if display not found
  }
  display.display();
  delay(2000);

  Serial.println("Fingerprint Sensor LED Test");

  // Initialize the fingerprint sensor
  finger.begin(57600);

  // Check if the fingerprint sensor is connected
  if (finger.verifyPassword()) {
    Serial.println("Fingerprint sensor detected successfully!");
    display.clearDisplay();
    display.setTextSize(1);
    display.setTextColor(SSD1306_WHITE);
    display.setCursor(0, 0);
    display.print("Fingerprint sensor");
    display.setCursor(0, 10);
    display.print("detected!");
    display.display();
  } else {
    Serial.println("Fingerprint sensor not detected. Check wiring!");
    display.clearDisplay();
    display.setTextSize(1);
    display.setTextColor(SSD1306_WHITE);
    display.setCursor(0, 0);
    display.print("Fingerprint sensor");
    display.setCursor(0, 10);
    display.print("not detected!");
    display.display();
    while (1);  // Stop if sensor is not detected
  }

  delay(1000);
}

void loop() {
  long duration, distance;
  
  // Trigger ultrasonic sensor to send pulse
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);
  
  // Read the pulse from the Echo pin
  duration = pulseIn(echoPin, HIGH);
  
  // Calculate the distance in centimeters
  distance = (duration / 2) / 29.1;

  Serial.print("Distance: ");
  Serial.println(distance);

  // If object is detected within a certain range (e.g., 50 cm)
  if (distance < 50) {
    digitalWrite(relayPin, HIGH);  // Turn on relay (power to fingerprint and SIM800L)
    delay(2000);  // Ensure the SIM800L has time to power on
    display.clearDisplay();
    display.setCursor(0, 0);
    display.print("Object detected!");
    display.setCursor(0, 10);
    display.print("Powering on devices.");
    display.display();

    Serial.println("Object detected! Powering on devices.");

    // Wait for finger scan
    if (finger.getImage() == FINGERPRINT_OK) {
      display.clearDisplay();
      display.setCursor(0, 0);
      display.print("Fingerprint found!");
      display.display();

      // Sending SMS using SIM800L module
      sendSMS();

      delay(3000); // Pause briefly
    }
  } else {
    digitalWrite(relayPin, LOW);  // Turn off relay (power to fingerprint and SIM800L)
    display.clearDisplay();
    display.setCursor(0, 0);
    display.print("No object detected.");
    display.setCursor(0, 10);
    display.print("Power off devices.");
    display.display();
    Serial.println("No object detected. Power off devices.");
  }

  delay(1000);  // Delay before next loop
}

void sendSMS() {
  // Sending SMS using SIM800L module
  sim800lSerial.println("AT");
  updateSerial(sim800lSerial);

  sim800lSerial.println("AT+CMGF=1"); 
  updateSerial(sim800lSerial);
  sim800lSerial.println("AT+CMGS=\"+91xxxxxxxxxx\""); // Enter your phone number here (prefix country code)
  updateSerial(sim800lSerial);
  sim800lSerial.print("Hello "); // Enter your message here
  updateSerial(sim800lSerial);
  sim800lSerial.write(26); // Send the SMS

  delay(3000); // Pause briefly to ensure SMS is sent
}

void updateSerial(SoftwareSerial &serialPort) {
  delay(500);
  while (Serial.available()) {
    serialPort.write(Serial.read()); // Forward what Serial received to Software Serial Port
  }
  while (serialPort.available()) {
    Serial.write(serialPort.read()); // Forward what Software Serial received to Serial Port
  }
}
