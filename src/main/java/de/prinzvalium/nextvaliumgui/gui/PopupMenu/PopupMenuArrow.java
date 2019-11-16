package de.prinzvalium.nextvaliumgui.gui.PopupMenu;

import java.awt.Dimension;

import javax.swing.JPopupMenu;

import de.prinzvalium.nextvaliumgui.lib.Util;
import de.prinzvalium.nextvaliumgui.nextcolony.galaxymap.GalaxyMapValueExplore;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.util.Map.Entry;

import javax.swing.JScrollPane;
import java.awt.GridBagConstraints;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import java.awt.SystemColor;

import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import java.awt.Insets;
import javax.swing.JTextField;

public class PopupMenuArrow extends JPopupMenu {

    private static final long serialVersionUID = 1L;
    private JTextField txtArrival;
    private JTextField txtReturn;

    public PopupMenuArrow(GalaxyMapValueExplore val) {
        
        setPopupSize(new Dimension(180, 250));
        
        JPanel panelArrow = new JPanel();
        panelArrow.setBorder(new TitledBorder(null, val.user + " / " + val.type, TitledBorder.LEADING, TitledBorder.TOP, null, null));
        add(panelArrow);
        GridBagLayout gbl_panelArrow = new GridBagLayout();
        gbl_panelArrow.columnWidths = new int[]{0, 0, 0};
        gbl_panelArrow.rowHeights = new int[]{0, 0, 0, 0};
        gbl_panelArrow.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gbl_panelArrow.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
        panelArrow.setLayout(gbl_panelArrow);
        
        JLabel lblArrival = new JLabel("Arrival:");
        GridBagConstraints gbc_lblArrival = new GridBagConstraints();
        gbc_lblArrival.anchor = GridBagConstraints.EAST;
        gbc_lblArrival.insets = new Insets(0, 0, 5, 5);
        gbc_lblArrival.gridx = 0;
        gbc_lblArrival.gridy = 0;
        panelArrow.add(lblArrival, gbc_lblArrival);
        
        txtArrival = new JTextField();
        txtArrival.setEnabled(false);
        txtArrival.setEditable(false);
        GridBagConstraints gbc_txtArrival = new GridBagConstraints();
        gbc_txtArrival.insets = new Insets(0, 0, 5, 0);
        gbc_txtArrival.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtArrival.gridx = 1;
        gbc_txtArrival.gridy = 0;
        panelArrow.add(txtArrival, gbc_txtArrival);
        txtArrival.setColumns(10);
        txtArrival.setText((val.date == null) ? "-" : Util.getDateAsString(val.date));
        
        JLabel lblReturn = new JLabel("Return:");
        GridBagConstraints gbc_lblReturn = new GridBagConstraints();
        gbc_lblReturn.anchor = GridBagConstraints.EAST;
        gbc_lblReturn.insets = new Insets(0, 0, 5, 5);
        gbc_lblReturn.gridx = 0;
        gbc_lblReturn.gridy = 1;
        panelArrow.add(lblReturn, gbc_lblReturn);
        
        txtReturn = new JTextField();
        txtReturn.setEnabled(false);
        txtReturn.setEditable(false);
        GridBagConstraints gbc_txtReturn = new GridBagConstraints();
        gbc_txtReturn.insets = new Insets(0, 0, 5, 0);
        gbc_txtReturn.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtReturn.gridx = 1;
        gbc_txtReturn.gridy = 1;
        panelArrow.add(txtReturn, gbc_txtReturn);
        txtReturn.setColumns(10);
        
        txtReturn.setText((val.date_return == null) ? "-" : Util.getDateAsString(val.date_return));
        
        JScrollPane scrollPaneShips = new JScrollPane();
        GridBagConstraints gbc_scrollPaneShips = new GridBagConstraints();
        gbc_scrollPaneShips.gridwidth = 2;
        gbc_scrollPaneShips.fill = GridBagConstraints.BOTH;
        gbc_scrollPaneShips.gridx = 0;
        gbc_scrollPaneShips.gridy = 2;
        panelArrow.add(scrollPaneShips, gbc_scrollPaneShips);
        
        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> listShips = new JList<>(model);
        listShips.setEnabled(false);
        listShips.setBackground(SystemColor.menu);
        scrollPaneShips.setViewportView(listShips);
        
        for (Entry<String, Integer> entry : val.mapShips.entrySet())
            model.addElement(String.format(entry.getKey() + ": " + entry.getValue()));
    }
}
