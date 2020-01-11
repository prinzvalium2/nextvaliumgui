package de.prinzvalium.nextvaliumgui.gui.dialog.planetdetails;

import javax.swing.JPanel;

import de.prinzvalium.nextvaliumgui.NextValiumGui;
import de.prinzvalium.nextvaliumgui.lib.CustomJson;
import de.prinzvalium.nextvaliumgui.lib.Util;
import de.prinzvalium.nextvaliumgui.nextcolony.Mission;
import de.prinzvalium.nextvaliumgui.nextcolony.Missions;
import de.prinzvalium.nextvaliumgui.nextcolony.Planet;
import de.prinzvalium.nextvaliumgui.nextcolony.Planets;

import java.awt.GridBagLayout;
import javax.swing.JTable;
import javax.swing.SwingWorker;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;

public class PanelMissions extends JPanel {
    
    private static final long serialVersionUID = 1L;
    private JTable tableMissions;
    private DefaultTableModel model;
    private JButton btnJumpToPlanet;
    private JButton btnCancelmission;
    private DialogPlanet dialogPlanet;
    
    public PanelMissions(DialogPlanet dialogPlanet, Planet planet) {
        
        this.dialogPlanet = dialogPlanet;
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent arg0) {
                dialogPlanet.setStatusOk("Ok");
            }
        });
        
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{0, 0};
        gridBagLayout.rowHeights = new int[]{0, 0, 0};
        gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);
        
        JScrollPane scrollPaneMissions = new JScrollPane();
        GridBagConstraints gbc_scrollPaneMissions = new GridBagConstraints();
        gbc_scrollPaneMissions.insets = new Insets(0, 0, 5, 0);
        gbc_scrollPaneMissions.fill = GridBagConstraints.BOTH;
        gbc_scrollPaneMissions.gridx = 0;
        gbc_scrollPaneMissions.gridy = 0;
        add(scrollPaneMissions, gbc_scrollPaneMissions);
        
        tableMissions = new JTable();
        tableMissions.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                if (tableMissions.getSelectedRowCount()==1) {
                    Mission mission = (Mission) model.getValueAt(tableMissions.getSelectedRow(), 0);
                    ArrayList<String> users = NextValiumGui.getNextValiumGui().getListUsers();
                    btnCancelmission.setEnabled(users.contains(mission.getUser()) && mission.getCancel_trx() == null);
                }
                btnJumpToPlanet.setEnabled(tableMissions.getSelectedRowCount() == 0 ? false : true);
                if (arg0.getClickCount() == 2)
                    btnJumpToPlanet.doClick();
            }
        });
        
        tableMissions.setModel(new DefaultTableModel(
            new Object[][] {
                {"Loading data...", null, null, null, null, null, null, null, null},
            },
            new String[] {
                "Type", "Origin", "Destination", "Ships", "Start", "Arrival", "Return", "Result", "Cancel"
            }
        ) {
            boolean[] columnEditables = new boolean[] {
                false, false, false, false, false, false, false, false, false
            };
            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        });
        tableMissions.getColumnModel().getColumn(0).setPreferredWidth(60);
        tableMissions.getColumnModel().getColumn(1).setPreferredWidth(125);
        tableMissions.getColumnModel().getColumn(2).setPreferredWidth(125);
        tableMissions.getColumnModel().getColumn(3).setPreferredWidth(40);
        tableMissions.getColumnModel().getColumn(4).setPreferredWidth(125);
        tableMissions.getColumnModel().getColumn(5).setPreferredWidth(125);
        tableMissions.getColumnModel().getColumn(6).setPreferredWidth(125);
        tableMissions.getColumnModel().getColumn(7).setPreferredWidth(40);
        tableMissions.getColumnModel().getColumn(8).setPreferredWidth(40);
        scrollPaneMissions.setViewportView(tableMissions);
        
        JPanel panel = new JPanel();
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.fill = GridBagConstraints.BOTH;
        gbc_panel.gridx = 0;
        gbc_panel.gridy = 1;
        add(panel, gbc_panel);
        
        btnJumpToPlanet = new JButton("Jump to planet");
        btnJumpToPlanet.setEnabled(false);
        btnJumpToPlanet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                dialogPlanet.setVisible(false);
                Planet origin = (Planet) model.getValueAt(tableMissions.getSelectedRow(), 1);
                Planet destination = (Planet) model.getValueAt(tableMissions.getSelectedRow(), 2);
                if (!origin.equals(planet))
                    NextValiumGui.getNextValiumGui().setCenterPosition(origin);
                else
                    NextValiumGui.getNextValiumGui().setCenterPosition(destination);
            }
        });
        
        btnCancelmission = new JButton("Cancel mission");
        btnCancelmission.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                Mission mission = (Mission)model.getValueAt(tableMissions.getSelectedRow(), 0);
                actionPerformed_btnCancelmission(mission);
            }
        });
        btnCancelmission.setEnabled(false);
        panel.add(btnCancelmission);
        panel.add(btnJumpToPlanet);
        
        model = (DefaultTableModel)tableMissions.getModel();   
        
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        new SwingWorker<Vector<Mission>, Object>() {

            @Override
            protected Vector<Mission> doInBackground() throws Exception {
                return Missions.loadAllActiveMissionsOfPlanet(planet.getUserName(), planet.getId());
            }

            @Override
            protected void done() {
                model.removeRow(0);
                try {
                    HashMap<String, Planet> p = Planets.getAllPlanets();
                    for (Mission m : get()) {
                        model.addRow(new Object[] {
                                m, 
                                p.get(m.getFromPlanetId()), 
                                p.get(m.getToPlanetId()), 
                                m.getShips_total(),
                                Util.getDateAsString(m.getStart_date()), 
                                Util.getDateAsString(m.getArrival()),
                                Util.getDateAsString(m.getReturning()),
                                m.getResult(),
                                m.getCancel_trx()});
                    }
                    
                } catch (Exception e) {
                    model.addRow(new Object[] {e.getMessage(), null, null, null, null, null, null, null, null});
                }
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                super.done();
            }
        }.execute();
    }
    
    private void actionPerformed_btnCancelmission(Mission mission) {
        
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        dialogPlanet.setStatusInfo("Sending transaction to Steem. Please wait...");
        
        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                CustomJson.cancelMission(mission.getUser(), mission.getId());
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    dialogPlanet.setStatusOk("Transaction sent to Steem. Check later for NextColony accepting the transaction.");
                    
                } catch (InterruptedException | ExecutionException e) {
                    dialogPlanet.setStatusError(e.getClass().getSimpleName() + ": " + e.getMessage());
                }
                
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                super.done();
            }
        }.execute();
    }
}
