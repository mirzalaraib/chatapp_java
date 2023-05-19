import java.net.Socket;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Insets;
import java.io.*;
import java.awt.event.*;




public class Client extends JFrame{

    Socket socket;

    BufferedReader br;
    PrintWriter out;
    
    // declaring component for Gui
    private JLabel heading=new JLabel("Client Area");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font=new Font("Roboto", Font.PLAIN, 20);
    private Font inputFont = new Font("Roboto", Font.PLAIN, 15);
    private JButton sendButton = new JButton("Send");
    public Client(){
        try{
            // creating object
             System.out.println("sending request to server");
            socket=new Socket("127.0.0.1",777);
            System.out.println("connection established");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
             
             craeteGUI();
             handleEvents();

             startReading();
             //startWritting(); // writing is happeninh with console
                                // no use of this function
                                // only for understanding
                                //gui thread is managing this task

        }catch(Exception e){
            e.printStackTrace();
        } 
    }

    private void handleEvents(){
     messageInput.addKeyListener(new KeyListener() {

        @Override
        public void keyTyped(KeyEvent e) {
            // TODO Auto-generated method stub
    
        }

        @Override
        public void keyPressed(KeyEvent e) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void keyReleased(KeyEvent e) {
            // TODO Auto-generated method stub
           
            // System.out.println("key released " + e.getKeyCode());
            if(e.getKeyCode()==10){  // 10 is enter code
                // for sending text from typing area to body area
                String contentToSend=messageInput.getText();
                messageArea.append("Me : "+contentToSend+"\n");
                out.println(contentToSend);
                out.flush();
                messageInput.setText("");
                messageInput.requestFocus();
            }
        }
         
     });
    }


    private void craeteGUI(){
        this.setTitle("Client Messenger");
        this.setSize(300, 500);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        heading.setIcon(new ImageIcon("clogo.png"));
        heading.setHorizontalTextPosition(SwingConstants.LEFT);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);


        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.LEFT);
        messageInput.setMargin(new Insets(10, 10, 10, 10));
        messageInput.setFont(inputFont);

        sendButton.setSize(65, 42);
        sendButton.setLocation(220, 0);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String contentToSend=messageInput.getText();
                messageArea.append("Me : "+contentToSend+"\n");
                out.println(contentToSend);
                out.flush();
                messageInput.setText("");
                messageInput.requestFocus();
            }
        });

        // setting frame input
        this.setLayout(new BorderLayout());
        // adding component to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messageArea);
        messageInput.add(sendButton);
        this.add(messageArea,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);
        messageInput.setAlignmentX(LEFT_ALIGNMENT);
        this.setVisible(true); // for screen visisble on pc

    }

    // reading method
    public void startReading() {
        // this thread will read the data
        Runnable r1 = () -> { // thread created
            System.out.println("reader started..");
            try {
            while (true) { // read data constantly
                           // when it comes from server

                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("server terminated the chat");
                        JOptionPane.showMessageDialog(this,"server terminated tha chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }

                   // System.out.println("server : " + msg);
                   messageArea.append("Server : " + msg+"\n");
                } 
                }catch (Exception e) {
                    System.out.println("connection closed");
            }
        };
        // starting thread
        new Thread(r1).start(); // creating object of thread

    }

    public void startWritting() {
        // this thread will accept data from user
        // and send it to the server
        Runnable r2 = () -> {
            System.out.println("writer started");
            try {
            while (!socket.isClosed()) {
              
                    // reading data from console with the help of br1
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();

                    out.println(content); // sending to server
                    out.flush();

                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }

                } 

                System.out.println("connection closed");
                }catch (Exception e) {
                    e.printStackTrace();
            }
        };

        // starting thread
        new Thread(r2).start(); // creating object of thread r2

    }

    public static void main(String args[]) {
      System.out.println("this is client"); 
      new Client();  
    }
}
