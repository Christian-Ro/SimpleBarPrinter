package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private AnchorPane rootPane;
    @FXML private Button browse;
    @FXML private Button load;
    @FXML private Label showPath;
    @FXML public ComboBox<String> excelSheets;
    @FXML private TextField showTextPath;

    Workbook newWb = new XSSFWorkbook();
    ObservableList<Inventory> inventoryList = FXCollections.observableArrayList();
    ObservableList<ControlSystem> controlList = FXCollections.observableArrayList();
    ObservableList<SimpleInventory> simpleInventoryList = FXCollections.observableArrayList();
    public int numOfTableCells;
    //DataFormatter dataFormatter = new DataFormatter();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        load.setDisable(true);

    }

    public void clickBrowse(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        File selectFile = fileChooser.showOpenDialog(null);
        showPath.setText(selectFile.getAbsolutePath());
        fileSelected(selectFile);
    }

    @FXML
    private void handleDragOver(DragEvent event){
        if(event.getDragboard().hasFiles()){
            event.acceptTransferModes(TransferMode.ANY);
        }
    }
    @FXML
    private void handleDrop(DragEvent event){
        List<File> file = event.getDragboard().getFiles();
        showPath.setText(file.get(0).getAbsolutePath());
        fileSelected(file.get(0));
    }

    public void fileSelected(File file){
        List<String> sheetNames = new ArrayList<>();

        if(file != null){
            try(Workbook wb = WorkbookFactory.create(file)){
                for(int i = 0; i < wb.getNumberOfSheets(); i++){
                    sheetNames.add(wb.getSheetName(i));
                }
                newWb = wb;
            }
            catch(IOException e){
                e.printStackTrace();
            }
            catch (InvalidFormatException ie){
                ie.printStackTrace();
            }
        }
        ObservableList<String> list = FXCollections.observableArrayList(sheetNames);
        excelSheets.setItems(list);
    }

    @FXML
    public void comboChange(ActionEvent event){

        String selectedSheet = new String(excelSheets.getValue());
        Sheet sheet = newWb.getSheet(selectedSheet);
        loadScene(sheet);
    }

    public void loadScene(Sheet sheet){

        int rowStart = sheet.getFirstRowNum();
        int rowEnd = sheet.getLastRowNum();
        int numDescription = 0;
        int numDescription2 = 0;
        int numRows = 0;

        FormulaEvaluator evaluator = newWb.getCreationHelper().createFormulaEvaluator();
        //***************************************************************************************************************
        for(int i = rowStart; i <= rowEnd; i++) {
            Row row = sheet.getRow(i);
            for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                CellValue cellValue = evaluator.evaluate(cell);
                if (cellValue == null || cellValue.equals(null) || cellValue.equals("")) {
                    //System.out.println("It's a Date" + cell.getRichStringCellValue() + " " + cell.getCellTypeEnum() + " " + cell.getCellTypeEnum());
                    break;
                }
                else {
                    if (numDescription == j) {
                        numDescription += 1;

                    }
                    else {

                        break;
                    }
                }
            }
        }
        //***************************************************************************************************************
        String[] str = new String[numDescription];

        for(int i = rowStart; i <= rowEnd; i++) {
            Row row = sheet.getRow(i);
            for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                CellValue cellValue = evaluator.evaluate(cell);
                if (cellValue == null || cellValue.equals(null) || cellValue.equals("")) {
                    break;
                } else {
                    if (numDescription2 == j) {
                        numDescription2 += 1;
                    } else {
                        if (numRows == j) {
                            //str[numRows] = cellValue.formatAsString();

                            if(cell.getCellTypeEnum().equals(CellType.STRING)){
                                //System.out.println("It is a String " + cell.getStringCellValue());
                                str[numRows] = cellValue.formatAsString();
                            }
                            else if(cell.getCellTypeEnum() == CellType.NUMERIC){
                                if(DateUtil.isCellDateFormatted(cell)){
                                    //System.out.println("Its is a Date " + cell.getDateCellValue().toString());
                                    str[numRows] = "Date Here";
                                }
                                else{
                                    //System.out.println("It is not a Date and not a String " + cell.getNumericCellValue());
                                    //System.out.println("Number appear here: " + cellValue.formatAsString());
                                    str[numRows] = cellValue.formatAsString();
                                }
                            }
                            else{
                                str[numRows] = cellValue.formatAsString();
                            }
                            numRows += 1;
                        }
                        System.out.println(str[j]);
                    }
                }
            }
            if(numRows == 8){
                inventoryList.add(new Inventory(str[0], str[1], str[2], str[3], str[4], str[5], str[6], str[7]));
                numOfTableCells = numRows;
                numRows = 0;
            }
            else if (numRows == 4){
                controlList.add(new ControlSystem(str[0], str[1], str[2], str[3]));
                numOfTableCells = numRows;
                numRows = 0;
            }
            else if(numRows == 2){
                simpleInventoryList.add(new SimpleInventory(str[0], str[1]));
                numOfTableCells = numRows;
                numRows = 0;
            }
            else{
                System.out.println("No available sheet");
            }
        }
        load.setDisable(false);
    }

    @FXML
    public void loadTable(ActionEvent event){

        try{
            if(numOfTableCells == 8){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("InventoryFile.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root1));
                stage.show();

                InventoryFileController controller = fxmlLoader.getController();
                controller.getObservableList(inventoryList);
            }
            else if(numOfTableCells == 4){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("ControlSystemFile.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root1));
                stage.show();

                ControlSystemFile controller = fxmlLoader.getController();
                controller.getObservableList(controlList);
            }
            else if(numOfTableCells == 2){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("SimpleInventory.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root1));
                stage.show();

                SimpleInventoryController controller = fxmlLoader.getController();
                controller.getObservableList(simpleInventoryList);
            }
            else{
                System.out.println("Nothing to show in new window" + numOfTableCells);
            }

        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Can't load the inventory file");
        }
    }
}
