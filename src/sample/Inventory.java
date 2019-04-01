package sample;

import javafx.beans.property.SimpleStringProperty;

public class Inventory {

    private SimpleStringProperty code;
    private SimpleStringProperty description;
    private SimpleStringProperty initialStock;
    private SimpleStringProperty in;
    private SimpleStringProperty out;
    private SimpleStringProperty totalStock;
    private SimpleStringProperty loc;
    private SimpleStringProperty box;

    public Inventory(String code, String description, String initialStock, String in, String out, String totalStock, String loc, String box){
        this.code = new SimpleStringProperty(code);
        this.description = new SimpleStringProperty(description);
        this.initialStock = new SimpleStringProperty(initialStock);
        this.in = new SimpleStringProperty(in);
        this.out = new SimpleStringProperty(out);
        this.totalStock = new SimpleStringProperty(totalStock);
        this.loc = new SimpleStringProperty(loc);
        this.box = new SimpleStringProperty(box);
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
    public String getOut(){
        return out.get();
    }
    public String getTotalStock(){
        return totalStock.get();
    }
    public String getLoc(){
        return loc.get();
    }
    public String getBox(){
        return box.get();
    }

    public void setCode(String code){
        this.code = new SimpleStringProperty(code);
    }
    public void setDescription(String description){
        this.description = new SimpleStringProperty(description);
    }
    public void setInitialStock(String initialStock){
        this.initialStock = new SimpleStringProperty(initialStock);
    }
    public void setIn(String in){
        this.in = new SimpleStringProperty(in);
    }
    public void setOut(String out){
        this.out = new SimpleStringProperty(out);
    }
    public void setTotalStock(String totalStock){
        this.totalStock = new SimpleStringProperty(totalStock);
    }
    public void setLoc(String loc){
        this.loc = new SimpleStringProperty(loc);
    }
    public void setBox(String box){
        this.box = new SimpleStringProperty(box);
    }

    public String getAll(){
        return code.get() + " " + description.get() + " " + initialStock.get() + " " + in.get() + " " + out.get() + " " + totalStock.get() + " " + loc.get() + " " + box.get();
    }
}
