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
         ///handle receiving here... find the right mapnode from the content,
            // geenrate the text generations to send back,
            //send an email to the sender
        });
    }

    public void startWatching()
    {
        inbox.startMonitoring();
    }


}
