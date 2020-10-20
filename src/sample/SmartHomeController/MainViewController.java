package sample.SmartHomeController;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sample.SmartHomeModel.HouseModel;
import sample.SmartHomeModel.RoomModel;
import sample.SmartHomeModel.UserModel;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Class for the Main view controller.
 */
public class MainViewController {

    /**
     * A controller which handles the house and user information (i.e. the simulation data).
     */
    private SimulationDataController simulationDataController;

    /**
     * A controller which handles the drawing of the house layout.
     */
    private HouseViewController houseViewController;

    /**
     * A controller which handles the modification of the simulation data.
     */
    private SHSController shsController;

    /**
     * An array with all the rooms of the house.
     */
    private RoomModel[] roomArray;

    /**
     * A instance of a HouseModel object which represents the house itself.
     */
    private HouseModel houseModel;

    /**
     * A list which contains all the user profiles present in the simulation.
     */
    private ArrayList<UserModel> userModelArrayList;

    /**
     * A list which contains all the names of the rooms of the house.
     */
    private ArrayList<String> roomNameArrayList;

    /**
     * The BorderPane holding the house layout.
     */
    @FXML
    BorderPane bp;

    /**
     * The SHS GridPane containing the SHS module.
     */
    @FXML
    GridPane gridSHS;

    /**
     * The SHC GridPane containing the SHC module.
     */
    @FXML
    GridPane gridSHC;

    /**
     * The SHP GridPane containing the SHP module.
     */
    @FXML
    GridPane gridSHP;

    /**
     * The SHH GridPane containing the SHH module.
     */
    @FXML
    GridPane gridSHH;

    /**
     * The GridPane where the user can add a new module.
     */
    @FXML
    GridPane gridAddModule;

    /**
     * The Button used to start/stop the simulation.
     */
    @FXML
    Button turnOnOffSimulation;

    /**
     * A ComboBox containing the different permission levels (roles) that a user can have.
     */
    @FXML
    ComboBox<String> addModifyRoleComboBoxSHS;

    /**
     * A ComboBox containing the different locations that a user can be in.
     */
    @FXML
    ComboBox<String> addModifyLocComboBoxSHS;

    /**
     * A ComboBox containing the different locations that a user can block a window in.
     */
    @FXML
    ComboBox<String> blockWinLocComboBoxSHS;

    /**
     * The SHS table which displays all the users that are present in the simulation.
     */
    @FXML
    TableView<UserModel> userTable;

    /**
     * The date of the simulation.
     */
    @FXML
    JFXDatePicker dateSHS;

    /**
     * The time of the simulation.
     */
    @FXML
    JFXTimePicker timeSHS;

    /**
     * TextField used to specify the name of a new user or to modify the name of an existing user.
     */
    @FXML
    TextField addModifyUserName;

    /**
     * TabPane used as a container which holds the different modules tabs (one tab per module).
     */
    @FXML
    TabPane moduleTabs;

    /**
     * The TextArea used where the console messages will print.
     */
    @FXML
    TextArea consoleTextField;

    /**
     * Spinner used to specify the ID of a new or existing user.
     */
    @FXML
    Spinner<Integer> addModifyUserID;

    /**
     * Spinner used to specify the ID of an existing user that the user wants to removed.
     */
    @FXML
    Spinner<Integer> userIdToRemove;

    /**
     * Spinner used to specify the ID of an existing user that the user wants to log in as.
     */
    @FXML
    Spinner<Integer> userIdToLogin;

    /**
     * Spinner used to specify the outside temperature of the simulation.
     */
    @FXML
    Spinner<Double> outTempSHS;

    /**
     * Spinner used to specify the inside temperature of the simulation.
     */
    @FXML
    Spinner<Double> inTempSHS;

    /**
     * Button used to save the date of the simulation.
     */
    @FXML
    Button saveDate;

