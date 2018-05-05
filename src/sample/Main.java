package sample;

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
    Button loginButton, signOutButton, signOutButton2, createAccountLoginButton, createAccountCreateButton, createAccountCancelButton, adminAuthorizationAuthorizeButton, adminAuthorizationCancelButton, adminSaveChangesButton; //button that user can interact with
    Label loginDirections, createAccountDirections1, createAccountDirections2, createAccountEmailWarning1, createAccountEmailWarning2, personalInfo, accountInfo, adminDirections1, adminDirections2, adminAuthorizationDirections; //String that will tell user how to login
    TextField username, createAccountName, createAccountEmail, createAccountUsername, adminCheckCustID, adminCheckTrackID, adminCheckSchedID, adminCreateCustName, adminCreateTrainName, adminCheckTrainName, adminCreateTrainStationName, adminCreateModel, adminCreateNumOfSeats, adminCreateLocation, adminCreateSchedIn, adminCreateSchedOut, adminCreateDate, adminCreateStationFrom, adminCreateStationTo, adminCreateLength,  adminCreateCustSeat, adminCreatePrice, adminCreateEmail, adminCreateUsername; //Where user can input information
    PasswordField password, createAccountPassword, createAccountConfirmPassword, adminUsername, adminPassword, adminCreatePassword, adminCreateConfirmPassword; //Where user password's will be entered
    CheckBox rememberUsernameBox; //Box user can check if it wants application to remember their username after signing out
    ChoiceBox<String> accountTypeBox, adminManipulateDropdownBox, adminElementDropdownBox; //Dropdown menus
    Separator horizontalSeparator1, horizontalSeparator2, horizontalSeparator3, horizontalSeparator4, horizontalSeparator5; //Horizontal Separators used to separate information in the window more clearly
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
        adminElementDropdownBox.getItems().addAll("Customer", "Train", "Train Station", "Track", "Schedule Entry", "Ticket Order");
        adminElementDropdownBox.setTooltip(new Tooltip("Select what element you want to manipulate."));
        adminElementDropdownBox.getSelectionModel().select(0);

        //initializing horizontal separators
        horizontalSeparator1 = new Separator();
        horizontalSeparator2 = new Separator();
        horizontalSeparator3 = new Separator();
        horizontalSeparator4 = new Separator();
        horizontalSeparator5 = new Separator();

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
        adminCheckTrackID.setPromptText("Track ID Number");
        adminCheckTrackID.setTooltip(new Tooltip("Enter Track ID Number"));

        adminCheckCustID = new TextField();
        adminCheckCustID.setMaxWidth(300);
        adminCheckCustID.setPromptText("Pre-Existing Customer ID Number");
        adminCheckCustID.setTooltip(new Tooltip("Enter Pre-Existing Customer ID Number"));

        adminCheckSchedID = new TextField();
        adminCheckSchedID.setMaxWidth(300);
        adminCheckSchedID.setPromptText("Pre-Existing Schedule Entry ID Number");
        adminCheckSchedID.setTooltip(new Tooltip("Enter Pre-Existing Schedule Entry ID Number"));

        adminCreateCustName = new TextField();
        adminCreateCustName.setMaxWidth(300);
        adminCreateCustName.setPromptText("Customer Name");
        adminCreateCustName.setTooltip(new Tooltip("Enter Customer Name"));

        adminCreateTrainName = new TextField();
        adminCreateTrainName.setMaxWidth(300);
        adminCreateTrainName.setPromptText("Train Name");
        adminCreateTrainName.setTooltip(new Tooltip("Enter Train Name"));

        adminCheckTrainName = new TextField();
        adminCheckTrainName.setMaxWidth(300);
        adminCheckTrainName.setPromptText("Pre-Existing Train Name");
        adminCheckTrainName.setTooltip(new Tooltip("Enter Pre-Existing Train Name"));

        adminCreateTrainStationName = new TextField();
        adminCreateTrainStationName.setMaxWidth(300);
        adminCreateTrainStationName.setPromptText("Train Station Name");
        adminCreateTrainStationName.setTooltip(new Tooltip("Enter Train Station Name"));

        adminCreateModel = new TextField();
        adminCreateModel.setMaxWidth(300);
        adminCreateModel.setPromptText("Train Model");
        adminCreateModel.setTooltip(new Tooltip("Enter Train Model"));

        adminCreateNumOfSeats = new TextField();
        adminCreateNumOfSeats.setMaxWidth(300);
        adminCreateNumOfSeats.setPromptText("# of Seats");
        adminCreateNumOfSeats.setTooltip(new Tooltip("Enter Capacity of Train"));

        adminCreateLocation = new TextField();
        adminCreateLocation.setMaxWidth(300);
        adminCreateLocation.setPromptText("Location");
        adminCreateLocation.setTooltip(new Tooltip("Enter Location of Train Station"));

        adminCreateSchedOut = new TextField();
        adminCreateSchedOut.setMaxWidth(300);
        adminCreateSchedOut.setPromptText("Time of Departure (HH:MM format)");
        adminCreateSchedOut.setTooltip(new Tooltip("Enter Time of Departure"));

        adminCreateSchedIn = new TextField();
        adminCreateSchedIn.setMaxWidth(300);
        adminCreateSchedIn.setPromptText("Time of Arrival (HH:MM format)");
        adminCreateSchedIn.setTooltip(new Tooltip("Enter Time of Arrival"));

        adminCreateDate = new TextField();
        adminCreateDate.setMaxWidth(300);
        adminCreateDate.setPromptText("Date (MM/DD/YY format)");
        adminCreateDate.setTooltip(new Tooltip("Enter Date of Event"));

        adminCreateStationFrom = new TextField();
        adminCreateStationFrom.setMaxWidth(300);
        adminCreateStationFrom.setPromptText("Station From");
        adminCreateStationFrom.setTooltip(new Tooltip("Enter Station the Train is Leaving From"));

        adminCreateStationTo = new TextField();
        adminCreateStationTo.setMaxWidth(300);
        adminCreateStationTo.setPromptText("Station To");
        adminCreateStationTo.setTooltip(new Tooltip("Enter Station the Train is Going to"));

        adminCreateLength = new TextField();
        adminCreateLength.setMaxWidth(300);
        adminCreateLength.setPromptText("Length of Track (miles)");
        adminCreateLength.setTooltip(new Tooltip("Enter Length of Track in Miles"));

        adminCreateCustSeat = new TextField();
        adminCreateCustSeat.setMaxWidth(300);
        adminCreateCustSeat.setPromptText("Customer Seat #");
        adminCreateCustSeat.setTooltip(new Tooltip("Enter the Seat of the Customer"));

        adminCreatePrice = new TextField();
        adminCreatePrice.setMaxWidth(300);
        adminCreatePrice.setPromptText("Price of Ticket (Two Digits After Decimal)");
        adminCreatePrice.setTooltip(new Tooltip("Enter the Price of the Ticket"));

        adminCreateEmail = new TextField();
        adminCreateEmail.setMaxWidth(300);
        adminCreateEmail.setPromptText("Email");
        adminCreateEmail.setTooltip(new Tooltip("Enter Email of the Customer"));

        adminCreateUsername = new TextField();
        adminCreateUsername.setMaxWidth(300);
        adminCreateUsername.setPromptText("Username");
        adminCreateUsername.setTooltip(new Tooltip("Enter Username of Customer"));

        //PasswordFields used in admin view
        adminCreatePassword = new PasswordField();
        adminCreatePassword.setMaxWidth(300);
        adminCreatePassword.setPromptText("Password");
        adminCreatePassword.setTooltip(new Tooltip("Enter Password of Customer"));

        adminCreateConfirmPassword = new PasswordField();
        adminCreateConfirmPassword.setMaxWidth(300);
        adminCreateConfirmPassword.setPromptText("Confirm Password");
        adminCreateConfirmPassword.setTooltip(new Tooltip("Enter Password of Customer Again"));

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
                if(adminElementDropdownBox.getValue().equals("Customer")) {
                    invalidDomain = SendEmail.readDomains(adminCreateEmail.getText());
                    if(adminCreateEmail.getText().equals("") || invalidDomain) {
                        AlertBox.display("Email Invalid", 500, 200, "You did not enter a proper email address.");
                        return;
                    }
                    else if(adminCreateCustName.getText().equals("")) {
                        AlertBox.display("Name Invalid", 500, 200, "You did not enter a proper name.");
                        return;
                    }
                    else if(adminCreateUsername.getText().equals("")) {
                        AlertBox.display("Username Invalid", 500, 200, "You did not enter a proper username.");
                        return;
                    }
                    else if(adminCreatePassword.getText().equals("")) {
                        AlertBox.display("Password Invalid", 500, 200, "You did not enter a proper password.");
                        return;
                    }
                    else if(!adminCreatePassword.getText().equals(adminCreateConfirmPassword.getText())) {
                        AlertBox.display("Password Confirmation Error", 500, 200, "Password and Confirmation do not match.");
                        return;
                    }
                    else {
                        emailAddress = adminCreateEmail.getText();
                        if(SendEmail.isValidEmailAddress(emailAddress) && SendEmail.isValidRegex(emailAddress)) {
                            SendEmail.send(emailAddress);
                            adminCreateCustName.clear();
                            adminCreateEmail.clear();
                            adminCreateUsername.clear();
                            adminCreatePassword.clear();
                            adminCreateConfirmPassword.clear();
                            AlertBox.display("Create Account Successful", 500, 200, "Successfully created new account! An email has been sent to the customer's email address.");

                        }
                    }
                }
                else if(adminElementDropdownBox.getValue().equals("Train")) {
                    if(adminCreateTrainName.getText().equals("")) {
                        AlertBox.display("Name Invalid", 500, 200, "You did not enter a proper name.");
                        return;
                    }
                    else if(adminCreateModel.getText().equals("")) {
                        AlertBox.display("Model Invalid", 500, 200, "You did not enter a proper train model.");
                        return;
                    }
                    else if(!(Pattern.matches("[0-9]+", adminCreateNumOfSeats.getText())) || Integer.parseInt(adminCreateNumOfSeats.getText()) < 1) {
                        AlertBox.display("Number of Seats Invalid", 500, 200, "You did not enter a proper number of seats");
                        return;
                    }
                    else {
                        adminCreateTrainName.clear();
                        adminCreateModel.clear();
                        adminCreateNumOfSeats.clear();
                        AlertBox.display("Create Train Successful", 500, 200, "Successfully created new train!");
                    }
                }
                else if(adminElementDropdownBox.getValue().equals("Train Station")) {
                    if(adminCreateTrainStationName.getText().equals("")) {
                        AlertBox.display("Name Invalid", 500, 200, "You did not enter a proper name.");
                        return;
                    }
                    else if(adminCreateLocation.getText().equals("")) {
                        AlertBox.display("Location Invalid", 500, 200, "You did not enter a proper location.");
                        return;
                    }
                    else {
                        adminCreateTrainStationName.clear();
                        adminCreateLocation.clear();
                        AlertBox.display("Create Train Station Successful", 500, 200, "Successfully created a new train station!");
                    }
                }
                else if(adminElementDropdownBox.getValue().equals("Schedule Entry")) {
                    if(adminCheckTrainName.getText().equals("")) {
                        AlertBox.display("Name Invalid", 500, 200, "You did not enter a train name that exists.");
                        return;
                    }
                    else if(adminCheckTrackID.getText().equals("")) {
                        AlertBox.display("Track ID Invalid", 500, 200, "You did not enter a track ID that exists.");
                        return;
                    }
                    else if(adminCreateSchedOut.getText().equals("") || adminCreateSchedOut.getText().length() != 5 || !(Pattern.matches("[0-9]+", adminCreateSchedOut.getText().substring(0, 2))) || !(Pattern.matches("[0-9]+", adminCreateSchedOut.getText().substring(3, 5))) || !adminCreateSchedOut.getText().substring(2, 3).equals(":") || Integer.parseInt(adminCreateSchedOut.getText().substring(0, 2)) < 1 || Integer.parseInt(adminCreateSchedOut.getText().substring(0, 2)) > 23 || Integer.parseInt(adminCreateSchedOut.getText().substring(3, 5)) < 0 || Integer.parseInt(adminCreateSchedOut.getText().substring(3, 5)) > 59) {
                        AlertBox.display("Departure Time Invalid", 500, 200, "You did not enter a proper departure time (Must be in HH:MM format).");
                        return;
                    }
                    else if(adminCreateSchedIn.getText().equals("") || adminCreateSchedIn.getText().length() != 5 || !(Pattern.matches("[0-9]+", adminCreateSchedIn.getText().substring(0, 2))) || !(Pattern.matches("[0-9]+", adminCreateSchedIn.getText().substring(3, 5))) || !adminCreateSchedIn.getText().substring(2, 3).equals(":")|| Integer.parseInt(adminCreateSchedIn.getText().substring(0, 2)) < 1 || Integer.parseInt(adminCreateSchedIn.getText().substring(0, 2)) > 23 || Integer.parseInt(adminCreateSchedIn.getText().substring(3, 5)) < 0 || Integer.parseInt(adminCreateSchedIn.getText().substring(3, 5)) > 59) {
                        AlertBox.display("Arrival Time Invalid", 500, 200, "You did not enter a proper arrival time (Must be in HH:MM format).");
                        return;
                    }
                    else {
                        adminCheckTrainName.clear();
                        adminCheckTrackID.clear();
                        adminCreateSchedOut.clear();
                        adminCreateSchedIn.clear();
                        AlertBox.display("Create Schedule Entry Successful", 500, 200, "Successfully created a new Schedule Entry!");
                    }
                }
                else if(adminElementDropdownBox.getValue().equals("Track")) {
                    if(adminCreateStationFrom.getText().equals("")) {
                        AlertBox.display("Station From Invalid", 500, 200, "You did not enter a proper station to depart from.");
                        return;
                    }
                    else if(adminCreateStationTo.getText().equals("")) {
                        AlertBox.display("Station To Invalid", 500, 200, "You did not enter a proper station to arrive at.");
                        return;
                    }
                    else if(adminCreateLength.getText().equals("") || !(Pattern.matches("[0-9]+", adminCreateLength.getText())) || Integer.parseInt(adminCreateLength.getText()) < 1) {
                        AlertBox.display("Length Invalid", 500, 200, "You did not enter a proper length (Must be a whole number).");
                        return;
                    }
                    else {
                        adminCreateStationFrom.clear();
                        adminCreateStationTo.clear();
                        adminCreateLength.clear();
                        AlertBox.display("Create Track Successful", 500, 200, "Successfully created a new track!");
                    }
                }
                else if(adminElementDropdownBox.getValue().equals("Ticket Order")) {
                    if(adminCheckSchedID.getText().equals("")) {
                        AlertBox.display("Schedule Entry ID Invalid", 500, 200, "You did not enter a schedule entry that exists.");
                        return;
                    }
                    else if(adminCreateDate.getText().equals("") || adminCreateDate.getText().length() != 8 || !adminCreateDate.getText().substring(2, 3).equals("/") || !adminCreateDate.getText().substring(5, 6).equals("/") || !(Pattern.matches("[0-9]+", adminCreateDate.getText().substring(0, 2))) || !(Pattern.matches("[0-9]+", adminCreateDate.getText().substring(3, 5))) || !(Pattern.matches("[0-9]+", adminCreateDate.getText().substring(6, 8))) || Integer.parseInt(adminCreateDate.getText().substring(0, 2)) < 1 || Integer.parseInt(adminCreateDate.getText().substring(0, 2)) > 12 || Integer.parseInt(adminCreateDate.getText().substring(3, 5)) < 1 || Integer.parseInt(adminCreateDate.getText().substring(3, 5)) > 30 || Integer.parseInt(adminCreateDate.getText().substring(6, 8)) < 1 || Integer.parseInt(adminCreateDate.getText().substring(6, 8)) > 99) {
                        AlertBox.display("Date Invalid", 500, 200, "You did not enter a proper date (Must be in MM/DD/YY format).");
                        return;
                    }
                    else if(adminCheckCustID.getText().equals("")) {
                        AlertBox.display("Customer ID Invalid", 500, 200, "You did not enter a customer that exists.");
                        return;
                    }
                    else if(adminCreateCustSeat.getText().equals("") || !(Pattern.matches("[0-9]+", adminCreateCustSeat.getText())) || Integer.parseInt(adminCreateCustSeat.getText()) < 1) {
                        AlertBox.display("Customer Seat # Invalid", 500, 200, "You did not enter a proper customer seat number.");
                        return;
                    }
                     if(adminCreatePrice.getText().equals("") || adminCreatePrice.getText().length() < 4 || !adminCreatePrice.getText().contains(".") || !(Pattern.matches("[0-9]+", adminCreatePrice.getText().substring(0, adminCreatePrice.getText().indexOf(".")))) || !(Pattern.matches("[0-9]+", adminCreatePrice.getText().substring(adminCreatePrice.getText().indexOf(".") + 1))) || Integer.parseInt(adminCreatePrice.getText().substring(0, adminCreatePrice.getText().indexOf("."))) < 1 || Integer.parseInt(adminCreatePrice.getText().substring(adminCreatePrice.getText().indexOf(".") + 1)) > 99) {
                        AlertBox.display("Price Invalid", 500, 200, "You did not enter a proper price (Must be two digits after the decimal).");
                        return;
                    }
                    else {
                        adminCheckSchedID.clear();
                        adminCreateDate.clear();
                        adminCheckCustID.clear();
                        adminCreateCustSeat.clear();
                        adminCreatePrice.clear();
                        AlertBox.display("Create Ticket Order Successful", 500, 200, "Successfully created a new ticket!");
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

        VBox createTrainDisplay = new VBox(20);
        createTrainDisplay.getChildren().addAll(adminCreateTrainName, adminCreateModel, adminCreateNumOfSeats);
        createTrainDisplay.setAlignment(Pos.CENTER);
        createTrainDisplay.setPadding(new Insets(0, 0, 90, 0));

        VBox createTrainStationDisplay = new VBox(20);
        createTrainStationDisplay.getChildren().addAll(adminCreateTrainStationName, adminCreateLocation);
        createTrainStationDisplay.setAlignment(Pos.CENTER);
        createTrainStationDisplay.setPadding(new Insets(0, 0, 135, 0));

        VBox createSchedDisplay = new VBox(20);
        createSchedDisplay.getChildren().addAll(adminCheckTrainName, adminCheckTrackID, adminCreateSchedOut, adminCreateSchedIn);
        createSchedDisplay.setAlignment(Pos.CENTER);
        createSchedDisplay.setPadding(new Insets(0, 0, 45, 0));

        VBox createTrackDisplay = new VBox(20);
        createTrackDisplay.getChildren().addAll(adminCreateStationFrom, adminCreateStationTo, adminCreateLength);
        createTrackDisplay.setAlignment(Pos.CENTER);
        createTrackDisplay.setPadding(new Insets(0, 0, 90, 0));

        VBox createTicketDisplay = new VBox(20);
        createTicketDisplay.getChildren().addAll(adminCheckSchedID, adminCreateDate, adminCheckCustID, adminCreateCustSeat, adminCreatePrice);
        createTicketDisplay.setAlignment(Pos.CENTER);

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
            else if(adminManipulateDropdownBox.getValue().equals("Create") && adminElementDropdownBox.getValue().equals("Ticket Order")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, createTicketDisplay, adminButtonOuterLayout);
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
            else if(adminManipulateDropdownBox.getValue().equals("Create") && adminElementDropdownBox.getValue().equals("Ticket Order")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, createTicketDisplay, adminButtonOuterLayout);
            }
            else{
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, adminButtonOuterLayout);
            }
        });

        //creating a layout for what will go on our customer Scene
        StackPane custLayout = new StackPane();
        custLayout.getChildren().add(signOutButton2);
        custLayout.setAlignment(Pos.CENTER);

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
        custUI = new Scene(custLayout, primScreenBounds.getWidth() - 10, primScreenBounds.getHeight() - 40);

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
