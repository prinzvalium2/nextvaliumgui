package de.prinzvalium.nextvaliumgui.gui.dialog.info;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import de.prinzvalium.nextvaliumgui.nextcolony.RessourceQuantitiesRessources;
import de.prinzvalium.nextvaliumgui.NextValiumGui;
import de.prinzvalium.nextvaliumgui.nextcolony.Planet;
import de.prinzvalium.nextvaliumgui.nextcolony.Planets;
import de.prinzvalium.nextvaliumgui.nextcolony.RessourceQuantities;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DialogFullDepots extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTable table;
    private JButton btnJumpToPlanet;
    private JDialog dialog;
    private ArrayList<Planet> listPlanets;
    private DefaultTableModel model;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            DialogFullDepots dialog = new DialogFullDepots();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public DialogFullDepots() {
        dialog = this;
        setModal(true);
        setTitle("Full depots");
        setBounds(100, 100, 600, 500);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        {
            JScrollPane scrollPane = new JScrollPane();
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            {
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
                        {"Loading data...", null, null},
                    },
                    new String[] {
                        "User", "Planet", "Id"
                    }
                ) {
                    Class[] columnTypes = new Class[] {
                        String.class, String.class, String.class, String.class
                    };
                    public Class getColumnClass(int columnIndex) {
                        return columnTypes[columnIndex];
                    }
                    boolean[] columnEditables = new boolean[] {
                        false, false, false, false
                    };
                    public boolean isCellEditable(int row, int column) {
                        return columnEditables[column];
                    }
                });
                table.getColumnModel().getColumn(0).setPreferredWidth(125);
                table.getColumnModel().getColumn(1).setPreferredWidth(125);
                table.getColumnModel().getColumn(2).setPreferredWidth(125);
                scrollPane.setViewportView(table);
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
                        String id = (String) model.getValueAt(table.getSelectedRow(), 2);  
                        
                        Planet planet = null;
                        for (Planet p : listPlanets) {
                            if (p.getId().equalsIgnoreCase(id) ) {
                                planet = p;
                                break;
                            }
                        }
                        
                        if (planet != null)
                            NextValiumGui.getNextValiumGui().setCenterPosition(planet);
                    }
                });
                btnJumpToPlanet.setActionCommand("");
                buttonPane.add(btnJumpToPlanet);
            }
            {
                JButton okButton = new JButton("OK");
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent arg0) {
                        dialog.setVisible(false);
                    }
                });
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
        }
        
        model = (DefaultTableModel)table.getModel();            }

        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        new SwingWorker<ArrayList<Planet>, Object>() {

            @Override
            protected ArrayList<Planet> doInBackground() throws Exception {
                
                listPlanets = new ArrayList<>();

                for (String user : NextValiumGui.getNextValiumGui().getListUsers()) {
                    
                    List<Planet> planets = new ArrayList<>(Planets.loadUserPlanets(user).values());
                    Collections.sort(planets);
                    
                    for (Planet p : planets) { 
                        RessourceQuantitiesRessources res = RessourceQuantities.loadRessourceQuantites(p.getName(), p.getId());
                        if (res.getCoal() >= res.getCoalDepot() &&
                                res.getOre() >= res.getOreDepot() &&
                                res.getCopper() >= res.getCopperDepot() &&
                                res.getUranium() >= res.getUraniumDepot() )
                            
                            listPlanets.add(p);
                    }
                }
                return listPlanets;
            }

            @Override
            protected void done() {
                try {
                    model.removeRow(0);
                    for (Planet p : get())
                        model.addRow(new Object[] { p.getUserName(), p.getName(), p.getId()});
                    
                } catch (InterruptedException | ExecutionException e) {
                }
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                super.done();
            }
        }.execute();
    }
}
