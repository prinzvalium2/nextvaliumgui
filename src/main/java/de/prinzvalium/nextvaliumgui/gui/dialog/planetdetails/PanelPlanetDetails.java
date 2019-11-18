package de.prinzvalium.nextvaliumgui.gui.dialog.planetdetails;

import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import de.prinzvalium.nextvaliumgui.lib.CustomJson;
import de.prinzvalium.nextvaliumgui.lib.SteemUtil;
import de.prinzvalium.nextvaliumgui.nextcolony.Planet;
import de.prinzvalium.nextvaliumgui.nextcolony.PlanetDetails;

import java.awt.GridBagLayout;
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

public class PanelPlanetDetails extends JPanel {

    private static final long serialVersionUID = 1L;
    private DialogPlanet dialogPlanet;
    private JLabel panelImage;
    private JTextField txtRenameplanet;
    private JTextField txtGiftPlanet;
    private JTextField txtPlanetid;
    private Planet planet;
    private JButton btnRenameplanet;
    
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
        gbl_panelImageText.columnWidths = new int[]{0, 0, 0};
        gbl_panelImageText.rowHeights = new int[]{0, 0, 0, 0, 0};
        gbl_panelImageText.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gbl_panelImageText.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        panelImageText.setLayout(gbl_panelImageText);
        
        JLabel lblPlanetName = new JLabel(planet.getName());
        lblPlanetName.setForeground(Color.WHITE);
        lblPlanetName.setFont(new Font("Comic Sans MS", Font.PLAIN, 46));
        GridBagConstraints gbc_lblPlanetName = new GridBagConstraints();
        gbc_lblPlanetName.insets = new Insets(0, 0, 5, 0);
        gbc_lblPlanetName.gridx = 1;
        gbc_lblPlanetName.gridy = 1;
        panelImageText.add(lblPlanetName, gbc_lblPlanetName);
        
        txtPlanetid = new JTextField(planet.getId());
        txtPlanetid.setHorizontalAlignment(SwingConstants.CENTER);
        txtPlanetid.setForeground(Color.WHITE);
        txtPlanetid.setFont(new Font("Comic Sans MS", Font.PLAIN, 46));
        txtPlanetid.setBorder(null);
        txtPlanetid.setEditable(false);
        txtPlanetid.setOpaque(false);
        GridBagConstraints gbc_txtPlanetid = new GridBagConstraints();
        gbc_txtPlanetid.insets = new Insets(0, 0, 5, 0);
        gbc_txtPlanetid.gridx = 1;
        gbc_txtPlanetid.gridy = 2;
        panelImageText.add(txtPlanetid, gbc_txtPlanetid);
        
        JLabel lblPlanettype = new JLabel("");
        lblPlanettype.setForeground(new Color(255, 255, 224));
        lblPlanettype.setFont(new Font("Comic Sans MS", Font.PLAIN, 36));
        GridBagConstraints gbc_lblPlanettype = new GridBagConstraints();
        gbc_lblPlanettype.gridx = 1;
        gbc_lblPlanettype.gridy = 3;
        panelImageText.add(lblPlanettype, gbc_lblPlanettype);
        
        panelImage = new JLabel();
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
        gbl_panelContent.columnWidths = new int[]{0, 0, 0, 0};
        gbl_panelContent.rowHeights = new int[]{0, 0, 0};
        gbl_panelContent.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_panelContent.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        panelContent.setLayout(gbl_panelContent);
        
        JLabel lblRenamePlanet = new JLabel("Rename planet:");
        GridBagConstraints gbc_lblRenamePlanet = new GridBagConstraints();
        gbc_lblRenamePlanet.insets = new Insets(0, 0, 5, 5);
        gbc_lblRenamePlanet.anchor = GridBagConstraints.EAST;
        gbc_lblRenamePlanet.gridx = 0;
        gbc_lblRenamePlanet.gridy = 0;
        panelContent.add(lblRenamePlanet, gbc_lblRenamePlanet);
        
