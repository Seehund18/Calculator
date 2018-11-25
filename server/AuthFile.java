package server;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class AuthFile {
    private static File authData = new File("src\\AuthenticationFile.txt");
    private static int numberOfUsers;
    private static AuthSecurity authSecurity;

    AuthFile() {
        authSecurity = new AuthSecurity();

        try {
            if(authData.createNewFile()) {

                String initData = "1)  login: admin\n" + "\tpassword: admin";

                writeToFile(authSecurity.encrypt(initData));
                numberOfUsers = 1;
            } else {
                numberOfUsers = countUsers();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkUser(String login, String password) {

        HashMap<String, String> dataFromFile = readFileToHashMap();

        if(!dataFromFile.containsKey(login)) {
            throw new NoSuchElementException("No such user");
        }
        if(!dataFromFile.get(login).equals(password)) {
            throw new NoSuchElementException("Wrong password");
        }

        return true;
    }

    public static void addUser(String login, String password) {

        if (login == null || login.equals("") || login.contains(" ")) {
            throw new IllegalArgumentException("Illegal login");
        }
        if (password == null || password.equals("") || password.contains(" ")) {
            throw new IllegalArgumentException("Illegal password");
        }
        if (checkLogin(login)) {
            throw new IllegalArgumentException("User already exists");
        }

        String dataToWrite = Integer.toString(++numberOfUsers) + ")  login: " + login + "\tpassword: " + password;
        writeToFile(authSecurity.encrypt(dataToWrite));
    }

    private static void writeToFile(String data) {
        try ( FileWriter writer = new FileWriter(authData,true) ) {
            writer.write(data);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeToFile(byte[] data) {
        try ( FileOutputStream out = new FileOutputStream(authData,true) ) {
            out.write(data);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int countUsers() {
        int result = 0;
        String fileData = authSecurity.decrypt(authData);
        Scanner fileScan = new Scanner(fileData);

        while (fileScan.hasNext()) {
            String a = fileScan.next();
            if (a.equals("login:")) {
                result++;
            }
        }
        fileScan.close();
        return result;
    }

    private static HashMap<String, String> readFileToHashMap() {

        HashMap<String,String> result = new HashMap<>();
        String fileData = authSecurity.decrypt(authData);

        fileData = fileData.replace('\u000F','\u0020');

        String login;
        String password;

        Scanner scan = new Scanner(fileData);

        while(scan.hasNext()) {
            String read = scan.next();
            if(read.equals("login:")) {
                login = scan.next();
                scan.next();
                password = scan.next();
                result.put(login,password);
            }
        }
        scan.close();
        return result;
    }

    private static boolean checkLogin (String login) {
        HashMap<String, String> dataFromFile = readFileToHashMap();
        return dataFromFile.containsKey(login);
    }


}
