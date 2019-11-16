package de.prinzvalium.nextvaliumgui.gui.dialog.seasonranking;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.json.JSONArray;
import org.json.JSONObject;

import de.prinzvalium.nextvaliumgui.lib.Util;

import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;
import java.awt.event.ActionEvent;

public class SeasonRanking extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTable table;
    private JLabel lblSeasonname;
    private DefaultTableModel model;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            SeasonRanking dialog = new SeasonRanking();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public SeasonRanking() {
        setModal(true);
        setTitle("Season ranking");
        setBounds(100, 100, 600, 500);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        {
            lblSeasonname = new JLabel("SeasonName");
            lblSeasonname.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
            contentPanel.add(lblSeasonname, BorderLayout.NORTH);
        }
        {
            JScrollPane scrollPaneSeasonRanking = new JScrollPane();
            contentPanel.add(scrollPaneSeasonRanking, BorderLayout.CENTER);
            
            table = new JTable();
            table.setModel(new DefaultTableModel(
                new Object[][] {
                    {null, null, null, null, null},
                },
                new String[] {
                    "Pos.", "Account", "Build", "Destroyed", "Total"
                }
            ) {
                private static final long serialVersionUID = 1L;
                boolean[] columnEditables = new boolean[] {
                    false, false, false, false, false
                };
                public boolean isCellEditable(int row, int column) {
                    return columnEditables[column];
                }
            });
            table.getColumnModel().getColumn(0).setPreferredWidth(50);
            table.getColumnModel().getColumn(1).setPreferredWidth(200);
            table.getColumnModel().getColumn(2).setPreferredWidth(100);
            table.getColumnModel().getColumn(3).setPreferredWidth(100);
            table.getColumnModel().getColumn(4).setPreferredWidth(100);

            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            table.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
            table.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
            table.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);

            scrollPaneSeasonRanking.setViewportView(table);
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
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
        lblSeasonname.setText("Loading data...");
        
        new SwingWorker<JSONObject, Object>() {
            @Override
            protected JSONObject doInBackground() throws Exception {
                return Util.getJSONObjectFromApiCommand(Util.NEXTCOLONY_API_CMD_SEASONRANKING);
            }
            @Override
            protected void done() {
                try {
                    model.removeRow(0);
                    JSONObject seasonRanking = get();
                    lblSeasonname.setText(seasonRanking.getString("name"));
                    JSONArray ranking = seasonRanking.getJSONArray("ranking");
                    for (int i = 0; i < ranking.length(); i++) {
                        JSONObject rank = ranking.getJSONObject(i);
                        String user = rank.getString("user");
                        String build_reward = String.format("%.3f", rank.getDouble("build_reward")/10E7);
                        String destroy_reward = String.format("%.3f", rank.getDouble("destroy_reward")/10E7);
                        String total_reward = String.format("%.3f", rank.getDouble("total_reward")/10E7);
                        model.addRow(new Object[] { i, user, build_reward, destroy_reward, total_reward});
                    }
                    
                } catch (InterruptedException | ExecutionException e) {
                    model.addRow(new Object[] { null, e.getCause()+": "+e.getMessage(), null, null, null });
                }
                super.done();
            }
        }.execute();
    }
}
