import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

class  Server{
    private static final int PORT = 58421;
    public static LinkedList<Servers> ServerList = new LinkedList<Servers>(); //Список созданых сокетов


    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(PORT); //Содание Сервер скоет и прослушиваем порт 58421
        System.out.println("Server start");
        try {
            while (true) {
                Socket socket = server.accept(); //server.accept() блокирующая функции, ждет рукопожатия на выходе создает сокет соединение.
                try {
                    ServerList.add(new Servers(socket)); //добавляем новое соединение в лист и создаем для него свой поток
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
        in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //стрим для чтения данных
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()),1024); //стрим для отправки данных
        start(); // запуск потока при иницализаци конструктора.
    }

    @Override
    public void run(){
        String message;
        while (true){
            try {
                message = in.readLine(); //читаем все из буфера если есть что читать
                System.out.println(message);
                for(Servers sr : Server.ServerList){ //рассылаем сообщение на все сокеты изспика
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