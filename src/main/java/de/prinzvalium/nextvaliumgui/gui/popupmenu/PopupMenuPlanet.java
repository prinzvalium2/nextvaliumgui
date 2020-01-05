package de.prinzvalium.nextvaliumgui.gui.popupmenu;

import javax.swing.JPopupMenu;

import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import de.prinzvalium.nextvaliumgui.NextValiumGui;
import de.prinzvalium.nextvaliumgui.gui.PanelPlanet;
import de.prinzvalium.nextvaliumgui.nextcolony.Fleet;
import de.prinzvalium.nextvaliumgui.nextcolony.Planet;
import de.prinzvalium.nextvaliumgui.nextcolony.Production;
import de.prinzvalium.nextvaliumgui.nextcolony.ProductionRessources;
import de.prinzvalium.nextvaliumgui.nextcolony.RessourceQuantities;
import de.prinzvalium.nextvaliumgui.nextcolony.RessourceQuantitiesRessources;
import de.prinzvalium.nextvaliumgui.nextcolony.TransactionsRecent;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.SystemColor;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class PopupMenuPlanet extends JPopupMenu {
    
    private static final long serialVersionUID = 1L;
    private PopupMenuPlanet popupMenuPlanet;
    private Date dateLastTransaction = null;
    private Double loot = null;
    private JTextField txtLoot;
    
    public PopupMenuPlanet(PanelPlanet panelPlanet) {
        
        popupMenuPlanet = this;
        
        setPopupSize(new Dimension(180, 250));
        
        Planet planet = panelPlanet.getPlanet();
        String titleBorder = planet.getUserName() + " / " + planet.getName();
        
        JPanel panelPlanetDetails = new JPanel();
        panelPlanetDetails.setBorder(new TitledBorder(null, titleBorder, TitledBorder.LEADING, TitledBorder.TOP, null, null));
        add(panelPlanetDetails);
        
        DefaultListModel<String> model = new DefaultListModel<>();
        GridBagLayout gbl_panelPlanetDetails = new GridBagLayout();
        gbl_panelPlanetDetails.columnWidths = new int[]{0, 0};
        gbl_panelPlanetDetails.rowHeights = new int[]{0, 0, 0, 0, 0};
        gbl_panelPlanetDetails.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gbl_panelPlanetDetails.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
        panelPlanetDetails.setLayout(gbl_panelPlanetDetails);
        
        JCheckBox chckbxMarkAsTarget = new JCheckBox("Mark as target");
        chckbxMarkAsTarget.setMargin(new Insets(0, 0, 0, 0));
        Planet planetMarked = NextValiumGui.getNextValiumGui().getPlanetMarkedAsTarget();
        boolean unselected = planetMarked == null || !planet.getId().equalsIgnoreCase(planetMarked.getId());
        chckbxMarkAsTarget.setSelected(!unselected);
        chckbxMarkAsTarget.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                panelPlanet.setMarked(chckbxMarkAsTarget.isSelected());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(250);
                            popupMenuPlanet.setVisible(false);
                        } catch (InterruptedException e) {
                        }
                    }}).start();
            }
        });
        GridBagConstraints gbc_chckbxMarkAsTarget = new GridBagConstraints();
        gbc_chckbxMarkAsTarget.anchor = GridBagConstraints.WEST;
        gbc_chckbxMarkAsTarget.gridx = 0;
        gbc_chckbxMarkAsTarget.gridy = 0;
        panelPlanetDetails.add(chckbxMarkAsTarget, gbc_chckbxMarkAsTarget);
        
        JCheckBox chckbxCenterMap = new JCheckBox("Center map");
        chckbxCenterMap.setMargin(new Insets(0, 0, 0, 0));
        chckbxCenterMap.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                new SwingWorker<Object, Object>() {
                   @Override
                    protected Object doInBackground() throws Exception {
                        Thread.sleep(250);
                        popupMenuPlanet.setVisible(false);
                        return null;
                    }
                    @Override
                    protected void done() {
                        NextValiumGui.getNextValiumGui().setCenterPosition(planet);
                        super.done();
                    }
                }.execute();
            }
        });
        GridBagConstraints gbc_chckbxCenterMap = new GridBagConstraints();
        gbc_chckbxCenterMap.anchor = GridBagConstraints.WEST;
        gbc_chckbxCenterMap.gridx = 0;
        gbc_chckbxCenterMap.gridy = 1;
        panelPlanetDetails.add(chckbxCenterMap, gbc_chckbxCenterMap);
        
        JPanel panelLoot = new JPanel();
        GridBagConstraints gbc_panelLoot = new GridBagConstraints();
        gbc_panelLoot.insets = new Insets(0, 0, 5, 0);
        gbc_panelLoot.fill = GridBagConstraints.BOTH;
        gbc_panelLoot.gridx = 0;
        gbc_panelLoot.gridy = 2;
        panelPlanetDetails.add(panelLoot, gbc_panelLoot);
        GridBagLayout gbl_panelLoot = new GridBagLayout();
        gbl_panelLoot.columnWidths = new int[]{0, 0, 0, 0};
        gbl_panelLoot.rowHeights = new int[]{14, 0};
        gbl_panelLoot.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
        gbl_panelLoot.rowWeights = new double[]{0.0, Double.MIN_VALUE};
        panelLoot.setLayout(gbl_panelLoot);
        
        JLabel lblLoot = new JLabel("Loot:");
        lblLoot.setEnabled(false);
        GridBagConstraints gbc_lblLoot = new GridBagConstraints();
        gbc_lblLoot.insets = new Insets(0, 0, 0, 2);
        gbc_lblLoot.anchor = GridBagConstraints.EAST;
        gbc_lblLoot.gridx = 1;
        gbc_lblLoot.gridy = 0;
        panelLoot.add(lblLoot, gbc_lblLoot);
        
        txtLoot = new JTextField();
        txtLoot.setBorder(null);
        txtLoot.setEnabled(false);
        txtLoot.setEditable(false);
        GridBagConstraints gbc_txtLoot = new GridBagConstraints();
        gbc_txtLoot.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtLoot.gridx = 2;
        gbc_txtLoot.gridy = 0;
        panelLoot.add(txtLoot, gbc_txtLoot);
        txtLoot.setColumns(10);
        
        JCheckBox checkBoxFarm = new JCheckBox("Farm");
        checkBoxFarm.setMargin(new Insets(0, 0, 0, 0));
        checkBoxFarm.setEnabled(false);
        GridBagConstraints gbc_checkBoxFarm = new GridBagConstraints();
        gbc_checkBoxFarm.insets = new Insets(0, 0, 0, 5);
        gbc_checkBoxFarm.gridx = 0;
        gbc_checkBoxFarm.gridy = 0;
        panelLoot.add(checkBoxFarm, gbc_checkBoxFarm);
        
        JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 3;
        panelPlanetDetails.add(scrollPane, gbc_scrollPane);
        
        JList<String> listShips = new JList<>(model);
        scrollPane.setViewportView(listShips);
        listShips.setVisibleRowCount(8);
        listShips.setEnabled(false);
        listShips.setBackground(SystemColor.menu);
        
        model.addElement("Loading ships...");
        
        new SwingWorker<HashMap<String, Integer>, Object>() {

            @Override
            protected HashMap<String, Integer> doInBackground() throws Exception {

                RessourceQuantitiesRessources res;
                res = RessourceQuantities.loadRessourceQuantites(planet.getName(), planet.getId());
                
                ProductionRessources pr = new Production(planet).loadProduction();
                
                Double lootCoal = res.getCoal() - pr.getCoal().getSafe();
                if (lootCoal < 0)
                    lootCoal = 0.0;
                Double lootOre = res.getOre() - pr.getOre().getSafe();
                if (lootOre < 0)
                    lootOre = 0.0;
                Double lootCopper = res.getCopper() - pr.getCopper().getSafe();
                if (lootCopper < 0)
                    lootCopper = 0.0;
                Double lootUranium = res.getUranium() - pr.getUranium().getSafe();
                if (lootUranium < 0)
                    lootUranium = 0.0;
                
                loot = lootCoal + lootOre + lootCopper + lootUranium;
                
                
                dateLastTransaction = TransactionsRecent.getLastTransactionOfUser(planet.getUserName());
                
                Fleet fleet = new Fleet(planet.getUserName(), planet.getName(), planet.getId());
                return fleet.getNumberOfShipTypesInShipyard();
            }
            
            @Override
            protected void done() {
                
                if (dateLastTransaction != null) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date());
                    cal.add(Calendar.DATE, -7);
                    if (dateLastTransaction.before(cal.getTime()))
                        checkBoxFarm.setSelected(true);
                }
                
                if (loot != null)
                    txtLoot.setText(String.format("%.0f", loot));
                
                try {
                    HashMap<String, Integer> mapShips = get();
                    model.removeAllElements();
                    
                    if (mapShips.isEmpty()) {
                        model.addElement("No ships");
                        return;
                    }
                        
                    for (Map.Entry<String,Integer> entry : mapShips.entrySet())
                        model.addElement(entry.getKey() + ": " + entry.getValue());
                    
                } catch (InterruptedException | ExecutionException e) {
                    model.addElement(e.getMessage());
                }
            }
        }.execute();
    }
}
