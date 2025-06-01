package ar.edu.itba.pod.common;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class TypesCHIRow implements DataSerializable {
    private String srShortCode;
    private String srType;

    public TypesCHIRow(String srShortCode, String srType) {
        this.srShortCode = srShortCode;
        this.srType = srType;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(srShortCode);
        out.writeUTF(srType);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        srShortCode = in.readUTF();
        srType = in.readUTF();
    }

    public String getSrShortCode() {
        return srShortCode;
    }

    public String getSrType() {
        return srType;
    }
}
