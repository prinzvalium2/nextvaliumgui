package de.prinzvalium.nextvalium.nextcolony;


public class GalaxyMapValue {
    
    private String status;
    private String userName;
    
    public GalaxyMapValue(String status, String userName) {
        this.status = status;
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public String getUserName() {
        return userName;
    }
}
