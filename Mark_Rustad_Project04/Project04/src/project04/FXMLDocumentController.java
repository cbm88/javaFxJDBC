/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project04;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.sql.*;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


/**
 *  Program Name: Payroll Administration Modulus
 *  Program Description: Creates, Views, Revises, and Deletes records stored in a database
 *  Program Author: Mark Rustad
 *  Date Last Modified: December 10, 2016
 * 
 * @author Mark Rustad
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Pane titlePane;
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Label titleLabel;
    @FXML
    private GridPane theGridPane;
    @FXML
    private Label idLabel;
    @FXML
    private TextField idField;
    @FXML
    private Label lastNameLabel;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField middleField;
    @FXML
    private TextField stateField;
    @FXML
    private TextField departmentField;
    @FXML
    private TextField levelField;
    @FXML
    private TextField salaryField;
    @FXML
    private TextField minField;
    @FXML
    private TextField maxField;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label empnoLabel;
    @FXML
    private Label penLevelLabel;
    @FXML
    private TextField penLevelField;
    @FXML
    private HBox hboxOne;
    @FXML
    private Font x1;
    @FXML
    private Button createButton;
    @FXML
    private Button updateButton;
    @FXML
    private Label progressLabel;
    @FXML
    private Button clearButton;
    @FXML
    private Button exitButton;
    @FXML
    private HBox basePane;
    @FXML
    private TextField empnoField;
     @FXML
    private HBox actionPane;
    @FXML
    private Button deleteButton;
    @FXML
    private Button eraseButton;
    @FXML
    private Button storeButton;
    @FXML
    private Button runButton;
    
    @FXML
    private GridPane penPane;
    @FXML
    private Label promptPane;
    @FXML
    private Button readButton;
    
    private Statement statement;
    @FXML
    private HBox buttonPane;
    @FXML
    private Font x2;
    @FXML
    private Button saveButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //Display only the resources that the user will need to begin processing
        //Call the initializeDB() method to pre-load the database
        progressLabel.setText("Initializing");
        actionPane.setVisible(false);
        theGridPane.setVisible(false);
        penPane.setVisible(false);
        hboxOne.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        empnoField.setVisible(false);
        initializeDB();
        
    }    
    
    private void initializeDB(){
        
        //Establish the connection to the Client Driver
        try{
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        }
        
        //Handle connectivity error
        catch(ClassNotFoundException ex){
            progressLabel.setText("Driver failed");
            System.err.println(ex);
        }
        
        //Establish the connection to the database through the Client Driver
        try{
            Connection connect = DriverManager.getConnection("jdbc:derby://localhost:1527/PROJECT04;create=true;user=nbuser;password=nbuser"); 
            statement = connect.createStatement();
            progressLabel.setText("Connected to database");
            statement.getConnection();
            
        }
        
        //Handle connectivity error
        catch(SQLException ex){
            progressLabel.setText("Connection failed");
            System.err.println(ex);
        }
        
    }

    @FXML
    private void createPressed(ActionEvent event) throws SQLException {
        
        //Display only the resources the user will need to perform record creation
        hboxOne.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        hboxOne.setVisible(true);
        empnoField.setEditable(false);
        runButton.setVisible(false);
        saveButton.setVisible(false);
        eraseButton.setVisible(false);
        theGridPane.setVisible(true);
        storeButton.setVisible(true);
        basePane.setVisible(true);
        penPane.setVisible(true);
        actionPane.setVisible(true);
        minField.setVisible(false);
        maxField.setVisible(false);
        
    }
    
    @FXML
    private void storePressed(ActionEvent event) {

        //Initialize SQL query statements to load data from its proper, respective tables
        String insertStatement =
            "INSERT INTO EMPLOYEE(EMPNO, LASTNAME, FIRSTNAME, MI, STATE) VALUES(" + 
            Integer.valueOf(idField.getText().trim())+ ",'" +
            lastNameField.getText().trim() + "','" +
            firstNameField.getText().trim() + "','" +
            middleField.getText().trim() + "','" +
            stateField.getText().trim() + "')";
        
        String insertStatementTwo = 
                "INSERT INTO SALARY(EMPNO, DEPT_NO, JOB_LEVEL, SALARY_AMOUNT) VALUES(" +
                Integer.valueOf(idField.getText().trim()) + ", '" +
                departmentField.getText().trim() + "', '" +
                levelField.getText().trim() + "', " +
                salaryField.getText().trim() + ")";
        
        //Initialize another SQL statement to check for preexisting record ID (EMPNO)
        String queryExistingID = "SELECT * FROM EMPLOYEE WHERE EMPNO = " + "" + idField.getText().trim() + "";

        //Connect to the database
        //Execute queries
        try {
            Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/PROJECT04;create=true;user=nbuser;password=nbuser");
            
            PreparedStatement prepStatement = connection.prepareStatement(insertStatement);
            PreparedStatement prepStatementTwo = connection.prepareStatement(insertStatementTwo);
            PreparedStatement prepStatementThree = connection.prepareStatement(queryExistingID);
            ResultSet res = prepStatementThree.executeQuery();
            
            //Display error message should a record ID exist
            //Display error message if the job level field is empty
            if (res.next()){
                progressLabel.setText("Record already exists");
                idField.setText("");
            }
            else if(levelField.getText().equals("")){
                progressLabel.setText("Enter a job level");
            }
            
            //Continue processing if above errors are nullified
            else{
            prepStatement.executeUpdate();
            prepStatementTwo.executeUpdate();
            
            progressLabel.setText("Record inserted");
            System.out.println("Record successfully entered");
            }
            
            res.close();
            
        }
        
        catch (SQLException ex) {
            progressLabel.setText("Insertion failed");
            System.err.println(ex);
        }
       
    }
    
    @FXML
    private void readPressed(ActionEvent event) throws SQLException{
        
        //Display only the resources the user will need to read records
        hboxOne.setVisible(true);
        empnoField.setVisible(true);
        storeButton.setVisible(false);
        saveButton.setVisible(false);
        eraseButton.setVisible(false);
        basePane.setVisible(true);
        theGridPane.setVisible(true);
        penPane.setVisible(true);
        actionPane.setVisible(true);
        runButton.setVisible(true);
        hboxOne.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        empnoField.setEditable(true);
        
    }
    
    @FXML
    private void runPressed(ActionEvent event) throws SQLException {
        
        //Call methods to view record
        readEmployee();
        readSalary();
        readSalaryLevel();
        idField.setEditable(false);
        idField.setBackground(new Background(new BackgroundFill(Color.KHAKI, CornerRadii.EMPTY, Insets.EMPTY)));
        minField.setBackground(new Background(new BackgroundFill(Color.KHAKI, CornerRadii.EMPTY, Insets.EMPTY)));
        maxField.setBackground(new Background(new BackgroundFill(Color.KHAKI, CornerRadii.EMPTY, Insets.EMPTY)));
        
    }
    
    private void readEmployee() throws SQLException{
        
        //Create an SQL statement to read from the primary key (EMPNO)
        String query = "SELECT * FROM EMPLOYEE WHERE EMPNO = " + "" + empnoField.getText().trim() + "";
        
        //Execute search
        //Display results
        try {
        
            ResultSet rset = statement.executeQuery(query);
            
            while(rset.next()){
        
            int id  = rset.getInt("EMPNO");
            String last = rset.getString("LASTNAME");
            String first = rset.getString("FIRSTNAME");
            String middle = rset.getString("MI");
            String state = rset.getString("STATE");
            
            idField.setText(id + "");
            lastNameField.setText(last + "");
            firstNameField.setText(first + "");
            middleField.setText(middle + "");
            stateField.setText(state + "");
            
            System.out.print("ID: " + id);
            System.out.print("First: " + first);
            System.out.print("Last: " + last);
            System.out.println("MI: " + middle);
            System.out.println("State: " + state);
        
            }
            
            rset.close();
            
        }
        
        //Handle error if no record is found in the database
        catch(SQLException ex) {
            progressLabel.setText("Record not found");
            System.err.println(ex);
        }
        
    }
    
    private void readSalary() throws SQLException{
        
        //Create an SQL statement to perform search based on the primary key (EMPNO)
        String queryTwo = "SELECT * FROM SALARY WHERE EMPNO = " + "" + empnoField.getText().trim() + ""; 
        
        //Execute search
        //Display results
        try {
        ResultSet rsetTwo = statement.executeQuery(queryTwo);
      
        if (rsetTwo.next()){
       
            String department = rsetTwo.getString("DEPT_NO");
            String jobLevel = rsetTwo.getString("JOB_LEVEL");
            double salary = rsetTwo.getDouble("SALARY_AMOUNT");

            departmentField.setText(department + "");
            levelField.setText(jobLevel + "");
            salaryField.setText(salary + "");
         
            System.out.println("Department: " + department);
            System.out.println("Job Level: " + jobLevel);
            System.out.println("Salary: " + salary);
           
        }
        
        //Handle error if no record was found in the database
        else{
            progressLabel.setText("Record not found");
        }
        
        rsetTwo.close();
        
        }
        catch(SQLException ex) {
            progressLabel.setText("Processing failed");
            System.err.println(ex);
        }

    }
    
    private void readSalaryLevel() throws SQLException{
        
        //Create an SQL statement to execute search based on primary key (JOB_LEVEL)
        String queryThree = "SELECT * FROM SALARY_LEVEL WHERE JOB_LEVEL = " + "'" + levelField.getText().trim() +"'";
        
        //Declare local variables
        double minSalary;
        double maxSalary;

        //Execute search
        //Display results
        try {
            ResultSet rsetThree = statement.executeQuery(queryThree);
        
        if (rsetThree.next()){

            minSalary = rsetThree.getDouble("SALARY_MINIMUM");
            maxSalary = rsetThree.getDouble("SALARY_MAXIMUM");
            
            minField.setText("$" + minSalary + "");
            maxField.setText("$" + maxSalary + "");

            System.out.println("Minimum Salary: " + minSalary);
            System.out.println("Maximum Salary: " + maxSalary);
            
            //Call the readSalary() method to import necessary data
            readSalary();
            
            //Calculate job level penetration
            //Display result
            double salary = Double.parseDouble(salaryField.getText());
            double penLevel = (salary-minSalary)/(maxSalary-minSalary);
            double levelResult = Math.round((penLevel * 100)*100)/100;
            int finalResult = (int) levelResult;
            
            if (finalResult > 90 || finalResult < 0){
                penLevelField.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                penLevelField.setText(Integer.toString(finalResult));
                System.out.println("Penetration: " + Integer.toString(finalResult));
            }
            else{
                penLevelField.setText(Integer.toString(finalResult));
            }
            progressLabel.setText("Record located");
        }
        
        //Handle error if no record was found in the database
        else{
            progressLabel.setText("Record not found");
        }

            rsetThree.close();
            
        }
         catch(SQLException ex) {
            progressLabel.setText("Processing Failed");
            System.err.println(ex);
        }
        
    }
    
    @FXML
    private void updatePressed(ActionEvent event) {
        
        //Display only the resources the user will need to perform revisions to a record
        hboxOne.setVisible(true);
        empnoField.setVisible(true);
        theGridPane.setVisible(true);
        clearButton.setVisible(true);
        exitButton.setVisible(true);
        runButton.setVisible(true);
        penPane.setVisible(true);
        createButton.isDisabled();
        deleteButton.isDisabled();
        storeButton.setVisible(false);
        actionPane.setVisible(true);
        saveButton.setVisible(true);
        eraseButton.setVisible(false);
        empnoField.setEditable(true);
        hboxOne.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        
    }
    
    @FXML
    private void savePressed(ActionEvent event) throws SQLException {
        
        //Call the updateRecord() method to execute update and calculate penetration level
        updateRecord();
        
        //Declare variables
        //Calculate penetration level
        //Display result
        double salary = Double.parseDouble(salaryField.getText());
        
        String minSalary = minField.getText();
        String newMinSalary = minSalary.replace("$", "");
        double minimum = Double.parseDouble(newMinSalary);
        
        String maxSalary = maxField.getText();
        String newMaxSalary = maxSalary.replace("$", "");
        double maximum = Double.parseDouble(newMaxSalary);
                        
        double penLevel = (salary-minimum)/(maximum-minimum);
        double levelResult = Math.round((penLevel *100)*100)/100;
        int finalResult = (int) levelResult;
                
            if (finalResult > 90 || finalResult < 0){
                penLevelField.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                penLevelField.setText(Integer.toString(finalResult));
                System.out.println("Penetration: " + Integer.toString(finalResult));
            }
            
            else{
                penLevelField.setText(Integer.toString(finalResult));
                System.out.println("Penetration: " + Integer.toString(finalResult));
            }
        
    }

    private void updateRecord() throws SQLException{
        
        //Connect to the database
        //Create individualized SQL statements for every revised text field
        //Execute update with individual PreparedStatement(s)
        try{
            
            Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/PROJECT04;create=true;user=nbuser;password=nbuser");
            statement = connection.createStatement();
            
            String editLastName = "UPDATE EMPLOYEE SET LASTNAME = '" + lastNameField.getText().trim() + "'" 
				+ " WHERE EMPNO = " + "" + empnoField.getText().trim() + "";
        
            statement.executeUpdate(editLastName);
        
            String editFirstName = "UPDATE EMPLOYEE SET FIRSTNAME = '" + firstNameField.getText().trim() + "'"
                                + "WHERE EMPNO = " + "" + empnoField.getText().trim() + "";
        
            statement.executeUpdate(editFirstName);
        
            String editMI = "UPDATE EMPLOYEE SET MI = '" + middleField.getText().trim() + "'"
                                + "WHERE EMPNO = " + "" + empnoField.getText().trim() + "";
        
            statement.executeUpdate(editMI);
        
            String editState = "UPDATE EMPLOYEE SET STATE = '" + stateField.getText().trim() + "'"
                                + "WHERE EMPNO = " + "" + empnoField.getText().trim() + "";
        
            statement.executeUpdate(editState);
        
            String editDepartment = "UPDATE SALARY SET DEPT_NO = '" + departmentField.getText().trim() + "'"
                                + "WHERE EMPNO = " + "" + empnoField.getText().trim() + "";
        
            statement.executeUpdate(editDepartment);
        
            String editJobLevel = "UPDATE SALARY SET JOB_LEVEL = '" + levelField.getText().trim() + "'"
                                + "WHERE EMPNO = " + "" + empnoField.getText().trim() + "";
        
            statement.executeUpdate(editJobLevel);
        
            String editSalary = "UPDATE SALARY SET SALARY_AMOUNT = " + "" + salaryField.getText().trim() + ""
                                + "WHERE EMPNO = " + "" + empnoField.getText().trim() + "";
        
            statement.executeUpdate(editSalary);
            
            if (lastNameField.getText()!=null){
                PreparedStatement prep = connection.prepareStatement(editLastName);
                prep.execute();
            }
            else if (firstNameField.getText()!=null){
                PreparedStatement prep = connection.prepareStatement(editFirstName);
                prep.execute();
            }
            else if (middleField.getText()!=null){
                PreparedStatement prep = connection.prepareStatement(editMI);
                prep.execute();
            }
            else if (stateField.getText()!=null){
                PreparedStatement prep = connection.prepareStatement(editState);
                prep.execute();
            }
            else if (departmentField.getText()!=null){
                PreparedStatement prep = connection.prepareStatement(editDepartment);
                prep.execute();
            }
            else if (levelField.getText()!=null){
                PreparedStatement prep = connection.prepareStatement(editJobLevel);
                prep.execute();
            }
            else if (salaryField.getText()!=null){
                PreparedStatement prep = connection.prepareStatement(editSalary);
                prep.execute();  
            }
            
            progressLabel.setText("Record updated");
            
        }
        
        //Handle error should the update fail
        catch(Exception ex){
            progressLabel.setText("Update Failed");
            System.err.println(ex);
            
        }
        
    }
    
    @FXML
    private void deletePressed(ActionEvent event) throws ClassNotFoundException, SQLException{
        
        //Display only the resources the user will need to execute record deletion
        hboxOne.setVisible(true);
        hboxOne.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        theGridPane.setVisible(false);
        runButton.setVisible(false);
        saveButton.setVisible(false);
        clearButton.setVisible(false);
        penPane.setVisible(false);
        actionPane.setVisible(true);
        eraseButton.setVisible(true);
        empnoField.setVisible(true);
        empnoField.setEditable(true);
        storeButton.setVisible(false);
        
    }
    
    @FXML
    private void erasePressed(ActionEvent event) throws ClassNotFoundException, SQLException{
         
        //Connect to the Client Driver
        try {  
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        }
        
        catch(ClassNotFoundException ex){
            progressLabel.setText("Connection/driver failed");
            System.err.println(ex);
        }
        
        //Connect to the database
        try{
            try (Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/PROJECT04;create=true;user=nbuser;password=nbuser")) {
                
                //Create SQL statements to delete records from the Employee and Salary tables based on the primary key (EMPNO)
                String deleteQuery = "delete from EMPLOYEE where EMPNO = " + "" + Integer.valueOf(empnoField.getText().trim()) + "";
                String deleteQueryTwo = "delete from SALARY where EMPNO = " + "" + Integer.valueOf(empnoField.getText().trim()) + "";
                
                //Execute the deletion
                PreparedStatement prepStatement = connection.prepareStatement(deleteQuery);
                PreparedStatement prepStatementTwo = connection.prepareStatement(deleteQueryTwo);
                
                prepStatement.executeUpdate();
                prepStatementTwo.executeUpdate();   
                
                progressLabel.setText("Record deleted");
            }
        }
  
        //Handle error if deletion were to fail
        catch(SQLException ex){
            progressLabel.setText("Delete failed");
            System.err.println(ex);
        }
        
    }
    
    @FXML
    private void clearPressed(ActionEvent event) {
        
        //Clear all user input fields
        empnoField.setText("");
        idField.setText("");
        lastNameField.setText("");
        firstNameField.setText("");
        middleField.setText("");
        stateField.setText("");
        departmentField.setText("");
        levelField.setText("");
        salaryField.setText("");
        minField.setText("");
        maxField.setText("");
        penLevelField.setText("");
        penLevelField.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        
    }

    @FXML
    private void exitPressed(ActionEvent event) {
        
        //Exit the program
        System.exit(0);
        
    }
    
}
