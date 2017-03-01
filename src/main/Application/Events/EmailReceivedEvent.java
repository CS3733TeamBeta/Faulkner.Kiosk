package main.Application.Events;

/**
 * Created by benhylak on 3/1/17.
 */
public class EmailReceivedEvent
{
    private String content;
    private String sender;
    private boolean alexa;

    public EmailReceivedEvent(String sender, String content)
    {
       this(sender, content, false);
    }

    public EmailReceivedEvent(String sender, String content,boolean alexa)
    {
        this.content = content;
        this.sender = sender;
        this.alexa = alexa;
    }

    public boolean isAlexa()
    {
        return alexa;
    }

    public String getContent()
    {
        return content;
    }

    public String getSender()
    {
        return sender;
    }
}


