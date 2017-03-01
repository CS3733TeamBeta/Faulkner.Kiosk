package main.Application;

import org.subethamail.smtp.server.SMTPServer;

/**
 * Created by benhylak on 3/1/17.
 */
public class AutoResponder
{
    Inbox inbox;

    public AutoResponder()
    {
        inbox = new Inbox();
        inbox.addReceivedHandler(e->
        {
         System.out.println(e.getContent());
        });
    }

    public void startWatching()
    {
        inbox.startMonitoring();
    }


}