    /**
     * Button used to save the time of the simulation.
     */
    @FXML
    Button saveTime;

    /**
     * Button used to log in as a user with the specified ID.
     */
    @FXML
    Button loginButton;

    /**
     * Button used to save the outside temperature of the simulation.
     */
    @FXML
    Button saveOutsideTemp;

    /**
     * Button used to save the inside temperature of the simulation.
     */
    @FXML
    Button saveInsideTemp;

    /**
     * Button used to save the location in which the window must be blocked by an object.
     */
    @FXML
    Button saveWindowBlock;

    /**
     * VBox as the container (left-side panel) which contains the start/stop simulation button as well as
     * general information about the simulation and the logged-in user.
     */
    @FXML
    VBox locationsVBox;

    /**
     * The Label displaying the simulation date in the left-side panel.
     */
    @FXML
    Label leftPanelDate;

    /**
     * The Label displaying the simulation time in the left-side panel.
     */
    @FXML
    Label leftPanelTime;

    /**
     * The Label displaying the inside temperature of the simulation in the left-side panel.
     */
    @FXML
    Label leftPanelInTemp;

    /**
     * The Label displaying the outside temperature of the simulation in the left-side panel.
     */
    @FXML
    Label leftPanelOutTemp;

    /**
     * The Label which warns the user of the conditions that must be met before being able to start the simulation.
     */
    @FXML
    Label warningLabelSimulation;

    /**
     * The Label displaying the name of the user that is currently logged into the simulation.
     */
    @FXML
    Label userNameLabel;

    /**
     * The Label displaying the ID of the user that is currently logged into the simulation.
     */
    @FXML
    Label userIDLabel;

    /**
     * The Label displaying the location of the user that is currently logged into the simulation.
     */
    @FXML
    Label userLocationLabel;

    /**
     * The Label displaying the permission level of the user that is currently logged into the simulation.
     */
    @FXML
    Label userPermissionLabel;

    /**
     * A TextField where the user enters the path of the house layout JSON file that they wish to upload.
     */
    @FXML
    TextField houseLayoutFilePath;

    /**
     * A Button which saves the path of the house layout JSON file that the user wishes to upload.
     */
    @FXML
    Button saveHouseLayoutFilePath;

    /**
     * A Label which describes the section where the user must upload a path to the house layout JSON file.
     */
    @FXML
    Label labelHouseLayoutFile;

    /**
     * A Label which informs the user of any issues that many arise during the processing of the path to the house layout JSON file.
     */
    @FXML
    Label errorLabelHouseLayoutFile;

    /**
     * A container which holds the default avatar profile picture of the logged-in user.
     */
    @FXML
    ImageView avatarImageView;

    /**
     * A Label which describes the section that holds general user information (avatar, name, ID, permission level, and
     * location).
     */
    @FXML
    Label labelUserInfoLeftPanel;

    /**
     * A label which describes the section that holds general simulation parameters (date, time, inside temperature, and
     * outside temperature).
     */
    @FXML
    Label labelLeftPanelSimParam;

    /**
     * A boolean indicating whether a user is logged into the simulation or not.
     */
    private boolean isLoggedIn = false;

    /**
     * The list of users that are currently in the simulation.
     */
    private ObservableList<UserModel> data;

    /**
     * Information about the user that has either been added, modified, or logged-in as.
     */
    private Object[] userInfo;

    /**
     * All the information on the user that is currently logged in.
     */
    private UserModel loggedInUser = null;

    /**
     * Instantiate a new Main view controller.
     */
    public MainViewController() {
        simulationDataController = new SimulationDataController();
        houseViewController = new HouseViewController();
        shsController = new SHSController();
    }

