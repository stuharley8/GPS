/*
 * Course: SE2030-041
 * Fall 2019
 * Lab: GPS
 * Author: Stuart Harley, Joey Rundlett, Anthony Lohmiller
 * Created: 10/4/2019
 */

package gps;

import java.util.List;

/**
 * Represents a Collection of Points that form a GPS track
 */
public class Track {

    private double aveSpeedKM;
    private double aveSpeedMiles;
    private double distanceKM;
    private double distanceMiles;
    private double maxElevation;
    private double maxLatitude;
    private double maxLongitude;
    private double maxSpeedKM = 0.0;
    private double maxSpeedMiles = 0.0;
    private double minElevation;
    private double minLatitude;
    private double minLongitude;

    private final static double METERS_TO_FEET = 3.28084;
    private final static int EARTH_RADIUS_METERS = 6371000;
    private final static double KM_TO_MILES = 0.621371;

    private String name;
    private List<Point> points;

    /**
     * Constructor. Calculates metrics for the track when initialized.
     * @param points a not null list of points that represents the path of the track.
     * @param name the name of the track
     * @throws NullPointerException if points is null
     * @throws IllegalArgumentException if points contains 0 points
     */
    public Track(List<Point> points, String name) {
        if(points == null) {
            throw new NullPointerException("List of point is null");
        } else if(points.size() == 0) {
            throw new IllegalArgumentException("Track must contain at least one point");
        }
        this.points = points;
        this.name = name;
        calcMinsAndMaxes();
        Calculations();
    }

    private void Calculations(){
        calcMinsAndMaxes();
        for (int i = 0; i < points.size()-1; i++) {
            distanceKM += distanceCalc(points.get(i), points.get(i+1));
            maxSpeedCalc(points.get(i), points.get(i+1));
        }
        distanceMiles = distanceKM*KM_TO_MILES;
        aveSpeedCalc();
    }

    private void maxSpeedCalc(Point pointA, Point pointB){
        double tempKM = distanceCalc(pointA, pointB);
        double tempMiles = tempKM*KM_TO_MILES;
        double speedKM = tempKM/timeCalc(pointA, pointB);
        double speedMiles = tempMiles/timeCalc(pointA, pointB);
        System.out.println(speedKM);
        if(speedKM > maxSpeedKM){
            maxSpeedKM = speedKM;
            maxSpeedMiles = speedMiles;
        }
    }

    private double timeCalc(Point pointA, Point pointB){
        long totalTime = Math.abs(pointB.getDate().getTime()-pointA.getDate().getTime());
        double seconds = totalTime/1000;
        double minutes = seconds/60;
        return minutes/60;
    }

    private double distanceCalc(Point pointA, Point pointB){
        double deltaX = (EARTH_RADIUS_METERS + (pointB.getElevation()+pointA.getElevation())/2)*(Math.toRadians(Math.abs(pointB.getLongitude()))-Math.toRadians(Math.abs(pointA.getLongitude())))*Math.cos((Math.toRadians(pointB.getLatitude())+Math.toRadians(pointA.getLatitude()))/2);
        double deltaY = (EARTH_RADIUS_METERS + (pointB.getElevation() + pointA.getElevation())/2)*(Math.toRadians(pointB.getLatitude())-Math.toRadians(pointA.getLatitude()));
        double deltaZ = pointB.getElevation() - pointA.getElevation();
        return Math.sqrt(Math.pow(deltaX, 2)+Math.pow(deltaY, 2)+Math.pow(deltaZ, 2))/1000;
    }
    private void aveSpeedCalc(){
        if(points.size() > 1) {
            aveSpeedKM = distanceKM/timeCalc(points.get(points.size()-1), points.get(0));
            aveSpeedMiles = distanceMiles/timeCalc(points.get(points.size()-1), points.get(0));
        }else{
            aveSpeedKM = 0.0;
            aveSpeedMiles = 0.0;
        }
    }

    private void calcMinsAndMaxes() {
        double minLat = points.get(0).getLatitude();
        double minLong = points.get(0).getLongitude();
        double minEle = points.get(0).getElevation();
        double maxLat = points.get(0).getLatitude();
        double maxLong = points.get(0).getLongitude();
        double maxEle = points.get(0).getElevation();
        for(int i = 1; i < points.size(); i++) {
            if(points.get(i).getElevation() > maxEle) {
                maxEle = points.get(i).getElevation();
            } else if(points.get(i).getElevation() < minEle) {
                minEle = points.get(i).getElevation();
            }
            if(points.get(i).getLatitude() > maxLat) {
                maxLat = points.get(i).getLatitude();
            } else if(points.get(i).getLatitude() < minLat) {
                minLat = points.get(i).getLatitude();
            }
            if(points.get(i).getLongitude() > maxLong) {
                maxLong = points.get(i).getLongitude();
            } else if(points.get(i).getLongitude() < minLong) {
                minLong = points.get(i).getLongitude();
            }
        }
        minElevation = minEle;
        minLatitude = minLat;
        minLongitude = minLong;
        maxElevation = maxEle;
        maxLatitude = maxLat;
        maxLongitude = maxLong;
    }

    public double getAveSpeedKM() {
        return aveSpeedKM;
    }

    public double getAveSpeedMiles() {
        return aveSpeedMiles;
    }

    public double getDistanceKM() {
        return distanceKM;
    }

    public double getDistanceMiles() {
        return distanceMiles;
    }

    public double getMaxElevation() {
        return maxElevation;
    }

    public double getMaxElevationFt(){
        return maxElevation*METERS_TO_FEET;
    }

    public double getMaxLatitude() {
        return maxLatitude;
    }

    public double getMaxLongitude() {
        return maxLongitude;
    }

    public double getMaxSpeedKM() {
        return maxSpeedKM;
    }

    public double getMaxSpeedMiles() {
        return maxSpeedMiles;
    }

    public double getMinElevation() {
        return minElevation;
    }

    public double getMinElevationFt(){
        return minElevation*METERS_TO_FEET;
    }

    public double getMinLatitude() {
        return minLatitude;
    }

    public double getMinLongitude() {
        return minLongitude;
    }

    public String getName() {
        return name;
    }

    public int getNumPoints() {
        return points.size();
    }
}