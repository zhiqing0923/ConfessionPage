/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package confessionpage2;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 *
 * @author ngsua
 */
public class database {
    
    static Stage stage;
    static Scene scene;
    static Parent root;
    
    public static void insertConfession(ActionEvent event,String id,String confession,String date)throws SQLException{ //a method to insert the confession from user into database
        
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String word = "fuck";
        
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confessionpage", "root", "root");
            preparedStatement = connection.prepareStatement("INSERT INTO confessionpage.confession(id,confession,date) VALUES (?,?,?)");
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, confession);
            preparedStatement.setString(3, date);
               
               int k = preparedStatement.executeUpdate();
               
               if(k==1){
                   Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                   alert.setContentText("YOUR SUBMISSION HAVE BEEN RECORDED");
                   alert.show();
                   
                   String[] array = confession.split(" ");
                   for(int i=0;i<array.length;i++){
                   if(array[i].equalsIgnoreCase(word)){
                       connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confessionpage", "root", "root");
                       preparedStatement = connection.prepareStatement("delete from confession where id=? and confession = ? and date=?");
                       preparedStatement.setString(1, id);
                       preparedStatement.setString(2, confession);
                       preparedStatement.setString(3,date);
                       preparedStatement.executeUpdate();
                   }
                   }
               }else{
                   Alert alert = new Alert(Alert.AlertType.ERROR);
                   alert.setContentText("THERE IS AN ERROR");
                   alert.show();
               }
            
        }catch(SQLException e){
            e.printStackTrace();
        } finally{
            if(resultSet!=null){
                try{
                    resultSet.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(preparedStatement!=null){
                try{
                    preparedStatement.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(connection!=null){
                try{
                    connection.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            
        }
    }
    
    public static void insertConfessionWithReply(ActionEvent event,String id,String confession,String replyID,String date)throws SQLException{ //a method to insert the confession from user into database
        
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement psCheckIDExists = null;
        ResultSet resultSet = null;
        String word = "fuck";
        
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confessionpage", "root", "root");
            psCheckIDExists = connection.prepareStatement("SELECT * FROM confession WHERE id=?");
            psCheckIDExists.setString(1, replyID);
            resultSet = psCheckIDExists.executeQuery();
            
            if(resultSet.isBeforeFirst()){
                preparedStatement = connection.prepareStatement("INSERT INTO confessionpage.confession(id,confession,replyid,date) VALUES (?,?,?,?)");
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, confession);
                preparedStatement.setString(3, replyID);
                preparedStatement.setString(4, date);
                
                int k = preparedStatement.executeUpdate();
               
                if(k==1){
                   Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                   alert.setContentText("YOUR SUBMISSION HAVE BEEN RECORDED");
                   alert.show();
                   
                   String[] array = confession.split(" ");
                   for(int i=0;i<array.length;i++){
                   if(array[i].equalsIgnoreCase(word)){
                       connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confessionpage", "root", "root");
                       preparedStatement = connection.prepareStatement("delete from confession where id=? and confession = ? and replyid=? and date=?");
                       preparedStatement.setString(1, id);
                       preparedStatement.setString(2, confession);
                       preparedStatement.setString(3,replyID);
                       preparedStatement.setString(4,date);
                       preparedStatement.executeUpdate();
                   }
                   }
                }else{
                   Alert alert = new Alert(Alert.AlertType.ERROR);
                   alert.setContentText("THERE IS AN ERROR");
                   alert.show();
                }
                
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Reply ID doesn't exits.");
                alert.show();
            }
            
        }catch(SQLException e){
            e.printStackTrace();
        } finally{
            if(resultSet!=null){
                try{
                    resultSet.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(preparedStatement!=null){
                try{
                    preparedStatement.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(connection!=null){
                try{
                    connection.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            
        }
    }
    
    
    public static Connection getConnect (){ //a method to get connection from database
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confessionpage", "root", "root");
        } catch (SQLException ex) {
            Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            return  connection;
        }
    
    public static void changeScene(ActionEvent e,String username,String fxmlfile) throws IOException{ // change the scene to the other fxml file
        Parent root = null;
        
        if(username != null){
            try{
                FXMLLoader loader = new FXMLLoader(database.class.getResource(fxmlfile));
                root = loader.load();
            }catch(IOException event){
                event.printStackTrace();
            }
        }
        else{
            try{
                root = FXMLLoader.load(database.class.getResource(fxmlfile));
            }catch(IOException event){
                event.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root,600,500));
        stage.show();   
        
    }
    
    public static void loginAdmin(ActionEvent event,String username,String password)throws SQLException, IOException{
        
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/adminlogin", "root", "root"); //connect to your databases, javafx-loginsigup is my scheme name, root is my user and root is my password 
            preparedStatement = connection.prepareStatement("SELECT password FROM admin WHERE username = ?"); 
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            
            if(!(resultSet.isBeforeFirst())){
                System.out.println("User not found");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect");
                alert.show();
            }
            else{
                while(resultSet.next()){
                    String retrivedPassword = resultSet.getString("password");
                    if(retrivedPassword.equals(password)){
                         changeScene(event,username,"adminPage.fxml"); 
                        
                    }
                    else{
                        System.out.println("Password is incorrect");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Provided credentials are incorrect");
                        alert.show();
                        
                    }
                }
            }
            
        }catch(SQLException e){
            e.printStackTrace();
        } finally{
            if(resultSet!=null){
                try{
                    resultSet.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(preparedStatement!=null){
                try{
                    preparedStatement.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(connection!=null){
                try{
                    connection.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            
        }
    }
    
    public static void removeConfession(ActionEvent event,String id,String confession,String replyid,String date){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookingseat", "root", "root");
            preparedStatement = connection.prepareStatement("delete from confession where id = ? and confession = ? and replyid = ? and date = ?");
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, confession);
            preparedStatement.setString(3, replyid);
            preparedStatement.setString(4, date);
            int k = preparedStatement.executeUpdate();
        
            if(k==1){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("THE CHANGES HAVE BEEN SAVED");
            alert.show();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                   alert.setContentText("THERE IS AN ERROR");
                   alert.show();
            }
            
           
            
        }catch(SQLException e){
            System.out.println("Error occured");
            e.printStackTrace();
        } finally{
            if(resultSet!=null){
                try{
                    resultSet.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(preparedStatement!=null){
                try{
                    preparedStatement.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(connection!=null){
                try{
                    connection.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            
        }
    }
    
    
    
    
}    
    

