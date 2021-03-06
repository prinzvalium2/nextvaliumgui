package de.prinzvalium.nextvaliumgui.gui.dialog.planetdetails;

import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.text.AbstractDocument;

import de.prinzvalium.nextvaliumgui.NextValiumGui;
import de.prinzvalium.nextvaliumgui.lib.CustomJson;
import de.prinzvalium.nextvaliumgui.lib.LimitDocumentFilter;
import de.prinzvalium.nextvaliumgui.lib.SteemUtil;
import de.prinzvalium.nextvaliumgui.lib.Util;
import de.prinzvalium.nextvaliumgui.nextcolony.Building;
import de.prinzvalium.nextvaliumgui.nextcolony.Buildings;
import de.prinzvalium.nextvaliumgui.nextcolony.Planet;
import de.prinzvalium.nextvaliumgui.nextcolony.PlanetDetails;
import de.prinzvalium.nextvaliumgui.nextcolony.Planets;

import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Dimension;

public class PanelPlanetDetails extends JPanel {

    private static final long serialVersionUID = 1L;
    private DialogPlanet dialogPlanet;
    private JLabel panelImage;
    private JTextField txtRenameplanet;
    private JTextField txtGiftPlanet;
    private Planet planet;
    private JButton btnRenameplanet;
    private JButton btnBurnPlanet;
    private HashMap<String, Building> mapBuildings;
    private JTextField txtShield;
    
