import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MainServer {
    private static Logger log = Logger.getLogger(MainServer.class.getName());

    private static AuthFile auth;
    private static MathEngine math;

    private static Socket client;
    private static ServerSocket server;

    private static void serverInitialize() throws IOException {
        auth = new AuthFile();
        math = new MathEngine();
        server = new ServerSocket(3345);
        server.setSoTimeout(10000);
    }

    private static void serverClose() throws IOException {
        ServerClientConnector.closeInAndOut();
        client.close();
    }


    public static void main(String[] args) {


        try ( BufferedReader consoleCommand = new BufferedReader(new InputStreamReader(System.in)) ) {
            serverInitialize();
            LoggerConfig();

            System.out.println("Сервер запущен...");


            while (true) {

                try {
                    client = server.accept();
                    System.out.println("Соеднинение установлено");
                    ServerClientConnector.initializeInAndOut(client);

                } catch (SocketTimeoutException ex) {

                    System.err.println("Время ожидания нового пользователя истекло");
                    System.err.println("Выключить сервер? [y/n]");
                    if (consoleCommand.read() == 'y') break;
                }

                while (!client.isClosed()) {

                    System.out.println("Жду запроса от клиента");
                    int code = ServerClientConnector.waitForRequest();
                    System.out.println("Запрос получен: " + code);
                    System.out.println();

                    switch (code) {
                        case 0:
                            serverClose();
                            break;
                        case 1:
                            sendAuthFile();
                            break;
                        case 2:
                            sendKeyWord();
                            break;
                        case 3:
                            getNewUser();
                            break;
                        case 4:
                            performCalc();
                            break;
                        case 5:
                            writeUserToLog();
                            break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Сервер закрыт");
    }

    private static void writeUserToLog() throws IOException {
        String login = ServerClientConnector.getData();
        log.info("Пользователь " + login + " вошёл в приложение");
    }

    private static void LoggerConfig() {
        FileHandler fh;
        try {
            fh = new FileHandler("Log.log");
            log.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            log.setUseParentHandlers(false);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void performCalc() throws IOException {
        System.out.println("Приниимаю выражение для вычисления:");

        String expression = ServerClientConnector.getData();
        double result = math.calculate(expression);
        System.out.println("Результат вычислений: " + expression + "=" + result);

        log.info("Вычисления: " + expression + "=" + result);

        ServerClientConnector.sendData(result);
        System.out.println("Результаты вычисления отправлены");
    }

    private static void getNewUser() throws IOException {
        System.out.println("Принял от клиента пользователя: ");

        String newUser = ServerClientConnector.getData();
        System.out.println(newUser);

        System.out.println("Длина данных: " + newUser.length());

        AuthFile.writeToFile(newUser);
        System.out.println("Принял нового пользователя");
        System.out.println();
    }

    private static void sendKeyWord() throws IOException {
        System.out.println("Отправляю клиенту слелующий зашифрованное ключевое слово: ");

        String word = AuthFile.getSecretWord();
        System.out.println(word);

        ServerClientConnector.sendData(word);
        System.out.println("Отправил ключевое слово");
        System.out.println();
    }

    private static void sendAuthFile() throws IOException {
        System.out.println("Отправляю клиенту данные из файла: ");

        String fileData = AuthFile.readFile();

        ServerClientConnector.sendData(fileData);
        System.out.println("Данные файла отправлены");
        System.out.println();
    }
}