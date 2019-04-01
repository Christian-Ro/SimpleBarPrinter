package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class SimpleInventoryController {
    @FXML private TableView<SimpleInventory> tableView;
    @FXML private TableView<SimpleInventory> tableView2;
    @FXML private TableColumn<SimpleInventory, String> code;
    @FXML private TableColumn<SimpleInventory, String> description;
    @FXML private TableColumn<SimpleInventory, String> code2;
    @FXML private TableColumn<SimpleInventory, String> description2;
    @FXML private TextField numOfCopies;
    //@FXML private Button carryOver;
    //@FXML private Button printSimple;


    ObservableList<SimpleInventory> list2 = FXCollections.observableArrayList();

    public void getObservableList(ObservableList<SimpleInventory> list){

        list2 = list;
        code.setCellValueFactory(new PropertyValueFactory<>("code"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        tableView.setItems(list2);
    }

    public void carryOverButton(){
        code2.setCellValueFactory(new PropertyValueFactory<>("code"));
        description2.setCellValueFactory(new PropertyValueFactory<>("description"));

        SimpleInventory simpleInventory1 = new SimpleInventory(tableView.getSelectionModel().getSelectedItem().getCode(), tableView.getSelectionModel().getSelectedItem().getDescription());

        tableView2.getItems().add(simpleInventory1);
        tableView2.setEditable(true);
        description2.setCellFactory(TextFieldTableCell.forTableColumn());
        tableView2.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void changeSecondCellEvent(TableColumn.CellEditEvent editEvent){

        SimpleInventory quantitySelected = tableView2.getSelectionModel().getSelectedItem();
        quantitySelected.setDescription(editEvent.getNewValue().toString());
    }

    public void deleteButtonPushed(){

        ObservableList<SimpleInventory> allSimpleInventory, selectedSimpleInventory;

        allSimpleInventory = tableView2.getItems();

        selectedSimpleInventory = tableView2.getSelectionModel().getSelectedItems();

        for(SimpleInventory simpleInventory : selectedSimpleInventory){
            allSimpleInventory.remove(simpleInventory);
        }
    }

    public void buttonPrint(){

        ArrayList<String> tableInfo = new ArrayList<>();
        ArrayList<String> copy = new ArrayList<>();
        int numCopies = 1;
        String numberOfCopies = numOfCopies.getText();

        for(int i = 0; i < tableView2.getItems().size(); i++){
            tableInfo.add(tableView2.getItems().get(i).getCode().replace("\"", "") + "%" + tableView2.getItems().get(i).getDescription().replace("\"", ""));
        }

        if(numberOfCopies != null && numberOfCopies.matches("\\d+")){

            numCopies = Integer.parseInt(numOfCopies.getText());
            if(numCopies > 1) {
                for(int i = 0; i < numCopies; i++){
                    for(int j = 0; j < tableInfo.size(); j++){
                        copy.add(tableInfo.get(j));
                    }
                }
            }
        }
        else{
            //System.out.println("Its null/empty");
            copy = tableInfo;
        }

        for(int i = 0; i < copy.size(); i++){
            System.out.println("The copy: " + copy.get(i));
        }

        //Getting barcode starts here
        ArrayList<BufferedImage> image = new ArrayList<>();
        Code39Bean bean = new Code39Bean();
        final int dpi = 160;
        bean.setModuleWidth(UnitConv.in2mm(2.8f/dpi));
        bean.doQuietZone(false);

        for(int i = 0; i < copy.size(); i++){
            BitmapCanvasProvider provider = new BitmapCanvasProvider(dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
            bean.generateBarcode(provider, copy.get(i));
            try {
                provider.finish();
            } catch (IOException e) {
                e.printStackTrace();
            }
            image.add(provider.getBufferedImage());
            System.out.println(copy.get(i));
        }
        //printing starts here
        printToPrinter(image);

    }

    public static void printToPrinter(ArrayList<BufferedImage> image){

        PrinterJob printJob = PrinterJob.getPrinterJob();
        printJob.setPrintable(new Printable() {
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if (pageIndex < image.size()) {
                    graphics.drawImage(image.get(pageIndex), 0, 0, null);
                    return PAGE_EXISTS;
                }
                else{
                    return NO_SUCH_PAGE;
                }

            }
        });
        try {
            printJob.print();
        } catch (PrinterException e1) {
            e1.printStackTrace();
        }
    }
}