    public PanelPlanetDetails(DialogPlanet dialogPlanet, Planet planet) {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent arg0) {
                checkPreconditionSendToSteemButton();
            }
        });
        
        this.dialogPlanet = dialogPlanet;
        this.planet = planet;
        
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{0, 0};
        gridBagLayout.rowHeights = new int[]{0, 0, 0};
        gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);
        
        JPanel panelImageText = new JPanel();
        panelImageText.setOpaque(false);
        GridBagConstraints gbc_panelImageText = new GridBagConstraints();
        gbc_panelImageText.insets = new Insets(0, 0, 5, 0);
        gbc_panelImageText.fill = GridBagConstraints.BOTH;
        gbc_panelImageText.gridx = 0;
        gbc_panelImageText.gridy = 0;
        add(panelImageText, gbc_panelImageText);
        GridBagLayout gbl_panelImageText = new GridBagLayout();
        gbl_panelImageText.columnWidths = new int[] {0, 400};
        gbl_panelImageText.rowHeights = new int[] {0, -18, 0, 30, 0, 0, 0};
        gbl_panelImageText.columnWeights = new double[]{1.0, 0.0};
        gbl_panelImageText.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
        panelImageText.setLayout(gbl_panelImageText);
        
        Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
        GridBagConstraints gbc_rigidArea = new GridBagConstraints();
        gbc_rigidArea.insets = new Insets(0, 0, 5, 5);
        gbc_rigidArea.gridx = 0;
        gbc_rigidArea.gridy = 0;
        panelImageText.add(rigidArea, gbc_rigidArea);
        
        Component horizontalStrut = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_horizontalStrut = new GridBagConstraints();
        gbc_horizontalStrut.insets = new Insets(0, 0, 5, 0);
        gbc_horizontalStrut.gridx = 1;
        gbc_horizontalStrut.gridy = 1;
        panelImageText.add(horizontalStrut, gbc_horizontalStrut);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
        gbc_scrollPane.gridheight = 6;
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 1;
        gbc_scrollPane.gridy = 0;
        panelImageText.add(scrollPane, gbc_scrollPane);
        
        JTextArea textArea = new JTextArea();
        scrollPane.setViewportView(textArea);
        textArea.setForeground(Color.WHITE);
        textArea.setOpaque(false);
        
        JLabel lblPlanetName = new JLabel(planet.getName());
        lblPlanetName.setForeground(Color.WHITE);
        lblPlanetName.setFont(new Font("Comic Sans MS", Font.PLAIN, 36));
        GridBagConstraints gbc_lblPlanetName = new GridBagConstraints();
        gbc_lblPlanetName.insets = new Insets(0, 0, 5, 5);
        gbc_lblPlanetName.gridx = 0;
        gbc_lblPlanetName.gridy = 2;
        panelImageText.add(lblPlanetName, gbc_lblPlanetName);
        
        JLabel txtPlanetid = new JLabel(planet.getId());
        txtPlanetid.setHorizontalAlignment(SwingConstants.CENTER);
        txtPlanetid.setForeground(Color.WHITE);
        txtPlanetid.setFont(new Font("Comic Sans MS", Font.PLAIN, 26));
        txtPlanetid.setBorder(null);
        txtPlanetid.setOpaque(false);
        GridBagConstraints gbc_txtPlanetid = new GridBagConstraints();
        gbc_txtPlanetid.insets = new Insets(0, 0, 5, 5);
        gbc_txtPlanetid.gridx = 0;
        gbc_txtPlanetid.gridy = 3;
        panelImageText.add(txtPlanetid, gbc_txtPlanetid);
        
        JLabel lblPlanettype = new JLabel("dsdf");
        lblPlanettype.setForeground(Color.WHITE);
        lblPlanettype.setFont(new Font("Comic Sans MS", Font.PLAIN, 36));
        GridBagConstraints gbc_lblPlanettype = new GridBagConstraints();
        gbc_lblPlanettype.insets = new Insets(0, 0, 5, 5);
        gbc_lblPlanettype.gridx = 0;
        gbc_lblPlanettype.gridy = 4;
        panelImageText.add(lblPlanettype, gbc_lblPlanettype);
        
        txtShield = new JTextField();
        txtShield.setBorder(null);
        txtShield.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        txtShield.setEditable(false);
        txtShield.setOpaque(false);
        txtShield.setForeground(Color.WHITE);
        txtShield.setHorizontalAlignment(SwingConstants.CENTER);
        txtShield.setText("Shield");
        GridBagConstraints gbc_txtShield = new GridBagConstraints();
        gbc_txtShield.insets = new Insets(0, 0, 5, 5);
        gbc_txtShield.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtShield.gridx = 0;
        gbc_txtShield.gridy = 5;
        panelImageText.add(txtShield, gbc_txtShield);
        txtShield.setColumns(10);
        
        panelImage = new JLabel();
        panelImage.setBackground(Color.BLUE);
        panelImage.setOpaque(false);
        GridBagConstraints gbc_panelImage = new GridBagConstraints();
        gbc_panelImage.insets = new Insets(0, 0, 5, 0);
        gbc_panelImage.fill = GridBagConstraints.BOTH;
        gbc_panelImage.gridx = 0;
        gbc_panelImage.gridy = 0;
        add(panelImage, gbc_panelImage);
        
        JPanel panelContent = new JPanel();
        GridBagConstraints gbc_panelContent = new GridBagConstraints();
        gbc_panelContent.insets = new Insets(0, 5, 0, 0);
        gbc_panelContent.fill = GridBagConstraints.BOTH;
        gbc_panelContent.gridx = 0;
        gbc_panelContent.gridy = 1;
        add(panelContent, gbc_panelContent);
        GridBagLayout gbl_panelContent = new GridBagLayout();
        gbl_panelContent.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
        gbl_panelContent.rowHeights = new int[]{0, 0, 0};
        gbl_panelContent.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_panelContent.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        panelContent.setLayout(gbl_panelContent);
        
        txtRenameplanet = new JTextField(planet.getName());
        GridBagConstraints gbc_txtRenameplanet = new GridBagConstraints();
        gbc_txtRenameplanet.insets = new Insets(0, 0, 5, 5);
        gbc_txtRenameplanet.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtRenameplanet.gridx = 0;
        gbc_txtRenameplanet.gridy = 0;
        panelContent.add(txtRenameplanet, gbc_txtRenameplanet);
        txtRenameplanet.setColumns(20);
        ((AbstractDocument)txtRenameplanet.getDocument()).setDocumentFilter(new LimitDocumentFilter(20));
        
        btnRenameplanet = new JButton("Rename planet");
        btnRenameplanet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                dialogPlanet.setStatusInfo("Sending transaction to Steem. Please wait...");
                
                new SwingWorker<Void, Void>(){

                    @Override
                    protected Void doInBackground() throws Exception {
                        CustomJson.renamePlanet(planet.getUserName(), planet.getId(), txtRenameplanet.getText());
                        Planets.getAllPlanets().get(planet.getId()).setPlanetName(txtRenameplanet.getText());
                        
                        return null;
                    }

                    @Override
                    protected void done() {
                        try {
                            get();
                            
                            Planet target = NextValiumGui.getNextValiumGui().getPlanetMarkedAsTarget();
                            if (target != null && target.getId() == planet.getId())
                                NextValiumGui.getNextValiumGui().setPlanetMarkedAsTarget(planet);
                            
                            dialogPlanet.getPanelFleet().setTarget();
                            
                            dialogPlanet.setStatusOk("Transaction sent to Steem. Check later for NextColony accepting the transaction.");
                            
                        } catch (InterruptedException | ExecutionException e) {
                            dialogPlanet.setStatusError(e.getClass().getSimpleName() + ": " + e.getMessage());
                        }
                        
                        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        super.done();
                    }
                }.execute();
            }
        });
        GridBagConstraints gbc_btnRenameplanet = new GridBagConstraints();
        gbc_btnRenameplanet.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnRenameplanet.insets = new Insets(0, 0, 5, 5);
        gbc_btnRenameplanet.gridx = 1;
        gbc_btnRenameplanet.gridy = 0;
        panelContent.add(btnRenameplanet, gbc_btnRenameplanet);
        
        JButton btnChargeShield = new JButton("Charge shield");
        btnChargeShield.setEnabled(false);
        btnChargeShield.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                dialogPlanet.setStatusInfo("Sending transaction to Steem. Please wait...");
                
                new SwingWorker<Void, Void>(){

                    @Override
                    protected Void doInBackground() throws Exception {
                        CustomJson.chargeShield(planet.getUserName(), planet.getId());
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
        });
        GridBagConstraints gbc_btnChargeShield = new GridBagConstraints();
        gbc_btnChargeShield.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnChargeShield.insets = new Insets(0, 0, 5, 5);
        gbc_btnChargeShield.gridx = 2;
        gbc_btnChargeShield.gridy = 0;
        panelContent.add(btnChargeShield, gbc_btnChargeShield);
        
        Component horizontalStrut_1 = Box.createHorizontalStrut(20);
        GridBagConstraints gbc_horizontalStrut_1 = new GridBagConstraints();
        gbc_horizontalStrut_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_horizontalStrut_1.insets = new Insets(0, 0, 5, 5);
        gbc_horizontalStrut_1.gridx = 3;
        gbc_horizontalStrut_1.gridy = 0;
        panelContent.add(horizontalStrut_1, gbc_horizontalStrut_1);
        
        btnBurnPlanet = new JButton("Burn Planet");
        btnBurnPlanet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                if (JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(null, "Burn planet " + planet.getName() + " ?", "Danger zone!", JOptionPane.YES_NO_OPTION))
                    return;
                
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                dialogPlanet.setStatusInfo("Sending transaction to Steem. Please wait...");
                
                new SwingWorker<Void, Void>(){

                    @Override
                    protected Void doInBackground() throws Exception {
                        CustomJson.burnPlanet(planet.getUserName(), planet.getId());
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
        });
        GridBagConstraints gbc_btnBurnPlanet = new GridBagConstraints();
        gbc_btnBurnPlanet.insets = new Insets(0, 0, 5, 0);
        gbc_btnBurnPlanet.gridx = 4;
        gbc_btnBurnPlanet.gridy = 0;
        panelContent.add(btnBurnPlanet, gbc_btnBurnPlanet);
        
        txtGiftPlanet = new JTextField();
        txtGiftPlanet.setEnabled(false);
        txtGiftPlanet.setText("Gift planet");
        GridBagConstraints gbc_txtGiftPlanet = new GridBagConstraints();
        gbc_txtGiftPlanet.insets = new Insets(0, 0, 0, 5);
        gbc_txtGiftPlanet.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtGiftPlanet.gridx = 0;
        gbc_txtGiftPlanet.gridy = 1;
        panelContent.add(txtGiftPlanet, gbc_txtGiftPlanet);
        txtGiftPlanet.setColumns(10);
        
        JButton btnGiftPlanet = new JButton("Gift planet");
        btnGiftPlanet.setEnabled(false);
        GridBagConstraints gbc_btnGiftPlanet = new GridBagConstraints();
        gbc_btnGiftPlanet.insets = new Insets(0, 0, 0, 5);
        gbc_btnGiftPlanet.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnGiftPlanet.gridx = 1;
        gbc_btnGiftPlanet.gridy = 1;
        panelContent.add(btnGiftPlanet, gbc_btnGiftPlanet);
        
        JButton btnActivateShield = new JButton("Activate shield");
        btnActivateShield.setEnabled(false);
        btnActivateShield.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                dialogPlanet.setStatusInfo("Sending transaction to Steem. Please wait...");
                
                new SwingWorker<Void, Void>(){

                    @Override
                    protected Void doInBackground() throws Exception {
                        CustomJson.activateShield(planet.getUserName(), planet.getId());
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
        });
        GridBagConstraints gbc_btnActivateShield = new GridBagConstraints();
        gbc_btnActivateShield.insets = new Insets(0, 0, 0, 5);
        gbc_btnActivateShield.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnActivateShield.gridx = 2;
        gbc_btnActivateShield.gridy = 1;
        panelContent.add(btnActivateShield, gbc_btnActivateShield);
    
        checkPreconditionSendToSteemButton();
        
        new SwingWorker<PlanetDetails, Object>() {

            @Override
            protected PlanetDetails doInBackground() throws Exception {
                PlanetDetails planetDetails = new PlanetDetails(planet.getId());
                
                Buildings buildings = new Buildings(planet);
                mapBuildings = buildings.getBuildings();
                
                try {
                    String path = "https://nextcolony.io/img/planets/" + planetDetails.getImg();
                    URL url = new URL(path);
                    BufferedImage image = ImageIO.read(url);
                    Image newimg = image.getScaledInstance( panelImage.getWidth(), panelImage.getWidth(), java.awt.Image.SCALE_SMOOTH ) ;  
                    panelImage = new JLabel(new ImageIcon(newimg));
                }
                catch (Exception e) {
                }
                return planetDetails;
            }

            @Override
            protected void done() {
                
                try {
                    PlanetDetails planetDetails = get();
                    lblPlanettype.setText(planetDetails.getPlanet_rarity() + " " + planetDetails.getPlanet_type());
                    
                    String[] as = planetDetails.getJsonString().split("[ ,]");
                    
                    for (int i = 0; i < as.length; i++) {
                        String s = as[i];
                        if (s.length() != 10 || !s.matches("[0-9]+")) 
                            continue;
                        
                        long timemillis = Long.parseLong(s)*1000;
                        Date date = new Date(timemillis);
                        as[i] = Util.getDateAsString(date);
                    }
                    
                    String s = String.join("", as);
                    s = s.replaceAll("\\{\\n", "");
                    s = s.replaceAll("\\}", "");
                    s = s.replaceAll("\":", ": ");
                    s = s.replaceAll("\"", "");
                    
                    as = s.split("\\n");
                    Arrays.parallelSort(as);
                    s = String.join("\n", as);
                    
                    textArea.setText(s);
                    textArea.setCaretPosition(0);
                    panelImage.setBackground(Color.BLACK);
                    panelImage.setOpaque(true);
                    add(panelImage, gbc_panelImage);
                    
                    Building shield = mapBuildings.get("shieldgenerator");
                    Building bunker = mapBuildings.get("bunker");
                    
                    txtShield.setText("Shield Level: " + shield.getCurrent());
                    
                    if (SteemUtil.isAccountRegistered(planet.getUserName())) {
                        btnChargeShield.setEnabled(planetDetails.getShieldcharged() == 0);
                        btnActivateShield.setEnabled(planetDetails.getShieldcharged() == 1);
                    }
                    
                } catch (InterruptedException | ExecutionException e) {
                }
                super.done();
            }
        }.execute();
    }
    private void checkPreconditionSendToSteemButton() {
        String but = "";
        
        if (!SteemUtil.isAccountRegistered(planet.getUserName())) {
            dialogPlanet.setStatusError(but + "Private posting key of user " + planet.getUserName() + " not in nextvaliumgui.ini -> Buttons disabled");
            btnRenameplanet.setEnabled(false);
            txtRenameplanet.setEnabled(false);
            btnBurnPlanet.setEnabled(false);
            return;
        }
        
        dialogPlanet.setStatusOk("Ok");
        btnRenameplanet.setEnabled(true);
        btnBurnPlanet.setEnabled(true);
    }
 }
