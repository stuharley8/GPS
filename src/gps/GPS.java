/*
 * Course: SE2030-041
 * Fall 2019
 * Lab: GPS
 * Author: Stuart Harley, Joey Rundlett, Anthony Lohmiller
 * Created: 10/4/2019
 */

package gps;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a GPS that contains tracks
 */
public class GPS {

    private static final int MAX_TRACKS = 10;

    private List<Track> tracks;

    /**
     * Default constructor
     */
    public GPS() {
        tracks = new ArrayList<>();
    }

    /**
     * Adds a track to the list of tracks
     *
     * @param track the track to be
     * @throws IllegalArgumentException if adding a track to tracks would increase
     *                                  tracks size to greater than MAX_TRACKS
     */
    public void addTrack(Track track) throws IllegalArgumentException {
        if (tracks.size() == MAX_TRACKS) {
            throw new IllegalArgumentException("Max number (" +
                    MAX_TRACKS + ") of tracks already contained");
        }
        tracks.add(track);
    }

    /**
     * Returns the Track from tracks at the specified index
     *
     * @param index the index
     * @return the Track at the specified index
     */
    public Track getTrack(int index) {
        return tracks.get(index);
    }

    public int getNumTracks() {
        return tracks.size();
    }

    public List<Track> getTracks() {
        return tracks;
    }
}