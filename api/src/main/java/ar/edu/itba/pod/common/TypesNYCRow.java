package ar.edu.itba.pod.common;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

@Deprecated
public class TypesNYCRow implements DataSerializable {
    private String complaintType;

    public TypesNYCRow(String complaintType) {
        this.complaintType = complaintType;
    }

    public TypesNYCRow() {}

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(complaintType);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        complaintType = in.readUTF();
    }

    public String getComplaintType() {
        return complaintType;
    }
}
