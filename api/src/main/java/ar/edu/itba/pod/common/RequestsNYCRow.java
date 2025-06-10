package ar.edu.itba.pod.common;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.time.LocalDateTime;

@Deprecated
public class RequestsNYCRow extends RequestRow implements DataSerializable {
    private int uniqueKey;
    private String incidentAddress;

    public RequestsNYCRow() {}

    public RequestsNYCRow(int uniqueKey, LocalDateTime createdDate, String agencyName, String complaintType,
                          String incidentAddress, String status, String borough, long latitude, long longitude) {
        super(createdDate, status, borough, new GeoLocation(latitude, longitude), complaintType, agencyName);

        this.uniqueKey = uniqueKey;
        this.incidentAddress = incidentAddress;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
            out.writeInt(uniqueKey);
            out.writeObject(getCreatedDate());
            out.writeUTF(getAgency());
            out.writeUTF(getComplaintType());
            out.writeUTF(incidentAddress);
            out.writeUTF(getStatus());
            out.writeUTF(getNeighbourhood());
            out.writeLong(getGeoLocation().getLatitude());
            out.writeLong(getGeoLocation().getLongitude());
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
            uniqueKey = in.readInt();
            setCreatedDate(in.readObject());
            setAgency(in.readUTF());
            setComplaintType(in.readUTF());
            incidentAddress = in.readUTF();
            setStatus(in.readUTF());
            setNeighbourhood(in.readUTF());

            long latitude = in.readLong();
            long longitude = in.readLong();

            setGeoLocation(new GeoLocation(latitude, longitude));
    }

    public int getUniqueKey() {
        return uniqueKey;
    }

    public String getIncidentAddress(){
        return incidentAddress;
    }
}
