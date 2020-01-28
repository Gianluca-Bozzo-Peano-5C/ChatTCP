/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servertcp;

import java.io.*;
import java.net.*;

/**
 *
 * @author bozzo.gianluca
 */
class SocketWorker implements Runnable, EventoListener, EventoPublisher {

    private Socket client;
    private PrintWriter out = null;
    EventoReceiver receiver;

    SocketWorker(Socket client) {
        this.client = client;
        System.out.println("Connesso con: " + client);
    }

    @Override

    public void sendMessaggio(String messaggio) {

        out.println("Server->> " + messaggio);

    }

    public void run() {

        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Errore: in|out fallito");
            System.exit(-1);
        }

        String line = "";
        int clientPort = client.getPort();
        while (line != null) {
            try {
                line = in.readLine();

                receiver.setNewMessaggio(line);

                System.out.println(clientPort + ">> " + line);
            } catch (IOException e) {
                System.out.println("lettura da socket fallito");
                System.exit(-1);
            }
        }
        try {
            client.close();
            System.out.println("connessione con client: " + client + " terminata!");
        } catch (IOException e) {
            System.out.println("Errore connessione con client: " + client);
        }
    }

    @Override
    public void registraReceiver(EventoReceiver r) {
        this.receiver = r;
    }

    @Override
    public void messaggioReceived(String m) {
        this.receiver.setNewMessaggio(m);
    }
}

interface EventoListener {

    public void sendMessaggio(String m);
}

interface EventoPublisher {

    public void registraReceiver(EventoReceiver r);

    public void messaggioReceived(String m);
}
