package de.prinzvalium.nextvaliumgui.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.util.Collection;
import java.util.Date;

import javax.swing.JPanel;

import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.MultiValuedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.prinzvalium.nextvaliumgui.NextValiumGui;
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
    private Stroke strokeExploreReturn = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10,2}, 0);
    private Stroke strokeExploreCanceled = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10,2}, 0);
    
    
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
        
        boolean showExplore = NextValiumGui.getNextValiumGui().isRadarExplorationsEnabled();
        boolean showOthers = NextValiumGui.getNextValiumGui().isRadarOthersEnabled();
        
        Graphics2D g2 = (Graphics2D) g;
        
        MapIterator<GalaxyMapKey, GalaxyMapValue> mapIterator = galaxyMap.mapIterator();
        
        Date dateCurrent = new Date();
        
        while (mapIterator.hasNext()) {
            
            Collection<GalaxyMapValue> galaxyMapValues = galaxyMap.get(mapIterator.next());
            
            galaxyMapValues.forEach(galaxyMapValue -> {
            
                if (galaxyMapValue.getGalaxyMapValueExplore() == null)
                    return;
                        
                GalaxyMapValueExplore val = galaxyMapValue.getGalaxyMapValueExplore();
                
                if (val.type.equalsIgnoreCase("explore") && !showExplore)
                    return;
                
                if (!val.type.equalsIgnoreCase("explore") && !showOthers)
                    return;
                
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
                g.setColor(color);
                g2.draw(new Line2D.Float(xs, ys, xe, ye));
            });
        }
    }
}
