package project;

//all necessary imports
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.*;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.regex.Pattern;

//main class that holds all of the UI
public class Driver extends Application {
    Stage mainWindow, adminAuthorizationWindow; //the literal frame that pops up
    Scene loginUI, adminUI, custUI, createAccountUI, adminAuthorizationUI; //the different screens that we can get to within our "Stage" or frame
    Button loginButton, adminSignOutButton, CustSignOutButton, createAccountLoginButton, createAccountCreateButton, createAccountCancelButton, adminAuthorizationAuthorizeButton, adminAuthorizationCancelButton, adminSaveChangesButton, custSaveChangesButton; //button that user can interact with
    Label loginDirections, createAccountDirections1, createAccountDirections2, createAccountEmailWarning1, createAccountEmailWarning2, createAccountPersonalInfo, createAccountAccountInfo, adminDirections1, adminDirections2, adminAuthorizationDirections, adminDeleteDirections1, adminDeleteDirections2, adminDeleteDirections3, adminDeleteDirections4, adminDeleteDirections5, adminDeleteDirections6, custDirections; //String that will will appear on screen within Stage
    TextField username, createAccountName, createAccountEmail, createAccountUsername, adminUpdateCheckCustID, adminCheckTrackID, adminUpdateCheckTrainID, adminUpdateTrackID, adminUpdateSchedID, adminUpdateCheckTrainStationID, adminUpdateCheckTicketID, adminUpdateCheckTrackID, adminCheckSchedID, adminUpdateCheckSchedID, adminCreateCustName, adminCreateTrainName, adminCheckTrainName, adminCreateTrainStationName, adminCreateModel, adminCreateNumOfSeats, adminCreateLocation, adminCreateSchedIn, adminCreateSchedOut, adminCreateDate, adminCreateStationFrom, adminCreateStationTo, adminCreateLength,  adminCreateCustSeat, adminCreatePrice, adminCreateEmail, adminCreateUsername, adminUpdateCustName, adminUpdateEmail, adminUpdateUsername, adminUpdatePassword, adminUpdateConfirmPassword, adminUpdateTrainName, adminUpdateCheckTrainName,adminUpdateTrainStationName, adminUpdateModel, adminUpdatePrice, adminUpdateDate, adminUpdateSchedIn, adminUpdateSchedOut, adminUpdateNumOfSeats, adminUpdateLocation, adminUpdateStationFrom, adminUpdateStationTo, adminUpdateLength, adminUpdateCustSeat, adminDeleteCustID, adminDeleteTrainID, adminDeleteTrainStationID, adminDeleteSchedID, adminDeleteTrackID, adminDeleteTicketID, custTicketID; //Where user can input information
    PasswordField password, createAccountPassword, createAccountConfirmPassword, adminUsername, adminPassword, adminCreatePassword, adminCreateConfirmPassword; //Where user password's will be entered
    CheckBox rememberUsernameBox; //Box user can check if it wants application to remember their username after signing out
    ChoiceBox<String> accountTypeBox, adminManipulateDropdownBox, adminElementDropdownBox, custDropdownBox; //Dropdown menus
    Separator horizontalSeparator1, horizontalSeparator2, horizontalSeparator3, horizontalSeparator4, horizontalSeparator5, horizontalSeparator6, horizontalSeparator7; //Horizontal Separators used to separate information in the window more clearly
    Image mainWindowIcon, adminAuthorizationWindowIcon; //icon for window
    String emailAddress, custUsername; //email address user gives us and the username that a user inputs
    boolean result, invalidDomain; //result of ConfirmBox prompt (true means they selected "Yes") and invalidDomain is true if user input an email without a proper domain
    ArrayList<String> sqlInfo = new ArrayList<>(); //first ArrayList that holds information retrieved from SQL Database
    ArrayList<String> sqlInfo2 = new ArrayList<>(); //second ArrayList that holds information retrieved from SQL Database when we need to save info from first ArrayList
    Date currentDate; //date that will be used to determine the current date when creating a ticket
    SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd"); //the date format we need to be compatible with SQL
    String tempSQL; //string that will hold the temporary information from SQL

