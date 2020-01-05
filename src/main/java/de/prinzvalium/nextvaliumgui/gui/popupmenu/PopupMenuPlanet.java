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

public class PopupMenuPlanet extends JPopupMenu {
    
    private static final long serialVersionUID = 1L;
    private PopupMenuPlanet popupMenuPlanet;
    private Date dateLastTransaction;
    
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
        gbl_panelPlanetDetails.columnWidths = new int[]{0, 0, 0};
        gbl_panelPlanetDetails.rowHeights = new int[]{0, 0, 0, 0};
        gbl_panelPlanetDetails.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gbl_panelPlanetDetails.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
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
        
        JCheckBox chckbxFarm = new JCheckBox("Farm");
        chckbxFarm.setEnabled(false);
        chckbxFarm.setMargin(new Insets(0, 0, 0, 0));
        GridBagConstraints gbc_chckbxFarm = new GridBagConstraints();
        gbc_chckbxFarm.anchor = GridBagConstraints.WEST;
        gbc_chckbxFarm.insets = new Insets(0, 5, 0, 0);
        gbc_chckbxFarm.gridx = 1;
        gbc_chckbxFarm.gridy = 0;
        panelPlanetDetails.add(chckbxFarm, gbc_chckbxFarm);
        GridBagConstraints gbc_chckbxCenterMap = new GridBagConstraints();
        gbc_chckbxCenterMap.insets = new Insets(0, 0, 5, 0);
        gbc_chckbxCenterMap.anchor = GridBagConstraints.WEST;
        gbc_chckbxCenterMap.gridx = 0;
        gbc_chckbxCenterMap.gridy = 1;
        panelPlanetDetails.add(chckbxCenterMap, gbc_chckbxCenterMap);
        
        JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.gridwidth = 2;
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 2;
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
                        chckbxFarm.setSelected(true);
                }
                
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
