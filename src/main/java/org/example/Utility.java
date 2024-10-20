package org.example;

import java.util.Scanner;

public class Utility {
    public static char readChar(Scanner scanner, char a, char b){
        char result = ' ';
        do {
            if (result != ' '){
                System.err.println("Invalid option");
            }
            System.out.print("-->");
            result = scanner.nextLine().toLowerCase().charAt(0);
            System.out.println();
        }while (result == ' ' || (result != a && result != b));
        return result;
    }
}
