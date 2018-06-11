package za.co.samtakie.samtakieradio;

public class RadioOnline {

    private String appwidget;
    private String radioName;
    private String radioImage;
    private int radioID;
    private String radioLink;

    public RadioOnline(String appwidget, String radioName, String radioImage, int radioID, String radioLink) {
        this.appwidget = appwidget;
        this.radioName = radioName;
        this.radioImage = radioImage;
        this.radioID = radioID;
        this.radioLink = radioLink;
    }

    public RadioOnline() {

    }

    public String getAppwidget() {
        return appwidget;
    }

    public void setAppwidget(String appwidget) {
        this.appwidget = appwidget;
    }

    public String getRadioName() {
        return radioName;
    }

    public void setRadioName(String radioName) {
        this.radioName = radioName;
    }

    public String getRadioImage() {
        return radioImage;
    }

    public void setRadioImage(String radioImage) {
        this.radioImage = radioImage;
    }

    public int getRadioID() {
        return radioID;
    }

    public void setRadioID(int radioID) {
        this.radioID = radioID;
    }

    public String getRadioLink() {
        return radioLink;
    }

    public void setRadioLink(String radioLink) {
        this.radioLink = radioLink;
    }

}