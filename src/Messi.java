import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Car {
    int id;
    String brand;
    String model;
    double pricePerDay;
    boolean isAvailable;

    Car(int id, String brand, String model, double pricePerDay) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.pricePerDay = pricePerDay;
        this.isAvailable = true;
    }
}

class Booking {
    int customerId;
    Car car;
    int days;
    double totalPrice;

    Booking(int customerId, Car car, int days) {
        this.customerId = customerId;
        this.car = car;
        this.days = days;
        this.totalPrice = car.pricePerDay * days;
        car.isAvailable = false;
    }
}

public class Messi {
    private static List<Car> cars = new ArrayList<>();
    private static List<Booking> bookings = new ArrayList<>();

    public static void main(String[] args) {
        initializeCars();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Welcome to the Car Rental System");
        
        while (true) {
            System.out.println("\n1. View Cars");
            System.out.println("2. Rent a Car");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            
            if (choice == 1) {
                viewCars();
            } else if (choice == 2) {
                System.out.print("Enter Customer ID: ");
                int customerId = scanner.nextInt();
                System.out.print("Enter Car ID to Rent: ");
                int carId = scanner.nextInt();
                rentCar(customerId, carId);
            } else {
                System.out.println("Thank you for using the Car Rental System");
                break;
            }
        }
        scanner.close();
    }

    private static void initializeCars() {
        cars.add(new Car(1, "Toyota", "Corolla", 40));
        cars.add(new Car(2, "Honda", "Civic", 50));
        cars.add(new Car(3, "Ford", "Focus", 45));
    }

    private static void viewCars() {
        System.out.println("\nAvailable Cars:");
        for (Car car : cars) {
            if (car.isAvailable) {
                System.out.println(car.id + ". " + car.brand + " " + car.model + " - " + car.pricePerDay + " per day");
            }
        }
    }

    private static void rentCar(int customerId, int carId) {
        for (Car car : cars) {
            if (car.id == carId && car.isAvailable) {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter number of days to rent: ");
                int days = scanner.nextInt();
                Booking booking = new Booking(customerId, car, days);
                bookings.add(booking);
                System.out.println("Car rented successfully for " + booking.totalPrice);
                return;
            }
        }
        System.out.println("Car not available or does not exist");
    }
}
