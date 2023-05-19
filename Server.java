import java.net.*;
import java.io.*;

class Server {

    ServerSocket server; // variable
    Socket socket; // variable
    BufferedReader br; // variable for reading
    PrintWriter out; // variable for writting

    // constructor..
    public Server() {
        // making server socket
        try {
            server = new ServerSocket(777); // client request will send to 7777 port number
            // accepting client request

            System.out.println("server ready to accept connection");
            System.out.println("waiting....");

            socket = server.accept(); // returning client socket object
            // for reading data from socket
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // for writting

            out = new PrintWriter(socket.getOutputStream());
            // for reading and writting function call
            startReading();
            startWritting();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // for reading and writting simultaneously
    // we ae using multithreading

    public void startReading() {
        // this thread will read the data
        Runnable r1 = () -> { // thread created
            System.out.println("reader started..");

            try {
                while (true) { // read data constantly
                               // when it comes from client

                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("client terminated the chat");
                        socket.close();
                        break;
                    }

                    System.out.println("client : " + msg);
                }

            } catch (Exception e) {
                System.out.println("connection closed");
            }
        };
        // starting thread
        new Thread(r1).start(); // creating object of thread r1

    }

    public void startWritting() {
        // this thread will accept data from user
        // and send it to the client
        Runnable r2 = () -> {
            System.out.println("writer started");
            try {
                while (!socket.isClosed()) {

                    // reading data from console with the help of br1
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();

                    out.println(content); // sending to client
                    out.flush();

                   if(content.equals("exit")){
                       socket.close();
                       break;
                   }

                }
            } catch (Exception e) {
                System.out.println("connection closed");

            }
        };

        // starting thread
        new Thread(r2).start(); // creating object of thread r2

    }

    public static void main(String args[]) {
        System.out.println("this is server...going to start");
        new Server();
    }
}