    @Override
    //allows GUI to load and controls what is shown at what periods and what events happen based on user input
    public void start(Stage primaryStage) throws Exception{
        mainWindow = primaryStage; //the main window that pops up when the application starts running

        //putting train icon in top left of window
        mainWindowIcon = new Image(getClass().getResourceAsStream("ApplicationIcon.png"));
        mainWindow.getIcons().add(mainWindowIcon);

        adminAuthorizationWindow = new Stage();

        //creating object that holds dimensions of user's screen
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();

        //giving title to our frame
        mainWindow.setTitle("Railway System Simulation: Login");

        //labels for login directions
        loginDirections = new Label("Welcome to Shane's Railway System Simulation! To Login, enter your username and password.");

        //labels for create account
        createAccountDirections1 = new Label("To create an account, select the account type in the dropdown menu below and enter your information.");
        createAccountDirections2 = new Label("NOTE: If you are creating an admin account, another admin must put in their information to confirm the new admin account.");
        createAccountEmailWarning1 = new Label("IMPORTANT: Entering your email address will result in an email being sent to that address stating you registered");
        createAccountEmailWarning2 = new Label("for this simulation. You may only use your email address for one account.");
        createAccountPersonalInfo = new Label("Personal Information");
        createAccountAccountInfo = new Label("Account Information");

        //labels for admin view
        adminDirections1 = new Label("As an administrator, you have the privileges of being able to create, update, and/or delete multiple elements from the Railway System.");
        adminDirections2 = new Label("In order to do so, select how you want to manipulate an element and the specific element you want to manipulate in the dropdown boxes below.");
        adminDeleteDirections1 = new Label("IMPORTANT: Deleting a customer PERMANENTLY deletes them from the system. Please be cautious.");
        adminDeleteDirections2 = new Label("IMPORTANT: Deleting a train PERMANENTLY deletes it from the system. Please be cautious.");
        adminDeleteDirections3 = new Label("IMPORTANT: Deleting a train station PERMANENTLY deletes it from the system. Please be cautious.");
        adminDeleteDirections4 = new Label("IMPORTANT: Deleting a schedule entry PERMANENTLY deletes it from the system. Please be cautious.");
        adminDeleteDirections5 = new Label("IMPORTANT: Deleting a track PERMANENTLY deletes it from the system. Please be cautious.");
        adminDeleteDirections6 = new Label("IMPORTANT: Deleting a ticket PERMANENTLY deletes it from the system. Please be cautious.");

        //labels for customer view
        custDirections = new Label("As a customer, you can purchase listed tickets or cancel a previously purchased ticket. Select what you want to do in the dropdown box below.");


        //labels for admin account authorization directions
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

        //TextField and PasswordField that hold user information on login screen
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

        //Password Fields that hold the passwords of user creating account of screen
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
        adminCheckTrainName.setTooltip(new Tooltip("Enter Pre-Existing Train ID Number"));

        adminUpdateCheckTrainName = new TextField();
        adminUpdateCheckTrainName.setMaxWidth(300);
        adminUpdateCheckTrainName.setPromptText("Update Pre-Existing Train ID Number");
        adminUpdateCheckTrainName.setTooltip(new Tooltip("Update Pre-Existing Train ID Number"));

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

        //on button click...
        loginButton.setOnAction(e -> {
            //set sqlInfo equal to whatever password came back with the given username from the Admin Table
            sqlInfo = SQL.getFromDatabase("select PASSWORD from ADMIN where USERNAME = '" + username.getText() + "';");
            //if there was a password associated with that username within the admin table
            if(SQL.wentInLoop) {
                SQL.wentInLoop = false;
                //if password in database equals the password the user entered then allow them in; if they checked the "remember me" box then their username is not cleared
                if (password.getText().equals(sqlInfo.get(0)) && !rememberUsernameBox.isSelected()) {
                    sqlInfo.clear();
                    username.clear();
                    password.clear();
                    mainWindow.setScene(adminUI);
                    mainWindow.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
                    mainWindow.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
                    mainWindow.setTitle("Railway System Simulation: Admin View");
                    return;
                    //if password in database equals the password the user entered then allow them in; clear username box
                } else if (password.getText().equals(sqlInfo.get(0)) && rememberUsernameBox.isSelected()) {
                    sqlInfo.clear();
                    password.clear();
                    mainWindow.setScene(adminUI);
                    mainWindow.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
                    mainWindow.setY(((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2));
                    mainWindow.setTitle("Railway System Simulation: Admin View");
                    return;
                }
                //if password in database does not equal the password the user entered, then don't allow them in
                else {
                    sqlInfo.clear();
                    AlertBox.display("Account Authentication Error", 500, 200, "Username or Password was incorrect. Try again.");
                    password.clear();
                }
            }
            //set sqlInfo equal to whatever password came back with the given username from the Customer Table
            sqlInfo = SQL.getFromDatabase("select PASSWORD from CUSTOMER where USERNAME = '" + username.getText() + "';");
            //if there was a password associated with that username within the customer table
            if(SQL.wentInLoop) {
                SQL.wentInLoop = false;
                //if password in database equals the password the user entered then allow them in; if they checked the "remember me" box then their username is not cleared
                if (password.getText().equals(sqlInfo.get(0)) && !rememberUsernameBox.isSelected()) {
                    custUsername = username.getText();
                    sqlInfo.clear();
                    username.clear();
                    password.clear();
                    mainWindow.setScene(custUI);
                    mainWindow.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
                    mainWindow.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
                    mainWindow.setTitle("Railway System Simulation: Customer View");
                    return;
                    //if password in database equals the password the user entered then allow them in; clear username box
                } else if (password.getText().equals(sqlInfo.get(0)) && rememberUsernameBox.isSelected()) {
                    custUsername = username.getText();
                    sqlInfo.clear();
                    password.clear();
                    mainWindow.setScene(custUI);
                    mainWindow.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
                    mainWindow.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
                    mainWindow.setTitle("Railway System Simulation: Customer View");
                    return;
                }
                //if password in database does not equal the password the user entered, then don't allow them in
                else {
                    sqlInfo.clear();
                    AlertBox.display("Account Authentication Error", 500, 200, "Username or Password was incorrect. Try again.");
                    password.clear();
                }
            }
            //if the username the user entered is not recognized, then don't let allow them in
            else {
                sqlInfo.clear();
                AlertBox.display("Account Authentication Error", 500, 200, "Username or Password was incorrect. Try again.");
                password.clear();
            }
        });

        //creating a button with text called "Sign out"; will prompt user if they actually want to sign out, then will if they hit "Yes"; appears in admin view
        adminSignOutButton = new Button("Sign Out");
        adminSignOutButton.setPrefWidth(100);
        //on button click...
        adminSignOutButton.setOnAction(e -> {
            result = ConfirmBox.display("Confirming Sign Out Request", 500, 200, "Are you sure you want to sign out of your account?");
            //if the user said "yes", then initiate popup dialogue saying logout was successful and return to login screen; appears in admin view
            if(result) {
                mainWindow.setTitle("Railway System Simulation: Login");
                mainWindow.setScene(loginUI);
                mainWindow.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
                mainWindow.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
                AlertBox.display("Sign Out Successful", 500, 200, "You have successfully signed out!");
            }
        });

        //creating a button with text called "Sign out"; will prompt user if they actually want to sign out, then will if they hit "Yes"; appears in customer view
        CustSignOutButton = new Button("Sign Out");
        adminSignOutButton.setPrefWidth(100);
        //on button click...
        CustSignOutButton.setOnAction(e -> {
            result = ConfirmBox.display("Confirming Sign Out Request", 500, 200, "Are you sure you want to sign out of your account?");
            //if the user said "yes", then initiate popup dialogue saying logout was successful and return to login screen
            if(result) {
                mainWindow.setTitle("Railway System Simulation: Login");
                mainWindow.setScene(loginUI);
                mainWindow.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
                mainWindow.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
                AlertBox.display("Sign Out Successful", 500, 200, "You have successfully signed out!");
            }
        });

        //creating a button with text called "Create Account"; will prompt user to enter in a name, email, username, and password so that they can get an account
        createAccountLoginButton = new Button("Create Account");
        //on button click...
        createAccountLoginButton.setOnAction(e -> {
            //brings up create account view where user can create an account
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
        //on button click...
        createAccountCreateButton.setOnAction(e -> {
            invalidDomain = SendEmail.readDomains(createAccountEmail.getText()); //checking if the domain is invalid

            //checking if all fields are completed by the user and have the right kind of data and formatting
            if(createAccountName.getText().equals("")) {
                AlertBox.display("Name Invalid", 500, 200, "You did not enter a proper name.");
            }
            //if the username APPEARS to be invalid, then say that the email was invalid; not perfect because it is impossible to have a list of all correct emails
            else if(createAccountEmail.getText().equals("") || invalidDomain || !SendEmail.isValidEmailAddress(createAccountEmail.getText()) || !SendEmail.isValidRegex(createAccountEmail.getText())) {
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
            //if all fields have valid information...
            else {
                SQL.wentInLoop = false;
                emailAddress = createAccountEmail.getText();
                //set SQLInfo equal to the username that is associated with the username given; really is just here to see if the username entered by the user exists in the database
                sqlInfo = SQL.getFromDatabase("select USERNAME from ADMIN where USERNAME = '" + createAccountUsername.getText() + "';");
                //checking if username is already an admin username
                if (!SQL.wentInLoop) {
                    sqlInfo = SQL.getFromDatabase("select USERNAME from CUSTOMER where USERNAME = '" + createAccountUsername.getText() + "';");
                    //checking if username is already a customer username
                    if(!SQL.wentInLoop) {
                        sqlInfo = SQL.getFromDatabase("select EMAIL from ADMIN where EMAIL = '" + createAccountEmail.getText() + "';");
                        //set SQLInfo equal to the email that is associated with the email given; really is just here to see if the email entered by the user exists in the database
                        if(!SQL.wentInLoop) {
                            sqlInfo = SQL.getFromDatabase("select EMAIL from CUSTOMER where EMAIL = '" + createAccountEmail.getText() + "';");
                            //checking if email is already a customer email
                            if(!SQL.wentInLoop) {
                                //if no information is already in the system and this is an admin creation, then prompt pre-existing admin information
                                if(accountTypeBox.getValue().equals("Administrator")) {
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
                                    //wait for an admin to enter their information and then return
                                    adminAuthorizationWindow.showAndWait();
                                    return;
                                }
                                ////if no information is already in the system and this is a customer creation, then send the customer's information to the database, creating a new customer and send an email to the email address provided
                                else {
                                    SendEmail.send(emailAddress); //sends email to customer
                                    //insert new customer into database
                                    SQL.sendToDatabase("insert into CUSTOMER (NAME, EMAIL, USERNAME, PASSWORD) values('" + createAccountName.getText() + "','" + createAccountEmail.getText() + "','" + createAccountUsername.getText() + "','" + createAccountPassword.getText() + "');");
                                    mainWindow.setScene(loginUI);
                                    mainWindow.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
                                    mainWindow.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
                                    mainWindow.setTitle("Railway System Simulation: Login");
                                    createAccountName.clear();
                                    createAccountEmail.clear();
                                    createAccountUsername.clear();
                                    createAccountPassword.clear();
                                    createAccountConfirmPassword.clear();
                                    AlertBox.display("Create Account Successful", 500, 200, "Successfully created new account! An email has been sent to the customer's email address.");
                                }
                            }
                            else {
                                SQL.wentInLoop = false;
                                AlertBox.display("Email Already Exists", 500, 200, "The email you entered already exists.");
                            }
                        }
                        else {
                            SQL.wentInLoop = false;
                            AlertBox.display("Email Already Exists", 500, 200, "The email you entered already exists.");
                        }
                    }
                    else {
                        SQL.wentInLoop = false;
                        AlertBox.display("Username Already Exists", 500, 200, "The username you entered already exists.");
                    }
                }
                else {
                    SQL.wentInLoop = false;
                    AlertBox.display("Username Already Exists", 500, 200, "The username you entered already exists.");
                }
            }
        });

        //creating a button with text called "Cancel"; will stop user from creating account and bring them back to login screen
        createAccountCancelButton = new Button("Cancel");
        //on button click...
        createAccountCancelButton.setOnAction(e -> {
            result = ConfirmBox.display("Confirming Cancellation of Account Creation", 500, 200, "Are you sure you want to cancel creating an account?");
            //if user says "yes", then they are brought to the login screen
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
        //on button click...
        adminSaveChangesButton.setOnAction(e -> {
            result = ConfirmBox.display("Confirm Changes", 500, 200, "Are you sure you want to make this change?");
            if(result) {
                if(adminManipulateDropdownBox.getValue().equals("Create")) {
                    //if an admin wants to create a customer
                    if (adminElementDropdownBox.getValue().equals("Customer")) {
                        invalidDomain = SendEmail.readDomains(adminCreateEmail.getText());
                        //checking if all information entered is valid
                        if (adminCreateEmail.getText().equals("") || invalidDomain || !SendEmail.isValidEmailAddress(adminCreateEmail.getText()) || !SendEmail.isValidRegex(adminCreateEmail.getText())) {
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
                        } 
                          //if all information entered is valid...
                          else {
                            emailAddress = adminCreateEmail.getText();
                            //set SQLInfo equal to the username that is associated with the username given; really is just here to see if the username entered by the user exists in the database
                            sqlInfo = SQL.getFromDatabase("select USERNAME from ADMIN where USERNAME = '" + adminCreateUsername.getText() + "';");
                            if (!SQL.wentInLoop) {
                                sqlInfo = SQL.getFromDatabase("select USERNAME from CUSTOMER where USERNAME = '" + adminCreateUsername.getText() + "';");
                                if(!SQL.wentInLoop) {
                                    //set SQLInfo equal to the email that is associated with the email given; really is just here to see if the email entered by the user exists in the database
                                    sqlInfo = SQL.getFromDatabase("select EMAIL from ADMIN where EMAIL = '" + adminCreateEmail.getText() + "';");
                                    if(!SQL.wentInLoop) {
                                        sqlInfo = SQL.getFromDatabase("select EMAIL from CUSTOMER where EMAIL = '" + adminCreateEmail.getText() + "';");
                                        if(!SQL.wentInLoop) {
                                            SendEmail.send(emailAddress); //send an email to the customer
                                            //inserting the new customer in the database
                                            SQL.sendToDatabase("insert into CUSTOMER (NAME, EMAIL, USERNAME, PASSWORD) values('" + adminCreateCustName.getText() + "','" + adminCreateEmail.getText() + "','" + adminCreateUsername.getText() + "','" + adminCreatePassword.getText() + "');");
                                            adminCreateCustName.clear();
                                            adminCreateEmail.clear();
                                            adminCreateUsername.clear();
                                            adminCreatePassword.clear();
                                            adminCreateConfirmPassword.clear();
                                            AlertBox.display("Create Account Successful", 500, 200, "Successfully created new account! An email has been sent to the customer's email address.");
                                        }
                                        else {
                                            SQL.wentInLoop = false;
                                            AlertBox.display("Email Already Exists", 500, 200, "The email you entered already exists.");
                                        }
                                    }
                                    else {
                                        SQL.wentInLoop = false;
                                        AlertBox.display("Email Already Exists", 500, 200, "The email you entered already exists.");
                                    }
                                }
                                else {
                                    SQL.wentInLoop = false;
                                    AlertBox.display("Username Already Exists", 500, 200, "The username you entered already exists.");
                                }
                            }
                            else {
                                SQL.wentInLoop = false;
                                AlertBox.display("Username Already Exists", 500, 200, "The username you entered already exists.");
                            }
                        }
                    }
                    //if an admin wants to create a train
                    else if (adminElementDropdownBox.getValue().equals("Train")) {
                        //check that all information entered is valid
                        if (adminCreateTrainName.getText().equals("")) {
                            AlertBox.display("Name Invalid", 500, 200, "You did not enter a proper name.");
                            return;
                        } else if (adminCreateModel.getText().equals("")) {
                            AlertBox.display("Model Invalid", 500, 200, "You did not enter a proper train model.");
                            return;
                        } else if (!(Pattern.matches("[0-9]+", adminCreateNumOfSeats.getText())) || Integer.parseInt(adminCreateNumOfSeats.getText()) < 1) {
                            AlertBox.display("Number of Seats Invalid", 500, 200, "You did not enter a proper number of seats");
                            return;
                        } 
                          //if all information entered is valid, then insert the train into the database
                          else {
                            SQL.sendToDatabase("insert into TRAIN (NAME, MODEL, NUM_OF_SEATS) values('" + adminCreateTrainName.getText() + "','" + adminCreateModel.getText() + "','" + adminCreateNumOfSeats.getText() + "');");
                            adminCreateTrainName.clear();
                            adminCreateModel.clear();
                            adminCreateNumOfSeats.clear();
                            AlertBox.display("Create Train Successful", 500, 200, "Successfully created new train!");
                        }
                    }
                    //if an admin wants to create a train station
                    else if (adminElementDropdownBox.getValue().equals("Train Station")) {
                        //check that all information entered is valid
                        if (adminCreateTrainStationName.getText().equals("")) {
                            AlertBox.display("Name Invalid", 500, 200, "You did not enter a proper name.");
                            return;
                        } else if (adminCreateLocation.getText().equals("")) {
                            AlertBox.display("Location Invalid", 500, 200, "You did not enter a proper location.");
                            return;
                        } 
                          //if all information entered is valid, then insert the train station into the database 
                          else {
                            SQL.sendToDatabase("insert into TRAIN_STATION (NAME, LOCATION) values('" + adminCreateTrainStationName.getText() + "','" + adminCreateLocation.getText() + "');");
                            adminCreateTrainStationName.clear();
                            adminCreateLocation.clear();
                            AlertBox.display("Create Train Station Successful", 500, 200, "Successfully created a new train station!");
                        }
                    }
                    //if an admin wants to create a schedule entry
                    else if (adminElementDropdownBox.getValue().equals("Schedule Entry")) {
                        //check that all information entered is valid
                        //sqlInfo equals the train id of the train given, if it doesn't exist then sqlInfo is empty and the id is invalid
                        sqlInfo = SQL.getFromDatabase("select TRAIN_ID from TRAIN where ID = " + adminCheckTrainName.getText() + ";");
                        tenpSQL = sqlInfo.get(0);
                        if (!SQL.wentInLoop) {
                            AlertBox.display("Train ID Invalid", 500, 200, "You did not enter a train ID that exists.");
                            return;
                        }
                        //sqlInfo equals the track id of the track given, if it doesn't exist then sqlInfo2 is empty and the id is invalid; the reason a second ArrayList is used is that we don't want to overwrite the other one that may have information we need
                        sqlInfo2 = SQL.getFromDatabase("select TRACK_ID from TRACK where ID = " + adminCheckTrackID.getText() + ";");
                        if (!SQL.wentInLoop) {
                            AlertBox.display("Track ID Invalid", 500, 200, "You did not enter a track ID that exists.");
                            return;
                        } else if (adminCreateSchedOut.getText().equals("") || adminCreateSchedOut.getText().length() != 5 || !(Pattern.matches("[0-9]+", adminCreateSchedOut.getText().substring(0, 2))) || !(Pattern.matches("[0-9]+", adminCreateSchedOut.getText().substring(3, 5))) || !adminCreateSchedOut.getText().substring(2, 3).equals(":") || Integer.parseInt(adminCreateSchedOut.getText().substring(0, 2)) < 1 || Integer.parseInt(adminCreateSchedOut.getText().substring(0, 2)) > 23 || Integer.parseInt(adminCreateSchedOut.getText().substring(3, 5)) < 0 || Integer.parseInt(adminCreateSchedOut.getText().substring(3, 5)) > 59) {
                            AlertBox.display("Departure Time Invalid", 500, 200, "You did not enter a proper departure time (Must be in HH:MM format).");
                            return;
                        } else if (adminCreateSchedIn.getText().equals("") || adminCreateSchedIn.getText().length() != 5 || !(Pattern.matches("[0-9]+", adminCreateSchedIn.getText().substring(0, 2))) || !(Pattern.matches("[0-9]+", adminCreateSchedIn.getText().substring(3, 5))) || !adminCreateSchedIn.getText().substring(2, 3).equals(":") || Integer.parseInt(adminCreateSchedIn.getText().substring(0, 2)) < 1 || Integer.parseInt(adminCreateSchedIn.getText().substring(0, 2)) > 23 || Integer.parseInt(adminCreateSchedIn.getText().substring(3, 5)) < 0 || Integer.parseInt(adminCreateSchedIn.getText().substring(3, 5)) > 59) {
                            AlertBox.display("Arrival Time Invalid", 500, 200, "You did not enter a proper arrival time (Must be in HH:MM format).");
                            return;
                        } 
                          //if all information entered is valid, insert the schedule entry into the database
                          else {
                            SQL.sendToDatabase("insert into SCHEDULE (TRAIN_ID, TRACK_ID, DEPARTURE_TIME, ARRIVAL_TIME) values('" + tempSQL + "','" + sqlInfo2.get(0) + "','" + adminCreateSchedOut.getText() + "','" + adminCreateSchedIn.getText() + "');");
                            adminCheckTrainName.clear();
                            adminCheckTrackID.clear();
                            adminCreateSchedOut.clear();
                            adminCreateSchedIn.clear();
                            AlertBox.display("Create Schedule Entry Successful", 500, 200, "Successfully created a new Schedule Entry!");
                        }
                    }
                    //if an admin wants to create a track
                    else if (adminElementDropdownBox.getValue().equals("Track")) {
                        //check that all information entered is valid
                        //sqlInfo equals the train station id of the train given, if it doesn't exist then sqlInfo is empty and the id is invalid
                        sqlInfo = SQL.getFromDatabase("select TRAIN_STATION_ID from TRAIN_STATION where ID = " + adminCreateStationFrom.getText() + ";");
                        tempSQL = sqlInfo.get(0); //must set a variable equal to what is in sqlInfo because of pass by reference...probably should change this up it is very sloppy
                        if (!SQL.wentInLoop) {
                            AlertBox.display("Train ID Invalid", 500, 200, "You did not enter a proper station ID to depart from.");
                            return;
                        }
                        //sqlInfo2 equals the train station id of the train station that is going to be arriving, if it doesn't exist then sqlInfo2 is empty and the id is invalid
                        sqlInfo2 = SQL.getFromDatabase("select TRAIN_STATION_ID from TRAIN_STATION where ID = " + adminCreateStationTo.getText() + ";");
                        if (!SQL.wentInLoop) {
                            AlertBox.display("Train ID Invalid", 500, 200, "You did not enter a proper station ID to arrive at.");
                            return;
                        }
                        else if (adminCreateLength.getText().equals("") || !(Pattern.matches("[0-9]+", adminCreateLength.getText())) || Integer.parseInt(adminCreateLength.getText()) < 1) {
                            AlertBox.display("Length Invalid", 500, 200, "You did not enter a proper length (Must be a whole number).");
                            return;
                        }
                        //if all information entered is valid, insert the track into the database
                        else {
                            SQL.sendToDatabase("insert into TRACK (STATION_FROM_ID, STATION_TO_ID, LENGTH) values('" + tempSQL + "','" + sqlInfo2.get(0) + "'," + adminCreateLength.getText() + ");");
                            adminCreateStationFrom.clear();
                            adminCreateStationTo.clear();
                            adminCreateLength.clear();
                            AlertBox.display("Create Track Successful", 500, 200, "Successfully created a new track!");
                        }
                    }
                    //if an admin wants to create a ticket
                    else if (adminElementDropdownBox.getValue().equals("Ticket")) {
                        try {
                            currentDate = new Date();
                            currentDate = sqlFormat.parse(sqlFormat.format(currentDate));
                        }
                        catch (ParseException excep) {
                            excep.printStackTrace(System.out);
                        }
                        //sqlInfo equals the ticket id of the ticket given, if it doesn't exist then sqlInfo is empty and the id is invalid
                        sqlInfo = SQL.getFromDatabase("select SCHEDULE_ID from SCHEDULE where ID = " + adminCheckSchedID.getText() + ";");
                        tempSQL = sqlInfo.get(0);
                        if (!SQL.wentInLoop) {
                            AlertBox.display("Schedule Entry ID Invalid", 500, 200, "You did not enter a proper schedule entry ID.");
                            return;
                        } else {
                            ////sqlInfo equals the number of seats that the train holds; this is done to determine if the admin entered in a seat number that is too large and doesn't exist
                            sqlInfo2 = SQL.getFromDatabase("select NUM_OF_SEATS from TRAIN where TRAIN_ID = (select TRAIN_ID from SCHEDULE where SCHEDULE_ID = '" + tempSQL + "');");
                            if (!(Pattern.matches("[0-9]+", adminCreateCustSeat.getText())) || Integer.parseInt(adminCreateCustSeat.getText()) < 1 || Integer.parseInt(adminCreateCustSeat.getText()) > Integer.parseInt(sqlInfo2.get(0))) {
                                AlertBox.display("Customer Seat # Invalid", 500, 200, "You did not enter a proper customer seat number.");
                                return;
                            } else if (adminCreateDate.getText().equals("") || adminCreateDate.getText().length() != 10 || !adminCreateDate.getText().substring(4, 5).equals("-") || !adminCreateDate.getText().substring(7, 8).equals("-") || !(Pattern.matches("[0-9]+", adminCreateDate.getText().substring(0, 4))) || !(Pattern.matches("[0-9]+", adminCreateDate.getText().substring(5, 7))) || !(Pattern.matches("[0-9]+", adminCreateDate.getText().substring(8, 10))) || Integer.parseInt(adminCreateDate.getText().substring(0, 4)) < 1 || Integer.parseInt(adminCreateDate.getText().substring(5, 7)) > 12 || Integer.parseInt(adminCreateDate.getText().substring(5, 7)) < 1 || Integer.parseInt(adminCreateDate.getText().substring(8, 10)) > 30 || Integer.parseInt(adminCreateDate.getText().substring(8, 10)) < 1 || Integer.parseInt(adminCreateDate.getText().substring(0, 4)) > 9999 || Integer.parseInt(adminCreateDate.getText().substring(0, 4)) != Integer.parseInt(currentDate.toString().substring(24, 28))) {
                                AlertBox.display("Date Invalid", 500, 200, "You did not enter a proper date (Must be in YYYY-MM-DD format) and must be a current or future date within this month.");
                                return;
                            } else if (adminCreatePrice.getText().equals("") || adminCreatePrice.getText().length() < 4 || !adminCreatePrice.getText().contains(".") || !(Pattern.matches("[0-9]+", adminCreatePrice.getText().substring(0, adminCreatePrice.getText().indexOf(".")))) || !(Pattern.matches("[0-9]+", adminCreatePrice.getText().substring(adminCreatePrice.getText().indexOf(".") + 1))) || Integer.parseInt(adminCreatePrice.getText().substring(0, adminCreatePrice.getText().indexOf("."))) < 1 || Integer.parseInt(adminCreatePrice.getText().substring(adminCreatePrice.getText().indexOf(".") + 1)) > 99) {
                                AlertBox.display("Price Invalid", 500, 200, "You did not enter a proper price (Must be two digits after the decimal).");
                                return;
                            } 
                            //if all information entered is valid, insert the ticket into the database
                            else {
                                SQL.sendToDatabase("insert into TICKET (SCHEDULE_ID, EVENT_DATE, SEAT, PRICE) values('" + tempSQL + "','" + adminCreateDate.getText() + "','" + adminCreateCustSeat.getText() + "'," + adminCreatePrice.getText() + ");");
                                adminCheckSchedID.clear();
                                adminCreateDate.clear();
                                adminCreateCustSeat.clear();
                                adminCreatePrice.clear();
                                AlertBox.display("Create Ticket Successful", 500, 200, "Successfully created a new ticket!");
                            }
                        }
                    }
                }
                //if admin chooses to update an element
                else if(adminManipulateDropdownBox.getValue().equals("Update")) {
                    //if an admin wants to update an already existing customer
                    if (adminElementDropdownBox.getValue().equals("Customer")) {
                        invalidDomain = SendEmail.readDomains(adminUpdateEmail.getText());
                        //sqlInfo will be populated if the customer id supplied by the admin actually exists; if sqlInfo is empty, then the customer id does not exist
                        sqlInfo = SQL.getFromDatabase("select CUST_ID from CUSTOMER where ID = '" + adminUpdateCheckCustID.getText() + "';");
                        if (SQL.wentInLoop) {
                            SQL.wentInLoop = false;
                            //checking if information entered was invalid
                            if (adminUpdateCheckCustID.getText().equals("")) {
                                AlertBox.display("Customer ID Invalid", 500, 200, "You did not enter a proper pre-existing customer ID.");
                            } else if (adminUpdateEmail.getText().equals("") || invalidDomain || !SendEmail.isValidEmailAddress(adminUpdateEmail.getText()) || !SendEmail.isValidRegex(adminUpdateEmail.getText())) {
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
                            } 
                            //if information entered is valid, then check if the username and email entered already exists
                            else {
                                sqlInfo = SQL.getFromDatabase("select USERNAME from CUSTOMER where USERNAME = '" + adminUpdateUsername.getText() + "';");
                                if (!SQL.wentInLoop) {
                                    sqlInfo = SQL.getFromDatabase("select USERNAME from ADMIN where USERNAME = '" + adminUpdateUsername.getText() + "';");
                                    if (!SQL.wentInLoop) {
                                        sqlInfo = SQL.getFromDatabase("select EMAIL from CUSTOMER where EMAIL = '" + adminUpdateEmail.getText() + "';");
                                        if (!SQL.wentInLoop) {
                                            sqlInfo = SQL.getFromDatabase("select EMAIL from ADMIN where EMAIL = '" + adminUpdateEmail.getText() + "';");
                                            //if the username and email don't already exist, then update the customer in the database based on the given information
                                            if (!SQL.wentInLoop) {
                                                emailAddress = adminUpdateEmail.getText();
                                                SQL.sendToDatabase("update CUSTOMER set NAME = '" + adminUpdateCustName.getText() + "', EMAIL = '" + adminUpdateEmail.getText() + "', USERNAME = '" + adminUpdateUsername.getText() + "', PASSWORD = '" + adminUpdatePassword.getText() + "' where ID = '" + adminUpdateCheckCustID.getText() + "';");
                                                SendEmail.send(emailAddress);
                                                adminUpdateCheckCustID.clear();
                                                adminUpdateCustName.clear();
                                                adminUpdateEmail.clear();
                                                adminUpdateUsername.clear();
                                                adminUpdatePassword.clear();
                                                adminUpdateConfirmPassword.clear();
                                                AlertBox.display("Update Account Successful", 500, 200, "Successfully updated account! An email has been sent to the customer's email address.");
                                            } else {
                                                SQL.wentInLoop = false;
                                                AlertBox.display("Email Already Exists", 500, 200, "The email you entered already exists.");
                                            }
                                        } else {
                                            SQL.wentInLoop = false;
                                            AlertBox.display("Email Already Exists", 500, 200, "The email you entered already exists.");
                                        }
                                    } else {
                                        SQL.wentInLoop = false;
                                        AlertBox.display("Username Already Exists", 500, 200, "The username you entered already exists.");
                                    }
                                } else {
                                    SQL.wentInLoop = false;
                                    AlertBox.display("Username Already Exists", 500, 200, "The username you entered already exists.");
                                }
                            }
                        } else {
                            AlertBox.display("Customer ID Invalid", 500, 200, "You did not enter a proper pre-existing customer ID.");
                        }
                    }
                    //if an admin wants to update an already existing train
                    else if (adminElementDropdownBox.getValue().equals("Train")) {
                        //check if information entered is valid
                        if (adminUpdateCheckTrainID.getText().equals(" ")) {
                            AlertBox.display("Train ID Invalid", 500, 200, "You did not enter a proper pre-existing train ID.");
                        } else if (adminUpdateTrainName.getText().equals("")) {
                            AlertBox.display("Name Invalid", 500, 200, "You did not enter a proper name.");
                            return;
                        } else if (adminUpdateModel.getText().equals("")) {
                            AlertBox.display("Model Invalid", 500, 200, "You did not enter a proper train model.");
                            return;
                        } else if (!(Pattern.matches("[0-9]+", adminUpdateNumOfSeats.getText())) || Integer.parseInt(adminUpdateNumOfSeats.getText()) < 1) {
                            AlertBox.display("Number of Seats Invalid", 500, 200, "You did not enter a proper number of seats");
                            return;
                        } else {
                            //if all informaion is valid, update the train based on the information given by the admin
                            SQL.sendToDatabase("update TRAIN set NAME = '" + adminUpdateTrainName.getText() + "', MODEL = '" + adminUpdateModel.getText() + "', NUM_OF_SEATS = '" + adminUpdateNumOfSeats.getText() + "' where ID = " + adminUpdateCheckTrainID.getText() + ";");
                            adminUpdateCheckTrainID.clear();
                            adminUpdateTrainName.clear();
                            adminUpdateModel.clear();
                            adminUpdateNumOfSeats.clear();
                            AlertBox.display("Update Train Successful", 500, 200, "Successfully updated train!");
                        }
                    }
                    //if an admin wants to update an already existing train station
                    else if (adminElementDropdownBox.getValue().equals("Train Station")) {
                        //check if all information is valid
                        if (adminUpdateCheckTrainStationID.getText().equals(" ")) {
                            AlertBox.display("Train Station ID Invalid", 500, 200, "You did not enter a proper pre-existing train station ID.");
                        } else if (adminUpdateTrainStationName.getText().equals("")) {
                            AlertBox.display("Name Invalid", 500, 200, "You did not enter a proper name.");
                            return;
                        } else if (adminUpdateLocation.getText().equals("")) {
                            AlertBox.display("Location Invalid", 500, 200, "You did not enter a proper location.");
                            return;
                        } 
                        //if all information is valid, update the train station based on admin information
                        else {
                            SQL.sendToDatabase("update TRAIN_STATION set NAME = '" + adminUpdateTrainStationName.getText() + "', LOCATION = '" + adminUpdateLocation.getText() + "' where ID = " + adminUpdateCheckTrainStationID.getText() + ";");
                            adminUpdateCheckTrainStationID.clear();
                            adminUpdateTrainStationName.clear();
                            adminUpdateLocation.clear();
                            AlertBox.display("Update Train Station Successful", 500, 200, "Successfully updated a train station!");
                        }
                    }
                    //if an admin wants to update an already existing schedule entry
                    else if (adminElementDropdownBox.getValue().equals("Schedule Entry")) {
                        //check if all information is valid
                        if (adminUpdateSchedID.getText().equals(" ")) {
                            AlertBox.display("Schedule Entry ID Invalid", 500, 200, "You did not enter a proper pre-existing schedule entry ID.");
                        }
                        sqlInfo = SQL.getFromDatabase("select TRAIN_ID from TRAIN where ID = " + adminUpdateCheckTrainName.getText() + ";");
                        if (!SQL.wentInLoop) {
                            AlertBox.display("Train ID Invalid", 500, 200, "You did not enter a train ID that exists.");
                            return;
                        }
                        sqlInfo2 = SQL.getFromDatabase("select TRACK_ID from TRACK where ID = " + adminUpdateCheckTrackID.getText() + ";");
                        tempSQL = sqlInfo.get(0);
                        if (!SQL.wentInLoop) {
                            AlertBox.display("Track ID Invalid", 500, 200, "You did not enter a track ID that exists.");
                            return;
                        } else if (adminUpdateSchedOut.getText().equals("") || adminUpdateSchedOut.getText().length() != 5 || !(Pattern.matches("[0-9]+", adminUpdateSchedOut.getText().substring(0, 2))) || !(Pattern.matches("[0-9]+", adminUpdateSchedOut.getText().substring(3, 5))) || !adminUpdateSchedOut.getText().substring(2, 3).equals(":") || Integer.parseInt(adminUpdateSchedOut.getText().substring(0, 2)) < 1 || Integer.parseInt(adminUpdateSchedOut.getText().substring(0, 2)) > 23 || Integer.parseInt(adminUpdateSchedOut.getText().substring(3, 5)) < 0 || Integer.parseInt(adminUpdateSchedOut.getText().substring(3, 5)) > 59) {
                            AlertBox.display("Departure Time Invalid", 500, 200, "You did not enter a proper departure time (Must be in HH:MM format).");
                            return;
                        } else if (adminUpdateSchedIn.getText().equals("") || adminUpdateSchedIn.getText().length() != 5 || !(Pattern.matches("[0-9]+", adminUpdateSchedIn.getText().substring(0, 2))) || !(Pattern.matches("[0-9]+", adminUpdateSchedIn.getText().substring(3, 5))) || !adminUpdateSchedIn.getText().substring(2, 3).equals(":") || Integer.parseInt(adminUpdateSchedIn.getText().substring(0, 2)) < 1 || Integer.parseInt(adminUpdateSchedIn.getText().substring(0, 2)) > 23 || Integer.parseInt(adminUpdateSchedIn.getText().substring(3, 5)) < 0 || Integer.parseInt(adminUpdateSchedIn.getText().substring(3, 5)) > 59) {
                            AlertBox.display("Arrival Time Invalid", 500, 200, "You did not enter a proper arrival time (Must be in HH:MM format).");
                            return;
                        } 
                          //if information is valid, then update schedule the schedule based on the information
                          else {
                            SQL.sendToDatabase("update SCHEDULE set TRAIN_ID = '" + tempSQL + "', TRACK_ID = '" + sqlInfo2.get(0) + "', DEPARTURE_TIME = '" + adminUpdateSchedOut.getText() + "', ARRIVAL_TIME = '" + adminUpdateSchedIn.getText() + "' where ID = " + adminUpdateSchedID.getText() + ";");
                            adminUpdateSchedID.clear();
                            adminUpdateCheckTrainName.clear();
                            adminUpdateCheckTrackID.clear();
                            adminUpdateSchedOut.clear();
                            adminUpdateSchedIn.clear();
                            AlertBox.display("Update Schedule Entry Successful", 500, 200, "Successfully updated a Schedule Entry!");
                        }
                    }
                    //if an admin wants to update an already existing track
                    else if (adminElementDropdownBox.getValue().equals("Track")) {
                        if (adminUpdateTrackID.getText().equals(" ")) {
                            AlertBox.display("Schedule Entry ID Invalid", 500, 200, "You did not enter a proper pre-existing track ID.");
                        }
                        //if sqlInfo is occupied, then that means the train station from id supplied exists
                        sqlInfo = SQL.getFromDatabase("select TRAIN_STATION_ID from TRAIN_STATION where ID = " + adminUpdateStationFrom.getText() + ";");
                        tempSQL = sqlInfo.get(0); //setting variable equal to sqlInfo so that information isn't lost due to pass by reference
                        if (!SQL.wentInLoop) {
                            AlertBox.display("Train ID Invalid", 500, 200, "You did not enter a proper station ID to depart from.");
                            return;
                        }
                        //if sqlInfo2 is occupied, then that means the train station to id supplied exists
                        sqlInfo2 = SQL.getFromDatabase("select TRAIN_STATION_ID from TRAIN_STATION where ID = " + adminUpdateStationTo.getText() + ";");
                        if (!SQL.wentInLoop) {
                            AlertBox.display("Track ID Invalid", 500, 200, "You did not enter a proper station ID to arrive at.");
                            return;
                        } else if (adminUpdateLength.getText().equals("") || !(Pattern.matches("[0-9]+", adminUpdateLength.getText())) || Integer.parseInt(adminUpdateLength.getText()) < 1) {
                            AlertBox.display("Length Invalid", 500, 200, "You did not enter a proper length (Must be a whole number).");
                            return;
                        } 
                          //if all info provided is valid, then update the track with the new information given by the admin
                          else {
                            SQL.sendToDatabase("update TRACK set STATION_FROM_ID = '" + tempSQL + "', STATION_TO_ID = '" + sqlInfo2.get(0) + "', LENGTH = " + adminUpdateLength.getText() + " where ID = " + adminUpdateTrackID.getText() + ";");
                            adminUpdateTrackID.clear();
                            adminUpdateStationFrom.clear();
                            adminUpdateStationTo.clear();
                            adminUpdateLength.clear();
                            AlertBox.display("Update Track Successful", 500, 200, "Successfully updated a track!");
                        }
                    }
                    //if an admin wants to update an already existing ticket
                    else if (adminElementDropdownBox.getValue().equals("Ticket")) {
                        if (adminUpdateCheckTicketID.getText().equals(" ")) {
                            AlertBox.display("Ticket ID Invalid", 500, 200, "You did not enter a proper pre-existing ticket ID.");
                        }
                        //if sqlInfo is occupied, then that means the schedule entry id supplied exists
                        sqlInfo = SQL.getFromDatabase("select SCHEDULE_ID from SCHEDULE where ID = " + adminUpdateCheckSchedID.getText() + ";");
                        if (!SQL.wentInLoop) {
                            AlertBox.display("Schedule Entry ID Invalid", 500, 200, "You did not enter a proper schedule entry ID.");
                            return;
                        } else {
                            //sqlInfo holds how many seats are in the train given by the ID
                            sqlInfo2 = SQL.getFromDatabase("select NUM_OF_SEATS from TRAIN where TRAIN_ID = (select TRAIN_ID from SCHEDULE where SCHEDULE_ID = " + tempSQL + ");");
                            //check if information is valid
                            if (!(Pattern.matches("[0-9]+", adminUpdateCustSeat.getText())) || Integer.parseInt(adminUpdateCustSeat.getText()) < 1 || Integer.parseInt(adminUpdateCustSeat.getText()) > Integer.parseInt(sqlInfo2.get(0))) {
                                AlertBox.display("Customer Seat # Invalid", 500, 200, "You did not enter a proper customer seat number.");
                                return;
                            } else if (adminUpdateDate.getText().equals("") || adminUpdateDate.getText().length() != 10 || !adminUpdateDate.getText().substring(4, 5).equals("-") || !adminUpdateDate.getText().substring(7, 8).equals("-") || !(Pattern.matches("[0-9]+", adminUpdateDate.getText().substring(0, 4))) || !(Pattern.matches("[0-9]+", adminUpdateDate.getText().substring(5, 7))) || !(Pattern.matches("[0-9]+", adminUpdateDate.getText().substring(8, 10))) || Integer.parseInt(adminUpdateDate.getText().substring(0, 4)) < 1 || Integer.parseInt(adminUpdateDate.getText().substring(5, 7)) > 12 || Integer.parseInt(adminUpdateDate.getText().substring(5, 7)) < 1 || Integer.parseInt(adminUpdateDate.getText().substring(8, 10)) > 30 || Integer.parseInt(adminUpdateDate.getText().substring(8, 10)) < 1 || Integer.parseInt(adminUpdateDate.getText().substring(0, 4)) > 9999) {
                                AlertBox.display("Date Invalid", 500, 200, "You did not enter a proper date (Must be in YYYY-MM-DD format).");
                                return;
                            } else if (adminUpdatePrice.getText().equals("") || adminUpdatePrice.getText().length() < 4 || !adminUpdatePrice.getText().contains(".") || !(Pattern.matches("[0-9]+", adminUpdatePrice.getText().substring(0, adminUpdatePrice.getText().indexOf(".")))) || !(Pattern.matches("[0-9]+", adminUpdatePrice.getText().substring(adminUpdatePrice.getText().indexOf(".") + 1))) || Integer.parseInt(adminUpdatePrice.getText().substring(0, adminUpdatePrice.getText().indexOf("."))) < 1 || Integer.parseInt(adminUpdatePrice.getText().substring(adminUpdatePrice.getText().indexOf(".") + 1)) > 99) {
                                AlertBox.display("Price Invalid", 500, 200, "You did not enter a proper price (Must be two digits after the decimal).");
                                return;
                            } 
                            //if all information is valid, update the ticket based on the admin's input
                            else {
                                SQL.sendToDatabase("update TICKET set SCHEDULE_ID = '" + tempSQL + "', EVENT_DATE = '" + adminUpdateDate.getText() + "', SEAT = " + adminUpdateCustSeat.getText() + ", PRICE = '" + adminUpdatePrice.getText() + "' where ID = " + adminUpdateCheckTicketID.getText() + ";");
                                adminUpdateCheckTicketID.clear();
                                adminUpdateCheckSchedID.clear();
                                adminUpdateCustSeat.clear();
                                adminUpdateDate.clear();
                                adminUpdatePrice.clear();
                                AlertBox.display("Update Ticket Successful", 500, 200, "Successfully updated a ticket!");
                            }
                        }
                    }
                }
                //if user wants to delete elements
                else if(adminManipulateDropdownBox.getValue().equals("Delete")) {
                    //if user wants to delete a customer
                    if(adminElementDropdownBox.getValue().equals("Customer")) {
                        //if id entered is invalid
                        if(adminDeleteCustID.getText().equals("")) {
                            AlertBox.display("Customer ID Invalid", 500, 200, "You did not enter a proper pre-existing customer ID.");
                            return;
                        }
                        //if id entered is valid, then delete the customer
                        else {
                            SQL.sendToDatabase("delete from CUSTOMER where ID = " + adminDeleteCustID.getText());
                            adminDeleteCustID.clear();
                            AlertBox.display("Delete Customer Successful", 500, 200, "Successfully deleted a customer!");
                        }
                    }
                    else if(adminElementDropdownBox.getValue().equals("Train")) {
                        //if user wants to delete a train
                        if(adminDeleteTrainID.getText().equals("")) {
                            //if id entered is invalid
                            AlertBox.display("Train ID Invalid", 500, 200, "You did not enter a proper pre-existing train ID.");
                            return;
                        }
                        //if id entered is valid, then delete the train
                        else {
                            SQL.sendToDatabase("delete from TRAIN where ID = " + adminDeleteTrainID.getText());
                            adminDeleteTrainID.clear();
                            AlertBox.display("Delete Train Successful", 500, 200, "Successfully deleted a train!");
                        }
                    }
                    else if(adminElementDropdownBox.getValue().equals("Train Station")) {
                        //if user wants to delete a train station
                        if(adminDeleteTrainStationID.getText().equals("")) {
                            //if id entered is invalid
                            AlertBox.display("Train Station ID Invalid", 500, 200, "You did not enter a proper pre-existing train station ID.");
                            return;
                        }
                        //if id entered is valid, then delete the train station
                        else {
                            SQL.sendToDatabase("delete from TRAIN_STATION where ID = " + adminDeleteTrainStationID.getText());
                            adminDeleteTrainStationID.clear();
                            AlertBox.display("Delete Train Station Successful", 500, 200, "Successfully deleted a train station!");
                        }
                    }
                    else if(adminElementDropdownBox.getValue().equals("Schedule Entry")) {
                        //if user wants to delete a schedule entry
                        if(adminDeleteSchedID.getText().equals("")) {
                            //if id entered is invalid
                            AlertBox.display("Schedule Entry ID Invalid", 500, 200, "You did not enter a proper pre-existing schedule entry ID.");
                            return;
                        }
                        //if id entered is valid, then delete the schedule entry
                        else {
                            SQL.sendToDatabase("delete from SCHEDULE where ID = " + adminDeleteSchedID.getText());
                            adminDeleteSchedID.clear();
                            AlertBox.display("Delete Schedule Successful", 500, 200, "Successfully deleted a schedule entry!");
                        }
                    }
                    else if(adminElementDropdownBox.getValue().equals("Track")) {
                        //if user wants to delete a track
                        if(adminDeleteTrackID.getText().equals("")) {
                            //if id entered is invalid
                            AlertBox.display("Track ID Invalid", 500, 200, "You did not enter a proper pre-existing track ID.");
                            return;
                        }
                        //if id entered is valid, then delete the track
                        else {
                            SQL.sendToDatabase("delete from TRACK where ID = " + adminDeleteTrackID.getText());
                            adminDeleteTrackID.clear();
                            AlertBox.display("Delete Track Successful", 500, 200, "Successfully deleted a track!");
                        }
                    }
                    else if(adminElementDropdownBox.getValue().equals("Ticket")) {
                        //if user wants to delete a ticket
                        if(adminDeleteTicketID.getText().equals("")) {
                            //if id entered is invalid
                            AlertBox.display("Ticket ID Invalid", 500, 200, "You did not enter a proper pre-existing ticket ID.");
                            return;
                        }
                        //if id entered is valid, then delete the ticket
                        else {
                            SQL.sendToDatabase("delete from TICKET where ID = " + adminDeleteTicketID.getText());
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
        //on button click...
        custSaveChangesButton.setOnAction(e -> {
            result = ConfirmBox.display("Confirm Changes", 500, 200, "Are you sure you want to make this change?");
            if(result) {
                //sqlInfo holds the ticket id and owner id based on the ticket id input; if ticket id doesn't exist or another customer already owns the ticket, then a ticket will not be purchased
                sqlInfo = SQL.getFromDatabase("select TICKET_ID,OWNER_ID from TICKET where ID = " + custTicketID.getText() + ";");
                if (SQL.wentInLoop) {
                    SQL.wentInLoop = false;
                    //if there is no owner of the ticket, the customer can have it
                    if (sqlInfo.get(1) == null) {
                        if (custDropdownBox.getValue().equals("Purchase")) {
                            //update the ticket so that the owner is equivalent to the customer id of the buyer
                            SQL.sendToDatabase("Update TICKET set OWNER_ID = (select CUST_ID from CUSTOMER where USERNAME = '" + custUsername + "');");
                            custTicketID.clear();
                            AlertBox.display("Purchase Ticket Successful", 500, 200, "You have successfully purchased a ticket!");
                            return;
                        }
                        //but if a customer is trying to cancel a ticket they don't own, then they must be stopped
                        else if (custDropdownBox.getValue().equals("Cancel")) {
                            AlertBox.display("Invalid Ticket Cancel", 500, 200, "You are trying to cancel a ticket you don't own.");
                            return;
                        }
                    } 
                     //if there is an owner of the ticket
                     else {
                        //find the username of the person that owns the ticket
                        sqlInfo = SQL.getFromDatabase("select USERNAME from CUSTOMER where CUST_ID = '" + sqlInfo.get(1) + "';");
                        if (SQL.wentInLoop) {
                            SQL.wentInLoop = false;
                            //if the customer is trying to buy a ticket they already own
                            if (custDropdownBox.getValue().equals("Purchase") && (sqlInfo.get(0).equals(custUsername))) {
                                custTicketID.clear();
                                AlertBox.display("Invalid Ticket Purchase", 500, 200, "You already own this ticket.");
                            }
                            //if the customer is trying to buy a ticket someone else owns
                            else if (custDropdownBox.getValue().equals("Purchase") && !(sqlInfo.get(0).equals(custUsername))) {
                                custTicketID.clear();
                                AlertBox.display("Invalid Ticket Purchase", 500, 200, "This ticket was already purchased by another user.");
                            } 
                            //if the customer is trying to cancel and they own the ticket
                            else if (custDropdownBox.getValue().equals("Cancel") && (sqlInfo.get(0).equals(custUsername))) {
                                custTicketID.clear();
                                //update the ticket owner to NULL as now there is no owner of the ticket
                                SQL.sendToDatabase("update TICKET set OWNER_ID = NULL;");
                                AlertBox.display("Cancel Ticket Successful", 500, 200, "You have successfully cancelled a ticket!");
                            }
                        }
                    }
                } 
                //if the ticket id entered doesn't exist
                else {
                    AlertBox.display("Ticket ID Does Not Exist", 500, 200, "You have entered a ticket ID that does not exist.");
                }
            }
        });

        //creating a new button with text called "Authorize"; will open confirmation box confirming that the admin wants to add a new admin
        adminAuthorizationAuthorizeButton = new Button("Authorize");
        adminAuthorizationAuthorizeButton.setPrefWidth(80);
        //on button click...
        adminAuthorizationAuthorizeButton.setOnAction(e -> {
            result = ConfirmBox.display("Authorize New Administrator User", 500, 200, "Are you sure you want to create a new Administrator user?");
            //if no information is duplicated and the pre-existing admin said they wanted to create another admin, then create the admin account and send an email
            if(result) {
                SendEmail.send(createAccountEmail.getText()); //send email to the new admin
                adminAuthorizationWindow.close();
                mainWindow.setScene(loginUI);
                mainWindow.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
                mainWindow.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
                mainWindow.setTitle("Railway System Simulation: Login");
                //create the new admin within the database
                SQL.sendToDatabase("insert into ADMIN (NAME, EMAIL, USERNAME, PASSWORD) values('" + createAccountName.getText() + "','" + createAccountEmail.getText() + "','" + createAccountUsername.getText() + "','" + createAccountPassword.getText() + "');");
                AlertBox.display("Create Account Successful", 500, 200, "Successfully created new account! An email has been sent to your email address.");
                return;
            }
        });

        //creating a new button with text called "Cancel"; will stop pre-existing admin from creating a new admin user
        adminAuthorizationCancelButton = new Button("Cancel");
        adminAuthorizationCancelButton.setPrefWidth(80);
        //on button click...
        adminAuthorizationCancelButton.setOnAction(e -> {
            result = ConfirmBox.display("Cancellation of New Adminstrator User", 500, 200, "Are you sure you do not want to create a new Administrator user?");
            //if admin confirms they don't want to create another admin
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

        //creating a layout for what CAN appear on our admin Scene
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
        adminButtonInnerLayout.getChildren().addAll(adminSaveChangesButton, adminSignOutButton);
        adminButtonOuterLayout.getChildren().addAll(horizontalSeparator5, adminButtonInnerLayout);
        adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, createCustDisplay, adminButtonOuterLayout);
        adminDirectionsLayout.setAlignment(Pos.CENTER);
        adminDropdownInnerLayout.setAlignment(Pos.CENTER);
        adminDropdownOuterLayout.setAlignment(Pos.CENTER);
        adminButtonInnerLayout.setAlignment(Pos.CENTER);
        adminButtonOuterLayout.setAlignment(Pos.CENTER);
        adminOuterLayout.setAlignment(Pos.CENTER);
        adminOuterLayout.setPadding(new Insets(20, 30, 20, 30));

        //when dropdown menu selection changes...
        adminManipulateDropdownBox.setOnAction(e -> {
            //if the first dropdown menu is "Create" and the second is "Customer"
            if(adminManipulateDropdownBox.getValue().equals("Create") && adminElementDropdownBox.getValue().equals("Customer")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, createCustDisplay, adminButtonOuterLayout);
            }
            //...
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
            else if(adminManipulateDropdownBox.getValue().equals("Create") && adminElementDropdownBox.getValue().equals("Schedule Entry")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, createSchedDisplay, adminButtonOuterLayout);
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

        //on dropdown menu selection changes; important to note that we need two of these because if we don't then the Scene will only update when the first dropdown menu is changed
        adminElementDropdownBox.setOnAction(event -> {
            //if the first dropdown menu is "Create" and the second is "Customer"
            if(adminManipulateDropdownBox.getValue().equals("Create") && adminElementDropdownBox.getValue().equals("Customer")) {
                adminOuterLayout.getChildren().clear();
                adminOuterLayout.getChildren().addAll(adminDropdownOuterLayout, createCustDisplay, adminButtonOuterLayout);
            }
            //...
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
        custButtonLayout.getChildren().addAll(custSaveChangesButton, CustSignOutButton);
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
