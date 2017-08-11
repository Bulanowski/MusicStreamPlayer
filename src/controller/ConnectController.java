package controller;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.util.Pair;
import model.AudioPlayer;
import model.CommandSender;
import model.TCP;
import view.ConnectView;
import view.PrimaryView;

import java.io.*;
import java.util.ArrayList;

public class ConnectController {
    private static ConnectView cv;
    private final PrimaryView primaryView;
    private  TCP tcp;
    private  CommandSender commandSender;
    private  AudioPlayer audioPlayer;
    private  MainController mainController;
    private EventHandler<ConnectEvent> handler;

    public ConnectController(PrimaryView primaryView, TCP tcp, CommandSender commandSender, AudioPlayer audioPlayer, MainController mainController) {


        this.tcp = tcp;
        this.audioPlayer = audioPlayer;
        this.commandSender = commandSender;
        this.mainController = mainController;

        handler = event -> {
            int PORT = 53308;
            if (tcp.connect(event.ip, PORT)) {
                    tcp.start();
                    commandSender.requestSongs();
                    commandSender.username(event.username);
                    cv.addAddress(new Pair<>(event.ip, event.username));
                    audioPlayer.start();
                    saveList();
                    mainController.showMainView();
                }
        };

        cv = new ConnectView(handler);



        this.primaryView = primaryView;

        getAddressesFromFile();

        cv.setConnectButtonAction( actionEvent -> {
            try {
                int PORT = 53308;
                if(cv.getUsername() != null && cv.getIP() != null) {
                    if (tcp.connect(cv.getIP(), PORT)) {
                        tcp.start();
                        commandSender.requestSongs();
                        commandSender.username(cv.getUsername());
//                    Random rand = new Random();
//                    commandSender.username("guest" + (100000 + rand.nextInt(899999)));
                        audioPlayer.start();
                        cv.addAddress(new Pair<>(cv.getIP(), cv.getUsername()));
                        saveList();
                        mainController.showMainView();
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        });
    }

    public void getAddressesFromFile() {
        try {
            FileInputStream fis = new FileInputStream("savedAddresses");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object o = ois.readObject();
            ArrayList<Pair<String,String>> list = (ArrayList) o;
            cv.addAddresses(list);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void saveList() {
        ArrayList<Pair> list = new ArrayList();


        try {
            FileInputStream fis = new FileInputStream("savedAddresses");
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<Pair> temp = (ArrayList) ois.readObject();

        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {

            FileOutputStream fos = new FileOutputStream("savedAddresses");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            for (Pair pair: cv.getSavedAddresses()) {

                boolean same = false;
                for (Pair pair2:list) {

                    System.out.println(pair+ " : "+pair2);

                    if(pair.getKey().equals(pair2.getKey())&&pair.getValue()!=null) {
                        same = true;
                        if(!pair.getValue().equals(pair2.getValue())) {
                            list.remove(pair2);
                            list.add(pair);
                        }
                    }
                }
                if(!same) {
                    System.out.println(pair);
                    list.add(pair);
                }
            }
            oos.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setAsCenter() {
        primaryView.setCenter(cv);
    }

    public static class ConnectEvent extends Event {
        String username;
        String ip;

        public ConnectEvent(EventType<? extends Event> eventType,String username, String ip ) {
            super(eventType);
            this.ip = ip;
            this.username = username;

        }
    }

}
