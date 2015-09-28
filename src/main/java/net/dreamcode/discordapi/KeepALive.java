package net.dreamcode.discordapi;


public class KeepALive extends Thread {

    private  SocketConnector sock;
    private int interval;

    public KeepALive(SocketConnector sock, int interval){
        this.sock = sock;
        this.interval = interval;

    }

    @Override
    public void run()  {

        while (this.sock.isOnline()) {

            try {

            this.sock.send("{\"op\": 1,\"d\": " + (int) System.currentTimeMillis() + "}");



                Thread.sleep(interval);
            }
            catch (Exception e){
                //e.printStackTrace();
            }
        }
    }
}
