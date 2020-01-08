package de.prinzvalium.nextvaliumgui.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import org.json.JSONArray;

import de.prinzvalium.nextvaliumgui.NextValiumGui;
import de.prinzvalium.nextvaliumgui.lib.Util;
import de.prinzvalium.nextvaliumgui.nextcolony.GalaxyPlanet;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;

public class PanelUniverse extends JPanel {
    
    private static final long serialVersionUID = 1L;
    private HashMap<String, GalaxyPlanet> mapPlanets = null;
    private Integer[] ai = null;
    private Double zoomX;
    private Double zoomY;
    private Double offsetX;
    private Double offsetY;
    private Double centerOffsetX;
    private Double centerOffsetY;
    private NextValiumGui nextValiumGui;
    
    public PanelUniverse() {
        
        nextValiumGui = NextValiumGui.getNextValiumGui();
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent arg0) {
                double mouseX = arg0.getX();
                double mouseY = arg0.getY();
                
                Rectangle rectPanel = getBounds();
                rectPanel.width -= 20;
                rectPanel.height -= 20;
                
                double height = rectPanel.getHeight();
                
                double planetX = mouseX/zoomX - 10.0/zoomX - centerOffsetX/zoomX - offsetX;
                double planetY = 10.0/zoomY + centerOffsetY/zoomY + height/zoomY - mouseY/zoomY - offsetY;
                
                nextValiumGui.setCenterPosition((int)planetX, (int)planetY, false);
                repaint();
                
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Util.sleep(100);
                        EventQueue.invokeLater(new Runnable() {
                           @Override
                            public void run() {
                                nextValiumGui.setCenterPosition((int)planetX, (int)planetY, true);                                
                            }});
                    }
                }).start();
            }
        });
        setBackground(Color.BLACK);
        setLayout(null);
        
        JLabel lblLoadingData = new JLabel("Loading data...");
        lblLoadingData.setForeground(Color.WHITE);
        lblLoadingData.setBounds(0, 0, 80, 14);
        add(lblLoadingData);
        
        new SwingWorker <Object, Object> () {

            @Override
            protected Object doInBackground() throws Exception {
                
                mapPlanets = new HashMap<String, GalaxyPlanet>();
                
                JSONArray jsonPlanets = Util.getJSONArrayFromApiCommand(Util.NEXTCOLONY_API_CMD_GALAXYPLANETS);
                
                for (int i = 0; i < jsonPlanets.length(); i++) {
                    GalaxyPlanet p = new GalaxyPlanet(jsonPlanets.getJSONObject(i));
                    mapPlanets.put(p.getId(), p);
                }
                
                ai = getUniverseBounds(mapPlanets);
                
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    
                } catch (InterruptedException | ExecutionException e) {
                }
                lblLoadingData.setText("");
                repaint();
                
                super.done();
                nextValiumGui.getFrmNextvaliumManagementGui().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

            }
        }.execute();
    }
    
    private Integer[] getUniverseBounds(HashMap<String, GalaxyPlanet> mapPlanets) {
        
        Integer[] ai = new Integer[] {0, 0, 0, 0};
       
        mapPlanets.forEach((id, planet) -> {
            if (planet.getX() < ai[0])
                ai[0] = planet.getX();
            if (planet.getY() < ai[1])
                ai[1] = planet.getY();
            if (planet.getX() > ai[2])
                ai[2] = planet.getX();
            if (planet.getY() > ai[3])
                ai[3] = planet.getY();
        });
        
        return ai;        
    }

    public static void main(String[] args) {
        JDialog dialog = new JDialog();
        dialog.getContentPane().add(new PanelUniverse());
        dialog.setBounds(100,  100,  600,  400);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (ai == null)
            return;
        
        String selectedUser = nextValiumGui.getSelectedUniverseUser();
        
        Graphics2D g2 = (Graphics2D) g;
        
        Rectangle rectPanel = getBounds();
        rectPanel.width -= 20;
        rectPanel.height -= 20;
        
        Rectangle rectUniverse = new Rectangle(ai[0], ai[1], ai[2]-ai[0], ai[3]-ai[1]);
        zoomX = rectPanel.getWidth() / rectUniverse.getWidth();
        zoomY = rectPanel.getHeight() / rectUniverse.getHeight();
        offsetX = rectPanel.getMinX() - rectUniverse.getMinX();
        offsetY = rectPanel.getMinY() - rectUniverse.getMinY();
        
        zoomX = Math.min(zoomX, zoomY);
        zoomY = zoomX;
        
        centerOffsetX = (rectPanel.getWidth() - rectUniverse.getWidth() * zoomX) / 2;
        centerOffsetY = (rectPanel.getHeight() - rectUniverse.getHeight() * zoomY) / 2;
        
        mapPlanets.forEach((id, planet) -> {
            
            double x = 10 + centerOffsetX +(offsetX + planet.getX()) * zoomX;
            double y = (offsetY + planet.getY()) * zoomY;
            y = 10 - centerOffsetY + rectPanel.getHeight() - y;
            
            if (selectedUser.isEmpty())
                g2.setColor(Util.getUserColor(planet.getUser()));
            else if (planet.getUser().equalsIgnoreCase(selectedUser))
                g2.setColor(Util.getUserColor(planet.getUser()).brighter());
            else
                g2.setColor(Color.DARK_GRAY);
                
            g2.fillOval((int)x, (int)y, 2, 2);
        });
        
        int posX = NextValiumGui.getNextValiumGui().getTextFieldPosX();
        int posY = NextValiumGui.getNextValiumGui().getTextFieldPosY();
        
        if (posX == -1 || posY == -1)
            return;
        
        double x = 10 + centerOffsetX +(offsetX + posX) * zoomX;
        double y = (offsetY + posY) * zoomY;
        y = 10 - centerOffsetY + rectPanel.getHeight() - y;
        
        double width = 200 * zoomX;
        double height = 125 * zoomY;
        
        g2.setColor(Color.BLACK);
        g2.drawRect((int)(x-width/2+2), (int)(y-height/2+2), (int)width-4, (int)height-4);
        g2.setColor(Color.BLACK);
        g2.drawRect((int)(x-width/2+1), (int)(y-height/2+1), (int)width-2, (int)height-2);
        g2.setColor(Color.WHITE);
        g2.drawRect((int)(x-width/2), (int)(y-height/2), (int)width, (int)height);
        g2.setColor(Color.BLACK);
        g2.drawRect((int)(x-width/2-1), (int)(y-height/2-1), (int)width+2, (int)height+2);
        g2.setColor(Color.BLACK);
        g2.drawRect((int)(x-width/2-2), (int)(y-height/2-2), (int)width+4, (int)height+4);
    }
}
