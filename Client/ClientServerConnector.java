import java.io.*;
import java.net.Socket;


public class ClientServerConnector {
    public static final int CLOSE = 0;
    public static final int GET_AUTH_FILE = 1;
    public static final int GET_AUTHSEC_KEY = 2;
    public static final int SEND_NEW_USER = 3;
    public static final int SEND_EXPRESSION = 4;
    public static final int SEND_CURRENT_USER = 5;

    private static Socket client;
    private static ObjectInputStream inObj = null;
    private static ObjectOutputStream outObj = null;


    static boolean connectToServer() {

        try {
            client = new Socket("localhost", 3345);

            inObj = new ObjectInputStream(client.getInputStream());
            outObj = new ObjectOutputStream(client.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    static void closeClient() throws IOException {
        inObj.close();
        outObj.close();
        client.close();
    }

    /**
     * Метод отправляет запросы к серверу
     *
     * @param operationCode Код операции:
     *                      0 - клиент закрывается;
     *                      1 - клиент запрашивает файл аутентификации;
     *                      2 - клиент запрашивает ключ расшифровки файла;
     *                      3 - клиент отправляет нового пользователя;
     *                      4 - клиент хочет отправить выражение;
     *                      5 - клиент хочет отправить имя зашедшего пользователя
     */
    static void sendRequest(int operationCode) {

        System.out.println("Отправляю запрос: " + operationCode);

        try {
            outObj.writeInt(operationCode);
            outObj.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Запрос отправлен");
        System.out.println();
    }

    static void sendDataToServer(String data) {

        System.out.println("Отправляю данные на сервер:");
        System.out.println("Данные для отправки: ");
        System.out.println(data);

        try {
            outObj.writeObject(data);
            outObj.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Данные отправлены");
        System.out.println();
    }



    static String getDataFromServer() {
        System.out.println("Получаю данные с сервера:");

        String data = "";

        try {
//            if(inObj.available() != 0) {
//                Thread.sleep(100);
//            }

            data = (String) inObj.readObject();


        } catch (IOException e) {
            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("Получены данные с сервера:");
        System.out.println(data);
        System.out.println();

        return data;
    }

    static double getCalcResultFromServer() {
        System.out.println("Получаю данные с сервера:");

        double result = 0;
        try {
            result = inObj.readDouble();
        } catch (IOException e1) {
            e1.printStackTrace();
        }


        System.out.println("Получены данные с сервера:");
        System.out.println(result);
        System.out.println();

        return result;
    }
}
