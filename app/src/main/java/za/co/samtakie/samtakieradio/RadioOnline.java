
/*Copyright [2018] [Jurgen Emanuels]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/package za.co.samtakie.samtakieradio;

@SuppressWarnings("ALL")
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