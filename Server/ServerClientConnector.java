import java.io.*;
import java.net.Socket;



public class ServerClientConnector {

    private static ObjectInputStream inObj = null;
    private static ObjectOutputStream outObj = null;


    static void initializeInAndOut (Socket client) throws IOException {
        outObj = new ObjectOutputStream(client.getOutputStream());
        inObj = new ObjectInputStream(client.getInputStream());
    }

    static void closeInAndOut() throws IOException {
        inObj.close();
        outObj.close();
    }

    static int waitForRequest() throws IOException {

        int result = inObj.readInt();
        return result;
    }

    static void sendData(String data) throws IOException {

        outObj.writeObject(data);
        outObj.flush();
    }

    static void sendData(double data) throws IOException {

        outObj.writeDouble(data);
        outObj.flush();
    }


    static String getData() throws IOException {

        String data = "";

        try {
            data = (String) inObj.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return data;
    }

}
