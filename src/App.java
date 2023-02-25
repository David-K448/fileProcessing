import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class App {
    
    public static void main(String[] args) throws Exception {
        Scanner s = new Scanner(System.in);
        
        String pathInput = "";
        int userInput;
        int pathLoaded = 0;
        

        while (true) {
            
            System.out.println("\n\nMenu:\n\t0 - Exit\n\t1 - Select Directory\n\t2 - List Directory Content\n\t3 - Display file (hexadecimal view)\n\t4 - Delete file\n\t5 - Mirror reflect file (byte level)\n\nSelect Option:\n");
            userInput = s.nextInt();
            s.nextLine(); //clear next line character

            if (userInput < 0 || userInput > 5) {
                System.out.println("Invalid input");
                continue;
            }

            switch(userInput) {
                case 0:
                    System.out.println("Goodbye!");
                    s.close();
                    System.exit(0);
                case 1:
                    //get path
                    System.out.println("\nPlease enter the directory path you want to access:");
                    pathInput = s.nextLine();

                    // copying the path can leave "", strip them off if they're there 
                    if (pathInput.startsWith("\"") && pathInput.endsWith("\"")) {
                        pathInput = pathInput.substring(1, pathInput.length() - 1);
                    } 

                    // check path validity
                    if (!isValidPath(pathInput)) {
                        System.out.println("The path is not a valid directory, please re-enter and try again.");
                        break;
                    } else {
                        System.out.println("Path Loaded!");
                        // set value to true opening the other selections to the user
                        pathLoaded = 1;
                        System.out.println(pathInput);
                        break;
                    }
                case 2:
                    if(pathLoaded == 1){
                        System.out.println("Option 2 selected\n");
                        printDirectory(pathInput);
                        break;
                    }

                    System.out.println("\nNo directory selected.");
                    break;
                case 3:
                    if(pathLoaded == 1){
                        System.out.println("Option 3 selected\n");
                        toHex(pathInput);
                        break;
                    }

                    System.out.println("\nNo directory selected.");
                    break;
                case 4:
                    if(pathLoaded == 1){
                        System.out.println("Option 4 selected\n");
                        deleteFile(pathInput);
                        break;
                    }

                    System.out.println("\nNo directory selected.");
                    break;
                case 5:
                    if(pathLoaded == 1){
                        System.out.println("Option 5 selected\n");
                        mirrorFile(pathInput);
                        break;
                    }

                    System.out.println("\nNo directory selected.");
                    break;
                default:
                    System.out.println("Invalid input");
                    break;
            }
        }
    }

    private static boolean isValidPath(String pathInput) {
        File test = new File(pathInput);
        if(test.isDirectory()) {
            return true;
        }
        return false;
    }

    private static void printDirectory(String pathInput) {
        File dir = new File(pathInput);
        System.out.println("Contents of directory \"" + pathInput + "\":");
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                System.out.println("[File] " + file.getName());
            } else if (file.isDirectory()) {
                System.out.println("[Directory] " + file.getName());
            }
        }
    }

    private static void toHex(String pathInput) {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter file name: ");
        String fileName = s.nextLine();
    
        File file = new File(pathInput, fileName);
        
        // Check if file exists
        if (!file.exists()) {
            System.out.println("File does not exist.");
            return;
        }
        
        try (InputStream inputStream = new FileInputStream(file)) {
            // Read file bytes
            byte[] fileBytes = inputStream.readAllBytes();
            
            // Display bytes in hexadecimal view
            for (int i = 0; i < fileBytes.length; i++) {
                if (i % 16 == 0) {
                    System.out.println();
                    //System.out.printf("%08X  ", i);
                }
                System.out.printf("%02X ", fileBytes[i]);
            }
        } catch (IOException e) {
            System.out.println("Error reading file.");
        }
    }
    
    private static void deleteFile(String pathInput) {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter file name: ");
        String fileName = s.nextLine();
    
        File file = new File(pathInput, fileName);
    
        // Check if file exists
        if (!file.exists()) {
            System.out.println("File does not exist.");
            return;
        }
    
        // Delete file
        if (file.delete()) {
            System.out.println("File deleted successfully.");
        } else {
            System.out.println("Failed to delete file.");
        }
    }
    
    private static void mirrorFile(String pathInput) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter file name: ");
        String fileName = scanner.nextLine();
        
        File inputFile = new File(pathInput, fileName);
        
        // Check if file exists
        if (!inputFile.exists()) {
            System.out.println("File does not exist.");
            return;
        }
        
        // Read input file
        try (FileInputStream inputStream = new FileInputStream(inputFile)) {
            byte[] inputBytes = inputStream.readAllBytes();
            
            // Mirror reflection for every byte
            for (int i = 0; i < inputBytes.length; i++) {
                inputBytes[i] = (byte) (~inputBytes[i] & 0xFF);
            }
            
            // Overwrite input file with mirrored content
            try (FileOutputStream outputStream = new FileOutputStream(inputFile)) {
                outputStream.write(inputBytes);
            }
            
            System.out.println("File mirrored successfully.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
