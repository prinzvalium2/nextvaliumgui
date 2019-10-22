package de.prinzvalium.nextvaliumgui.gui;

import javax.swing.JPopupMenu;

import de.prinzvalium.nextvaliumgui.NextValiumGui;

import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PopupMenuPlanet extends JPopupMenu {
    
    private PanelPlanet panelPlanet;
    
    public PopupMenuPlanet(PanelPlanet panelPlanet) {
        
        this.panelPlanet = panelPlanet;
        
        JMenuItem mntmMarkAsTarget = new JMenuItem("Mark as target");
        mntmMarkAsTarget.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                panelPlanet.setMarked(true);
            }
        });
        add(mntmMarkAsTarget);
    }

}
