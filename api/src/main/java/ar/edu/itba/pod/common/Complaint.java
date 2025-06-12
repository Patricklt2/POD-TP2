package ar.edu.itba.pod.common;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

@Deprecated
public class Complaint implements DataSerializable, Comparable<Complaint> {
    private String type;
    private String agency;

    public Complaint() {}

    public Complaint(String type, String agency) {
        this.type = type;
        this.agency = agency;
    }

    public String getType() { return type; }
    public String getAgency() { return agency; }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(type);
        out.writeUTF(agency);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        type = in.readUTF();
        agency = in.readUTF();
    }

    @Override
    public int compareTo(Complaint other) {
        int cmp = this.type.compareTo(other.type);
        if (cmp != 0) return cmp;
        return this.agency.compareTo(other.agency);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Complaint that &&
                this.type.equals(that.type) &&
                this.agency.equals(that.agency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, agency);
    }

    @Override
    public String toString() {
        return "Complaint [type=" + type + ", agency=" + agency + "]";
    }
}
