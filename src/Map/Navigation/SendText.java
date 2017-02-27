package Map.Navigation;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

/**
 * Created by IanCJ on 2/20/2017.
 */
public class SendText {

    public static final String ACCOUNT_SID = "AC05e287036a90a50b5e3809718b692984";
    public static final String AUTH_TOKEN = "faac7f60b4323c9d486f850ceae38285";

    String address;
    String messageBody;

    SendText(String address, String messageBody) {
        this.address = address;
        this.messageBody = messageBody;
        sendTextTo();

    }

    public void sendTextTo() {

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        //Phone num MUST be of syntax "3107404451"
        Message message = Message.creator(new PhoneNumber("+1" + address),
                new PhoneNumber("+14242958960"),
                messageBody).create();

        System.out.println(message.getSid());

    }
}

