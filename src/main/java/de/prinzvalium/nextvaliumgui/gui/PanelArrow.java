package de.prinzvalium.nextvaliumgui.gui;

import javax.swing.JPanel;

import de.prinzvalium.nextvaliumgui.gui.popupmenu.PopupMenuMission;
import de.prinzvalium.nextvaliumgui.nextcolony.galaxymap.GalaxyMapValueExplore;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelArrow extends JPanel {
    
    private static final long serialVersionUID = 1L;

    public PanelArrow(GalaxyMapValueExplore val) {
        setOpaque(false);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent arg0) {
                PopupMenuMission popupMenuArrow = new PopupMenuMission(val);
                popupMenuArrow.show(arg0.getComponent(), arg0.getX(), arg0.getY());
            }
        });
    }
}
