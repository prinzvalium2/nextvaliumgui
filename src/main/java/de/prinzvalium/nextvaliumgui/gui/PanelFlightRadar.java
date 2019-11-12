package de.prinzvalium.nextvaliumgui.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.Date;

import javax.swing.JPanel;

import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.MultiValuedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.prinzvalium.nextvaliumgui.NextValiumGui;
import de.prinzvalium.nextvaliumgui.lib.Util;
import de.prinzvalium.nextvaliumgui.nextcolony.galaxymap.GalaxyMapKey;
import de.prinzvalium.nextvaliumgui.nextcolony.galaxymap.GalaxyMapValue;
import de.prinzvalium.nextvaliumgui.nextcolony.galaxymap.GalaxyMapValueExplore;

import java.awt.BasicStroke;
import java.awt.Color;

public class PanelFlightRadar extends JPanel {
    
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(PanelGalaxyMap.class);
    private MultiValuedMap<GalaxyMapKey, GalaxyMapValue> galaxyMap = null;
    private int locationX;
    private int locationY;
    private Color colorAttack = new Color(245, 66, 66, 150);
    private Color colorSupport = new Color(66, 245, 132, 255);
    private Color colorTransport = new Color(255, 255, 59, 255);
    private Color colorExplore = new Color(0, 0, 0, 80);
    private Color colorExploreSpace = new Color(0, 0, 0, 80);
    private Color colorSiege = new Color(219, 48, 240, 160); 
    private Color colorBreakSiege = new Color(219, 48, 211, 255); 
    private Color colorDeploy = new Color(78, 66, 245, 128);
    private Stroke strokeFlightOutward = new BasicStroke(3);
    private Stroke strokeFlightReturn = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10,5}, 0);
    private Stroke strokeExploreOutward = new BasicStroke(1);
    //private Stroke strokeExploreReturn = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10,2}, 0);
    private Stroke strokeExploreCanceled = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10,2}, 0);
    private int loopCounter;
    
    public PanelFlightRadar(MultiValuedMap<GalaxyMapKey, GalaxyMapValue> galaxyMap, int locationX, int locationY) {
        LOGGER.trace("PanelFlightRadar()");
        
        this.galaxyMap = galaxyMap;
        this.locationX = locationX;
        this.locationY = locationY;
        
        setLayout(null);
        setOpaque(false);
    }

    @Override 
    protected void paintComponent(Graphics g) {
        LOGGER.trace("PanelFlightRadar.paintComponent() - enter");
        
        boolean showExplore = NextValiumGui.getNextValiumGui().isRadarExplorationsEnabled();
        boolean showOthers = NextValiumGui.getNextValiumGui().isRadarOthersEnabled();
        
        Graphics2D g2 = (Graphics2D) g;
        
        MapIterator<GalaxyMapKey, GalaxyMapValue> mapIterator = galaxyMap.mapIterator();
        
        Date dateCurrent = new Date();
        
        removeAll();
        
        loopCounter = 0;
        
        while (mapIterator.hasNext()) {
            
            mapIterator.next();
            GalaxyMapValue galaxyMapValue = mapIterator.getValue();
            
            if (galaxyMapValue.getGalaxyMapValueExplore() == null)
                continue;
                    
            GalaxyMapValueExplore val = galaxyMapValue.getGalaxyMapValueExplore();
            
            if (val.type.equalsIgnoreCase("explore") && !showExplore)
                continue;
            
            if (!val.type.equalsIgnoreCase("explore") && !showOthers)
                continue;
            
            loopCounter++;
            //LOGGER.trace(val.user+"/"+val.type+": "+val.start_x+"/"+val.start_y+"->"+val.x+"/"+val.y);
            
            Stroke stroke = strokeFlightOutward;
            if (val.date.before(dateCurrent))
                stroke = strokeFlightReturn;
            
            Color color;
            
            switch (val.type) {
            case "attack":
                color = colorAttack;
                break;
            case "support":
                color = colorSupport;
                break;
            case "transport":
                color = colorTransport;
                break;
            case "explore":
                stroke = strokeExploreOutward;
                color = colorExplore;
                break;
            case "explorespace":
                stroke = strokeExploreCanceled;
                color = colorExploreSpace;
                break;
            case "siege":
                color = colorSiege;
                break;
            case "breaksiege":
                color = colorBreakSiege;
                break;
            case "deploy":
                color = colorDeploy;
                break;
            default:
                color = Color.CYAN;
            }
            
            int xs = (val.start_x - locationX) * 6 + (getWidth() / 2);
            int ys = (val.start_y - locationY) * -6 + (getHeight() / 2);
            int xe = (val.x - locationX) * 6 + (getWidth() / 2);
            int ye = (val.y - locationY) * -6 + (getHeight() / 2);
           
            g2.setStroke(stroke);
            g2.setColor(color);
            g2.draw(new Line2D.Double(xs, ys, xe, ye));
            
            int deltaX = xe - xs;
            int deltaY = ye - ys;
            double distance = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
            int speed = Util.getSlowestSpeedOfShips(val.mapShips);
            
            double posShipX;
            double posShipY;
            Path2D path = null;
            
            // Outward flight
            if (val.date != null && dateCurrent.before(val.date)) {
                double seconds = Math.abs(val.date.getTime() - dateCurrent.getTime());
                double hours = seconds / 3600000;
                double deltaDistance = speed * 6 * hours;
                posShipX = xe - deltaDistance / distance * deltaX;
                posShipY = ye - deltaDistance / distance * deltaY;
                if (Math.abs(xe - posShipX) > Math.abs(deltaX))
                    posShipX = xs;
                if (Math.abs(ye - posShipY) > Math.abs(deltaY))
                    posShipY = ys;
                if (shipWithinWindow(posShipX, posShipY))
                    path = drawArrowLine(g2, xs-deltaX, ys-deltaY, posShipX, posShipY, 8, 4);
            }
            
            //Return
            else if (val.date_return != null && dateCurrent.before(val.date_return)){
                double seconds = Math.abs(val.date_return.getTime() - dateCurrent.getTime());
                double hours = seconds / 3600000;
                double deltaDistance = speed * 6 * hours;
                posShipX = xs + deltaX * deltaDistance / distance;
                posShipY = ys + deltaY * deltaDistance / distance;
                if (Math.abs(posShipX - xs) > Math.abs(deltaX))
                    posShipX = xe;
                if (Math.abs(posShipY - ys) > Math.abs(deltaY))
                    posShipY = ye;
                if (shipWithinWindow(posShipX, posShipY))
                    path = drawArrowLine(g2, xe+deltaX, ye+deltaY, posShipX, posShipY, 8, 4);
            }
            
            if (path == null)
                continue;

            PanelArrow panelArrow = new PanelArrow(val);
            panelArrow.setBounds(path.getBounds());
            add(panelArrow);
        }
        LOGGER.trace("PanelFlightRadar.paintComponent() - leave - loopCounter:" + loopCounter);
    }
    
    private boolean shipWithinWindow(double x, double y) {
        if (x < 0 || y < 0)
            return false;
        
        if (x > getBounds().width || y > getBounds().height)
            return false;
        
        return true;
    }
    
    private Path2D drawArrowLine(Graphics2D g2, double x1, double y1, double x2, double y2, int d, int h) {
        double dx = x2 - x1, dy = y2 - y1;
        double D = Math.sqrt(dx*dx + dy*dy);
        double xm = D - d, xn = xm, ym = h, yn = -h, x;
        double sin = dy / D, cos = dx / D;

        x = xm*cos - ym*sin + x1;
        ym = xm*sin + ym*cos + y1;
        xm = x;

        x = xn*cos - yn*sin + x1;
        yn = xn*sin + yn*cos + y1;
        xn = x;
       
        Path2D path = new Path2D.Double();
        path.moveTo(x2, y2);
        path.lineTo(xm, ym);
        path.lineTo(xn, yn);
        path.closePath();
        
        g2.setColor(Color.BLACK);
        g2.setStroke(strokeExploreOutward);
        g2.draw(path);
        g2.fill(path);
        
        return path;
    }
}
