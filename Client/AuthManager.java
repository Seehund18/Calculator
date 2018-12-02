import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.IllegalBlockSizeException;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.util.*;


public class AuthManager {
    private SecretKey secKey;
    private Cipher cipher;
    private String authData;

    AuthManager() {
        secretKeyInit();
        authDataInit();
        cipherInit();
    }

    private void secretKeyInit() {
        ClientServerConnector.sendRequest(ClientServerConnector.GET_AUTHSEC_KEY);
        String cryptSecWord = ClientServerConnector.getDataFromServer();

        byte[] cryptWordBytes = cryptSecWord.getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < cryptWordBytes.length; i++) {
            cryptWordBytes[i] -= 10;
        }
        String secWord = new String(cryptWordBytes, StandardCharsets.UTF_8);

        System.out.println("Проверка правильности передачи кодового слова: " + secWord.equals("Bal12345Bal12345"));
        System.out.println();

        secKey = new SecretKeySpec(secWord.getBytes(),"AES");
        cipherInit();
    }

    private void authDataInit() {
        ClientServerConnector.sendRequest(ClientServerConnector.GET_AUTH_FILE);
        String fileData = ClientServerConnector.getDataFromServer();

        System.out.println("С сервера получили следующие зашифрованные данные: ");
        System.out.println(fileData);
        System.out.println("Размер данных: " + fileData.length());
        System.out.println();

        try {
            authData = decrypt(fileData);
        } catch (NullPointerException ex) {
            authData = "";
        }

        System.out.println("Дешифрованные серверные данные: ");
        System.out.println(authData);
        System.out.println();
    }

    private void cipherInit() {
        try {
            cipher = Cipher.getInstance("AES");
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }





    public void addUser(String login, String password) {

        if (login == null || login.equals("") || login.contains(" ")) {
            throw new IllegalArgumentException("Illegal login");
        }
        if (password == null || password.equals("") || password.contains(" ")) {
            throw new IllegalArgumentException("Illegal password");
        }
        if (checkLogin(login)) {
            throw new IllegalArgumentException("User already exists");
        }


        String dataToWrite = " login: " + login + " password: " + password;
        authData = authData.concat(dataToWrite);

        System.out.println("Добавлен новый пользователь " + dataToWrite);
        System.out.println();

        byte[] encryptDataToWrite = encrypt(dataToWrite);

        // При шифровании по AES важно, чтобы длина данных была кратна 16.
        // При переводе массива байтов в строку по кодировке ISO_8859_1 количество байтов не изменится.
        // Оно останется кратным 16
        String strDataToWrite = new String(encryptDataToWrite, StandardCharsets.ISO_8859_1);

        ClientServerConnector.sendRequest(ClientServerConnector.SEND_NEW_USER);
        ClientServerConnector.sendDataToServer(strDataToWrite);
    }

    public boolean checkUser(String login, String password) {

        HashMap<String, String> dataFromFile = toHashMap();

        if(!dataFromFile.containsKey(login)) {
            throw new NoSuchElementException("No such user");
        }
        if(!dataFromFile.get(login).equals(password)) {
            throw new NoSuchElementException("Wrong password");
        }

        return true;
    }




    private HashMap<String,String> toHashMap() {

        HashMap<String,String> result = new HashMap<>();
        String login;
        String password;

        Scanner scan = new Scanner(authData);

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

    private boolean checkLogin (String login) {

        HashMap<String, String> dataFromFile = toHashMap();
        return dataFromFile.containsKey(login);
    }


    private byte[] encrypt(String data) {

        byte[] encryptBytes = null;

        try  {
            cipher.init(Cipher.ENCRYPT_MODE, secKey);
            encryptBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return encryptBytes;
    }

    private String decrypt(String data) {

        if (data.equals("null")) throw new NullPointerException();

        StringBuilder result = new StringBuilder();
        byte[] b;

        try {
            cipher.init(Cipher.DECRYPT_MODE, secKey);
            b = cipher.doFinal(data.getBytes(StandardCharsets.ISO_8859_1));

            // Убираем лишние системные символы, которые возникли при дешифровке
            for (byte i : b) {
                if (i <= 20) {
                    continue;
                }
                result.append(Character.toChars(i));
            }

        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
