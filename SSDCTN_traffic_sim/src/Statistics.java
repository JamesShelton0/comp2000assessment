import java.lang.Math;
import java.util.stream.*;
import java.awt.*;
import java.awt.geom.*;

public class Statistics {
    int width, height; // App dimensions
    int statWindowWidth, statWindowHeight;
    boolean keyToggled;

    double avgVehicleVelocity;
    long totalVehicleCount, curVehicleCount, explosionCount;

    Statistics() {
        keyToggled = false;
    }

    public void reset() {
        totalVehicleCount = 0;
        explosionCount = 0;
    }

    // ------------ Average Speed ------------
    public void calculateAvgVelocity(Stream<Vehicle> vehicleStream) {
        avgVehicleVelocity = vehicleStream.parallel()
                                          .mapToDouble(v -> v.getVelocity())
                                          .average()
                                          .orElse(0.0);
    }

    public double getAvgVelocity() {
        return avgVehicleVelocity;
    }

    // ------------ Active Vehicle Count ------------
    public void updateCurVehicleCount(Stream<?> stream) {
        curVehicleCount = stream.parallel()
                                .count();
    }

    public long getCurVehicleCount() {
        return curVehicleCount;
    }

    // ------------ Total Vehicle Count ------------
    public void inputTotalCount(long value) {
        this.totalVehicleCount = value;
    }

    public long getTotalVehicleCount() {
        return totalVehicleCount;
    }

    // ------------ Explosion Count ------------
    public void inputExplosionCount(long value) {
        this.explosionCount = value;
    }

    public long getExplosionCount() {
        return explosionCount;
    }


    // ------------ Drawing Related ------------
    public void keyPressed() {
        keyToggled = !keyToggled;
    }

    public void updateViewportBounds(double width, double height) {
        this.width = (int) width;
        this.height = (int) height;
        statWindowWidth = (int) width/3;
        statWindowHeight = (int) height/4;
    }

    public void draw(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // scales dimensions with frame size but not beyond specified minimum 
        int cardWidth = Math.min(550, Math.max(230, statWindowWidth - 40));
        int cardHeight = 130;
        int cardX = -8;
        int cardY = -8;

        int titleRow = 30;

        if (!keyToggled) {
            // draw help/guide string 
            g2d.setColor(new Color(200, 200, 200));
            g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 14f));
            g2d.drawString("Press 's' to toggle traffic statistics", cardX + 20, cardY + titleRow);
        } else {
            int rowOffset = 20;
            int row1 = 55;
            // draw stat card
            RoundRectangle2D.Double statCard = new RoundRectangle2D.Double(cardX, cardY, cardWidth, cardHeight, 22, 22);
            g2d.setColor(new Color(24, 29, 36, 135));
            g2d.fill(statCard);
            
            RoundRectangle2D.Double statCardOutline = new RoundRectangle2D.Double(cardX, cardY, cardWidth, cardHeight, 22, 22);
            g2d.setColor(new Color(255, 255, 255, 210));
            g2d.setStroke(new BasicStroke(2f));
            g2d.draw(statCardOutline);

            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 17f));
            g2d.drawString("STATISTICS", cardX + (int)(cardWidth*0.25)+8, cardY + titleRow);
            g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 13f));
            g2d.setColor(new Color(220, 225, 230));
            g2d.drawString("Average vehicle speed: " + String.format("%.2f", getAvgVelocity()), cardX + 20, cardY + row1);
            g2d.drawString("Active vehicle count: " + getCurVehicleCount(), cardX + 32, cardY + row1+rowOffset);
            g2d.drawString("Total vehicles seen: " + getTotalVehicleCount(), cardX + 39, cardY + row1+rowOffset*2);
            g2d.drawString("Number of explosions: " + getExplosionCount(), cardX + 19, cardY + row1+rowOffset*3);
            
            g2d.setColor(new Color(200, 200, 200));
            g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 14f));
            g2d.drawString("Press 'r' to reset stats", cardX + (int)(cardWidth*0.16), cardY + 150);

            g2d.dispose();
        }
    }
}