    /**
     * Initialize the simulator by restricting the access of the UI until the user uploads
     * a path of the house layout JSON file that they wishes to upload.
     */
    @FXML
    public void initialize() {
        turnOnOffSimulation.setDisable(true);
        moduleTabs.setDisable(true);

        showUIElement(avatarImageView, false);
        showUIElement(userNameLabel, false);
        showUIElement(userIDLabel, false);
        showUIElement(userPermissionLabel, false);
        showUIElement(userLocationLabel, false);
        showUIElement(leftPanelDate, false);
        showUIElement(leftPanelTime, false);
        showUIElement(leftPanelOutTemp, false);
        showUIElement(leftPanelInTemp, false);
        showUIElement(labelLeftPanelSimParam, false);
        showUIElement(labelUserInfoLeftPanel, false);
    }

    /**
     * Add users to the SHS table.
     */
    private void loadUsersInSHSTable() {
        data = FXCollections.observableArrayList();
        data.addAll(userModelArrayList);
    }

    /**
     * Draw the layout of the house.
     */
    @FXML
    private void drawLayout() {
        System.out.println(bp.getChildren().isEmpty());
        houseViewController.drawLayout(roomArray, bp, houseModel, userModelArrayList);
    }

    /**
     * Set outside temperature.
     *
     * @param event the event which indicates that the user interacted with the button that saves the outside temperature.
     * @return prints on left panel and also on House Layout
     */
    @FXML
    public void setOutsideTemperature(ActionEvent event) {
        double value = outTempSHS.getValue();

        shsController.setOutsideTemperature(houseModel, value);
        drawLayout();
        leftPanelOutTemp.setText("Outside Temperature: " + outTempSHS.getValue().toString() + " C");
        saveSimulationConditions(event);
    }

    /**
     * Set inside temperature.
     *
     * @param event the event which indicates that the user interacted with the button that saves the inside temperature.
     * @return prints on left panel and also on House Layout
     */
    @FXML
    public void setInsideTemperature(ActionEvent event) {
        double value = inTempSHS.getValue();

        shsController.setInsideTemperature(roomArray, value);
        drawLayout();
        leftPanelInTemp.setText("Inside Temperature: " + inTempSHS.getValue().toString() + " C");
        saveSimulationConditions(event);
    }

    /**
     * Add object to window.
     *
     * @param event the event which indicates that the user interacted with the button that saves the location in
     *              which the window must be blocked by an object.
     *
     */
    @FXML
    public void addObjectToWindow(ActionEvent event) {
        String value = blockWinLocComboBoxSHS.getValue();
        shsController.addObjectToWindow(roomArray, value, consoleTextField);
        drawLayout();
        saveSimulationConditions(event);
    }

    /**
     * Start or stop the simulation and enable or disable the needed UI controls.
     * @return prints on output console
     */
    @FXML
    public void startOrStopSimulation() {
        if (turnOnOffSimulation.getText().equals("Start the simulation")) {
            turnOnOffSimulation.setText("Stop the simulation");
            consoleTextField.setText("The simulation has been started!\n" + consoleTextField.getText());
            System.out.println("The simulation has been started!");
            saveDate.setDisable(true);
            saveTime.setDisable(true);
            saveOutsideTemp.setDisable(true);
            saveInsideTemp.setDisable(true);
            loginButton.setDisable(true);
        } else if (turnOnOffSimulation.getText().equals("Stop the simulation")) {
            turnOnOffSimulation.setText("Start the simulation");
            consoleTextField.setText("The simulation has been stopped!\n" + consoleTextField.getText());
            System.out.println("The simulation has been stopped!");
            saveDate.setDisable(false);
            saveTime.setDisable(false);
            saveOutsideTemp.setDisable(false);
            saveInsideTemp.setDisable(false);
            loginButton.setDisable(false);
        }
    }

    /**
     * Log in the user based on the user ID provided.
     */
    @FXML
    public void login() {
        int id = userIdToLogin.getValue();

        userInfo = shsController.login(houseModel, id, userModelArrayList, consoleTextField);

        turnOffSimulationWarning();
        processUserInfo("login");
    }

