package za.co.samtakie.samtakieradio.data;

public class Radio {

    private String radioName;
    private String radionLink;


    public Radio(String radioName, String radionLink){
        this.radioName = radioName;
        this.radionLink = radionLink;
    }

    public String getRadioName() {
        return radioName;
    }

    public String getRadionLink() {
        return radionLink;
    }

}