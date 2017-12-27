package com.google.android.gms.location.sample.locationaddress;

/**
 * Created by omer on 12/21/2017.
 */

public class distance {

    public double degreesToRadians(double degrees) {
        double pi = Math.PI;
        return degrees * Math.PI / 180.0;
    }

    public double distanceInKmBetweenEarthCoordinates(double lat1, double lon1,double lat2,double lon2) {
        double earthRadiusKm = 6371;

        double dLat = degreesToRadians(lat2-lat1);
        double dLon = degreesToRadians(lon2-lon1);

        lat1 = degreesToRadians(lat1);
        lat2 = degreesToRadians(lat2);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return earthRadiusKm * c;
    }

}
