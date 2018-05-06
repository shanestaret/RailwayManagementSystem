package sample;

import java.sql.*;
import com.sun.mail.smtp.SMTPAddressFailedException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.*;

import javax.mail.SendFailedException;
import javax.mail.internet.AddressException;
import java.util.regex.Pattern;

public class Main extends Application {
    Stage mainWindow, adminAuthorizationWindow; //the literal frame that pops up
    Scene loginUI, adminUI, custUI, createAccountUI, adminAuthorizationUI; //the different screens that we can get to within our "Stage" or frame
    Button loginButton, signOutButton, signOutButton2, createAccountLoginButton, createAccountCreateButton, createAccountCancelButton, adminAuthorizationAuthorizeButton, adminAuthorizationCancelButton, adminSaveChangesButton, custSaveChangesButton; //button that user can interact with
    Label loginDirections, createAccountDirections1, createAccountDirections2, createAccountEmailWarning1, createAccountEmailWarning2, personalInfo, accountInfo, adminDirections1, adminDirections2, adminAuthorizationDirections, adminDeleteDirections1, adminDeleteDirections2, adminDeleteDirections3, adminDeleteDirections4, adminDeleteDirections5, adminDeleteDirections6, custDirections; //String that will tell user how to login
    TextField username, createAccountName, createAccountEmail, createAccountUsername, adminUpdateCheckCustID, adminCheckTrackID, adminUpdateCheckTrainID, adminUpdateTrackID, adminUpdateSchedID, adminUpdateCheckTrainStationID, adminUpdateCheckTicketID, adminUpdateCheckTrackID, adminCheckSchedID, adminUpdateCheckSchedID, adminCreateCustName, adminCreateTrainName, adminCheckTrainName, adminCreateTrainStationName, adminCreateModel, adminCreateNumOfSeats, adminCreateLocation, adminCreateSchedIn, adminCreateSchedOut, adminCreateDate, adminCreateStationFrom, adminCreateStationTo, adminCreateLength,  adminCreateCustSeat, adminCreatePrice, adminCreateEmail, adminCreateUsername, adminUpdateCustName, adminUpdateEmail, adminUpdateUsername, adminUpdatePassword, adminUpdateConfirmPassword, adminUpdateTrainName, adminUpdateCheckTrainName,adminUpdateTrainStationName, adminUpdateModel, adminUpdatePrice, adminUpdateDate, adminUpdateSchedIn, adminUpdateSchedOut, adminUpdateNumOfSeats, adminUpdateLocation, adminUpdateStationFrom, adminUpdateStationTo, adminUpdateLength, adminUpdateCustSeat, adminDeleteCustID, adminDeleteTrainID, adminDeleteTrainStationID, adminDeleteSchedID, adminDeleteTrackID, adminDeleteTicketID, custTicketID; //Where user can input information
    PasswordField password, createAccountPassword, createAccountConfirmPassword, adminUsername, adminPassword, adminCreatePassword, adminCreateConfirmPassword; //Where user password's will be entered
    CheckBox rememberUsernameBox; //Box user can check if it wants application to remember their username after signing out
    ChoiceBox<String> accountTypeBox, adminManipulateDropdownBox, adminElementDropdownBox, custDropdownBox; //Dropdown menus
    Separator horizontalSeparator1, horizontalSeparator2, horizontalSeparator3, horizontalSeparator4, horizontalSeparator5, horizontalSeparator6, horizontalSeparator7; //Horizontal Separators used to separate information in the window more clearly
    Image mainWindowIcon, adminAuthorizationWindowIcon; //icon for window
    String emailAddress; //email address user gives us
    boolean result, emailExists, invalidDomain;

