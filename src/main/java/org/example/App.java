package org.example;

import java.io.*;
import java.util.Scanner;

/**
 * Hello world!
 */
public class App {
    public static void menu() {
            System.out.println("-----MENU-----");
            System.out.print("read <NameFile>:"+        "\t\t\t\t\tOpen a readeable file\n" +
                    "write <NameFile>:"+                "\t\t\t\t\tWrite a note\n" +
                    "create_blank_csv <NameFile>:"+     "\t\tCreates a blank CSV file\n" +
                    "delete <NameFile>:"+               "\t\t\t\t\tDeletes the indicated file\n" +
                    "create <NameFile>:"+               "\t\t\t\t\tCreates a File (you can also specify on the NameFile the extension)\n" +
                    "cd <path>:"+                       "\t\t\t\t\t\t\tAllows to change between directories\n" +
                    "ls:"+                              "\t\t\t\t\t\t\t\t\tShows all files and directories on the path you are located\n" +
                    "mkdir <nameDir>:"+                 "\t\t\t\t\tCreates a Folder / directory\n"+
                    "exit:"+                            "\t\t\t\t\t\t\t\tExits\n");
    }

    public static void main(String[] args) {
        System.out.println("Welcome to File Administrator");
        System.out.println("Write command 'help' for more information");
        Scanner scanner = new Scanner(System.in);
        String originPath = "C:\\";
        File file = new File(originPath);
        String nameFile;
        String command;
        String[] parameters;
        boolean exit = false;
        do {
            command = scanner.nextLine();
            parameters = command.split(" ");
            switch (parameters[0]) {
                case "help":
                    menu();
                    break;
                case "read":
                    if (parameters.length>1){
                        nameFile = command.substring(5);
                        System.out.println("Do you want to open a binary file [Y/N]:");
                        char option = Utility.readChar(scanner,'y','n');
                        if (option == 'y') {
                            read('b', file, nameFile);
                        }else{
                            read('n',file, nameFile);
                        }
                    }else{
                        System.err.println("Please specify a <NameFile> in your command");
                    }
                    break;
                case "write":
                    if (parameters.length>1){
                        nameFile = command.substring(6);
                        System.out.println("Do you want to save information in binary [Y/N]:");
                        char option = Utility.readChar(scanner,'y','n');
                        if (option == 'y') {
                            write('b', scanner, file, nameFile);
                        }else{
                            write('n',scanner,file, nameFile);
                        }
                    }else{
                        System.err.println("Please specify a <NameFile> in your command");
                    }
                    break;
                case "create_CSV":
                    if (parameters.length>1) {
                        nameFile = command.substring(10);
                        createCSV(scanner, file, nameFile);
                    }else{
                        System.err.println("Please specify a <NameFile> in your command");
                    }
                    break;
                case "delete":
                    if (parameters.length >1){
                        nameFile = command.substring(7);
                        if (!delete(file,nameFile)){
                            System.err.println("Error: Selected File have not been deleted");
                        }
                    }else{
                        System.err.println("Please specify a <NameFile> in your command");
                    }
                    break;
                case "create":
                    if (parameters.length > 1) {
                        nameFile = command.substring(7);
                        create(file, nameFile);
                    }else{
                        System.err.println("Please specify a <NameFile> in your command");
                    }
                    break;
                case "mkdir":
                    if (parameters.length>1) {
                        nameFile = command.substring(6);
                        mkdir(file, nameFile);
                    }else{
                        System.err.println("Please specify a <NameFile> in your command");
                    }
                    break;
                case "cd.":
                    System.out.println(file.getAbsolutePath());
                    break;
                case "cd..":
                    file = goToParentDirectory(file);
                    break;
                case "cd":
                    if (parameters.length>1) {
                        nameFile = command.substring(3);
                        file = addPath(file, nameFile);
                    }else{
                        file = new File(originPath);
                    }
                    break;
                case "ls":
                    listFiles(file);
                    break;
                case "exit":
                    exit = true;
                    System.out.println("-Ended Execution-");
                    break;
                default:
                    System.err.println("Invalid-command!");
            }
        } while (!exit);
    }
    private static void mkdir(File file, String nameDirectory){
        file = new File(file.getPath()+"\\"+nameDirectory);
        file.mkdir();
    }
    private static boolean delete(File file, String nameFile){
        boolean done = false;
        File toDeleteFile = addPath(file,nameFile);
        if (!toDeleteFile.equals(file)){
            done = toDeleteFile.delete();
        }
        return done;
    }
    private static void listFiles(File file){
        String[] files = file.list();
        if (files!=null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i] != null) {
                    System.out.print(files[i] + "\t\t\t");
                    if (i % 6 == 0) {
                        System.out.print("\n");
                    }
                }
            }
            System.out.println();
        }else{
            System.err.println(" No Files In This Directory! ");
        }
    }
    private static File goToParentDirectory(File file){
        if (!file.getPath().equals("C:\\")) {
            return new File(file.getParent());
        }else{
            return file;
        }
    }

    private static File addPath(File file, String path){
        String newPath = file.getPath()+"\\"+path;
        File newFile = new File(newPath);
        if (newFile.exists()){
            return newFile;
        }else{
            System.err.println("Error: Invalid Path!");
            return file;
        }
    }

    public static void read(char option, File file, String nameFile) {
        if (option == 'b' || option == 'B') {
            readBinaryFile(file,nameFile);
        } else {
            readNonBinaryFile(file,nameFile);
        }
    }

    private static void readBinaryFile(File file, String nameFile) {
        DataInputStream entrada = null;
        try {
            entrada = new DataInputStream(new FileInputStream(file+"\\"+nameFile));
            System.out.println(entrada.readUTF());
        } catch (IOException ex) {
            System.out.println("IOException al leer: " + ex.getMessage());
        } finally {
            if (entrada != null) {
                try {
                    entrada.close();
                } catch (IOException ex) {
                    System.out.println("IOException al cerrar: " + ex.getMessage());
                }
            }
        }
    }

    private static void readNonBinaryFile(File file, String nameFile) {
        BufferedReader entrada = null;
        try {
            entrada = new BufferedReader(
                    new FileReader(file+"\\"+nameFile));
            String cadena;
            while ((cadena = entrada.readLine()) != null) {
                System.out.println(cadena);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if (entrada != null) {
                    entrada.close();
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void write(char option,Scanner teclado, File file, String nameFile) {
        if (option == 'b' || option == 'B') {
            writeBinary(teclado, file.getPath(), nameFile);
        } else {
            writeNonBinary(teclado, file.getPath(), nameFile);
        }
    }

    private static void writeBinary(Scanner teclado, String path, String nameFile) {
        DataOutputStream salida = null;
        try {
            if (path.equals("C:\\")) {
                System.out.print("Seleccione la ruta (absoluta) del archivo: ");
                salida = new DataOutputStream(new FileOutputStream(teclado.nextLine()));
            } else {
                salida = new DataOutputStream(new FileOutputStream(path+"\\"+nameFile));
            }
            String cadena;
            System.out.println("Introduce texto. Para terminar FIN:");
            do {
                cadena = teclado.nextLine();
                if (!cadena.equals("FIN")) {
                    salida.writeUTF(cadena);
                }
            } while (!cadena.equals("FIN"));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (salida != null) {
                try {
                    salida.close();
                } catch (IOException ex) {
                    System.out.println("IOException al cerrar: " + ex.getMessage());
                }
            }
        }
    }

    private static void writeNonBinary(Scanner teclado, String path, String nameFile) {
        PrintWriter salida = null;
        try {
            if (path.equals("C:\\")) {
                System.out.print("Seleccione la ruta (absoluta) del archivo: ");
                String ruta = teclado.nextLine();
                salida = new PrintWriter(ruta);
            } else {
                salida = new PrintWriter(path+"\\"+nameFile);
            }
            String cadena;
            System.out.println("Introduce texto. Para terminar FIN:");
            do {
                cadena = teclado.nextLine();
                if (!cadena.equals("FIN")) {
                    salida.write(cadena);
                }
            } while (!cadena.equals("FIN"));
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (salida != null) {
                salida.close();
            }
        }
    }

    public static void createCSV(Scanner teclado, File file, String nameFile) {
        System.out.print("Inserte el número de columnas de tu CSV: ");
        int n = teclado.nextInt();
        System.out.println();
        System.out.print("Inserte el número de líneas: ");
        int m = teclado.nextInt();
        System.out.println();
        System.out.println("Quiere crear un CSV binario o en Texto plano (B/T)");
        char option = teclado.next().charAt(0);
        if (option == 'b' || option == 'B') {
            DataOutputStream salida = null;
            try {
                salida = new DataOutputStream(new FileOutputStream(file.getPath()+"\\"+nameFile));
                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < n; j++) {
                        salida.writeUTF(" - ;");
                    }
                    salida.writeUTF("\n");
                }
            } catch (IOException ex) {
                System.out.println("IOException al escribir: " + ex.getMessage());
            } finally {
                if (salida != null) {
                    try {
                        salida.close();
                    } catch (IOException ex) {
                        System.out.println("IOException al cerrar: " + ex.getMessage());
                    }
                }
            }
        } else {
            PrintWriter salida = null;
            try {
                salida = new PrintWriter(file.getPath()+"\\"+nameFile);
                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < n; j++) {
                        salida.write("\t;");
                    }
                    salida.write("\n");
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            } finally {
                if (salida != null) {
                    salida.close();
                }
            }
        }
        System.out.println("Done");
    }
    public static boolean create(File file, String nameNewFile) {
        boolean resultado = false;
        String newPath = file.getPath()+"\\"+nameNewFile;
        File toCreateFile = new File(newPath);

        if (!toCreateFile.exists()) {
            try {
                resultado = toCreateFile.createNewFile();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return resultado;
    }
}

