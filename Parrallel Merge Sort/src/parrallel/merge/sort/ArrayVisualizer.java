/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parrallel.merge.sort;

/**
 *
 * @author Basil
 */
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class ArrayVisualizer extends JPanel {
    
    private int[] array;
    
    public void setArray(int[] array) {
        this.array = array;
        repaint(); // Triggers paintComponent immediately
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (array == null || array.length == 0) return;

        int width = getWidth();
        int height = getHeight();

        int barWidth = Math.max(1, width / array.length); 


        int maxVal = Integer.MIN_VALUE;
        for (int val : array) {
            maxVal = Math.max(maxVal, val);
        }

        if (maxVal == 0) maxVal = 1;

        for (int i = 0; i < array.length; i++) {
            int val = array[i];


            int barHeight = (int) (((double)val / maxVal) * height);


            float hue = (float) val / maxVal * 0.7f;

            g.setColor(Color.getHSBColor(hue, 0.9f, 0.9f));

            g.fillRect(i * barWidth, height - barHeight, barWidth, barHeight);
        }
    }
}    