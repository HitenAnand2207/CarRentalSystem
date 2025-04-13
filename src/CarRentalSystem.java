import java.sql.*;
import java.util.Scanner;

public class CarRentalSystem {
    private static final String URL = "jdbc:mysql://localhost:3306/carrentaldb";
    private static final String USER = "root";
    private static final String PASSWORD = "Chirag@10";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Scanner scanner = new Scanner(System.in)) {
            System.out.println("Welcome to the Car Rental System");

            while (true) {
                System.out.println("\n1. View Cars");
                System.out.println("2. Rent a Car");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                if (choice == 1) {
                    viewCars(conn);
                } else if (choice == 2) {
                    System.out.print("Enter Customer ID: ");
                    int customerId = scanner.nextInt();
                    System.out.print("Enter Car ID to Rent: ");
                    int carId = scanner.nextInt();
                    rentCar(conn, customerId, carId);
                } else {
                    System.out.println("Thank you for using the Car Rental System");
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewCars(Connection conn) throws SQLException {
        String query = "SELECT car_id, brand, model, price_per_day FROM cars WHERE availability = TRUE";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("\nAvailable Cars:");
            while (rs.next()) {
                System.out.println(rs.getInt("car_id") + ". " + rs.getString("brand") + " " +
                        rs.getString("model") + " - " + rs.getDouble("price_per_day") + " per day");
            }
        }
    }

    private static void rentCar(Connection conn, int customerId, int carId) throws SQLException {
        String checkCarQuery = "SELECT price_per_day FROM cars WHERE car_id = ? AND availability = TRUE";
        try (PreparedStatement checkCarStmt = conn.prepareStatement(checkCarQuery)) {
            checkCarStmt.setInt(1, carId);
            ResultSet rs = checkCarStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Car not available or does not exist");
                return
            }

            double pricePerDay = rs.getDouble("price_per_day");
            System.out.print("Enter number of days to rent: ");
            Scanner scanner = new Scanner(System.in);
            int days = scanner.nextInt();
            double totalPrice = pricePerDay * days;

            String insertBooking = "INSERT INTO bookings (customer_id, car_id, start_date, end_date, total_price, status) " +
                                   "VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL ? DAY), ?, 'Booked')";
            try (PreparedStatement pstmt = conn.prepareStatement(insertBooking)) {
                pstmt.setInt(1, customerId);
                pstmt.setInt(2, carId);
                pstmt.setInt(3, days);
                pstmt.setDouble(4, totalPrice);
                pstmt.executeUpdate();
            }

            String updateCarAvailability = "UPDATE cars SET availability = FALSE WHERE car_id = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateCarAvailability)) {
                updateStmt.setInt(1, carId);
                updateStmt.executeUpdate();
            }

            System.out.println("Car rented successfully for " + totalPrice);
        }
    }
}
