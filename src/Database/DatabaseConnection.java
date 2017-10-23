package Database;

import Models.Patient;
import Models.Visit;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class DatabaseConnection {
    private static DatabaseConnection instance=null;
    
    public Connection conn;
    
    private DatabaseConnection(){
        String URL = "jdbc:derby://localhost:1527/MedicalApp";
        String USER = "admin1";
        String PASS = "admin";
        try {
            conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Connected database successfully...");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static DatabaseConnection getInstance(){
        if(instance==null){
            instance = new DatabaseConnection();   
        }
        return instance;
    }
    
    public boolean tryLogin(String login,String password){
        Statement stmt = null;
        try {
            String SQL = "SELECT * FROM Users WHERE Login = '"+login+"' AND Password = '"+password+"'";
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            if(rs.next()){
                 rs.close();
                 return true;
            }           
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return false;

    }
    
    public void getPatients(JTable jt, String filter){ 
        filter=filter.toUpperCase();
        Statement stmt;
        try {
            String SQL = "SELECT * FROM Patients WHERE upper(FirstName) LIKE '%"+filter+"%' OR upper(LastName) LIKE '%"+filter+"%' OR Pesel LIKE '%"+filter+"%'";
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            
            jt.setModel(new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                   return false;
                }
            });
            
            DefaultTableModel model = (DefaultTableModel) jt.getModel();
            ResultSetMetaData rsmd = rs.getMetaData();
            
            for(int i=2;i<=rsmd.getColumnCount();i++){
                model.addColumn(rsmd.getColumnName(i));
            }
            
            String firstName;
            String lastName;
            String pesel;
            String address;
            String gender;
            Date birthDate;
            
            while(rs.next()){
                firstName=rs.getString("FirstName");
                lastName=rs.getString("LastName"); 
                pesel = rs.getString("Pesel");
                address=rs.getString("Address");
                gender = rs.getString("Gender");
                birthDate = rs.getDate("BirthDate");
                model.addRow(new Object[]{firstName,lastName,address,gender,birthDate,pesel});
            }
            rs.close();
        }
        catch (SQLException e) {
            System.out.println(e);
        }           
        
    }
      
    public void addPatient(Patient p){
        Statement stmt;
        try {
            String SQL = "INSERT INTO Patients (FirstName,LastName,Pesel,Address,Gender,BirthDate)"
                    + " VALUES (\'"+ p.getFirstName() + "\', \'"+ p.getLastName() + "\',\'"+ p.getPesel() + "\',\'"+ p.getAddress()+ "\',\'"+ p.getSex() + "\',\'"+ p.getBirthDate() + "\')";
            stmt = conn.createStatement();
            stmt.executeUpdate(SQL);
            System.out.println("Patient added to database");
        }
        catch (SQLException e) {
            System.out.println(e);
        }           
    }
      
    public void removePatient(String pesel){
        Statement stmt;
        try {
            String SQL = "DELETE FROM Patients WHERE Pesel ='"+pesel+"'";
            stmt = conn.createStatement();
            stmt.executeUpdate(SQL);
            System.out.println("Patient removed from database");
        }
        catch (SQLException e) {
            System.out.println(e);
        }           
    }
    
    public void updatePatient(Patient p){
        Statement stmt;
        try {
            String SQL = "UPDATE Patients SET FirstName ='"+p.getFirstName()+"', LastName='"+p.getLastName()+"', Address='"+p.getAddress()+"', Gender='"+p.getSex()+"', BirthDate='"+p.getBirthDate()+"'"
                    + " WHERE Pesel = '"+p.getPesel()+"'";
            stmt = conn.createStatement();
            stmt.executeUpdate(SQL);
            System.out.println("Patient updated");
        }
        catch (SQLException e) {
            System.out.println(e);
        }
    }
    
    public Patient getPatient(String pesel){
        Statement stmt = null;
        try {
            String SQL = "SELECT * FROM Patients WHERE Pesel = '"+pesel+"'";
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            
            String patientId;
            String firstName;
            String lastName;
            String address;
            String gender;
            Date birthDate;
            
            while(rs.next()){
                patientId=rs.getString("PatientID");
                firstName=rs.getString("FirstName");
                lastName=rs.getString("LastName"); 
                address=rs.getString("Address");
                gender = rs.getString("Gender");
                birthDate = rs.getDate("BirthDate");
                return new Patient(patientId,firstName,lastName,address,pesel,gender,birthDate);
            }
            rs.close();
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }
    
    public void addVisit(String date,String description, String patientId){
        Statement stmt;
        try {
            String SQL = "INSERT INTO Visits (VisitDate,Description,PatientID)"
                    + " VALUES ('"+ date + "', '"+ description + "', "+patientId+")";
            stmt = conn.createStatement();
            stmt.executeUpdate(SQL);
            System.out.println("Visit added to database");
        }
        catch (SQLException e) {
            System.out.println(e);
        }           
    }
     
    public void getVisits(JTable jt,String patientId){ 
        Statement stmt;
        try {
            String SQL = "SELECT * FROM VISITS WHERE PatientID ="+patientId;
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            
            jt.setModel(new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                   return false;
                }
            });
            
            DefaultTableModel model = (DefaultTableModel) jt.getModel();
            ResultSetMetaData rsmd = rs.getMetaData();
            
            for(int i=1;i<=rsmd.getColumnCount()-1;i++){
                model.addColumn(rsmd.getColumnName(i));
            }
            
            int visitId;
            String description;
            Date visitDate;
            
            while(rs.next()){
                visitId = rs.getInt("VisitID");
                description=rs.getString("Description");
                visitDate = rs.getDate("VisitDate");
                model.addRow(new Object[]{visitId,visitDate,description});
            }
            rs.close();
        }
        catch (SQLException e) {
            System.out.println(e);
        }           
        
    }
     
    public void removeVisit(int visitId){
        Statement stmt;
        try {
            String SQL = "DELETE FROM Visits WHERE VisitID ="+visitId;
            stmt = conn.createStatement();
            stmt.executeUpdate(SQL);
            System.out.println("Visit removed from database");
        }
        catch (SQLException e) {
            System.out.println(e);
        }           
    }
    
    public void updateVisit(Visit v){
        Statement stmt;
        try {
            String SQL = "UPDATE Visits SET VisitDate ='"+v.getVisitDate()+"', Description='"+v.getDescription()+"'"
                    + " WHERE VisitID = "+v.getVisitID();
            stmt = conn.createStatement();
            stmt.executeUpdate(SQL);
            System.out.println("Visit updated");
        }
        catch (SQLException e) {
            System.out.println(e);
        }
    }
    
    public void getDoctors(JTable jt, String filter){ 
        filter=filter.toUpperCase();
        Statement stmt;
        try {
            String SQL = "SELECT * FROM Doctors WHERE upper(FirstName) LIKE '%"+filter+"%' OR upper(LastName) LIKE '%"+filter+"%' OR PhoneNumber LIKE '%"+filter+"%'";
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            
            
            jt.setModel(new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                   return false;
                }
            });
            
            DefaultTableModel model = (DefaultTableModel) jt.getModel();
            ResultSetMetaData rsmd = rs.getMetaData();
            
            for(int i=2;i<=rsmd.getColumnCount();i++){
                model.addColumn(rsmd.getColumnName(i));
            }
            
            String firstName;
            String lastName;
            String address;
            String phoneNumber;
            String email;
 
            
            while(rs.next()){
                firstName=rs.getString("FirstName");
                lastName=rs.getString("LastName"); 
                address=rs.getString("Address");
                phoneNumber=rs.getString("PhoneNumber");
                email = rs.getString("Email");
                model.addRow(new Object[]{firstName,lastName,address,phoneNumber,email});
            }
            rs.close();
        }
        catch (SQLException e) {
            System.out.println(e);
        }           
        
    }
    
    public void getAdministration(JTable jt, String filter){ 
        filter=filter.toUpperCase();
        Statement stmt;
        try {
            String SQL = "SELECT * FROM Administration WHERE upper(FirstName) LIKE '%"+filter+"%' OR upper(LastName) LIKE '%"+filter+"%' OR PhoneNumber LIKE '%"+filter+"%'";
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            
            jt.setModel(new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                   return false;
                }
            });
            
            DefaultTableModel model = (DefaultTableModel) jt.getModel();
            ResultSetMetaData rsmd = rs.getMetaData();
            
            for(int i=2;i<=rsmd.getColumnCount();i++){
                model.addColumn(rsmd.getColumnName(i));
            }
            
            String firstName;
            String lastName;
            String address;
            String phoneNumber;
            String email;
 
            
            while(rs.next()){
                firstName=rs.getString("FirstName");
                lastName=rs.getString("LastName"); 
                address=rs.getString("Address");
                phoneNumber=rs.getString("PhoneNumber");
                email = rs.getString("Email");
                model.addRow(new Object[]{firstName,lastName,address,phoneNumber,email});
            }
            rs.close();
        }
        catch (SQLException e) {
            System.out.println(e);
        }           
        
    }
}
