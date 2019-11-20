package de.prinzvalium.nextvaliumgui.gui.dialog.lastplanets;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import de.prinzvalium.nextvaliumgui.NextValiumGui;
import de.prinzvalium.nextvaliumgui.lib.Util;
import de.prinzvalium.nextvaliumgui.nextcolony.Planet;
import de.prinzvalium.nextvaliumgui.nextcolony.Planets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DialogLastPlanets extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTable table;
    private JButton btnJumpToPlanet;
    private JDialog dialog;
    private HashMap<String, Planet> mapPlanets;
    private DefaultTableModel model;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            DialogLastPlanets dialog = new DialogLastPlanets();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public DialogLastPlanets() {
        dialog = this;
        setModal(true);
        setTitle("Last planets");
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
                        {null, null, null, null},
                    },
                    new String[] {
                        "Date", "User", "Planet", "Id"
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
                table.getColumnModel().getColumn(3).setPreferredWidth(125);
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
                        String id = (String) model.getValueAt(table.getSelectedRow(), 3);   
                        Planet p = mapPlanets.get(id);
                        NextValiumGui.getNextValiumGui().setCenterPosition(p);
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

        new SwingWorker<ArrayList<Planet>, Object>() {

            @Override
            protected ArrayList<Planet> doInBackground() throws Exception {
                
                mapPlanets = new HashMap<String, Planet>();
                
                ArrayList<String> listUsers = NextValiumGui.getNextValiumGui().getListUsers();
                for (String user : listUsers)
                    mapPlanets.putAll(Planets.loadUserPlanets(user));
                
                ArrayList<Planet> listPlanets = new ArrayList<>(mapPlanets.values());
                Collections.sort(listPlanets, new Comparator<Planet>() {
                    @Override
                    public int compare(Planet arg0, Planet arg1) {
                        return arg0.getDate().before(arg1.getDate()) ? 1 : -1;
                    }});

                return listPlanets;
            }

            @Override
            protected void done() {
                try {
                    model.removeRow(0);
                    for (Planet p : get())
                        model.addRow(new Object[] { Util.getDateAsString(p.getDate()), p.getUserName(), p.getName(), p.getId()});
                    
                } catch (InterruptedException | ExecutionException e) {
                }
                super.done();
            }
        }.execute();
    }
}
