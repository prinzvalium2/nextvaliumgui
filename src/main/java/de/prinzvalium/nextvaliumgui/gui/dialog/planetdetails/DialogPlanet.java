package de.prinzvalium.nextvaliumgui.gui.dialog.planetdetails;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import de.prinzvalium.nextvaliumgui.NextValiumGui;
import de.prinzvalium.nextvaliumgui.nextcolony.Planet;
import javax.swing.JTabbedPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class DialogPlanet extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTextField textFieldStatus;
    private Color colorError = new Color(200, 0, 0);
    private Color colorOk = new Color(0, 200, 0);

    /**
     * Create the dialog.
     */
    public DialogPlanet(Planet planet) {
        super(NextValiumGui.getNextValiumGui().getFrmNextvaliumManagementGui(), true);
        
        setTitle(planet.getUserName() + " / " + planet.getName());
        
        setBounds(100, 150, 800, 600);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        {
            JPanel buttonPane = new JPanel();
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                buttonPane.setLayout(new BorderLayout(0, 0));
                {
                    JPanel panelContent = new JPanel();
                    buttonPane.add(panelContent, BorderLayout.CENTER);
                    panelContent.setBorder(new EmptyBorder(0, 5, 5, 5));
                    panelContent.setLayout(new BorderLayout(5, 0));
                    {
                        JLabel lblStatus = new JLabel(" Status:");
                        panelContent.add(lblStatus, BorderLayout.WEST);
                    }
                    {
                        textFieldStatus = new JTextField();
                        panelContent.add(textFieldStatus, BorderLayout.CENTER);
                        textFieldStatus.setBorder(null);
                        textFieldStatus.setEditable(false);
                    }
                    JButton buttonClose = new JButton("Close");
                    panelContent.add(buttonClose, BorderLayout.EAST);
                    buttonClose.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            dispose();
                        }
                    });
                    buttonClose.setActionCommand("Cancel");
                }
            }
        }
        {
            JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
            contentPanel.add(tabbedPane);
            {
                PanelFleet panelFleet = new PanelFleet(this, planet);
                tabbedPane.addTab("Fleet", null, panelFleet, null);
            }
            {
                PanelPlanetDetails panelPlanetDetails = new PanelPlanetDetails(planet);
                tabbedPane.addTab("Details", null, panelPlanetDetails, null);
            }
        }
    }

    public void setStatusInfo(String status) {
        textFieldStatus.setForeground(Color.BLACK);
        setStatus(status);
    }

    public void setStatusError(String status) {
        textFieldStatus.setForeground(colorError);
        setStatus(status);
    }

    public void setStatusOk(String status) {
        textFieldStatus.setForeground(colorOk);
        setStatus(status);
    }
    
    public void setStatus(String status) {
        textFieldStatus.setText(status);
        
//        new SwingWorker<Object, Object>(){
//
//            @Override
//            protected Object doInBackground() throws Exception {
//                Thread.sleep(3000);
//                return null;
//            }
//
//            @Override
//            protected void done() {
//                textFieldStatus.setText("");
//                super.done();
//            }}.execute();
    }
    
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            DialogPlanet dialog = new DialogPlanet(null);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
