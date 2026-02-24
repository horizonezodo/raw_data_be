package ngv.vn.naascccd.service;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface CardService {
    public ObjectNode readInfoCard(ObjectNode data) throws Exception;
    
    public ObjectNode verifyCertC06(ObjectNode data) throws Exception;
    
    public ObjectNode verifyFace(ObjectNode data) throws Exception;
    
    public ObjectNode getDeviceConfig(String deviceId) throws Exception;
}
