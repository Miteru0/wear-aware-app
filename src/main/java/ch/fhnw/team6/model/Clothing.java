package ch.fhnw.team6.model;

public class Clothing {
    private String name;
    private String barcode;

    /**
     *  creates new Clothing instance
     * @param name name of this Clothing instance
     * @param barcode barcode which corresponds to this Clothing instance
     * @throws IllegalArgumentException if name or barcode are blank and if barcode is already used for other Clothing instance
     */
    public Clothing(String name, String barcode) throws IllegalArgumentException{
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
