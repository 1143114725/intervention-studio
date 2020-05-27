package com.investigate.newsupper.util.locationutils;

/**
 * Created by EEH on 2018/8/7.
 */
public class Gps {

    private double mLatitude;
    private double mLongitude;
    
	private String add;
    private String lotscoord;
    
    public String getAdd() {
		return add;
	}

	public void setAdd(String add) {
		this.add = add;
	}

	public String getLotscoord() {
		return lotscoord;
	}

	public void setLotscoord(String lotscoord) {
		this.lotscoord = lotscoord;
	}



    public Gps() {
    }

    public Gps(double longitude, double mLatitude) {
        setLatitude(mLatitude);
        setLongitude(longitude);
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        this.mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        this.mLongitude = longitude;
    }

    @Override
    public String toString() {
        return mLongitude + "," + mLatitude;
    }
}