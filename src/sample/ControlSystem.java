package sample;

import javafx.beans.property.SimpleStringProperty;

public class ControlSystem {

    private SimpleStringProperty code;
    private SimpleStringProperty description;
    private SimpleStringProperty initialStock;
    private SimpleStringProperty in;

    public ControlSystem(String code, String description, String initialStock, String in){
        this.code = new SimpleStringProperty(code);
        this.description = new SimpleStringProperty(description);
        this.initialStock = new SimpleStringProperty(initialStock);
        this.in = new SimpleStringProperty(in);
    }

    public String getCode(){
        return code.get();
    }
    public String getDescription(){
        return description.get();
    }
    public String getInitialStock(){
        return initialStock.get();
    }
    public String getIn(){
        return in.get();
    }

    public void setCode(String code){
        this.code = new SimpleStringProperty(code);
    }
    public void setdescription(String description){
        this.description = new SimpleStringProperty(description);
    }
    public void setInitialStock(String initialStock){
        this.initialStock = new SimpleStringProperty(initialStock);
    }
    public void setIn(String in){
        this.in = new SimpleStringProperty(in);
    }

    public String getAll(){
        return code.get() + " " + description.get() + " " + initialStock.get() + " " + in.get();
    }
}