        txtRenameplanet = new JTextField(planet.getName());
        GridBagConstraints gbc_txtRenameplanet = new GridBagConstraints();
        gbc_txtRenameplanet.insets = new Insets(0, 0, 5, 5);
        gbc_txtRenameplanet.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtRenameplanet.gridx = 1;
        gbc_txtRenameplanet.gridy = 0;
        panelContent.add(txtRenameplanet, gbc_txtRenameplanet);
        txtRenameplanet.setColumns(10);
        
        btnRenameplanet = new JButton("Rename planet");
        btnRenameplanet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                dialogPlanet.setStatusInfo("Sending transaction to Steem. Please wait...");
                
                new SwingWorker<Void, Void>(){

                    @Override
                    protected Void doInBackground() throws Exception {
                        CustomJson.renamePlanet(planet.getUserName(), planet.getId(), txtRenameplanet.getText());
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
        GridBagConstraints gbc_btnRenameplanet = new GridBagConstraints();
        gbc_btnRenameplanet.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnRenameplanet.insets = new Insets(0, 0, 5, 0);
        gbc_btnRenameplanet.gridx = 2;
        gbc_btnRenameplanet.gridy = 0;
        panelContent.add(btnRenameplanet, gbc_btnRenameplanet);
        
        JLabel lblGiftPlanet = new JLabel("Gift planet:");
        GridBagConstraints gbc_lblGiftPlanet = new GridBagConstraints();
        gbc_lblGiftPlanet.anchor = GridBagConstraints.EAST;
        gbc_lblGiftPlanet.insets = new Insets(0, 0, 0, 5);
        gbc_lblGiftPlanet.gridx = 0;
        gbc_lblGiftPlanet.gridy = 1;
        panelContent.add(lblGiftPlanet, gbc_lblGiftPlanet);
        
        txtGiftPlanet = new JTextField();
        txtGiftPlanet.setEnabled(false);
        txtGiftPlanet.setText("Gift planet");
        GridBagConstraints gbc_txtGiftPlanet = new GridBagConstraints();
        gbc_txtGiftPlanet.insets = new Insets(0, 0, 0, 5);
        gbc_txtGiftPlanet.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtGiftPlanet.gridx = 1;
        gbc_txtGiftPlanet.gridy = 1;
        panelContent.add(txtGiftPlanet, gbc_txtGiftPlanet);
        txtGiftPlanet.setColumns(10);
        
        JButton btnGiftPlanet = new JButton("Gift planet");
        btnGiftPlanet.setEnabled(false);
        GridBagConstraints gbc_btnGiftPlanet = new GridBagConstraints();
        gbc_btnGiftPlanet.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnGiftPlanet.gridx = 2;
        gbc_btnGiftPlanet.gridy = 1;
        panelContent.add(btnGiftPlanet, gbc_btnGiftPlanet);
    
        checkPreconditionSendToSteemButton();
        
        new SwingWorker<PlanetDetails, Object>() {

            @Override
            protected PlanetDetails doInBackground() throws Exception {
                PlanetDetails planetDetails = new PlanetDetails(planet.getId());
                String path = "https://nextcolony.io/img/planets/" + planetDetails.getImg();
                URL url = new URL(path);
                BufferedImage image = ImageIO.read(url);
                panelImage = new JLabel(new ImageIcon(image));
                return planetDetails;
            }

            @Override
            protected void done() {
                
                try {
                    PlanetDetails planetDetails = get();
                    lblPlanettype.setText(planetDetails.getPlanet_rarity() + " " + planetDetails.getPlanet_type());
                    panelImage.setBackground(Color.BLUE);
                    panelImage.setOpaque(true);
                    add(panelImage, gbc_panelImage);
                } catch (InterruptedException | ExecutionException e) {
                }
                super.done();
            }
            
        }.execute();
    }
    private void checkPreconditionSendToSteemButton() {
        String but = "";
        
        if (!SteemUtil.isAccountRegistered(planet.getUserName())) {
            dialogPlanet.setStatusError(but + "Private posting key of user " + planet.getUserName() + " not in nextvaliumgui.ini -> Button <send transaction to Steem> disabled");
            btnRenameplanet.setEnabled(false);
            return;
        }
        
        dialogPlanet.setStatusOk("Ok");
        btnRenameplanet.setEnabled(true);
    }
 }
