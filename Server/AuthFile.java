import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

public class AuthFile {
    private static File authData = new File("src\\AuthenticationFile.txt");
    private static String secretWord = "Bal12345Bal12345";
    private static String cryptAlgorithm = "AES";

    AuthFile() {
        try {
            authData.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String getSecretWord() {

        byte[] secWord = secretWord.getBytes(StandardCharsets.UTF_8);

        for (int i = 0; i < secWord.length; i++) {
            secWord[i] += 10;
        }
        return new String(secWord, StandardCharsets.UTF_8);
    }

    static void writeToFile(String data) {

        try ( FileWriter writer = new FileWriter(authData,true) ) {
            writer.write(data);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String readFile() {

        System.out.println("Считывание из файла...");
        String result = "";

        try (BufferedReader reader = new BufferedReader(new FileReader(authData))) {
            while (reader.ready()) {
                result = result.concat(String.valueOf( (char) reader.read()) );
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(result);
        if (!result.equals("") && !result.equals("null"))
            System.out.println("Размер данных из файла: " + result.length());

        return result;
    }
}