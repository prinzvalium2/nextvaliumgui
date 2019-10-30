package de.prinzvalium.nextvaliumgui.nextcolony;

import java.io.IOException;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.prinzvalium.nextvaliumgui.lib.NextValiumException;
import de.prinzvalium.nextvaliumgui.lib.Util;

public class Skills {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Skills.class);
    private String userName;
    private Vector<Skill> skills;
    
    public Skills(String userName) {
        this.userName = userName;
        clean();
    }
    public void clean() {
        skills = null;
    }
    
    public int getMissionControlLevel() throws JSONException, IOException {
        return getLevel("missioncontrol");
    }
    
    public int getExplorerSkillLevel() throws JSONException, IOException {
        return getLevel("Explorer");
    }
    
    public Skill getSkill(String skillName) throws JSONException, IOException {
        
        for (Skill skill : getSkills()) {
            if (skill.getName().equalsIgnoreCase(skillName))
                return skill;
        }
        return null;
    }
    
    private int getLevel(String skillName) throws JSONException, IOException {
        
        for (Skill skill : getSkills()) {
            if (skill.getName().equalsIgnoreCase(skillName))
                return skill.getCurrent();
        }
        return -1;
    }
    
    public Skill getMissionControlSkill() throws JSONException, IOException, NextValiumException {
        
        String skillName = "missioncontrol";
        
        for (Skill skill : getSkills()) {
            if (skill.getName().equalsIgnoreCase(skillName))
                return skill;
        }
        throw new NextValiumException("Skill " + skillName + " not found");
    }
    
    public Vector<Skill> getSkills() throws JSONException, IOException {
        if (skills == null)
            skills = loadSkills(userName);

        return skills;
    }

    public static Vector<Skill> loadSkills(String userName) throws JSONException, IOException {
        LOGGER.trace("loadSkills()");
        LOGGER.debug("    LOADING skills of " + userName + ". Please wait...");
        
        Vector<Skill> skills = new Vector<Skill>();
        skills.clear();
        
        final String NEXTCOLONY_API_CMD_LOADPSKILLS = "loadskills?user=%s";
        String apiCmd = String.format(NEXTCOLONY_API_CMD_LOADPSKILLS, userName);
        JSONArray jsonSkills = Util.getJSONArrayFromApiCommand(apiCmd);
        
        for (int i = 0; i < jsonSkills.length(); i++)
            skills.add(new Skill(jsonSkills.getJSONObject(i)));
        
        return skills;
    }
    
    public static void main(String[] args) throws JSONException, IOException {
        Util.setProxy();
        int i = new Skills("prinzvalium").getMissionControlLevel();
        System.out.println("MC: " + i);
        int j = new Skills("prinzvalium").getExplorerSkillLevel();
        System.out.println("Explorer skill level: " + j);
    }
}
