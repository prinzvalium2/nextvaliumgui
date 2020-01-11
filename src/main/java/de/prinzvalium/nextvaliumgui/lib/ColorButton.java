package de.prinzvalium.nextvaliumgui.lib;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;

public class ColorButton extends JButton {
    
    private static final long serialVersionUID = 1L;
    JLabel label = new JLabel();

    public ColorButton(String text) {
        //super(text);

        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setHorizontalAlignment(CENTER);
        label.setText(text);
        label.setOpaque(true);

        setLayout(new BorderLayout());
        add(label, BorderLayout.CENTER);
    }

    @Override
    public void setBackground(Color bg) {
        if (label == null)
            return;
//        if(!UIManager.getSystemLookAndFeelClassName().endsWith("WindowsLookAndFeel")) {
//            super.setBackground(bg);
//        }
        label.setBackground(bg);
    }

    @Override
    public void setForeground(Color fg) {
        if (label == null)
            return;
        //super.setForeground(fg);
        label.setForeground(fg);
    }

//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//
//        Rectangle rectangle = getBounds();
//        rectangle.x = 3;
//        rectangle.y = 3;
//        rectangle.width -= rectangle.x * 2;
//        rectangle.height -= rectangle.y * 2;
//
//        Graphics2D g2d = (Graphics2D)g;
//        g2d.setColor(bg);
//        g2d.fill(rectangle);
//    }

    @Override
    public void setText(String text) {
        if (label == null)
            return;
        //super.setText(text);
        label.setText(text);
    }

    @Override
    public void setIcon(Icon defaultIcon) {
        if (label == null)
            return;
        label.setIcon(defaultIcon);
        //super.setIcon(defaultIcon);
    }
}