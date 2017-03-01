package main.Application;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import javax.mail.*;
import javax.mail.internet.MimeMultipart;

import com.sun.mail.pop3.POP3Store;
import main.Application.Events.EmailReceivedEvent;
import main.Application.Events.EmailReceivedEventHandler;

public class Inbox
{
    ArrayList<EmailReceivedEventHandler> emailReceivedEventHandlers;
    Timer inboxRefresher;

    String pop3Host = "pop.gmail.com";
    String user = "faulknerkioskdirections@gmail.com";
    String password = "FaulkPassword";
    String storeType = "pop3";

    public Inbox()
    {
        emailReceivedEventHandlers = new ArrayList<>();
        inboxRefresher = new Timer();
    }

    public void startMonitoring()
    {
        inboxRefresher.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                receiveEmail();
            }
        }, 0,5000);

    }
    public void addReceivedHandler(EmailReceivedEventHandler h)
    {
        emailReceivedEventHandlers.add(h);
    }

    public void notifyHandlers(EmailReceivedEvent e)
    {
        for(EmailReceivedEventHandler h: emailReceivedEventHandlers)
        {
            h.handle(e);
        }
    }

    public void receiveEmail() {
        try {
            System.out.println("Checking for messages");
            //1) get the session object
            Properties properties = new Properties();
            properties.put("mail.pop3.host", pop3Host);
            Session emailSession = Session.getDefaultInstance(properties);

            //2) create the POP3 store object and connect with the pop server
            POP3Store emailStore = (POP3Store) emailSession.getStore(storeType);
            emailStore.connect(user, password);

            //3) create the folder object and open it
            Folder emailFolder = emailStore.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            //4) retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();
            for (int i = 0; i < messages.length; i++) {
                Message message = messages[i];
                MimeMultipart multipart = (MimeMultipart)message.getContent(); //parse through mime data
                for(int j=0; j<multipart.getCount(); j++)
                {
                    BodyPart part = multipart.getBodyPart(j);
                    if(part.getContent() instanceof String && !part.getContent().toString().isEmpty())
                    {
                        notifyHandlers(new EmailReceivedEvent("", part.getContent().toString()));
                        break;
                    }
                }
                //
                //
            }

            //5) close the store and folder objects
            emailFolder.close(false);
            emailStore.close();

        } catch (NoSuchProviderException e) {e.printStackTrace();}
        catch (MessagingException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}
    }

    public static void main(String[] args)
    {

    }
}  