    @Override
    //allows GUI to load
    public void start(Stage primaryStage) throws Exception{
        mainWindow = primaryStage;

        //putting train icon in top left of window
        mainWindowIcon = new Image(getClass().getResourceAsStream("ApplicationIcon.png"));
        mainWindow.getIcons().add(mainWindowIcon);

        adminAuthorizationWindow = new Stage();

        //creating object that holds dimensions of user's screen
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();

        //giving title to our frame
        mainWindow.setTitle("Railway System Simulation: Login");

        //label for login directions
        loginDirections = new Label("Welcome to Shane's Railway System Simulation! To Login, enter your username and password.");

        //label for create account
        createAccountDirections1 = new Label("To create an account, select the account type in the dropdown menu below and enter your information.");
        createAccountDirections2 = new Label("NOTE: If you are creating an admin account, another admin must put in their information to confirm the new admin account.");
        createAccountEmailWarning1 = new Label("IMPORTANT: Entering your email address will result in an email being sent to that address stating you registered");
        createAccountEmailWarning2 = new Label("for this simulation. You may only use your email address for one account.");
        personalInfo = new Label("Personal Information");
        accountInfo = new Label("Account Information");

        //label for admin view
        adminDirections1 = new Label("As an administrator, you have the privileges of being able to create, update, and/or delete multiple elements from the Railway System.");
        adminDirections2 = new Label("In order to do so, select how you want to manipulate an element and the specific element you want to manipulate in the dropdown boxes below.");
        adminDeleteDirections1 = new Label("IMPORTANT: Deleting a customer PERMANENTLY deletes them from the system. Please be cautious.");
        adminDeleteDirections2 = new Label("IMPORTANT: Deleting a train PERMANENTLY deletes it from the system. Please be cautious.");
        adminDeleteDirections3 = new Label("IMPORTANT: Deleting a train station PERMANENTLY deletes it from the system. Please be cautious.");
        adminDeleteDirections4 = new Label("IMPORTANT: Deleting a schedule entry PERMANENTLY deletes it from the system. Please be cautious.");
        adminDeleteDirections5 = new Label("IMPORTANT: Deleting a track PERMANENTLY deletes it from the system. Please be cautious.");
        adminDeleteDirections6 = new Label("IMPORTANT: Deleting a ticket PERMANENTLY deletes it from the system. Please be cautious.");

        //label for customer view
        custDirections = new Label("As a customer, you can purchase listed tickets or cancel a previously purchased ticket. Select what you want to do in the dropdown box below.");


        //label for admin account authorization directions
        adminAuthorizationDirections = new Label("To create an admin account, enter a pre-existing admin username and password.");

        //checkbox that user can click if they want their username remembered
        rememberUsernameBox = new CheckBox("Remember me");

        //dropdown menu for selecting account type when creating account
        accountTypeBox = new ChoiceBox<>();
        accountTypeBox.getItems().addAll("Administrator", "Customer");
        accountTypeBox.setTooltip(new Tooltip("Select Account Type"));
        accountTypeBox.getSelectionModel().select(0);


        //dropdown menu for selecting what you want to do as an admin user
        adminManipulateDropdownBox = new ChoiceBox<>();
        adminManipulateDropdownBox.setPrefWidth(110);
        adminManipulateDropdownBox.getItems().addAll("Create", "Update", "Delete");
        adminManipulateDropdownBox.setTooltip(new Tooltip("Select whether you want to create, update, or delete certain elements."));
        adminManipulateDropdownBox.getSelectionModel().select(0);

        //dropdown menu for selecting what element you want to manipulate as an admin user
        adminElementDropdownBox = new ChoiceBox<>();
        adminElementDropdownBox.setPrefWidth(110);
        adminElementDropdownBox.getItems().addAll("Customer", "Train", "Train Station", "Track", "Schedule Entry", "Ticket");
        adminElementDropdownBox.setTooltip(new Tooltip("Select what element you want to manipulate."));
        adminElementDropdownBox.getSelectionModel().select(0);

        //dropdown menu for selecting whether you want to purchase or cancel a ticket as a customer user
        custDropdownBox = new ChoiceBox<>();
        custDropdownBox.setPrefWidth(100);
        custDropdownBox.getItems().addAll("Purchase", "Cancel");
        custDropdownBox.setTooltip(new Tooltip("Select whether you want to purchase or cancel a ticket."));
        custDropdownBox.getSelectionModel().select(0);

        //initializing horizontal separators
        horizontalSeparator1 = new Separator();
        horizontalSeparator2 = new Separator();
        horizontalSeparator3 = new Separator();
        horizontalSeparator4 = new Separator();
        horizontalSeparator5 = new Separator();
        horizontalSeparator6 = new Separator();
        horizontalSeparator7 = new Separator();

        //TextFields that hold user information on login screen
        username = new TextField();
        username.setPrefWidth(300);
        username.setPromptText("Username");

        password = new PasswordField();
        password.setPrefWidth(300);
        password.setPromptText("Password");

        //TextFields that hold personal and user information on create account screen
        createAccountName = new TextField();
        createAccountName.setMaxWidth(300);
        createAccountName.setPromptText("Full Legal Name");
        createAccountName.setTooltip(new Tooltip("Enter full legal name"));

        createAccountEmail = new TextField();
        createAccountEmail.setMaxWidth(300);
        createAccountEmail.setPromptText("Email");
        createAccountEmail.setTooltip(new Tooltip("Enter email address"));

        createAccountUsername = new TextField();
        createAccountUsername.setMaxWidth(300);
        createAccountUsername.setPromptText("Username");
        createAccountUsername.setTooltip(new Tooltip("Enter desired username"));

        createAccountPassword = new PasswordField();
        createAccountPassword.setMaxWidth(300);
        createAccountPassword.setPromptText("Password");
        createAccountPassword.setTooltip(new Tooltip("Enter desired password"));

        createAccountConfirmPassword = new PasswordField();
        createAccountConfirmPassword.setMaxWidth(300);
        createAccountConfirmPassword.setPromptText("Confirm Password");
        createAccountConfirmPassword.setTooltip(new Tooltip("Confirm password"));

        //TextFields that can appear on admin view
        adminCheckTrackID = new TextField();
        adminCheckTrackID.setMaxWidth(300);
        adminCheckTrackID.setPromptText("Pre-Existing Track ID Number");
        adminCheckTrackID.setTooltip(new Tooltip("Enter Pre-Existing Track ID Number"));

        adminUpdateTrackID = new TextField();
        adminUpdateTrackID.setMaxWidth(300);
        adminUpdateTrackID.setPromptText("Enter Pre-Existing Track ID Number");
        adminUpdateTrackID.setTooltip(new Tooltip("Enter Pre-Existing Track ID Number"));

        adminUpdateSchedID = new TextField();
        adminUpdateSchedID.setMaxWidth(300);
        adminUpdateSchedID.setPromptText("Enter Pre-Existing Schedule ID Number");
        adminUpdateSchedID.setTooltip(new Tooltip("Enter Pre-Existing Schedule ID Number"));

        adminUpdateCheckTrackID = new TextField();
        adminUpdateCheckTrackID.setMaxWidth(300);
        adminUpdateCheckTrackID.setPromptText("Update Track ID Number");
        adminUpdateCheckTrackID.setTooltip(new Tooltip("Update Track ID Number"));

        adminUpdateCheckTrainStationID = new TextField();
        adminUpdateCheckTrainStationID.setMaxWidth(300);
        adminUpdateCheckTrainStationID.setPromptText("Update Train Station ID Number");
        adminUpdateCheckTrainStationID.setTooltip(new Tooltip("Update Train Station ID Number"));

        adminUpdateCheckTicketID = new TextField();
        adminUpdateCheckTicketID.setMaxWidth(300);
        adminUpdateCheckTicketID.setPromptText("Enter Pre-Existing Ticket ID Number");
        adminUpdateCheckTicketID.setTooltip(new Tooltip("Enter Pre-Existing Ticket ID Number"));

        adminUpdateCheckCustID = new TextField();
        adminUpdateCheckCustID.setMaxWidth(300);
        adminUpdateCheckCustID.setPromptText("Pre-Existing Customer ID Number");
        adminUpdateCheckCustID.setTooltip(new Tooltip("Enter Pre-Existing Customer ID Number"));

        adminCheckSchedID = new TextField();
        adminCheckSchedID.setMaxWidth(300);
        adminCheckSchedID.setPromptText("Pre-Existing Schedule Entry ID Number");
        adminCheckSchedID.setTooltip(new Tooltip("Enter Pre-Existing Schedule Entry ID Number"));

        adminUpdateCheckTrainID = new TextField();
        adminUpdateCheckTrainID.setMaxWidth(300);
        adminUpdateCheckTrainID.setPromptText("Pre-Existing Train ID Number");
        adminUpdateCheckTrainID.setTooltip(new Tooltip("Enter Pre-Existing Train ID Number"));

        adminUpdateCheckSchedID = new TextField();
        adminUpdateCheckSchedID.setMaxWidth(300);
        adminUpdateCheckSchedID.setPromptText("Update Pre-Existing Schedule Entry ID Number");
        adminUpdateCheckSchedID.setTooltip(new Tooltip("Update Pre-Existing Schedule Entry ID Number"));

        adminCreateCustName = new TextField();
        adminCreateCustName.setMaxWidth(300);
        adminCreateCustName.setPromptText("Customer Name");
        adminCreateCustName.setTooltip(new Tooltip("Enter Customer Name"));

        adminUpdateCustName = new TextField();
        adminUpdateCustName.setMaxWidth(300);
        adminUpdateCustName.setPromptText("Update Customer Name");
        adminUpdateCustName.setTooltip(new Tooltip("Update Customer Name"));

        adminCreateTrainName = new TextField();
        adminCreateTrainName.setMaxWidth(300);
        adminCreateTrainName.setPromptText("Train Name");
        adminCreateTrainName.setTooltip(new Tooltip("Enter Train Name"));

        adminUpdateTrainName = new TextField();
        adminUpdateTrainName.setMaxWidth(300);
        adminUpdateTrainName.setPromptText("Update Train Name");
        adminUpdateTrainName.setTooltip(new Tooltip("Update Train Name"));

        adminCheckTrainName = new TextField();
        adminCheckTrainName.setMaxWidth(300);
        adminCheckTrainName.setPromptText("Pre-Existing Train ID Number");
        adminCheckTrainName.setTooltip(new Tooltip("Enter Pre-Existing Train ID"));

        adminUpdateCheckTrainName = new TextField();
        adminUpdateCheckTrainName.setMaxWidth(300);
        adminUpdateCheckTrainName.setPromptText("Update Pre-Existing Train Name");
        adminUpdateCheckTrainName.setTooltip(new Tooltip("Update Pre-Existing Train Name"));

        adminCreateTrainStationName = new TextField();
        adminCreateTrainStationName.setMaxWidth(300);
        adminCreateTrainStationName.setPromptText("Train Station Name");
        adminCreateTrainStationName.setTooltip(new Tooltip("Enter Train Station Name"));

        adminUpdateTrainStationName = new TextField();
        adminUpdateTrainStationName.setMaxWidth(300);
        adminUpdateTrainStationName.setPromptText("Update Train Station Name");
        adminUpdateTrainStationName.setTooltip(new Tooltip("Update Train Station Name"));

        adminCreateModel = new TextField();
        adminCreateModel.setMaxWidth(300);
        adminCreateModel.setPromptText("Train Model");
        adminCreateModel.setTooltip(new Tooltip("Enter Train Model"));

        adminUpdateModel = new TextField();
        adminUpdateModel.setMaxWidth(300);
        adminUpdateModel.setPromptText("Update Train Model");
        adminUpdateModel.setTooltip(new Tooltip("Update Train Model"));

        adminCreateNumOfSeats = new TextField();
        adminCreateNumOfSeats.setMaxWidth(300);
        adminCreateNumOfSeats.setPromptText("# of Seats");
        adminCreateNumOfSeats.setTooltip(new Tooltip("Enter Capacity of Train"));

        adminUpdateNumOfSeats = new TextField();
        adminUpdateNumOfSeats.setMaxWidth(300);
        adminUpdateNumOfSeats.setPromptText("Update # of Seats");
        adminUpdateNumOfSeats.setTooltip(new Tooltip("Update Capacity of Train"));

        adminCreateLocation = new TextField();
        adminCreateLocation.setMaxWidth(300);
        adminCreateLocation.setPromptText("Location");
        adminCreateLocation.setTooltip(new Tooltip("Enter Location of Train Station"));

        adminUpdateLocation = new TextField();
        adminUpdateLocation.setMaxWidth(300);
        adminUpdateLocation.setPromptText("Update Location");
        adminUpdateLocation.setTooltip(new Tooltip("Update Location of Train Station"));

        adminCreateSchedOut = new TextField();
        adminCreateSchedOut.setMaxWidth(300);
        adminCreateSchedOut.setPromptText("Time of Departure (HH:MM format)");
        adminCreateSchedOut.setTooltip(new Tooltip("Enter Time of Departure"));

        adminUpdateSchedOut = new TextField();
        adminUpdateSchedOut.setMaxWidth(300);
        adminUpdateSchedOut.setPromptText("Update Time of Departure (HH:MM format)");
        adminUpdateSchedOut.setTooltip(new Tooltip("Update Time of Departure"));

        adminCreateSchedIn = new TextField();
        adminCreateSchedIn.setMaxWidth(300);
        adminCreateSchedIn.setPromptText("Time of Arrival (HH:MM format)");
        adminCreateSchedIn.setTooltip(new Tooltip("Enter Time of Arrival"));

        adminUpdateSchedIn = new TextField();
        adminUpdateSchedIn.setMaxWidth(300);
        adminUpdateSchedIn.setPromptText("Update Time of Arrival (HH:MM format)");
        adminUpdateSchedIn.setTooltip(new Tooltip("Update Time of Arrival"));

        adminCreateDate = new TextField();
        adminCreateDate.setMaxWidth(300);
        adminCreateDate.setPromptText("Date (YYYY-MM-DD format)");
        adminCreateDate.setTooltip(new Tooltip("Enter Date of Event"));

        adminUpdateDate = new TextField();
        adminUpdateDate.setMaxWidth(300);
        adminUpdateDate.setPromptText("Update Date (YYYY-MM-DD format)");
        adminUpdateDate.setTooltip(new Tooltip("Update Date of Event"));

        adminCreateStationFrom = new TextField();
        adminCreateStationFrom.setMaxWidth(300);
        adminCreateStationFrom.setPromptText("Station From");
        adminCreateStationFrom.setTooltip(new Tooltip("Enter Station the Train is Leaving From"));

        adminUpdateStationFrom = new TextField();
        adminUpdateStationFrom.setMaxWidth(300);
        adminUpdateStationFrom.setPromptText("Update Station From");
        adminUpdateStationFrom.setTooltip(new Tooltip("Update Station the Train is Leaving From"));

        adminCreateStationTo = new TextField();
        adminCreateStationTo.setMaxWidth(300);
        adminCreateStationTo.setPromptText("Station To");
        adminCreateStationTo.setTooltip(new Tooltip("Enter Station the Train is Going to"));

        adminUpdateStationTo = new TextField();
        adminUpdateStationTo.setMaxWidth(300);
        adminUpdateStationTo.setPromptText("Update Station To");
        adminUpdateStationTo.setTooltip(new Tooltip("Update Station the Train is Going to"));

        adminCreateLength = new TextField();
        adminCreateLength.setMaxWidth(300);
        adminCreateLength.setPromptText("Length of Track (miles)");
        adminCreateLength.setTooltip(new Tooltip("Enter Length of Track in Miles"));

        adminUpdateLength = new TextField();
        adminUpdateLength.setMaxWidth(300);
        adminUpdateLength.setPromptText("Update Length of Track (miles)");
        adminUpdateLength.setTooltip(new Tooltip("Update Length of Track in Miles"));

        adminCreateCustSeat = new TextField();
        adminCreateCustSeat.setMaxWidth(300);
        adminCreateCustSeat.setPromptText("Customer Seat #");
        adminCreateCustSeat.setTooltip(new Tooltip("Enter the Seat of the Customer"));

        adminUpdateCustSeat = new TextField();
        adminUpdateCustSeat.setMaxWidth(300);
        adminUpdateCustSeat.setPromptText("Update Customer Seat #");
        adminUpdateCustSeat.setTooltip(new Tooltip("Update the Seat of the Customer"));

        adminCreatePrice = new TextField();
        adminCreatePrice.setMaxWidth(300);
        adminCreatePrice.setPromptText("Price of Ticket (Two Digits After Decimal)");
        adminCreatePrice.setTooltip(new Tooltip("Enter the Price of the Ticket"));

        adminUpdatePrice = new TextField();
        adminUpdatePrice.setMaxWidth(300);
        adminUpdatePrice.setPromptText("Update Price of Ticket (Two Digits After Decimal)");
        adminUpdatePrice.setTooltip(new Tooltip("Update the Price of the Ticket"));

        adminCreateEmail = new TextField();
        adminCreateEmail.setMaxWidth(300);
        adminCreateEmail.setPromptText("Email");
        adminCreateEmail.setTooltip(new Tooltip("Enter Email of the Customer"));

        adminUpdateEmail = new TextField();
        adminUpdateEmail.setMaxWidth(300);
        adminUpdateEmail.setPromptText("Update Email");
        adminUpdateEmail.setTooltip(new Tooltip("Update Email of the Customer"));

        adminCreateUsername = new TextField();
        adminCreateUsername.setMaxWidth(300);
        adminCreateUsername.setPromptText("Username");
        adminCreateUsername.setTooltip(new Tooltip("Enter Username of Customer"));

        adminUpdateUsername = new TextField();
        adminUpdateUsername.setMaxWidth(300);
        adminUpdateUsername.setPromptText("Update Username");
        adminUpdateUsername.setTooltip(new Tooltip("Update Username of Customer"));

        adminDeleteCustID = new TextField();
        adminDeleteCustID.setMaxWidth(300);
        adminDeleteCustID.setPromptText("Delete Customer ID");
        adminDeleteCustID.setTooltip(new Tooltip("Enter a customer ID to delete"));

        adminDeleteTrainID = new TextField();
        adminDeleteTrainID.setMaxWidth(300);
        adminDeleteTrainID.setPromptText("Delete Train ID");
        adminDeleteTrainID.setTooltip(new Tooltip("Enter a train ID to delete"));

        adminDeleteTrainStationID = new TextField();
        adminDeleteTrainStationID.setMaxWidth(300);
        adminDeleteTrainStationID.setPromptText("Delete Train Station ID");
        adminDeleteTrainStationID.setTooltip(new Tooltip("Enter a train station ID to delete"));

        adminDeleteSchedID = new TextField();
        adminDeleteSchedID.setMaxWidth(300);
        adminDeleteSchedID.setPromptText("Delete Schedule Entry ID");
        adminDeleteSchedID.setTooltip(new Tooltip("Enter a schedule entry ID to delete"));

        adminDeleteTrackID = new TextField();
        adminDeleteTrackID.setMaxWidth(300);
        adminDeleteTrackID.setPromptText("Delete Track ID");
        adminDeleteTrackID.setTooltip(new Tooltip("Enter a track ID to delete"));

        adminDeleteTicketID = new TextField();
        adminDeleteTicketID.setMaxWidth(300);
        adminDeleteTicketID.setPromptText("Delete Ticket ID");
        adminDeleteTicketID.setTooltip(new Tooltip("Enter a ticket ID to delete"));

        //PasswordFields used in admin view
        adminCreatePassword = new PasswordField();
        adminCreatePassword.setMaxWidth(300);
        adminCreatePassword.setPromptText("Password");
        adminCreatePassword.setTooltip(new Tooltip("Enter Password of Customer"));

        adminCreateConfirmPassword = new PasswordField();
        adminCreateConfirmPassword.setMaxWidth(300);
        adminCreateConfirmPassword.setPromptText("Confirm Password");
        adminCreateConfirmPassword.setTooltip(new Tooltip("Enter Password of Customer Again"));

        adminUpdatePassword = new PasswordField();
        adminUpdatePassword.setMaxWidth(300);
        adminUpdatePassword.setPromptText("Update Password");
        adminUpdatePassword.setTooltip(new Tooltip("Enter New Password of Customer"));

        adminUpdateConfirmPassword = new PasswordField();
        adminUpdateConfirmPassword.setMaxWidth(300);
        adminUpdateConfirmPassword.setPromptText("Confirm New Password");
        adminUpdateConfirmPassword.setTooltip(new Tooltip("Enter New Password of Customer Again"));

        //TextField used for purchasing/cancelling a customer ticket
        custTicketID = new TextField();
        custTicketID.setMaxWidth(300);
        custTicketID.setPromptText("Enter Ticket ID");
        custTicketID.setTooltip(new Tooltip("Enter the ticket ID of the ticket you want to purchase"));

        //PasswordFields used to enter pre-existing admin info so new admin user can be created
        adminUsername = new PasswordField();
        adminUsername.setMaxWidth(300);
        adminUsername.setPromptText("Username");
        adminUsername.setTooltip(new Tooltip("Enter pre-existing admin username"));

        adminPassword = new PasswordField();
        adminPassword.setMaxWidth(300);
        adminPassword.setPromptText("Password");
        adminPassword.setTooltip(new Tooltip("Enter pre-existing admin password"));

        //creating new button with text called "Login"; this button will log a user into the system if they are actually a user
        loginButton = new Button("Login");
        loginButton.setPrefWidth(100);
        loginButton.setOnAction(e -> {
            if(username.getText().equalsIgnoreCase("admin") && password.getText().equals("password!") && !rememberUsernameBox.isSelected()) {
                username.clear();
                password.clear();
                mainWindow.setScene(adminUI);
                mainWindow.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
                mainWindow.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
                mainWindow.setTitle("Railway System Simulation: Admin View");
                return;
            }
            else if(username.getText().equalsIgnoreCase("admin") && password.getText().equals("password!") && rememberUsernameBox.isSelected()) {
                password.clear();
                mainWindow.setScene(adminUI);
                mainWindow.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
                mainWindow.setY(((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2));
                mainWindow.setTitle("Railway System Simulation: Admin View");
                return;
            }
            else if(username.getText().equalsIgnoreCase("cust") && password.getText().equals("pizza") && !rememberUsernameBox.isSelected()) {
                username.clear();
                password.clear();
                mainWindow.setScene(custUI);
                mainWindow.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
                mainWindow.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
                mainWindow.setTitle("Railway System Simulation: Admin View");
                return;
            }
            else if(username.getText().equalsIgnoreCase("cust") && password.getText().equals("pizza") && rememberUsernameBox.isSelected()) {
                password.clear();
                mainWindow.setScene(custUI);
                mainWindow.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
                mainWindow.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
                mainWindow.setTitle("Railway System Simulation: Admin View");
                return;
            }
            else {
                AlertBox.display("Account Authentication Error", 500, 200, "Username or Password was incorrect. Try again.");
                password.clear();
            }

        });

        //creating a button with text called "Sign out"; will prompt user if they actually want to sign out, then will if they hit "Yes"
        signOutButton = new Button("Sign Out");
        signOutButton.setPrefWidth(100);
        signOutButton.setOnAction(e -> {
            result = ConfirmBox.display("Confirming Sign Out Request", 500, 200, "Are you sure you want to sign out of your account?");
            //if the user said "yes", then initiate popup dialogue saying logout was successful
            if(result) {
                mainWindow.setTitle("Railway System Simulation: Login");
                mainWindow.setScene(loginUI);
                mainWindow.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
                mainWindow.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
                AlertBox.display("Sign Out Successful", 500, 200, "You have successfully signed out!");
            }
        });

        //creating a button with text called "Sign out"; will prompt user if they actually want to sign out, then will if they hit "Yes"
        signOutButton2 = new Button("Sign Out");
        signOutButton.setPrefWidth(100);
        signOutButton2.setOnAction(e -> {
            result = ConfirmBox.display("Confirming Sign Out Request", 500, 200, "Are you sure you want to sign out of your account?");
            //if the user said "yes", then initiate popup dialogue saying logout was successful
            if(result) {
                mainWindow.setTitle("Railway System Simulation: Login");
                mainWindow.setScene(loginUI);
                mainWindow.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
                mainWindow.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
                AlertBox.display("Sign Out Successful", 500, 200, "You have successfully signed out!");
            }
        });

        //creating a button with text called "Create Account"; will prompt user to enter in an email, username, and password so that they can get an account
        createAccountLoginButton = new Button("Create Account");
        createAccountLoginButton.setOnAction(e -> {
            if(!rememberUsernameBox.isSelected())
                username.clear();
            password.clear();
            createAccountName.clear();
            createAccountEmail.clear();
            createAccountUsername.clear();
            createAccountPassword.clear();
            createAccountConfirmPassword.clear();
            mainWindow.setScene(createAccountUI);
            mainWindow.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
            mainWindow.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
            mainWindow.setTitle("Railway System Simulation: Create Account");
        });

        //creating a button with text called "Create"; will actually create a user account
        createAccountCreateButton = new Button("Create");
        createAccountCreateButton.setOnAction(e -> {
            invalidDomain = SendEmail.readDomains(createAccountEmail.getText());

            if(createAccountName.getText().equals("")) {
                AlertBox.display("Name Invalid", 500, 200, "You did not enter a proper name.");
            }
            else if(createAccountEmail.getText().equals("") || invalidDomain) {
                AlertBox.display("Email Invalid", 500, 200, "You did not enter a proper email address.");
            }
            else if(createAccountUsername.getText().equals("")) {
                AlertBox.display("Username Invalid", 500, 200, "You did not enter a proper username.");
            }
            else if(createAccountPassword.getText().equals("")) {
                AlertBox.display("Password Invalid", 500, 200, "You did not enter a proper password.");
            }
            else if(!createAccountPassword.getText().equals(createAccountConfirmPassword.getText())) {
                AlertBox.display("Password Confirmation Error", 500, 200, "Password and Confirmation do not match.");
            }
            else {
                emailAddress = createAccountEmail.getText();
                if(SendEmail.isValidEmailAddress(emailAddress) && SendEmail.isValidRegex(emailAddress)) {
                    if (accountTypeBox.getValue().equals("Administrator")) {
                        adminUsername.clear();
                        adminPassword.clear();
                        adminAuthorizationWindow = new Stage();
                        adminAuthorizationWindowIcon = new Image(ConfirmBox.class.getResourceAsStream("ConfirmBoxIcon.png"));
                        adminAuthorizationWindow.getIcons().add(adminAuthorizationWindowIcon);
                        adminAuthorizationWindow.initModality(Modality.APPLICATION_MODAL);
                        adminAuthorizationWindow.setTitle("Pre-Existing Administrator Information");
                        adminAuthorizationWindow.setHeight(500);
                        adminAuthorizationWindow.setWidth(200);
                        adminAuthorizationWindow.setScene(adminAuthorizationUI);
                        adminAuthorizationWindow.sizeToScene();
                        adminAuthorizationWindow.setResizable(false);
                        adminAuthorizationWindow.setX(primScreenBounds.getWidth() - (primScreenBounds.getWidth() / 1.7));
                        adminAuthorizationWindow.setY(primScreenBounds.getHeight() - (primScreenBounds.getHeight() / 1.75));
                        adminAuthorizationWindow.showAndWait();
                        return;
                    } else {
                        SendEmail.send(emailAddress);
                        SQL.sendToDatabase("insert into CUSTOMER (NAME, EMAIL, USERNAME, PASSWORD) values('" + adminCreateCustName.getText() + "','" + adminCreateEmail.getText() + "','" + adminCreateUsername.getText() + "','" + adminCreatePassword.getText() + "');");
                        adminAuthorizationWindow.close();
                        mainWindow.setScene(loginUI);
                        mainWindow.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
                        mainWindow.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
                        mainWindow.setTitle("Railway System Simulation: Login");
                        AlertBox.display("Create Account Successful", 500, 200, "Successfully created new account! An email has been sent to your email address.");
                    }
                }
            }
        });

        //creating a button with text called "Cancel"; will stop user from creating account and bring them back to login screen
        createAccountCancelButton = new Button("Cancel");
        createAccountCancelButton.setOnAction(e -> {
            result = ConfirmBox.display("Confirming Cancellation of Account Creation", 500, 200, "Are you sure you want to cancel creating an account?");
            if(result) {
                mainWindow.setScene(loginUI);
                mainWindow.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
                mainWindow.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
                mainWindow.setTitle("Railway System Simulation: Login");
            }
        });

        //creating a new button called "Save Changes"; when clicked, it will save the changes an admin made to an element (creating, updating, or deleting)
        adminSaveChangesButton = new Button("Save Changes");
        adminSaveChangesButton.setPrefWidth(100); //sets width to 100
        adminSaveChangesButton.setOnAction(e -> {
            result = ConfirmBox.display("Confirm Changes", 500, 200, "Are you sure you want to make this change?");
            if(result) {
                if(adminManipulateDropdownBox.getValue().equals("Create")) {
                    if (adminElementDropdownBox.getValue().equals("Customer")) {
                        invalidDomain = SendEmail.readDomains(adminCreateEmail.getText());
                        if (adminCreateEmail.getText().equals("") || invalidDomain) {
                            AlertBox.display("Email Invalid", 500, 200, "You did not enter a proper email address.");
                            return;
                        } else if (adminCreateCustName.getText().equals("")) {
                            AlertBox.display("Name Invalid", 500, 200, "You did not enter a proper name.");
                            return;
                        } else if (adminCreateUsername.getText().equals("")) {
                            AlertBox.display("Username Invalid", 500, 200, "You did not enter a proper username.");
                            return;
                        } else if (adminCreatePassword.getText().equals("")) {
                            AlertBox.display("Password Invalid", 500, 200, "You did not enter a proper password.");
                            return;
                        } else if (!adminCreatePassword.getText().equals(adminCreateConfirmPassword.getText())) {
                            AlertBox.display("Password Confirmation Error", 500, 200, "Password and Confirmation do not match.");
                            return;
                        } else {
                            emailAddress = adminCreateEmail.getText();
                            if (SendEmail.isValidEmailAddress(emailAddress) && SendEmail.isValidRegex(emailAddress)) {
                                SendEmail.send(emailAddress);
                                SQL.sendToDatabase("insert into CUSTOMER (NAME, EMAIL, USERNAME, PASSWORD) values('" + adminCreateCustName.getText() + "','" + adminCreateEmail.getText() + "','" + adminCreateUsername.getText() + "','" + adminCreatePassword.getText() + "');");
                                adminCreateCustName.clear();
                                adminCreateEmail.clear();
                                adminCreateUsername.clear();
                                adminCreatePassword.clear();
                                adminCreateConfirmPassword.clear();
                                AlertBox.display("Create Account Successful", 500, 200, "Successfully created new account! An email has been sent to the customer's email address.");

                            }
                        }
                    } else if (adminElementDropdownBox.getValue().equals("Train")) {
                        if (adminCreateTrainName.getText().equals("")) {
                            AlertBox.display("Name Invalid", 500, 200, "You did not enter a proper name.");
                            return;
                        } else if (adminCreateModel.getText().equals("")) {
                            AlertBox.display("Model Invalid", 500, 200, "You did not enter a proper train model.");
                            return;
                        } else if (!(Pattern.matches("[0-9]+", adminCreateNumOfSeats.getText())) || Integer.parseInt(adminCreateNumOfSeats.getText()) < 1) {
                            AlertBox.display("Number of Seats Invalid", 500, 200, "You did not enter a proper number of seats");
                            return;
                        } else {
                            SQL.sendToDatabase("insert into TRAIN (NAME, MODEL, NUM_OF_SEATS) values('" + adminCreateTrainName.getText() + "','" + adminCreateModel.getText() + "','" + adminCreateNumOfSeats.getText() + "');");
                            adminCreateTrainName.clear();
                            adminCreateModel.clear();
                            adminCreateNumOfSeats.clear();
                            AlertBox.display("Create Train Successful", 500, 200, "Successfully created new train!");
                        }
                    } else if (adminElementDropdownBox.getValue().equals("Train Station")) {
                        if (adminCreateTrainStationName.getText().equals("")) {
                            AlertBox.display("Name Invalid", 500, 200, "You did not enter a proper name.");
                            return;
                        } else if (adminCreateLocation.getText().equals("")) {
                            AlertBox.display("Location Invalid", 500, 200, "You did not enter a proper location.");
                            return;
                        } else {
                            SQL.sendToDatabase("insert into TRAIN_STATION (NAME, LOCATION) values('" + adminCreateTrainStationName.getText() + "','" + adminCreateLocation.getText() + "');");
                            adminCreateTrainStationName.clear();
                            adminCreateLocation.clear();
                            AlertBox.display("Create Train Station Successful", 500, 200, "Successfully created a new train station!");
                        }
                    } else if (adminElementDropdownBox.getValue().equals("Schedule Entry")) {
                        if (adminCheckTrainName.getText().equals("")) {
                            AlertBox.display("Train ID Invalid", 500, 200, "You did not enter a train ID that exists.");
                            return;
                        } else if (adminCheckTrackID.getText().equals("")) {
                            AlertBox.display("Track ID Invalid", 500, 200, "You did not enter a track ID that exists.");
                            return;
                        } else if (adminCreateSchedOut.getText().equals("") || adminCreateSchedOut.getText().length() != 5 || !(Pattern.matches("[0-9]+", adminCreateSchedOut.getText().substring(0, 2))) || !(Pattern.matches("[0-9]+", adminCreateSchedOut.getText().substring(3, 5))) || !adminCreateSchedOut.getText().substring(2, 3).equals(":") || Integer.parseInt(adminCreateSchedOut.getText().substring(0, 2)) < 1 || Integer.parseInt(adminCreateSchedOut.getText().substring(0, 2)) > 23 || Integer.parseInt(adminCreateSchedOut.getText().substring(3, 5)) < 0 || Integer.parseInt(adminCreateSchedOut.getText().substring(3, 5)) > 59) {
                            AlertBox.display("Departure Time Invalid", 500, 200, "You did not enter a proper departure time (Must be in HH:MM format).");
                            return;
                        } else if (adminCreateSchedIn.getText().equals("") || adminCreateSchedIn.getText().length() != 5 || !(Pattern.matches("[0-9]+", adminCreateSchedIn.getText().substring(0, 2))) || !(Pattern.matches("[0-9]+", adminCreateSchedIn.getText().substring(3, 5))) || !adminCreateSchedIn.getText().substring(2, 3).equals(":") || Integer.parseInt(adminCreateSchedIn.getText().substring(0, 2)) < 1 || Integer.parseInt(adminCreateSchedIn.getText().substring(0, 2)) > 23 || Integer.parseInt(adminCreateSchedIn.getText().substring(3, 5)) < 0 || Integer.parseInt(adminCreateSchedIn.getText().substring(3, 5)) > 59) {
                            AlertBox.display("Arrival Time Invalid", 500, 200, "You did not enter a proper arrival time (Must be in HH:MM format).");
                            return;
                        } else {
                            SQL.sendToDatabase("insert into SCHEDULE (TRAIN_ID, TRACK_ID, DEPARTURE_TIME, ARRIVAL_TIME) values('" + adminCheckTrainName.getText() + "','" + adminCheckTrackID.getText() + "','" + adminCreateSchedOut.getText() + "'" + adminCreateSchedIn.getText() + "');");
                            adminCheckTrainName.clear();
                            adminCheckTrackID.clear();
                            adminCreateSchedOut.clear();
                            adminCreateSchedIn.clear();
                            AlertBox.display("Create Schedule Entry Successful", 500, 200, "Successfully created a new Schedule Entry!");
                        }
                    } else if (adminElementDropdownBox.getValue().equals("Track")) {
                        if (adminCreateStationFrom.getText().equals("")) {
                            AlertBox.display("Station From Invalid", 500, 200, "You did not enter a proper station to depart from.");
                            return;
                        } else if (adminCreateStationTo.getText().equals("")) {
                            AlertBox.display("Station To Invalid", 500, 200, "You did not enter a proper station to arrive at.");
                            return;
                        } else if (adminCreateLength.getText().equals("") || !(Pattern.matches("[0-9]+", adminCreateLength.getText())) || Integer.parseInt(adminCreateLength.getText()) < 1) {
                            AlertBox.display("Length Invalid", 500, 200, "You did not enter a proper length (Must be a whole number).");
                            return;
                        } else {
                            SQL.sendToDatabase("insert into TRACK (STATION_FROM_ID, STATION_TO_ID, LENGTH) values('" + adminCreateStationFrom.getText() + "','" + adminCreateStationTo.getText() + "','" + adminCreateLength.getText() + "');");
                            adminCreateStationFrom.clear();
                            adminCreateStationTo.clear();
                            adminCreateLength.clear();
                            AlertBox.display("Create Track Successful", 500, 200, "Successfully created a new track!");
                        }
                    } else if (adminElementDropdownBox.getValue().equals("Ticket")) {
                        if (adminCheckSchedID.getText().equals("")) {
                            AlertBox.display("Schedule Entry ID Invalid", 500, 200, "You did not enter a schedule entry that exists.");
                            return;
                        } else if (adminCreateDate.getText().equals("") || adminCreateDate.getText().length() != 10 || !adminCreateDate.getText().substring(4, 5).equals("-") || !adminCreateDate.getText().substring(7, 8).equals("-") || !(Pattern.matches("[0-9]+", adminCreateDate.getText().substring(0, 4))) || !(Pattern.matches("[0-9]+", adminCreateDate.getText().substring(5, 7))) || !(Pattern.matches("[0-9]+", adminCreateDate.getText().substring(8, 10))) || Integer.parseInt(adminCreateDate.getText().substring(0, 4)) < 1 || Integer.parseInt(adminCreateDate.getText().substring(5, 7)) > 12 || Integer.parseInt(adminCreateDate.getText().substring(5, 7)) < 1 || Integer.parseInt(adminCreateDate.getText().substring(8, 10)) > 30 || Integer.parseInt(adminCreateDate.getText().substring(8, 10)) < 1 || Integer.parseInt(adminCreateDate.getText().substring(0, 4)) > 9999) {
                            AlertBox.display("Date Invalid", 500, 200, "You did not enter a proper date (Must be in YYYY-MM-DD format).");
                            return;
                        }
                        else if (!(Pattern.matches("[0-9]+", adminCreateCustSeat.getText())) || Integer.parseInt(adminCreateCustSeat.getText()) < 1) {
                            AlertBox.display("Customer Seat # Invalid", 500, 200, "You did not enter a proper customer seat number.");
                            return;
                        }
                        else if (adminCreatePrice.getText().equals("") || adminCreatePrice.getText().length() < 4 || !adminCreatePrice.getText().contains(".") || !(Pattern.matches("[0-9]+", adminCreatePrice.getText().substring(0, adminCreatePrice.getText().indexOf(".")))) || !(Pattern.matches("[0-9]+", adminCreatePrice.getText().substring(adminCreatePrice.getText().indexOf(".") + 1))) || Integer.parseInt(adminCreatePrice.getText().substring(0, adminCreatePrice.getText().indexOf("."))) < 1 || Integer.parseInt(adminCreatePrice.getText().substring(adminCreatePrice.getText().indexOf(".") + 1)) > 99) {
                            AlertBox.display("Price Invalid", 500, 200, "You did not enter a proper price (Must be two digits after the decimal).");
                            return;
                        } else {
                            adminCreateDate.setText("");
                            SQL.sendToDatabase("insert into TICKET (STATION_FROM_ID, STATION_TO_ID, LENGTH) values('" + adminCheckSchedID.getText() + "','" + adminCreateDate.getText() + "','" + adminCreateLength.getText() + "');");
                            adminCheckSchedID.clear();
                            adminCreateDate.clear();
                            adminCreateCustSeat.clear();
                            adminCreatePrice.clear();
                            AlertBox.display("Create Ticket Successful", 500, 200, "Successfully created a new ticket!");
                        }
                    }
                }
                else if(adminManipulateDropdownBox.getValue().equals("Update")) {
                    if (adminElementDropdownBox.getValue().equals("Customer")) {
                        invalidDomain = SendEmail.readDomains(adminUpdateEmail.getText());
                        if(adminUpdateCheckCustID.getText().equals(" ")) {
                            AlertBox.display("Customer ID Invalid", 500, 200, "You did not enter a proper pre-existing customer ID.");
                        }
                        else if (adminUpdateEmail.getText().equals("") || invalidDomain) {
                            AlertBox.display("Email Invalid", 500, 200, "You did not enter a proper email address.");
                            return;
                        } else if (adminUpdateCustName.getText().equals("")) {
                            AlertBox.display("Name Invalid", 500, 200, "You did not enter a proper name.");
                            return;
                        } else if (adminUpdateUsername.getText().equals("")) {
                            AlertBox.display("Username Invalid", 500, 200, "You did not enter a proper username.");
                            return;
                        } else if (adminUpdatePassword.getText().equals("")) {
                            AlertBox.display("Password Invalid", 500, 200, "You did not enter a proper password.");
                            return;
                        } else if (!adminUpdatePassword.getText().equals(adminUpdateConfirmPassword.getText())) {
                            AlertBox.display("Password Confirmation Error", 500, 200, "Password and Confirmation do not match.");
                            return;
                        } else {
                            emailAddress = adminUpdateEmail.getText();
                            if (SendEmail.isValidEmailAddress(emailAddress) && SendEmail.isValidRegex(emailAddress)) {
                                SendEmail.send(emailAddress);
                                if (adminManipulateDropdownBox.getValue().equals("Update")) {
                                    adminUpdateCheckCustID.clear();
                                }
                                adminUpdateCustName.clear();
                                adminUpdateEmail.clear();
                                adminUpdateUsername.clear();
                                adminUpdatePassword.clear();
                                adminUpdateConfirmPassword.clear();
                                AlertBox.display("Update Account Successful", 500, 200, "Successfully updated account! An email has been sent to the customer's email address.");

                            }
                        }
                    } else if (adminElementDropdownBox.getValue().equals("Train")) {
                        if(adminUpdateCheckTrainID.getText().equals(" ")) {
                            AlertBox.display("Train ID Invalid", 500, 200, "You did not enter a proper pre-existing train ID.");
                        }
                        else if (adminUpdateTrainName.getText().equals("")) {
                            AlertBox.display("Name Invalid", 500, 200, "You did not enter a proper name.");
                            return;
                        } else if (adminUpdateModel.getText().equals("")) {
                            AlertBox.display("Model Invalid", 500, 200, "You did not enter a proper train model.");
                            return;
                        } else if (!(Pattern.matches("[0-9]+", adminUpdateNumOfSeats.getText())) || Integer.parseInt(adminUpdateNumOfSeats.getText()) < 1) {
                            AlertBox.display("Number of Seats Invalid", 500, 200, "You did not enter a proper number of seats");
                            return;
                        } else {
                            adminUpdateTrainName.clear();
                            adminUpdateModel.clear();
                            adminUpdateNumOfSeats.clear();
                            AlertBox.display("Update Train Successful", 500, 200, "Successfully updated train!");
                        }
                    } else if (adminElementDropdownBox.getValue().equals("Train Station")) {
                        if(adminUpdateCheckTrainStationID.getText().equals(" ")) {
                            AlertBox.display("Train Station ID Invalid", 500, 200, "You did not enter a proper pre-existing train station ID.");
                        }
                        else if (adminUpdateTrainStationName.getText().equals("")) {
                            AlertBox.display("Name Invalid", 500, 200, "You did not enter a proper name.");
                            return;
                        } else if (adminUpdateLocation.getText().equals("")) {
                            AlertBox.display("Location Invalid", 500, 200, "You did not enter a proper location.");
                            return;
                        } else {
                            adminUpdateTrainStationName.clear();
                            adminUpdateLocation.clear();
                            AlertBox.display("Update Train Station Successful", 500, 200, "Successfully updated a train station!");
                        }
                    } else if (adminElementDropdownBox.getValue().equals("Schedule Entry")) {
                        if(adminUpdateSchedID.getText().equals(" ")) {
                            AlertBox.display("Schedule Entry ID Invalid", 500, 200, "You did not enter a proper pre-existing schedule entry ID.");
                        }
                        else if (adminUpdateCheckTrainName.getText().equals("")) {
                            AlertBox.display("Name Invalid", 500, 200, "You did not enter a train name that exists.");
                            return;
                        } else if (adminUpdateCheckTrackID.getText().equals("")) {
                            AlertBox.display("Track ID Invalid", 500, 200, "You did not enter a track ID that exists.");
                            return;
                        } else if (adminUpdateSchedOut.getText().equals("") || adminUpdateSchedOut.getText().length() != 5 || !(Pattern.matches("[0-9]+", adminUpdateSchedOut.getText().substring(0, 2))) || !(Pattern.matches("[0-9]+", adminUpdateSchedOut.getText().substring(3, 5))) || !adminUpdateSchedOut.getText().substring(2, 3).equals(":") || Integer.parseInt(adminUpdateSchedOut.getText().substring(0, 2)) < 1 || Integer.parseInt(adminUpdateSchedOut.getText().substring(0, 2)) > 23 || Integer.parseInt(adminUpdateSchedOut.getText().substring(3, 5)) < 0 || Integer.parseInt(adminUpdateSchedOut.getText().substring(3, 5)) > 59) {
                            AlertBox.display("Departure Time Invalid", 500, 200, "You did not enter a proper departure time (Must be in HH:MM format).");
                            return;
                        } else if (adminUpdateSchedIn.getText().equals("") || adminUpdateSchedIn.getText().length() != 5 || !(Pattern.matches("[0-9]+", adminUpdateSchedIn.getText().substring(0, 2))) || !(Pattern.matches("[0-9]+", adminUpdateSchedIn.getText().substring(3, 5))) || !adminUpdateSchedIn.getText().substring(2, 3).equals(":") || Integer.parseInt(adminUpdateSchedIn.getText().substring(0, 2)) < 1 || Integer.parseInt(adminUpdateSchedIn.getText().substring(0, 2)) > 23 || Integer.parseInt(adminUpdateSchedIn.getText().substring(3, 5)) < 0 || Integer.parseInt(adminUpdateSchedIn.getText().substring(3, 5)) > 59) {
                            AlertBox.display("Arrival Time Invalid", 500, 200, "You did not enter a proper arrival time (Must be in HH:MM format).");
                            return;
                        } else {
                            adminUpdateCheckTrainName.clear();
                            adminUpdateCheckTrackID.clear();
                            adminUpdateSchedOut.clear();
                            adminUpdateSchedIn.clear();
                            AlertBox.display("Update Schedule Entry Successful", 500, 200, "Successfully updated a Schedule Entry!");
                        }
                    } else if (adminElementDropdownBox.getValue().equals("Track")) {
                        if(adminUpdateTrackID.getText().equals(" ")) {
                            AlertBox.display("Schedule Entry ID Invalid", 500, 200, "You did not enter a proper pre-existing track ID.");
                        }
                        else if (adminUpdateStationFrom.getText().equals("")) {
                            AlertBox.display("Station From Invalid", 500, 200, "You did not enter a proper station to depart from.");
                            return;
                        } else if (adminUpdateStationTo.getText().equals("")) {
                            AlertBox.display("Station To Invalid", 500, 200, "You did not enter a proper station to arrive at.");
                            return;
                        } else if (adminUpdateLength.getText().equals("") || !(Pattern.matches("[0-9]+", adminUpdateLength.getText())) || Integer.parseInt(adminUpdateLength.getText()) < 1) {
                            AlertBox.display("Length Invalid", 500, 200, "You did not enter a proper length (Must be a whole number).");
                            return;
                        } else {
                            adminUpdateStationFrom.clear();
                            adminUpdateStationTo.clear();
                            adminUpdateLength.clear();
                            AlertBox.display("Update Track Successful", 500, 200, "Successfully updated a track!");
                        }
                    } else if (adminElementDropdownBox.getValue().equals("Ticket")) {
                        if(adminUpdateCheckTicketID.getText().equals(" ")) {
                            AlertBox.display("Ticket ID Invalid", 500, 200, "You did not enter a proper pre-existing ticket ID.");
                        }
                        else if (adminCheckSchedID.getText().equals("")) {
                            AlertBox.display("Schedule Entry ID Invalid", 500, 200, "You did not enter a schedule entry that exists.");
                            return;
                        } else if (adminUpdateDate.getText().equals("") || adminUpdateDate.getText().length() != 10 || !adminUpdateDate.getText().substring(4, 5).equals("-") || !adminUpdateDate.getText().substring(7, 8).equals("-") || !(Pattern.matches("[0-9]+", adminUpdateDate.getText().substring(0, 4))) || !(Pattern.matches("[0-9]+", adminUpdateDate.getText().substring(5, 7))) || !(Pattern.matches("[0-9]+", adminUpdateDate.getText().substring(8, 10))) || Integer.parseInt(adminUpdateDate.getText().substring(0, 4)) < 1 || Integer.parseInt(adminUpdateDate.getText().substring(5, 7)) > 12 || Integer.parseInt(adminUpdateDate.getText().substring(5, 7)) < 1 || Integer.parseInt(adminUpdateDate.getText().substring(8, 10)) > 30 || Integer.parseInt(adminUpdateDate.getText().substring(8, 10)) < 1 || Integer.parseInt(adminUpdateDate.getText().substring(0, 4)) > 9999) {
                            AlertBox.display("Date Invalid", 500, 200, "You did not enter a proper date (Must be in YYYY-MM-DD format).");
                            return;
                        }
                        else if (!(Pattern.matches("[0-9]+", adminUpdateCustSeat.getText())) || Integer.parseInt(adminUpdateCustSeat.getText()) < 1) {
                            AlertBox.display("Customer Seat # Invalid", 500, 200, "You did not enter a proper customer seat number.");
                            return;
                        }
                        else if (adminUpdatePrice.getText().equals("") || adminUpdatePrice.getText().length() < 4 || !adminUpdatePrice.getText().contains(".") || !(Pattern.matches("[0-9]+", adminUpdatePrice.getText().substring(0, adminUpdatePrice.getText().indexOf(".")))) || !(Pattern.matches("[0-9]+", adminUpdatePrice.getText().substring(adminUpdatePrice.getText().indexOf(".") + 1))) || Integer.parseInt(adminUpdatePrice.getText().substring(0, adminUpdatePrice.getText().indexOf("."))) < 1 || Integer.parseInt(adminUpdatePrice.getText().substring(adminUpdatePrice.getText().indexOf(".") + 1)) > 99) {
                            AlertBox.display("Price Invalid", 500, 200, "You did not enter a proper price (Must be two digits after the decimal).");
                            return;
                        } else {
                            adminUpdateCheckSchedID.clear();
                            adminUpdateDate.clear();
                            adminUpdatePrice.clear();
                            AlertBox.display("Update Ticket Successful", 500, 200, "Successfully updated a ticket!");
                        }
                    }
                }
                else if(adminManipulateDropdownBox.getValue().equals("Delete")) {
                    if(adminElementDropdownBox.getValue().equals("Customer")) {
                        if(adminDeleteCustID.getText().equals("")) {
                            AlertBox.display("Customer ID Invalid", 500, 200, "You did not enter a proper pre-existing customer ID.");
                            return;
                        }
                        else {
                            adminDeleteCustID.clear();
                            AlertBox.display("Delete Customer Successful", 500, 200, "Successfully deleted a customer!");
                        }
                    }
                    else if(adminElementDropdownBox.getValue().equals("Train")) {
                        if(adminDeleteTrainID.getText().equals("")) {
                            AlertBox.display("Train ID Invalid", 500, 200, "You did not enter a proper pre-existing train ID.");
                            return;
                        }
                        else {
                            adminDeleteTrainID.clear();
                            AlertBox.display("Delete Train Successful", 500, 200, "Successfully deleted a train!");
                        }
                    }
                    else if(adminElementDropdownBox.getValue().equals("Train Station")) {
                        if(adminDeleteTrainStationID.getText().equals("")) {
                            AlertBox.display("Train Station ID Invalid", 500, 200, "You did not enter a proper pre-existing train station ID.");
                            return;
                        }
                        else {
                            adminDeleteTrainStationID.clear();
                            AlertBox.display("Delete Train Station Successful", 500, 200, "Successfully deleted a train station!");
                        }
                    }
                    else if(adminElementDropdownBox.getValue().equals("Schedule Entry")) {
                        if(adminDeleteSchedID.getText().equals("")) {
                            AlertBox.display("Schedule Entry ID Invalid", 500, 200, "You did not enter a proper pre-existing schedule entry ID.");
                            return;
                        }
                        else {
                            adminDeleteSchedID.clear();
                            AlertBox.display("Delete Schedule Successful", 500, 200, "Successfully deleted a schedule entry!");
                        }
                    }
                    else if(adminElementDropdownBox.getValue().equals("Track")) {
                        if(adminDeleteTrackID.getText().equals("")) {
                            AlertBox.display("Track ID Invalid", 500, 200, "You did not enter a proper pre-existing track ID.");
                            return;
                        }
                        else {
                            adminDeleteTrackID.clear();
                            AlertBox.display("Delete Track Successful", 500, 200, "Successfully deleted a track!");
                        }
                    }
                    else if(adminElementDropdownBox.getValue().equals("Ticket")) {
                        if(adminDeleteTicketID.getText().equals("")) {
                            AlertBox.display("Ticket ID Invalid", 500, 200, "You did not enter a proper pre-existing ticket ID.");
                            return;
                        }
                        else {
                            adminDeleteTicketID.clear();
                            AlertBox.display("Delete Ticket Successful", 500, 200, "Successfully deleted a ticket!");
                        }
                    }
                }
            }
        });

        //creating a new button with text called "Save Changes"; will open confirmation box confirming customer actually wants to purchase/cancel a ticket
        custSaveChangesButton = new Button("Save Changes");
        custSaveChangesButton.setPrefWidth(100); //sets width to 100
        custSaveChangesButton.setOnAction(e -> {
            result = ConfirmBox.display("Confirm Changes", 500, 200, "Are you sure you want to make this change?");
            if(result) {
                if(custDropdownBox.getValue().equals("Purchase")) {
                    if(custTicketID.getText().equals("")) {
                        AlertBox.display("Ticket ID Invalid", 500, 200, "You did not enter a proper ticket ID");
                        return;
                    }
                    else {
                        custTicketID.clear();
                        AlertBox.display("Purchase Ticket Successful", 500, 200, "You have successfully purchased a ticket!");
                    }
                }
                else if(custDropdownBox.getValue().equals("Cancel")) {
                    if(custTicketID.getText().equals("")) {
                        AlertBox.display("Ticket ID Invalid", 500, 200, "You did not enter a proper ticket ID");
                        return;
                    }
                    else {
                        custTicketID.clear();
                        AlertBox.display("Cancel Ticket Successful", 500, 200, "You have successfully cancelled a ticket!");
                    }
                }
            }
        });

        //creating a new button with text called "Authorize"; will open confirmation box confirming that the admin wants to add a new admin
        adminAuthorizationAuthorizeButton = new Button("Authorize");
        adminAuthorizationAuthorizeButton.setPrefWidth(80);
        adminAuthorizationAuthorizeButton.setOnAction(e -> {
            result = ConfirmBox.display("Authorize New Administrator User", 500, 200, "Are you sure you want to create a new Administrator user?");
            if(result) {
                    SendEmail.send(createAccountEmail.getText());
                    adminAuthorizationWindow.close();
                    mainWindow.setScene(loginUI);
                    mainWindow.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
                    mainWindow.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
                    mainWindow.setTitle("Railway System Simulation: Login");
                    SQL.sendToDatabase("insert into ADMIN (NAME, EMAIL, USERNAME, PASSWORD) values('" + createAccountName.getText() + "','" + createAccountEmail.getText() + "','" + createAccountUsername.getText() + "','" + createAccountPassword.getText() + "');");
                    AlertBox.display("Create Account Successful", 500, 200, "Successfully created new account! An email has been sent to your email address.");
                    return;
                }
        });

        //creating a new button with text called "Cancel"; will stop pre-existing admin from creating a new admin user
        adminAuthorizationCancelButton = new Button("Cancel");
        adminAuthorizationCancelButton.setPrefWidth(80);
        adminAuthorizationCancelButton.setOnAction(e -> {
            result = ConfirmBox.display("Cancellation of New Adminstrator User", 500, 200, "Are you sure you do not want to create a new Administrator user?");
            if (result) {
                adminAuthorizationWindow.close();
            }
        });

        //creating a layout for what will go on our login Scene
        VBox loginOuterLayout = new VBox(20);
        HBox loginUsernameLayout = new HBox(20);
        HBox loginPasswordLayout = new HBox(20);
        HBox loginButtonLayout = new HBox(20);
        loginButtonLayout.getChildren().addAll(loginButton, createAccountLoginButton);
        loginButtonLayout.setAlignment(Pos.CENTER);
        loginUsernameLayout.getChildren().addAll(username, rememberUsernameBox);
        loginUsernameLayout.setTranslateX(100);
        loginPasswordLayout.setAlignment(Pos.CENTER);
        loginPasswordLayout.getChildren().add(password);
        loginOuterLayout.getChildren().addAll(loginDirections, loginUsernameLayout, loginPasswordLayout, loginButtonLayout);
        loginOuterLayout.setAlignment(Pos.CENTER);
        loginOuterLayout.setPadding(new Insets(20, 30, 20, 30));

        //creating a layout for what will go on our admin Scene

        VBox createCustDisplay = new VBox(20);
        createCustDisplay.getChildren().addAll(adminCreateCustName, adminCreateEmail, adminCreateUsername, adminCreatePassword, adminCreateConfirmPassword);
        createCustDisplay.setAlignment(Pos.CENTER);
        createCustDisplay.setPadding(new Insets(0, 0, 45, 0));

        VBox updateCustDisplay = new VBox(20);
        updateCustDisplay.getChildren().addAll(adminUpdateCheckCustID, adminUpdateCustName, adminUpdateEmail, adminUpdateUsername, adminUpdatePassword, adminUpdateConfirmPassword);
        updateCustDisplay.setAlignment(Pos.CENTER);

        VBox deleteCustDisplay = new VBox(20);
        deleteCustDisplay.getChildren().addAll(adminDeleteCustID, adminDeleteDirections1);
        deleteCustDisplay.setAlignment(Pos.CENTER);
        deleteCustDisplay.setPadding(new Insets(0, 0, 188, 0));

        VBox createTrainDisplay = new VBox(20);
        createTrainDisplay.getChildren().addAll(adminCreateTrainName, adminCreateModel, adminCreateNumOfSeats);
        createTrainDisplay.setAlignment(Pos.CENTER);
        createTrainDisplay.setPadding(new Insets(0, 0, 135, 0));

        VBox updateTrainDisplay = new VBox(20);
        updateTrainDisplay.getChildren().addAll(adminUpdateCheckTrainID, adminUpdateTrainName, adminUpdateModel, adminUpdateNumOfSeats);
        updateTrainDisplay.setAlignment(Pos.CENTER);
        updateTrainDisplay.setPadding(new Insets(0,0,90,0));

        VBox deleteTrainDisplay = new VBox(20);
        deleteTrainDisplay.getChildren().addAll(adminDeleteTrainID, adminDeleteDirections2);
        deleteTrainDisplay.setAlignment(Pos.CENTER);
        deleteTrainDisplay.setPadding(new Insets(0, 0, 188, 0));

        VBox createTrainStationDisplay = new VBox(20);
        createTrainStationDisplay.getChildren().addAll(adminCreateTrainStationName, adminCreateLocation);
        createTrainStationDisplay.setAlignment(Pos.CENTER);
        createTrainStationDisplay.setPadding(new Insets(0, 0, 180, 0));

        VBox updateTrainStationDisplay = new VBox(20);
        updateTrainStationDisplay.getChildren().addAll(adminUpdateCheckTrainStationID, adminUpdateTrainStationName, adminUpdateLocation);
        updateTrainStationDisplay.setAlignment(Pos.CENTER);
        updateTrainStationDisplay.setPadding(new Insets(0, 0, 135, 0));

        VBox deleteTrainStationDisplay = new VBox(20);
        deleteTrainStationDisplay.getChildren().addAll(adminDeleteTrainStationID, adminDeleteDirections3);
        deleteTrainStationDisplay.setAlignment(Pos.CENTER);
        deleteTrainStationDisplay.setPadding(new Insets(0, 0, 188, 0));

        VBox createSchedDisplay = new VBox(20);
        createSchedDisplay.getChildren().addAll(adminCheckTrainName, adminCheckTrackID, adminCreateSchedOut, adminCreateSchedIn);
        createSchedDisplay.setAlignment(Pos.CENTER);
        createSchedDisplay.setPadding(new Insets(0, 0, 90, 0));

        VBox updateSchedDisplay = new VBox(20);
        updateSchedDisplay.getChildren().addAll(adminUpdateSchedID, adminUpdateCheckTrainName, adminUpdateCheckTrackID, adminUpdateSchedOut, adminUpdateSchedIn);
        updateSchedDisplay.setAlignment(Pos.CENTER);
        updateSchedDisplay.setPadding(new Insets(0, 0, 45, 0));

        VBox deleteSchedDisplay = new VBox(20);
        deleteSchedDisplay.getChildren().addAll(adminDeleteSchedID, adminDeleteDirections4);
        deleteSchedDisplay.setAlignment(Pos.CENTER);
        deleteSchedDisplay.setPadding(new Insets(0, 0, 188, 0));

        VBox createTrackDisplay = new VBox(20);
        createTrackDisplay.getChildren().addAll(adminCreateStationFrom, adminCreateStationTo, adminCreateLength);
        createTrackDisplay.setAlignment(Pos.CENTER);
        createTrackDisplay.setPadding(new Insets(0, 0, 135, 0));

        VBox updateTrackDisplay = new VBox(20);
        updateTrackDisplay.getChildren().addAll(adminUpdateTrackID, adminUpdateStationFrom, adminUpdateStationTo, adminUpdateLength);
        updateTrackDisplay.setAlignment(Pos.CENTER);
        updateTrackDisplay.setPadding(new Insets(0, 0, 90, 0));

        VBox deleteTrackDisplay = new VBox(20);
        deleteTrackDisplay.getChildren().addAll(adminDeleteTrackID, adminDeleteDirections5);
        deleteTrackDisplay.setAlignment(Pos.CENTER);
        deleteTrackDisplay.setPadding(new Insets(0, 0, 188, 0));

        VBox createTicketDisplay = new VBox(20);
        createTicketDisplay.getChildren().addAll(adminCheckSchedID, adminCreateDate, adminCreateCustSeat, adminCreatePrice);
        createTicketDisplay.setAlignment(Pos.CENTER);
        createTicketDisplay.setPadding(new Insets(0, 0, 90, 0));

        VBox updateTicketDisplay = new VBox(20);
        updateTicketDisplay.getChildren().addAll(adminUpdateCheckTicketID, adminUpdateCheckSchedID, adminUpdateCustSeat, adminUpdateDate, adminUpdatePrice);
        updateTicketDisplay.setAlignment(Pos.CENTER);
        updateTicketDisplay.setPadding(new Insets(0, 0, 45, 0));

        VBox deleteTicketDisplay = new VBox(20);
        deleteTicketDisplay.getChildren().addAll(adminDeleteTicketID, adminDeleteDirections6);
        deleteTicketDisplay.setAlignment(Pos.CENTER);
        deleteTicketDisplay.setPadding(new Insets(0, 0, 188, 0));

        VBox adminOuterLayout = new VBox(20);
        VBox adminDirectionsLayout = new VBox();
        HBox adminDropdownInnerLayout = new HBox(20);
        VBox adminDropdownOuterLayout = new VBox(20);
        HBox adminButtonInnerLayout = new HBox(20);
        VBox adminButtonOuterLayout = new VBox(20);
        adminDirectionsLayout.getChildren().addAll(adminDirections1, adminDirections2);
        adminDropdownInnerLayout.getChildren().addAll(adminManipulateDropdownBox, adminElementDropdownBox);
        adminDropdownOuterLayout.getChildren().addAll(adminDirectionsLayout, adminDropdownInnerLayout, horizontalSeparator4);
        adminButtonInnerLayout.getChildren().addAll(adminSaveChangesButton, signOutButton);
        adminButtonOuterLayout.getChildren().addAll(horizontalSeparator5, adminButtonInnerLayout);
        adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, createCustDisplay, adminButtonOuterLayout);
        adminDirectionsLayout.setAlignment(Pos.CENTER);
        adminDropdownInnerLayout.setAlignment(Pos.CENTER);
        adminDropdownOuterLayout.setAlignment(Pos.CENTER);
        adminButtonInnerLayout.setAlignment(Pos.CENTER);
        adminButtonOuterLayout.setAlignment(Pos.CENTER);
        adminOuterLayout.setAlignment(Pos.CENTER);
        adminOuterLayout.setPadding(new Insets(20, 30, 20, 30));

        adminManipulateDropdownBox.setOnAction(e -> {
            if(adminManipulateDropdownBox.getValue().equals("Create") && adminElementDropdownBox.getValue().equals("Customer")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, createCustDisplay, adminButtonOuterLayout);
            }
            else if(adminManipulateDropdownBox.getValue().equals("Create") && adminElementDropdownBox.getValue().equals("Train")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, createTrainDisplay, adminButtonOuterLayout);
            }
            else if(adminManipulateDropdownBox.getValue().equals("Create") && adminElementDropdownBox.getValue().equals("Train Station")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, createTrainStationDisplay, adminButtonOuterLayout);
            }
            else if(adminManipulateDropdownBox.getValue().equals("Create") && adminElementDropdownBox.getValue().equals("Track")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, createTrackDisplay, adminButtonOuterLayout);
            }
            else if(adminManipulateDropdownBox.getValue().equals("Create") && adminElementDropdownBox.getValue().equals("Ticket")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, createTicketDisplay, adminButtonOuterLayout);
            }
            else if(adminManipulateDropdownBox.getValue().equals("Update") && adminElementDropdownBox.getValue().equals("Customer")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, updateCustDisplay, adminButtonOuterLayout);
            }
            else if(adminManipulateDropdownBox.getValue().equals("Update") && adminElementDropdownBox.getValue().equals("Train")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, updateTrainDisplay, adminButtonOuterLayout);
            }
            else if(adminManipulateDropdownBox.getValue().equals("Update") && adminElementDropdownBox.getValue().equals("Train Station")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, updateTrainStationDisplay, adminButtonOuterLayout);
            }
            else if(adminManipulateDropdownBox.getValue().equals("Update") && adminElementDropdownBox.getValue().equals("Schedule Entry")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, updateSchedDisplay, adminButtonOuterLayout);
            }
            else if(adminManipulateDropdownBox.getValue().equals("Update") && adminElementDropdownBox.getValue().equals("Track")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, updateTrackDisplay, adminButtonOuterLayout);
            }
            else if(adminManipulateDropdownBox.getValue().equals("Update") && adminElementDropdownBox.getValue().equals("Ticket")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, updateTicketDisplay, adminButtonOuterLayout);
            }
            else if(adminManipulateDropdownBox.getValue().equals("Delete") && adminElementDropdownBox.getValue().equals("Customer")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, deleteCustDisplay, adminButtonOuterLayout);
            }
            else if(adminManipulateDropdownBox.getValue().equals("Delete") && adminElementDropdownBox.getValue().equals("Train")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, deleteTrainDisplay, adminButtonOuterLayout);
            }
            else if(adminManipulateDropdownBox.getValue().equals("Delete") && adminElementDropdownBox.getValue().equals("Train Station")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, deleteTrainStationDisplay, adminButtonOuterLayout);
            }
            else if(adminManipulateDropdownBox.getValue().equals("Delete") && adminElementDropdownBox.getValue().equals("Schedule Entry")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, deleteSchedDisplay, adminButtonOuterLayout);
            }
            else if(adminManipulateDropdownBox.getValue().equals("Delete") && adminElementDropdownBox.getValue().equals("Track")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, deleteTrackDisplay, adminButtonOuterLayout);
            }
            else if(adminManipulateDropdownBox.getValue().equals("Delete") && adminElementDropdownBox.getValue().equals("Ticket")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, deleteTicketDisplay, adminButtonOuterLayout);
            }
            else{
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, adminButtonOuterLayout);
            }
        });

        adminElementDropdownBox.setOnAction(event -> {
            if(adminManipulateDropdownBox.getValue().equals("Create") && adminElementDropdownBox.getValue().equals("Customer")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, createCustDisplay, adminButtonOuterLayout);
            }
            else if(adminManipulateDropdownBox.getValue().equals("Create") && adminElementDropdownBox.getValue().equals("Train")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, createTrainDisplay, adminButtonOuterLayout);
            }
            else if(adminManipulateDropdownBox.getValue().equals("Create") && adminElementDropdownBox.getValue().equals("Train Station")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, createTrainStationDisplay, adminButtonOuterLayout);
            }
            else if(adminManipulateDropdownBox.getValue().equals("Create") && adminElementDropdownBox.getValue().equals("Schedule Entry")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, createSchedDisplay, adminButtonOuterLayout);
            }
            else if(adminManipulateDropdownBox.getValue().equals("Create") && adminElementDropdownBox.getValue().equals("Track")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, createTrackDisplay, adminButtonOuterLayout);
            }
            else if(adminManipulateDropdownBox.getValue().equals("Create") && adminElementDropdownBox.getValue().equals("Ticket")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, createTicketDisplay, adminButtonOuterLayout);
            }
            else if(adminManipulateDropdownBox.getValue().equals("Update") && adminElementDropdownBox.getValue().equals("Customer")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, updateCustDisplay, adminButtonOuterLayout);
            }
            else if(adminManipulateDropdownBox.getValue().equals("Update") && adminElementDropdownBox.getValue().equals("Train")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, updateTrainDisplay, adminButtonOuterLayout);
            }
            else if(adminManipulateDropdownBox.getValue().equals("Update") && adminElementDropdownBox.getValue().equals("Train Station")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, updateTrainStationDisplay, adminButtonOuterLayout);
            }
            else if(adminManipulateDropdownBox.getValue().equals("Update") && adminElementDropdownBox.getValue().equals("Schedule Entry")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, updateSchedDisplay, adminButtonOuterLayout);
            }
            else if(adminManipulateDropdownBox.getValue().equals("Update") && adminElementDropdownBox.getValue().equals("Track")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, updateTrackDisplay, adminButtonOuterLayout);
            }
            else if(adminManipulateDropdownBox.getValue().equals("Update") && adminElementDropdownBox.getValue().equals("Ticket")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, updateTicketDisplay, adminButtonOuterLayout);
            }
            else if(adminManipulateDropdownBox.getValue().equals("Delete") && adminElementDropdownBox.getValue().equals("Customer")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, deleteCustDisplay, adminButtonOuterLayout);
            }
            else if(adminManipulateDropdownBox.getValue().equals("Delete") && adminElementDropdownBox.getValue().equals("Train")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, deleteTrainDisplay, adminButtonOuterLayout);
            }
            else if(adminManipulateDropdownBox.getValue().equals("Delete") && adminElementDropdownBox.getValue().equals("Train Station")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, deleteTrainStationDisplay, adminButtonOuterLayout);
            }
            else if(adminManipulateDropdownBox.getValue().equals("Delete") && adminElementDropdownBox.getValue().equals("Schedule Entry")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, deleteSchedDisplay, adminButtonOuterLayout);
            }
            else if(adminManipulateDropdownBox.getValue().equals("Delete") && adminElementDropdownBox.getValue().equals("Track")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, deleteTrackDisplay, adminButtonOuterLayout);
            }
            else if(adminManipulateDropdownBox.getValue().equals("Delete") && adminElementDropdownBox.getValue().equals("Ticket")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, deleteTicketDisplay, adminButtonOuterLayout);
            }
            else{
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, adminButtonOuterLayout);
            }
        });

        //creating a layout for what will go on our customer Scene
        HBox custButtonLayout = new HBox(20);
        custButtonLayout.getChildren().addAll(custSaveChangesButton, signOutButton2);
        custButtonLayout.setAlignment(Pos.CENTER);

        VBox custDirectionsLayout = new VBox(20);
        custDirectionsLayout.getChildren().addAll(custDirections, custDropdownBox, horizontalSeparator6, custTicketID, horizontalSeparator7);
        custDirectionsLayout.setAlignment(Pos.CENTER);

        VBox custOuterLayout = new VBox(20);
        custOuterLayout.getChildren().addAll(custDirectionsLayout, custButtonLayout);
        custOuterLayout.setAlignment(Pos.CENTER);
        custOuterLayout.setPadding(new Insets(20, 30, 20, 30));

        //creating a layout for what will go on our create account Scene
        VBox createAccountOuterLayout = new VBox(20);
        VBox createAccountTopLayout = new VBox();
        VBox createAccountEmailWarningLayout = new VBox();
        VBox createAccountBottomLayout = new VBox(20);
        HBox bottomButtons = new HBox(20);
        horizontalSeparator1.setPrefWidth(500);
        horizontalSeparator2.setPrefWidth(500);
        horizontalSeparator3.setPrefWidth(500);
        bottomButtons.getChildren().addAll(createAccountCreateButton, createAccountCancelButton);
        createAccountEmailWarningLayout.getChildren().addAll(createAccountEmailWarning1, createAccountEmailWarning2);
        createAccountTopLayout.getChildren().addAll(createAccountDirections1, createAccountDirections2);
        createAccountBottomLayout.getChildren().addAll(accountTypeBox, horizontalSeparator1, personalInfo, createAccountName, createAccountEmail, createAccountEmailWarningLayout, horizontalSeparator2, accountInfo, createAccountUsername, createAccountPassword, createAccountConfirmPassword, horizontalSeparator3, bottomButtons);
        createAccountEmailWarningLayout.setAlignment(Pos.CENTER);
        createAccountTopLayout.setAlignment(Pos.CENTER);
        createAccountBottomLayout.setAlignment(Pos.CENTER);
        bottomButtons.setAlignment(Pos.CENTER);
        createAccountOuterLayout.setAlignment(Pos.CENTER);
        createAccountOuterLayout.getChildren().addAll(createAccountTopLayout, createAccountBottomLayout);
        createAccountOuterLayout.setPadding(new Insets(20, 30, 20, 30));

        //creating a layout for what will go on our admin authorization Scene
        VBox adminAuthorizationOuterLayout = new VBox(20);
        HBox adminAuthorizationBottomButtons = new HBox(20);
        adminAuthorizationBottomButtons.getChildren().addAll(adminAuthorizationAuthorizeButton, adminAuthorizationCancelButton);
        adminAuthorizationOuterLayout.getChildren().addAll(adminAuthorizationDirections, adminUsername, adminPassword, adminAuthorizationBottomButtons);
        adminAuthorizationOuterLayout.setAlignment(Pos.CENTER);
        adminAuthorizationBottomButtons.setAlignment(Pos.CENTER);
        adminAuthorizationOuterLayout.setPadding(new Insets(20, 30, 20, 30));

        //creating login scene
        loginUI = new Scene(loginOuterLayout);

        //creating admin scene (fullscreen)
        adminUI = new Scene(adminOuterLayout);

        //creating customer scene (fullscreen)
        custUI = new Scene(custOuterLayout);

        //creating create account scene
        createAccountUI = new Scene(createAccountOuterLayout);

        //creating admin authorization scene
        adminAuthorizationUI = new Scene(adminAuthorizationOuterLayout);

        //gets rid of whitespace and does not allow user to resize window
        mainWindow.sizeToScene();
        mainWindow.setResizable(false);

        //attaching the scene to the mainWindow and making it visible
        mainWindow.setScene(loginUI);
        mainWindow.show();

        //when user attempts to exit the program, check with them first
        mainWindow.setOnCloseRequest(e -> {
            e.consume();
            closeApplication();
        });

        //when user attempts to exit the authorization window, check with them first
        adminAuthorizationWindow.setOnCloseRequest(e -> {
            e.consume();
            result = ConfirmBox.display("Cancellation of New Adminstrator User", 500, 200, "Are you sure you do not want to create a new Administrator user?");
            if(result)
                adminAuthorizationWindow.close();
        });

        //centers window on user's screen
        mainWindow.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
        mainWindow.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
    }

    //calls GUI to load
    public static void main(String[] args) {
        launch(args);
    }

    private void closeApplication() {
        result = ConfirmBox.display("Confirming Exit", 500, 200, "Are you sure you want to exit the Railway System Simulation?");
        if(result)
            mainWindow.close();
    }
}
