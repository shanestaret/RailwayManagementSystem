package sample;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class SendEmail {
    private static final String u = "railwaysimulationtest@gmail.com";
    private static final String p = "railwayfun";
    public static void send(String userEmail) {
        // Recipient's email ID needs to be mentioned.
        String to = userEmail;

        // Sender's email ID needs to be mentioned
        String from = u;

        // Assuming you are sending email from localhost
        String host = "smtp.gmail.com";

        // Get system properties
        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Get the default Session object.

        //Session session = Session.getDefaultInstance(props);

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(u, p);
                    }
                });

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("*TEST* Registered for Shane's Railway Simulation!");

            // Now set the actual message
            message.setText("You signed up for Shane's Railway Simulation! This was just a project by Shane Staret for his database class and apparently you requested to \"sign up\".\nIF YOU DID NOT SIGN UP FOR THIS SIMULATION, then someone put in the incorrect email. So, ignore this message.\nEven if you put in the right email, this message is pretty pointless. I'm not selling you anything and you're not gaining anything by registering.");

            // Send message
            Transport.send(message);
        } catch (MessagingException mex) {
            AlertBox.display("Email Does Not Exist", 500, 200, "The email the user entered does not exist.");
        }
    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            AlertBox.display("Email Does Not Exist", 500, 200, "The email the user entered does not exist.");
            result = false;
        }
        return result;
    }

    public static boolean isValidRegex(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null) {
            AlertBox.display("Email Does Not Exist", 500, 200, "The email the user entered does not exist.");
            return false;
        }
        return pat.matcher(email).matches();
    }

    public static boolean readDomains(String userEmail) {
        try {
            File file = new File("VerifiedEmailDomains.txt");
            Scanner scanner = new Scanner(file);
            String domain;
            while (scanner.hasNextLine()) {
                domain = scanner.nextLine();
                if(userEmail.contains(domain))
                    return false;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }
}
