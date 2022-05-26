/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package confessionpage2;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author ngsua
 */
public class AdminPageController implements Initializable {

    @FXML
    private TableView<Confession> tableView;
    @FXML
    private TableColumn<Confession, String> idCol;
    @FXML
    private TableColumn<Confession, String> confessionCol;
    @FXML
    private TableColumn<Confession, String> replyIDCol;
    @FXML
    private TableColumn<Confession, String> dateCol;
    @FXML
    private TableColumn<Confession, String> editCol;
    @FXML
    private TableColumn<Confession, String> editCol1;
    
    
    private Stage stage;
    private Scene scene;
    private Parent root;
    
    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    Confession confession = null ;
    
    ObservableList<Confession>  confessionList = FXCollections.observableArrayList();
    
    public void refreshTable(){ //get data from database
         try {
            confessionList.clear();
            
            query = "SELECT * FROM confession ORDER BY idconfession DESC";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()){
                confessionList.add(new Confession(
                        
                        resultSet.getString("id"),
                        resultSet.getString("confession"),
                        resultSet.getString("replyid"),
                        resultSet.getString("date")
                
                ));
                      
                tableView.setItems(confessionList);
                
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(AdminPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadDate();
    }
    
    public void loadDate(){ //display the database to the tableview
        
        connection = database.getConnect();
        refreshTable();
        
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        confessionCol.setCellValueFactory(new PropertyValueFactory<>("confession"));
        replyIDCol.setCellValueFactory(new PropertyValueFactory<>("replyid"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        
        Callback<TableColumn<Confession, String>, TableCell<Confession, String>> cellFactory = (TableColumn<Confession, String> param) -> {
            // make cell containing buttons
            final TableCell<Confession, String> cell = new TableCell<Confession, String>() {
                
                private final Button btn = new Button("Approve");
                
                {
                        btn.setOnAction((ActionEvent event) -> {
                            Confession conf = getTableView().getItems().get(getIndex());
                            System.out.println("selectedData: " + conf);
                        });
                    }
                
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {
                        setGraphic(btn);
                    }
                }

            };

            return cell;
        };
        
        Callback<TableColumn<Confession, String>, TableCell<Confession, String>> cellFactory1 = (TableColumn<Confession, String> param) -> {
            // make cell containing buttons
            final TableCell<Confession, String> cell = new TableCell<Confession, String>() {
                
                private final Button btn = new Button("Reject");
                
                {
                        btn.setOnAction((ActionEvent event) -> {
                            try {    
                                confession = tableView.getSelectionModel().getSelectedItem();
                                query = "DELETE FROM `confession` WHERE confession = ?";
                                connection = database.getConnect();
                                preparedStatement = connection.prepareStatement(query);
                                preparedStatement.setString(1, confession.getConfession());
                                preparedStatement.executeUpdate();
                                refreshTable();
                            } catch (SQLException ex) {
                                Logger.getLogger(AdminPageController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                            ObservableList<Confession> allConfession,singleConfession;
                            allConfession=tableView.getItems();
                                singleConfession = tableView.getSelectionModel().getSelectedItems();
                                singleConfession.forEach(allConfession::remove);
                            
                        });
                    }
                
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {
                        setGraphic(btn);
                    }
                }

            };

            return cell;
        };
        
         editCol.setCellFactory(cellFactory);
         editCol1.setCellFactory(cellFactory1);
         tableView.setItems(confessionList);
         
         
    }
        
    }
    

