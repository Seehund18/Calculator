package server;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;



public class AuthSecurity {
    private File secFile = new File("src\\AuthenticationSecurity.txt");
    private SecretKey secKey;
    private Cipher cipher;


    AuthSecurity() {
        try {
            if (secFile.createNewFile()) {
                secKey = KeyGenerator.getInstance("AES").generateKey();
                writeToFile();

            } else {
                readFromFile();
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        cipherInit();
    }

    public byte[] encrypt(String data) {
        byte[] a = null;

        try  {
            cipher.init(Cipher.ENCRYPT_MODE, secKey);
            a = cipher.doFinal(data.getBytes());
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return a;
    }

    public String decrypt(File file) {
        StringBuilder sb = new StringBuilder();

        try (FileInputStream fileIn = new FileInputStream(file)) {
            cipher.init(Cipher.DECRYPT_MODE, secKey);

            try (   CipherInputStream cipherIn = new CipherInputStream(fileIn, cipher);
                    InputStreamReader inputReader = new InputStreamReader(cipherIn);
                    BufferedReader reader = new BufferedReader(inputReader) ) {

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
        } catch (IOException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private void writeToFile() {
        try ( FileOutputStream writer = new FileOutputStream(secFile, true) ) {
            writer.write(secKey.getEncoded());
            writer.flush();
            if( !secFile.setReadOnly() ) {
                throw new IOException("Setting permission error");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readFromFile() {
        try ( FileInputStream in = new FileInputStream(secFile) ) {
            byte[] encoded = new byte[16];
            if ( in.read(encoded) != 16) {
                throw new IOException("Reading from file error");
            }
            secKey = new SecretKeySpec(encoded, "AES");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cipherInit() {
        try {
            cipher = Cipher.getInstance("AES");
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }
}
