import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;


import java.util.Arrays;

class ChargingStation {
    private String name;
    private String location;
    private boolean[] slots;

    public ChargingStation(String name, String location) {
        this.name = name;
        this.location = location;
        this.slots = new boolean[5]; // Initialize with 5 slots
        Arrays.fill(this.slots, true); // All slots are available initially
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public boolean isSlotAvailable() {
        for (boolean slot : slots) {
            if (slot) {
                return true; // At least one slot is available
            }
        }
        return false;
    }

    public boolean bookSlot(int slotNumber) {
        if (slotNumber >= 0 && slotNumber < slots.length && slots[slotNumber]) {
            slots[slotNumber] = false;
            return true; // Slot booked successfully
        }
        return false; // Invalid or unavailable slot number
    }

    public boolean freeSlot(int slotNumber) {
        if (slotNumber >= 0 && slotNumber < slots.length && !slots[slotNumber]) {
            slots[slotNumber] = true;
            return true; // Slot freed successfully
        }
        return false; // Invalid or already free slot number
    }

    public void displaySlotAvailability() {
        System.out.println("Slot availability for " + name + ":");
        for (int i = 0; i < slots.length; i++) {
            System.out.println("Slot " + (i + 1) + ": " + (slots[i] ? "Available" : "Occupied"));
        }
    }

    @Override
    public String toString() {
        return "ChargingStation{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", availableSlots=" + Arrays.toString(slots) +
                '}';
    }
}


public class EVChargingApp {
    private static ChargingStation[] stations = {
            new ChargingStation("Station 1", "Location A"),
            new ChargingStation("Station 2", "Location B"),
            new ChargingStation("Station 3", "Location C")
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Welcome to the EV Charging App!");
            System.out.println("1. View Charging Stations");
            System.out.println("2. Charge Vehicle");
            System.out.println("3. Free Charging Station");
            System.out.println("4. Find My Location");
            System.out.println("5. Book Slot for Charging");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    viewChargingStations();
                    break;
                case 2:
                    chargeVehicle(scanner);
                    break;
                case 3:
                    freeChargingStation(scanner);
                    break;
                case 4:
                    findMyLocationAndPrintCoordinates();
                    break;
                case 5:
                    bookSlotForCharging(scanner);
                    break;
                case 6:
                    System.out.println("Exiting the app. Thank you!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }


    private static void viewChargingStations() {
        System.out.println("Charging Stations:");
        for (ChargingStation station : stations) {
            System.out.println(station);
        }
    }

    private static void chargeVehicle(Scanner scanner) {
        System.out.print("Enter the station name to charge your vehicle: ");
        String stationName = scanner.nextLine();
        for (ChargingStation station : stations) {
            if (station.getName().equalsIgnoreCase(stationName)) {
                if (station.isSlotAvailable()) {
                    System.out.println("Please enter the slot number to charge your vehicle:");
                    station.displaySlotAvailability();
                    int slotNumber = scanner.nextInt() - 1; // Get slot number from user
                    scanner.nextLine(); // consume newline
                    if (station.bookSlot(slotNumber)) {
                        System.out.println("Your vehicle is being charged at " + stationName + " Slot " + (slotNumber + 1));
                    } else {
                        System.out.println("Invalid or unavailable slot number.");
                    }
                    return;
                } else {
                    System.out.println("No available slots at " + stationName);
                    return;
                }
            }
        }
        System.out.println("Station " + stationName + " not found.");
    }

    private static void freeChargingStation(Scanner scanner) {
        System.out.print("Enter the station name to free up a slot: ");
        String stationName = scanner.nextLine();
        for (ChargingStation station : stations) {
            if (station.getName().equalsIgnoreCase(stationName)) {
                System.out.println("Please enter the slot number to free:");
                station.displaySlotAvailability();
                int slotNumber = scanner.nextInt() - 1; // Get slot number from user
                scanner.nextLine(); // consume newline
                if (station.freeSlot(slotNumber)) {
                    System.out.println("Slot " + (slotNumber + 1) + " has been freed at " + stationName);
                } else {
                    System.out.println("Invalid or already free slot number.");
                }
                return;
            }
        }
        System.out.println("Station " + stationName + " not found.");
    }

    private static void findMyLocationAndPrintCoordinates() {
        try {
            // Ensure the path to your Python interpreter is correctly set in IntelliJ IDEA
            String pythonInterpreter = "C:\\Users\\Ravi Vishwakarma\\AppData\\Local\\Programs\\Python\\Python313\\python.exe";

            // Run the Python script to get the coordinates
            ProcessBuilder pb = new ProcessBuilder(pythonInterpreter, "C:\\Users\\Ravi Vishwakarma\\IdeaProjects\\EVChargingApp\\src\\create_map.py");
            pb.inheritIO();
            Process p = pb.start();
            p.waitFor();

            // Read the coordinates from location.txt
            BufferedReader reader = new BufferedReader(new FileReader("src/location.txt"));
            String latitude = reader.readLine();
            String longitude = reader.readLine();
            reader.close();

            System.out.println("Your location: Latitude " + latitude + ", Longitude " + longitude);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
//        private static void findMyLocation() {
//        try {
//            String apiUrl = "http://ip-api.com/json";
//
//          URL url = new URL(apiUrl);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//
//            int responseCode = connection.getResponseCode();
//            if (responseCode == 200) {
//                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                String inputLine;
//                StringBuilder response = new StringBuilder();
//
//                while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
//                }
//                in.close();
//
//                JSONObject jsonResponse = new JSONObject(response.toString());
//               String city = jsonResponse.getString("city");
//               String region = jsonResponse.getString("regionName");
//            String country = jsonResponse.getString("country");
//             double latitude = jsonResponse.getDouble("lat");
//                double longitude = jsonResponse.getDouble("lon");
//
//               System.out.println("Your location: " + city + ", " + region + ", " + country +
//                        " (Latitude: " + latitude + ", Longitude: " + longitude + ")");
//            } else {
//              System.out.println("Error: Unable to get location (Response Code: " + responseCode + ")");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//       }
//    }

    private static void bookSlotForCharging(Scanner scanner) {
        System.out.print("Enter the station name to book a slot: ");
        String stationName = scanner.nextLine();
        for (ChargingStation station : stations) {
            if (station.getName().equalsIgnoreCase(stationName)) {
                station.displaySlotAvailability();
                System.out.print("Enter the slot number to book: ");
                int slotNumber = scanner.nextInt() - 1; // Get slot number from user
                scanner.nextLine(); // consume newline
                if (station.bookSlot(slotNumber)) {
                    System.out.println("Slot " + (slotNumber + 1) + " booked successfully at " + stationName);
                } else {
                    System.out.println("Invalid or unavailable slot number.");
                }
                return;
            }
        }
        System.out.println("Station " + stationName + " not found.");
    }
}


