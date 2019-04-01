package sample;

import javafx.beans.property.SimpleStringProperty;

public class SimpleInventory {

    private SimpleStringProperty code;
    private SimpleStringProperty description;

    public SimpleInventory(String code, String description){
        this.code = new SimpleStringProperty(code);
        this.description = new SimpleStringProperty(description);
    }

    public String getCode(){
        return code.get();
    }
    public String getDescription(){
        return description.get();
    }

    public void setCode(String code){
        this.code = new SimpleStringProperty(code);
    }
    public void setDescription(String description){
        this.description = new SimpleStringProperty(description);
    }

    public String getAll(){
        return code.get() + " " + description.get();
    }
}
