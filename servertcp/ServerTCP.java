/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servertcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 *
 * @author bozzo.gianluca
 */
public class ServerTCP {

    public static void main(String[] args) {

        int portNumber = 1234;
        EventoReceiver receiver = new EventoReceiver();

        try {
            ServerSocket server = new ServerSocket(portNumber);
            System.out.println("Server di Testo in esecuzione...  (CTRL-C quits)\n");

            while (true) {
                SocketWorker w;
                try {
                    w = new SocketWorker(server.accept());
                    w.registraReceiver(receiver);
                    receiver.addListener(w);
                    Thread t = new Thread(w);
                    t.start();
                } catch (IOException e) {
                    System.out.println("Connessione NON riuscita con client: ");
                    System.exit(-1);
                }
            }
        } catch (IOException e) {
            System.out.println("Error! Porta: " + portNumber + " non disponibile");
            System.exit(-1);
        }

    }
}

class EventoReceiver {

    private String messaggio;
    private ArrayList<SocketWorker> workers = new ArrayList<>();

    void addListener(SocketWorker worker) {
        this.workers.add(worker);
    }

    void removeListener(SocketWorker worker) {
        this.workers.remove(worker);
    }

    synchronized void setNewMessaggio(String m) {
        this.messaggio = m;
        for (SocketWorker worker : this.workers) {
            worker.sendMessaggio(this.messaggio);
        }
    }

}
