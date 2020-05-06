package graph;

import gps.Point;
import gps.Track;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.util.List;

public class GraphHandler {

    private static final int EARTH_RADIUS_METERS = 6371000;
    private static final double KM_TO_MILES = 0.621371;

    private LineChart<Double, Double> chart;
    private boolean isMiles;
    List<Track> selectedTracks;


    public GraphHandler(LineChart<Double, Double> chart, List<Track> selectedTracks, boolean isMiles){
        this.selectedTracks = selectedTracks;
        this.chart = chart;
        this.isMiles = isMiles;
    }

    public void drawAllGraphs(){
        chart.getData().clear();
        for (Track track : this.selectedTracks) {
            drawGraph(track.getName());
        }
    }

    public void setSelectedTracks(List<Track> selectedTracks){
        this.selectedTracks = selectedTracks;
    }

    private void drawGraph(String name){
        Track track = null;
        for (Track t:selectedTracks
        ) {
            if (t.getName().equals(name)){
                track = t;
            }
        }
        double distance = 0;
        double time = 0;
        String unit = "km";
        XYChart.Series points = new XYChart.Series();
        if(isMiles){

            unit = "mi";
        }

        List<Point> pointList = track.getPoints();
        distance = drawPoints(pointList, points, distance, time, isMiles);
        String rounded = String.format("%.3f", distance);
        points.setName(track.getName() + " Total Distance: " + rounded + " " + unit);
        chart.getData().add(points);
    }

    private double drawPoints(List<Point> pointList,  XYChart.Series points, double distance, double time, boolean isMiles){
        for (int i = 0; i < pointList.size()-1; i++){
            if(isMiles){
                distance += KM_TO_MILES*calculateDistance(pointList.get(i), pointList.get(i+1));
            }else{
                distance += calculateDistance(pointList.get(i), pointList.get(i+1));
            }

            time += calculateTime(pointList.get(i), pointList.get(i+1));
            points.getData().add(new XYChart.Data(time, distance));
        }
        return distance;
    }


    private double calculateDistance(Point pointA, Point pointB){
        double deltaX = (EARTH_RADIUS_METERS + (pointB.getElevation() + pointA.getElevation()) / 2)
                * (Math.toRadians(Math.abs(pointB.getLongitude())) - Math.toRadians(Math.abs(pointA.getLongitude())))
                * Math.cos((Math.toRadians(Math.abs(pointB.getLatitude())) + Math.toRadians(Math.abs(pointA.getLatitude()))) / 2);
        double deltaY = (EARTH_RADIUS_METERS + (pointB.getElevation() + pointA.getElevation()) / 2)
                * (Math.toRadians(pointB.getLatitude()) - Math.toRadians(pointA.getLatitude()));
        double deltaZ = pointB.getElevation() - pointA.getElevation();
        return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2) + Math.pow(deltaZ, 2)) / 1000;
    }

    private double calculateTime(Point pointA, Point pointB){
        long totalTime = Math.abs(pointB.getDate().getTime() - pointA.getDate().getTime());
        double seconds = totalTime / 1000.0;
        double minutes = seconds / 60;
        return minutes;
    }




}
