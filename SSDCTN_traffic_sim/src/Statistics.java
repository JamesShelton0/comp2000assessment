import java.lang.Math;
import java.util.stream.*;
import java.awt.*;
import java.awt.geom.*;

public class Statistics {
    int statWindowWidth, statWindowHeight;
    boolean keyToggled;

    double avgVehicleVelocity;
    long totalVehicleCount, curVehicleCount;

    Statistics() {
        keyToggled = false;
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
        curVehicleCount = stream.count();
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


    // ------------ Drawing Related ------------
    public void keyPressed() {
        keyToggled = !keyToggled;
    }

    public void updateViewportBounds(double width, double height) {
        statWindowWidth = (int) width/3;
        statWindowHeight = (int) height/4;
    }

    public void draw(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // scales dimensions with frame size but not beyond specified minimum 
        int cardWidth = Math.min(550, Math.max(230, statWindowWidth - 40));
        int cardHeight = 116;
        int cardX = -8;
        int cardY = -8;


        if (!keyToggled) {
            // draw help/guide string 
            g2d.setColor(new Color(200, 200, 200));
            g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 14f));
            g2d.drawString("Press 's' to toggle traffic statistics", cardX + 20, cardY + 30);
        } else {
            // draw stat card
            RoundRectangle2D.Double statCard = new RoundRectangle2D.Double(cardX, cardY, cardWidth, cardHeight, 22, 22);
            g2d.setColor(new Color(24, 29, 36, 135));
            g2d.fill(statCard);
            
            RoundRectangle2D.Double statCardOutline = new RoundRectangle2D.Double(cardX, cardY, cardWidth, cardHeight, 22, 22);
            g2d.setColor(new Color(255, 255, 255, 210));
            g2d.setStroke(new BasicStroke(2f));
            g2d.draw(statCardOutline);

            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 17f));
            g2d.drawString("STATISTICS", cardX + (int)(cardWidth*0.25)+8, cardY + 30);
            g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 13f));
            g2d.setColor(new Color(220, 225, 230));
            g2d.drawString("Average vehicle speed: " + String.format("%.2f", getAvgVelocity()), cardX + 20, cardY + 55);
            g2d.drawString("Active vehicle count: " + getCurVehicleCount(), cardX + 33, cardY + 75);
            g2d.drawString("Total vehicles seen: " + getTotalVehicleCount(), cardX + 39, cardY + 95);
            g2d.dispose();
        }
    }
}