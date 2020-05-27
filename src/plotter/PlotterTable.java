/*
 * Course: SE2800-031
 * Spring 2020
 * Lab: GPS
 * Author: Matt Haas
 * Created: 4/25/2020
 */

package plotter;

import gps.Track;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles drawing the table on the plotter
 */
public class PlotterTable extends CanvasLayer {
    private List<PlotterTableObject> list;
    private GraphicsContext gc = this.getGraphicsContext2D();
    private static double MAP_DIMENSIONS = 500;

    //MLH: Public methods should have complete javadoc
    PlotterTable(List<Track> tracks) {
        super(null, MAP_DIMENSIONS);
        list = new ArrayList<>();
        for (Track track : tracks) {
            list.add(new PlotterTableObject(track.getName(), track.getDistanceMiles(), track.getDistanceKM()));
        }
        draw();
    }

    private void draw() {
        double x = 10;
        double y = 10;
        DecimalFormat df = new DecimalFormat("#.00");
        for (PlotterTableObject track : list) {
            if (track.isVisible()) {
                gc.setStroke(track.getColor());
                gc.setFill(track.getColor());
                gc.fillRoundRect(x, y - 4, x + 35, 8, 10, 10);
                gc.strokeText(track.getName(), x + 50, y + 5);
                gc.strokeText("                " + df.format(track.getDistanceInMiles()) + " miles : " + df.format(track.getDistanceInKM()) + " km", x, y + 20);
                y += 35;
            }

        }
    }

    public void redraw(String name, boolean isSelected) {
        gc.clearRect(0, 0, MAP_DIMENSIONS, MAP_DIMENSIONS);
        list.get(getTrackFromName(name)).setVisible(isSelected);
        draw();
    }


    public void setTrackColor(String name, Color color) {
        gc.clearRect(0, 0, MAP_DIMENSIONS, MAP_DIMENSIONS);
        list.get(getTrackFromName(name)).setColor(color);
        draw();
    }

    private int getTrackFromName(String name) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }
}
