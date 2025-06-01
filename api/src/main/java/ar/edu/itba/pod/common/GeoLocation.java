package ar.edu.itba.pod.common;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

//Might not need to be serializable, will keep it like that for now
public class GeoLocation implements DataSerializable {
    private double latitude;
    private double longitude;

    public GeoLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeDouble(latitude);
        out.writeDouble(longitude);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
