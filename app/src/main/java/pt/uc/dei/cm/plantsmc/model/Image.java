package pt.uc.dei.cm.plantsmc.model;

public class Image {
    private String downloadUrl;
    private String greenhouseID;
    private boolean isDisplayPhoto;

    public Image() {
    }

    public Image(String downloadUrl, String greenhouseID, boolean isDisplayPhoto) {
        this.downloadUrl = downloadUrl;
        this.greenhouseID = greenhouseID;
        this.isDisplayPhoto = isDisplayPhoto;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getGreenhouseID() {
        return greenhouseID;
    }

    public void setGreenhouseID(String greenhouseID) {
        this.greenhouseID = greenhouseID;
    }

    public boolean isDisplayPhoto() {
        return isDisplayPhoto;
    }

    public void setDisplayPhoto(boolean displayPhoto) {
        isDisplayPhoto = displayPhoto;
    }
}