    /**
     * Delete user profile based on the user ID provided.
     */
    @FXML
    public void deleteUserProfile() {
        int id = userIdToRemove.getValue();

        shsController.deleteUserProfile(userModelArrayList, id, consoleTextField, houseModel);

        data.clear();
        loadUsersInSHSTable();
        userTable.setItems(data);
        drawLayout();
    }

    /**
     * Add/modify user based on the information provided.
     */
    @FXML
    public void addModifyUser() {
        int id = addModifyUserID.getValue();

        String name = addModifyUserName.getText();
        String userType = addModifyRoleComboBoxSHS.getValue();
        String location = addModifyLocComboBoxSHS.getValue();

        userInfo = shsController.addModifyUser(userModelArrayList, id, name, userType, location, consoleTextField);

        processUserInfo("add/modify");

        data.clear();
        loadUsersInSHSTable();
        userTable.setItems(data);
        drawLayout();
    }

    /**
     * Save the simulation conditions.
     *
     * @param event the event which specifies the button which the user interacted with in order to save a certain
     *              simulation condition.
     * @return prints on left panel and on House Layout
     */
    @FXML
    public void saveSimulationConditions(ActionEvent event) {
        if (event.getSource().equals(saveDate)) {
            consoleTextField.setText("The date has been changed to " + dateSHS.getValue().toString() + ".\n" + consoleTextField.getText());
            leftPanelDate.setText("Date: " + dateSHS.getValue().toString());
        } else if (event.getSource().equals(saveTime)) {
            consoleTextField.setText("The time has been changed to " + timeSHS.getValue().toString() + ".\n" + consoleTextField.getText());
            leftPanelTime.setText("Time: " + timeSHS.getValue().toString());
        } else if (event.getSource().equals(saveOutsideTemp)) {
            consoleTextField.setText("The outside temperature has been changed to " + outTempSHS.getValue().toString() + " Celsius.\n" + consoleTextField.getText());
        } else if (event.getSource().equals(saveInsideTemp)) {
            consoleTextField.setText("The inside temperature has been changed to " + inTempSHS.getValue().toString() + " Celsius.\n" + consoleTextField.getText());
        } else if (event.getSource().equals(saveWindowBlock)) {
            consoleTextField.setText("The window in " + blockWinLocComboBoxSHS.getValue() + " has been blocked\\unblocked.\n" + consoleTextField.getText());
        }

        turnOffSimulationWarning();
    }

    /**
     * Hide the simulation warning message and allow the user to start the simulation if all the necessary conditions have
     * been met.
     */
    private void turnOffSimulationWarning() {
        if (!leftPanelDate.getText().isEmpty() && !leftPanelTime.getText().isEmpty() && !leftPanelInTemp.getText().isEmpty() && !leftPanelOutTemp.getText().isEmpty() && isLoggedIn) {
            turnOnOffSimulation.setDisable(false);
            warningLabelSimulation.setVisible(false);
            warningLabelSimulation.setManaged(false);
        }
    }

    /**
     * Display all the information of the logged in user.
     *
     * @param action a String which specifies if the user is currently attempting to log in or to add/modify a user profile.
     */
    private void processUserInfo(String action) {

        boolean actionSuccessful = (boolean) (userInfo[0]);
        UserModel processedUser = (UserModel) (userInfo[1]);

        if (!actionSuccessful) {
            return;
        }

        if (action.equals("login") || (isLoggedIn && action.equals("add/modify") && loggedInUser.getId() == processedUser.getId())) {
            loggedInUser = new UserModel(processedUser.getName(), processedUser.getId(), processedUser.getUser_type(), processedUser.getLocation());

            avatarImageView.setVisible(true);
            avatarImageView.setManaged(true);
            userNameLabel.setText("Name: " + loggedInUser.getName());
            userIDLabel.setText("ID: " + loggedInUser.getId());
            userPermissionLabel.setText("Permission: " + loggedInUser.getUser_type());
            userLocationLabel.setText("Location: " + loggedInUser.getLocation());
            isLoggedIn = true;
            turnOffSimulationWarning();
        }
    }

