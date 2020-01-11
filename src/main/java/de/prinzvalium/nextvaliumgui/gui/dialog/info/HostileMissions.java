package de.prinzvalium.nextvaliumgui.gui.dialog.info;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import org.json.JSONException;

import de.prinzvalium.nextvaliumgui.NextValiumGui;
import de.prinzvalium.nextvaliumgui.lib.Util;
import de.prinzvalium.nextvaliumgui.nextcolony.Mission;
import de.prinzvalium.nextvaliumgui.nextcolony.Missions;
import de.prinzvalium.nextvaliumgui.nextcolony.Planets;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.awt.event.ActionEvent;

public class HostileMissions extends JDialog {

    private static final long serialVersionUID = 1L;
    private JDialog dialog;
    private final JPanel contentPanel = new JPanel();
    private JTable table;
    private JButton btnJumpToPlanet;
    private DefaultTableModel model;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            HostileMissions dialog = new HostileMissions();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public HostileMissions() {
        dialog = this;
        setModal(true);
        setTitle("Hostile missions");
        setBounds(100, 100, 900, 600);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        {
            JScrollPane scrollPaneSeasonRanking = new JScrollPane();
            contentPanel.add(scrollPaneSeasonRanking, BorderLayout.CENTER);
            
            table = new JTable();
            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent arg0) {
                    btnJumpToPlanet.setEnabled(table.getSelectedRowCount() == 0 ? false : true);
                    if (arg0.getClickCount() == 2)
                        btnJumpToPlanet.doClick();
                }
           });
           table.setModel(new DefaultTableModel(
                new Object[][] {
                    {"Loading data...", null, null, null, null, null, null, null, null},
                },
                new String[] {
                    "From user", "From planet", "Type", "Canceled", "To user", "To planet", "To ID", "Arrival", "Returning"
                }
            ) {
                private static final long serialVersionUID = 1L;
                boolean[] columnEditables = new boolean[] {
                    false, false, false, false, false, false, false, false
                };
                public boolean isCellEditable(int row, int column) {
                    return columnEditables[column];
                }
            });
            table.getColumnModel().getColumn(0).setPreferredWidth(100);
            table.getColumnModel().getColumn(1).setPreferredWidth(100);
            table.getColumnModel().getColumn(2).setPreferredWidth(40);
            table.getColumnModel().getColumn(3).setPreferredWidth(40);
            table.getColumnModel().getColumn(4).setPreferredWidth(100);
            table.getColumnModel().getColumn(5).setPreferredWidth(100);
            table.getColumnModel().getColumn(6).setPreferredWidth(100);
            table.getColumnModel().getColumn(7).setPreferredWidth(110);
            table.getColumnModel().getColumn(8).setPreferredWidth(110);

            scrollPaneSeasonRanking.setViewportView(table);
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                btnJumpToPlanet = new JButton("Jump to planet");
                btnJumpToPlanet.setEnabled(false);
                btnJumpToPlanet.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent arg0) {
                        dialog.setVisible(false);
                        String user = (String) model.getValueAt(table.getSelectedRow(), 4);   
                        String planetId = (String) model.getValueAt(table.getSelectedRow(), 6);   
                        
                        try {
                            NextValiumGui.getNextValiumGui().setCenterPosition(Planets.getAllPlanets().get(planetId));
                            
                        } catch (JSONException | IOException e) {
                        }
                    }
                });
                btnJumpToPlanet.setActionCommand("");
                buttonPane.add(btnJumpToPlanet);
            }
            {
                JButton okButton = new JButton("OK");
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent arg0) {
                        setVisible(false);
                    }
                });
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
        }
        model = (DefaultTableModel)table.getModel();
  
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        new SwingWorker<HashMap<String, Vector<Mission>>, Object>() {
            @Override
            protected HashMap<String, Vector<Mission>> doInBackground() throws Exception {
                
                HashMap<String, Vector<Mission>> mapHostileMissions = new HashMap<String, Vector<Mission>>();
                ArrayList<String> users = NextValiumGui.getNextValiumGui().getListUsers();
                for (String user : users)
                    mapHostileMissions.put(user, Missions.listAllIncomingAttacks(user));
                
                return mapHostileMissions;
            }
            @Override
            protected void done() {
                try {
                    model.removeRow(0);
                    HashMap<String, Vector<Mission>> mapHostileMissions = get();
                    for (Entry<String, Vector<Mission>> entry : mapHostileMissions.entrySet()) {
                        String user = entry.getKey();
                        Vector<Mission> missions = entry.getValue();
                        for (Mission mission : missions) {
                            String attacker = mission.getUser();
                            String planetFrom = mission.getFromPlanetName();
                            String type = mission.getType();
                            String planetTo = mission.getToPlanetName();
                            String planetIdTo = mission.getToPlanetId();
                            String canceled = mission.getCancel_trx() == null ? "" : "canceled";
                            String arrive = Util.getDateAsString(mission.getArrival());
                            String returning = Util.getDateAsString(mission.getReturning());
                            model.addRow(new Object[] { attacker, planetFrom, type, canceled, user, planetTo, planetIdTo, arrive, returning});
                        }
                    }
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    
                } catch (InterruptedException | ExecutionException e) {
                    model.addRow(new Object[] { null, e.getCause()+": "+e.getMessage(), null, null, null });
                }
                super.done();
            }
        }.execute();
    }
}
