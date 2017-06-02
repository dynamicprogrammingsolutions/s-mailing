/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.simplemailing.front;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ferenci84
 */
public class APIControllerTest {
    
    public APIControllerTest() {
    }

    @Test
    public void testProcessBounceMessage() throws Exception {
        
        APIController apiController = new APIController();        
        String[] emails = apiController.getEmailsFromBounceMessage("{\"notificationType\":\"Bounce\",\"bounce\":{\"bounceType\":\"Permanent\",\"bounceSubType\":\"Suppressed\",\"bouncedRecipients\":[{\"emailAddress\":\"hussain80@live.con\",\"action\":\"failed\",\"status\":\"5.1.1\",\"diagnosticCode\":\"Amazon SES has suppressed sending to this address because it has a recent history of bouncing as an invalid address. For more information about how to remove an address from the suppression list, see the Amazon SES Developer Guide: http://docs.aws.amazon.com/ses/latest/DeveloperGuide/remove-from-suppressionlist.html \"}],\"timestamp\":\"2017-05-31T17:34:33.248Z\",\"feedbackId\":\"0101015c5f92c4ee-6899d408-4627-11e7-a42c-ebe81263a55e-000000\",\"reportingMTA\":\"dns; amazonses.com\"},\"mail\":{\"timestamp\":\"2017-05-31T17:34:27.000Z\",\"source\":\"ferenci84@metatraderprogrammer.com\",\"sourceArn\":\"arn:aws:ses:us-west-2:874087778966:identity/ferenci84@metatraderprogrammer.com\",\"sourceIp\":\"94.21.126.194\",\"sendingAccountId\":\"874087778966\",\"messageId\":\"0101015c5f92baf0-bb4f10c9-ef80-4471-bf87-1dca27ee4650-000000\",\"destination\":[\"hussain80@live.con\"]}}");        
        assertEquals(emails[0],"hussain80@live.con");
    }
    
}
