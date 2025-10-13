package com.example.userservice;
import com.example.userservice.dao.UserDao;
import com.example.userservice.dao.UserDaoImpl;
import com.example.userservice.model.User;
import com.example.userservice.util.HibernateUtil;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
public class App {
    private static final UserDao userDao = new UserDaoImpl();
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("=== User Service ===");
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> createUser();
                case "2" -> listUsers();
                case "3" -> getUserById();
                case "4" -> updateUser();
                case "5" -> deleteUser();
                case "0" -> {
                    running = false;
                    System.out.println("Exiting...");
                }
                default -> System.out.println("Invalid option.");
            }
        }
        HibernateUtil.shutdown();
    }
    private static void printMenu() {
        System.out.println("\nChoose action:");
        System.out.println("1. Create user");
        System.out.println("2. List all users");
        System.out.println("3. Get user by ID");
        System.out.println("4. Update user");
        System.out.println("5. Delete user");
        System.out.println("0. Exit");
        System.out.print("Your choice: ");
    }

    private static void createUser() {
        try {
            System.out.print("Name: ");
            String name = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Age: ");
            Integer age = Integer.parseInt(scanner.nextLine());
            User user = new User(name, email, age);
            userDao.create(user);
            System.out.println("User created: " + user);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private static void listUsers() {
        List<User> users = userDao.findAll();
        if (users.isEmpty()) System.out.println("No users found.");
        else users.forEach(System.out::println);
    }
    private static void getUserById() {
        try {
            System.out.print("Enter ID: ");
            Long id = Long.parseLong(scanner.nextLine());
            Optional<User> userOpt = userDao.findById(id);
            userOpt.ifPresentOrElse(System.out::println, () -> System.out.println("User not found."));
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }
    private static void updateUser() {
        try {
            System.out.print("Enter ID to update: ");
            Long id = Long.parseLong(scanner.nextLine());
            Optional<User> userOpt = userDao.findById(id);
            if (userOpt.isEmpty()) {
                System.out.println("User not found.");
                return;
            }
            User user = userOpt.get();
            System.out.print("New name (" + user.getName() + "): ");
            String name = scanner.nextLine();
            if (!name.isBlank()) user.setName(name);
            System.out.print("New email (" + user.getEmail() + "): ");
            String email = scanner.nextLine();
            if (!email.isBlank()) user.setEmail(email);
            System.out.print("New age (" + user.getAge() + "): ");
            String ageStr = scanner.nextLine();
            if (!ageStr.isBlank()) user.setAge(Integer.parseInt(ageStr));
            userDao.update(user);
            System.out.println("User updated: " + user);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private static void deleteUser() {
        try {
            System.out.print("Enter ID to delete: ");
            Long id = Long.parseLong(scanner.nextLine());
            boolean deleted = userDao.delete(id);
            if (deleted) System.out.println("User deleted.");
            else System.out.println("User not found.");
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }
}
