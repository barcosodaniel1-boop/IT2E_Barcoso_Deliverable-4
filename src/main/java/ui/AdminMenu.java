package com.yourdomain.ui;

import com.yourdomain.dao.UserDao;
import com.yourdomain.model.User;

import java.util.List;
import java.util.Scanner;

public class AdminMenu {

    private final UserDao userDao;
    private final Scanner scanner;
    private final User admin;

    public AdminMenu(UserDao userDao, Scanner scanner, User admin) {
        this.userDao = userDao;
        this.scanner = scanner;
        this.admin = admin;
    }

    public void show() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== ADMIN MENU (" + admin.username() + ") ===");
            System.out.println("1. Review pending accounts");
            System.out.println("0. Logout");
            System.out.print("Choose: ");
            switch (scanner.nextLine().trim()) {
                case "1" -> reviewPending();
                case "0" -> running = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void reviewPending() {
        List<User> pending = userDao.findByStatus("PENDING");
        if (pending.isEmpty()) {
            System.out.println("No pending accounts.");
            return;
        }
        for (User u : pending) {
            System.out.printf("[%d] %s (%s) - %s%n", u.id(), u.fullName(), u.username(), u.role());
        }
        System.out.print("Enter user ID to review (blank to cancel): ");
        String idText = scanner.nextLine().trim();
        if (idText.isEmpty()) {
            return;
        }
        try {
            int id = Integer.parseInt(idText);
            System.out.print("(A)pprove or (R)eject? ");
            String action = scanner.nextLine().trim().toUpperCase();
            if (action.equals("A")) {
                System.out.println(userDao.updateStatus(id, "APPROVED") ? "Approved." : "Update failed.");
            } else if (action.equals("R")) {
                System.out.println(userDao.updateStatus(id, "REJECTED") ? "Rejected." : "Update failed.");
            } else {
                System.out.println("Cancelled.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID.");
        }
    }
}