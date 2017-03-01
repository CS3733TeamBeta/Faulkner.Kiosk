package main.Application.Events;

/**
 * Created by benhylak on 3/1/17.
 */
public interface EmailReceivedEventHandler
{
    void handle(EmailReceivedEvent event);
}
