package de.prinzvalium.nextvaliumgui.nextcolony;

import java.util.Date;

import org.json.JSONObject;

public class Skill {
    
    private String name;
    private int current;
    private Date busy;
    private int coal;
    private int ore;
    private int copper;
    private int uranium;
    
    Skill(JSONObject jsonSkill) {
        name = jsonSkill.getString("name");
        current = jsonSkill.getInt("current");
        busy = new Date(jsonSkill.getLong("busy") * 1000);
        coal = jsonSkill.getInt("coal");
        ore = jsonSkill.getInt("ore");
        copper = jsonSkill.getInt("copper");
        uranium = jsonSkill.getInt("uranium");
    }
       
    public String getName() {
        return name;
    }

    public int getCurrent() {
        return current;
    }
    
    public Date getBusy() {
        return busy;
    }
    
    public boolean isBusy() {
        if (busy.after(new Date()))
            return true;
        
        return false;
    }

    public int getCoal() {
        return coal;
    }

    public int getOre() {
        return ore;
    }

    public int getCopper() {
        return copper;
    }

    public int getUranium() {
        return uranium;
    }
}
