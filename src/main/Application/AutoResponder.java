package main.Application;

import main.Directory.Doctor;
import main.Map.Entity.Hospital;
import org.apache.commons.lang3.StringUtils;
import org.subethamail.smtp.server.SMTPServer;

/**
 * Created by benhylak on 3/1/17.
 */
public class AutoResponder
{
    Inbox inbox;

    Hospital h;

    public AutoResponder()
    {
        inbox = new Inbox();
        inbox.addReceivedHandler(e->
        {
         System.out.println(e.getContent());
         System.out.println("Best match: " + findBestMatch(e.getContent()).getName());
         ///handle receiving here... find the right mapnode from the content,
            // genrate the text directions to send back,
            //send an email to the sender
        });
    }

    public void startWatching()
    {
        inbox.startMonitoring();
        h=ApplicationController.getHospital();
    }

    /**
     * Finds a doctor with the best match from the input
     * @param input
     * @return Doctor
     */
    public Doctor findBestMatch(String input)
    {
        String bestMatch =null;

        int minLevDist = 0;
        int curLeven;

        for(String key: h.getDoctors().keySet())
        {
            curLeven = StringUtils.getLevenshteinDistance(key, input);

            if(bestMatch==null || curLeven < minLevDist)
            {
                bestMatch = key;
                minLevDist=curLeven;
            }
        }

        return h.getDoctors().get(bestMatch);
    }
}
