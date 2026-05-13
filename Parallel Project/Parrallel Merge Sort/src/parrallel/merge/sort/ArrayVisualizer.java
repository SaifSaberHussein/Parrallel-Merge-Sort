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
    
    // Call this to update the data and redraw the screen
    public void setArray(int[] array) {
        this.array = array;
        repaint(); // Triggers paintComponent immediately
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // If no array exists yet, do nothing
        if (array == null || array.length == 0) return;

        int width = getWidth();
        int height = getHeight();
        // Ensure bar width is at least 1 pixel
        int barWidth = Math.max(1, width / array.length); 

        // 1. Find the Maximum value in the array for scaling
        int maxVal = Integer.MIN_VALUE;
        for (int val : array) {
            maxVal = Math.max(maxVal, val);
        }
        // Prevent divide-by-zero if array is full of 0s
        if (maxVal == 0) maxVal = 1;

        for (int i = 0; i < array.length; i++) {
            int val = array[i];

            // 2. Calculate Height relative to the panel height
            int barHeight = (int) (((double)val / maxVal) * height);

            // 3. Calculate Color based on value (Rainbow effect)
            // Map value to a hue from 0.0 (Red) to 0.7 (Purple)
            float hue = (float) val / maxVal * 0.7f;
            // (Hue, Saturation, Brightness)
            g.setColor(Color.getHSBColor(hue, 0.9f, 0.9f));

            // 4. Draw the bar
            // Remember: y=0 is the top, so we draw from 'height - barHeight'
            g.fillRect(i * barWidth, height - barHeight, barWidth, barHeight);
        }
    }
}    