    /**
     * Process the path of the house layout JSON file that the user wishes to upload and give access to the rest of
     * the simulator if the file exists.
     */
    @FXML
    private void readHouseLayoutFile () {

        //For the path, try something like C:\Soen 343\Project\HouseInfo.json
        JSONParser parser = new JSONParser();
        String path = houseLayoutFilePath.getText().replaceAll("\\\\+","\\\\\\\\");
        Object obj = null;

        try {
            obj = parser.parse(new FileReader(path));
        } catch (IOException | ParseException e) {
            System.out.println("File not found or parse error occurred!");
            System.out.println("Textfield: " + houseLayoutFilePath.getText());
            System.out.println("Path: " + path);
            errorLabelHouseLayoutFile.setText("An error has occurred!\nEither the file was not found\nor a parsing error has\noccurred. Please double\ncheck your file path.");
            return;
        }

        warningLabelSimulation.setText("Please log in as a user as\nwell as set the date, time,\ninside temperature, and\noutside temperature before\nstarting the simulation.");
        System.out.println("File found!");
        System.out.println("Textfield: " + houseLayoutFilePath.getText());
        System.out.println("Path: " + path);

        moduleTabs.setDisable(false);
        showUIElement(labelHouseLayoutFile, false);
        showUIElement(houseLayoutFilePath, false);
        showUIElement(saveHouseLayoutFilePath, false);
        showUIElement(errorLabelHouseLayoutFile, false);

        showUIElement(avatarImageView, true);
        showUIElement(userNameLabel, true);
        showUIElement(userIDLabel, true);
        showUIElement(userPermissionLabel, true);
        showUIElement(userLocationLabel, true);
        showUIElement(leftPanelDate, true);
        showUIElement(leftPanelTime, true);
        showUIElement(leftPanelOutTemp, true);
        showUIElement(leftPanelInTemp, true);
        showUIElement(labelLeftPanelSimParam, true);
        showUIElement(labelUserInfoLeftPanel, true);

        // SHS prep

        simulationDataController.loadData(path);
        roomArray = simulationDataController.getRoomArray();
        roomNameArrayList = simulationDataController.getRoomNameList();
        userModelArrayList = simulationDataController.getUserArrayList();
        houseModel = simulationDataController.getHouseModel();
        loadUsersInSHSTable();

        houseViewController.drawLayout(roomArray, bp, houseModel, userModelArrayList);

        addModifyLocComboBoxSHS.getItems().add(roomNameArrayList.get(0));

        for (int i = 1; i < roomNameArrayList.size(); i++) {
            addModifyLocComboBoxSHS.getItems().add(roomNameArrayList.get(i));
            blockWinLocComboBoxSHS.getItems().add(roomNameArrayList.get(i));
        }

        addModifyLocComboBoxSHS.getItems().add("Outside");
        addModifyRoleComboBoxSHS.getItems().addAll("Parent", "Child", "Guest", "Stranger");

        addModifyLocComboBoxSHS.getSelectionModel().selectFirst();
        addModifyRoleComboBoxSHS.getSelectionModel().selectFirst();
        blockWinLocComboBoxSHS.getSelectionModel().selectFirst();

        userTable.setEditable(true);
        userTable.setItems(data);

        dateSHS.setValue(LocalDate.now());

        turnOnOffSimulation.setDisable(true);

        timeSHS.setValue(LocalTime.of(12, 0));
    }

    /**
     * Helper method which shows or hides a given UI element.
     *
     * @param node the UI element which must either be shown or hidden.
     * @param bool a boolean where true means to show and false means to hide.
     */
    private void showUIElement(Node node, boolean bool) {
        node.setVisible(bool);
        node.setManaged(bool);
    }
}
