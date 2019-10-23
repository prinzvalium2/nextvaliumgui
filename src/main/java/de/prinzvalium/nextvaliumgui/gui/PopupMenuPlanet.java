package de.prinzvalium.nextvaliumgui.gui;

import javax.swing.JPopupMenu;

import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PopupMenuPlanet extends JPopupMenu {
    
    private static final long serialVersionUID = 1L;
    
    public PopupMenuPlanet(PanelPlanet panelPlanet) {
        
        JMenuItem mntmMarkAsTarget = new JMenuItem("Mark as target");
        mntmMarkAsTarget.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                panelPlanet.setMarked(true);
            }
        });
        add(mntmMarkAsTarget);
    }
}
