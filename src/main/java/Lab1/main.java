package Lab1;
//victor GIan
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> userData = new ArrayList<>();

        // Step 1: Gather input
        while (true) {
            System.out.println("Enter Name:");
            String name = scanner.nextLine();

            System.out.println("Enter Age:");
            int age = Integer.parseInt(scanner.nextLine());

            if (age >= 18) {
                System.out.println(name + ", you are in Legal Age!");
            } else {
                System.out.println(name + ", you are a Minor!");
            }

            userData.add("Name: " + name + ", Age: " + age);

            System.out.println("Do you want to try again? (y/n)");
            String answer = scanner.nextLine();
            if (!answer.equalsIgnoreCase("y")) {
                System.out.println("Thank you. Program ended.");
                break;
            }
        }

        // Step 2: Write to file
        try (BufferedWriter out = new BufferedWriter(new FileWriter("test.txt"))) {
            for (String entry : userData) {
                out.write(entry);
                out.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Step 3: Read from file into fileData list
        List<String> fileData = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new FileReader("test.txt"))) {
            String line;
            while ((line = in.readLine()) != null) {
                fileData.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Step 4: Print array/list format
        System.out.println("\nData from file (as array/list):");
        System.out.println(fileData);

        scanner.close();
    }
}
