package model;

public class Clothing {
    private String name;
    private String barcode;

    public Clothing(String name, String barcode){
        if(name == null || name.isBlank() || barcode == null || barcode.isBlank()){
            throw new IllegalArgumentException("name and barcode cannot be empty");
        }
        //TODO check if barcode was already used
        this.name = name;
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public String getBarcode() {
        return barcode;
    }


}
