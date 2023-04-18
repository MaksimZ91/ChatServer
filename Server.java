import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

class  Server{
    private static final int PORT = 58421;
    public static LinkedList<Servers> ServerList = new LinkedList<Servers>();


    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("Server start");
        try {
            while (true) {
                Socket socket = server.accept();
                try {
                    ServerList.add(new Servers(socket));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } finally {
            server.close();
        }
    }
}

class Servers extends Thread{
    private Socket socket;
    private BufferedWriter out;
    private BufferedReader in;

    public  Servers(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()),1024);
        start();
    }

    @Override
    public void run(){
        String message;
        while (true){
            try {
                message = in.readLine();
                System.out.println(message);
                for(Servers sr : Server.ServerList){
                    sr.send(message);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void send(String message){
        try {
            out.write(message + "\n");
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}