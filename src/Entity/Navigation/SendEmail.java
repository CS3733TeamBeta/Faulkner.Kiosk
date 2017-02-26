package Entity.Navigation;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;
import java.util.Properties;

//http://www.codejava.net/java-se/graphics/how-to-capture-screenshot-programmatically-in-java
//use this link AS IS to capture screenshots

public class SendEmail {

    static final String username = "faulknerkioskdirections@gmail.com";
    static final String password = "FaulkPassword";
    String recipient;

    String subject;
    String message;

    int numDirectionFloors;

    boolean includeImage;

    public SendEmail(String recipient, String subject, String message, boolean includeImage, int numDirectionFloors){
        this.recipient = recipient;
        this.subject = subject;
        this.message = message;
        this.includeImage = includeImage;
        this.numDirectionFloors = numDirectionFloors;
        sendEmail();
    }

    public void sendEmail() {
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        // Get a Properties object
        Properties props = System.getProperties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.store.protocol", "pop3");
        props.put("mail.transport.protocol", "smtp");

        try {
            DataSource fds;
            Session session = Session.getDefaultInstance(props,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });

            // -- Create a new message --
            Message msg = new MimeMessage(session);

            MimeMultipart multipart = new MimeMultipart("related");

            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText;

            String imageDirectionsPortion = "";

            for (int i = 1; i <= numDirectionFloors; i++) {
                String tempString = "<img src =\"cid:imageDirections" + i + "\">";
                imageDirectionsPortion = imageDirectionsPortion + tempString;
            }
            int width = 0;
            int height = 0;
            BufferedImage bimg;
            try {
                bimg = ImageIO.read(new File("resources/scaled_falkner_banner.png"));
                width = bimg.getWidth();
                height = bimg.getHeight();
            } catch (Exception e) {

            }


            if (includeImage) {
                htmlText = "<img src=\"cid:imageLogo\" width = \"" + width + "\" height = \"" + height + "\" border=\"0\" /> " + this.message + imageDirectionsPortion;
            } else {
                htmlText = "<img src=\"cid:imageLogo\">" + this.message;
            }

            //htmlText = "<img src=\"googlelogo_color_272x92dp.png\" width=\"272\" height = \"92\" border=\"0\" />" + htmlText;


            messageBodyPart.setContent(htmlText, "text/html");

            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();
            fds = new FileDataSource(
                   "resources/scaled_falkner_banner.png");




            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setHeader("Content-ID", "<imageLogo>");

            multipart.addBodyPart(messageBodyPart);

            if (includeImage) {
                for (int i = 1; i <= numDirectionFloors; i++) {
                    messageBodyPart = new MimeBodyPart();
                    fds = new FileDataSource(
                            "combined" + i + ".png");

                    messageBodyPart.setDataHandler(new DataHandler(fds));
                    messageBodyPart.setHeader("Content-ID", "<imageDirections" + i + ">");

                    multipart.addBodyPart(messageBodyPart);
                }
            }


            msg.setContent(multipart);
            // -- Set the FROM and TO fields --
            msg.setFrom(new InternetAddress(username));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient, false));
            msg.setSubject(subject);
            //msg.setText(message);
            msg.setSentDate(new Date());
            Transport.send(msg);
            System.out.println("Message sent.");
        } catch (MessagingException e) {
            System.out.println("Message failed to send; cause:" + e);
        }
    }
}