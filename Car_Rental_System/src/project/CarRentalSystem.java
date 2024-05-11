package project;

import java.util.Scanner;

public class CarRentalSystem {
	private Car[] cars;
    private Customer[] customers;
    private Rental[] rentals;
    private int carCount;
    private int customerCount;
    private int rentalCount;

    public CarRentalSystem(int maxCars, int maxCustomers, int maxRentals) {
        cars = new Car[maxCars];
        customers = new Customer[maxCustomers];
        rentals = new Rental[maxRentals];
        carCount = 0;
        customerCount = 0;
        rentalCount = 0;
    }

    public void addCar(Car car) {
        if (carCount < cars.length) {
            cars[carCount++] = car;
        } else {
            System.out.println("Cannot add more cars.");
        }
    }

    public void addCustomer(Customer customer) {
        if (customerCount < customers.length) {
            customers[customerCount++] = customer;
        } else {
            System.out.println("Cannot add more customers.");
        }
    }

    public void rentCar(Car car, Customer customer, int days) {
        if (car.isAvailable()) {
            car.rent();
            if (rentalCount < rentals.length) {
                rentals[rentalCount++] = new Rental(car, customer, days);
            } else {
                System.out.println("Cannot rent more cars at the moment.");
            }
        } else {
            System.out.println("Car is not available for rent.");
        }
    }

    public void returnCar(Car car) {
        car.returnCar();
        for (int i = 0; i < rentalCount; i++) {
            if (rentals[i].getCar() == car) {
                rentals[i] = rentals[rentalCount - 1];
                rentalCount--;
                return;
            }
        }
        System.out.println("Car was not rented.");
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("===== Car Rental System =====");
            System.out.println("1. Rent a Car");
            System.out.println("2. Return a Car");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            if (choice == 1) {
                System.out.println("\n== Rent a Car ==\n");
                System.out.print("Enter your name: ");
                String customerName = scanner.nextLine();

                System.out.println("\nAvailable Cars:");
                for (int i = 0; i < carCount; i++) {
                    if (cars[i].isAvailable()) {
                        System.out.println(cars[i].getCarId() + " - " + cars[i].getBrand() + " " + cars[i].getModel());
                    }
                }

                System.out.print("\nEnter the car ID you want to rent: ");
                String carId = scanner.nextLine();

                System.out.print("Enter the number of days for rental: ");
                int rentalDays = scanner.nextInt();
                scanner.nextLine();

                Customer newCustomer = new Customer("CUS" + (customerCount + 1), customerName);
                addCustomer(newCustomer);

                Car selectedCar = null;
                for (int i = 0; i < carCount; i++) {
                    if (cars[i].getCarId().equals(carId) && cars[i].isAvailable()) {
                        selectedCar = cars[i];
                        break;
                    }
                }

                if (selectedCar != null) {
                    double totalPrice = selectedCar.calculatePrice(rentalDays);
                    System.out.println("\n== Rental Information ==\n");
                    System.out.println("Customer ID: " + newCustomer.getCustomerId());
                    System.out.println("Customer Name: " + newCustomer.getName());
                    System.out.println("Car: " + selectedCar.getBrand() + " " + selectedCar.getModel());
                    System.out.println("Rental Days: " + rentalDays);
                    System.out.printf("Total Price: $%.2f%n", totalPrice);

                    System.out.print("\nConfirm rental (Y/N): ");
                    String confirm = scanner.nextLine();

                    if (confirm.equalsIgnoreCase("Y")) {
                        rentCar(selectedCar, newCustomer, rentalDays);
                        System.out.println("\nCar rented successfully.");
                    } else {
                        System.out.println("\nRental canceled.");
                    }
                } else {
                    System.out.println("\nInvalid car selection or car not available for rent.");
                }
            } else if (choice == 2) {
                System.out.println("\n== Return a Car ==\n");
                System.out.print("Enter the car ID you want to return: ");
                String carId = scanner.nextLine();

                Car carToReturn = null;
                for (int i = 0; i < carCount; i++) {
                    if (cars[i].getCarId().equals(carId) && !cars[i].isAvailable()) {
                        carToReturn = cars[i];
                        break;
                    }
                }

                if (carToReturn != null) {
                    Customer customer = null;
                    for (int i = 0; i < rentalCount; i++) {
                        if (rentals[i].getCar() == carToReturn) {
                            customer = rentals[i].getCustomer();
                            break;
                        }
                    }

                    if (customer != null) {
                        returnCar(carToReturn);
                        System.out.println("Car returned successfully by " + customer.getName());
                    } else {
                        System.out.println("Car was not rented or rental information is missing.");
                    }
                } else {
                    System.out.println("Invalid car ID or car is not rented.");
                }
            } else if (choice == 3) {
                break;
            } else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }

        System.out.println("\nThank you for using the Car Rental System!");
    }

}
