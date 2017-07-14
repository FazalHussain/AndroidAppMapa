package com.expreso.androidapp.androidapp.Direcciones_GPS;

/**
 * Created by fazal on 7/13/2017.
 */

public class Info {
    Double distanceInKiloMeter;
    Double time;

    public Info(Double distanceInMeter, Double time) {
        this.distanceInKiloMeter = distanceInMeter;
        this.time = time;
    }

    public Double getDistanceInKiloMeter() {
        return distanceInKiloMeter;
    }

    public void setDistanceInKiloMeter(Double distanceInKiloMeter) {
        this.distanceInKiloMeter = distanceInKiloMeter;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }
}
