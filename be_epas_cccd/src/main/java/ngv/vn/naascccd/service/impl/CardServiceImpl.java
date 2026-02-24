package ngv.vn.naascccd.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import ngv.vn.naascccd.client.CardFeignClient;
import ngv.vn.naascccd.config.JwtAuthConverter;
import ngv.vn.naascccd.entity.Message;
import ngv.vn.naascccd.entity.Register;
import ngv.vn.naascccd.service.CardService;
import ngv.vn.naascccd.service.MessageService;
import ngv.vn.naascccd.service.RegisterService;
import ngv.vn.naascccd.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@RequiredArgsConstructor
@Service
public class CardServiceImpl implements CardService {
    
    @Value("${app.client.app-id}")
    private String clientId;
    
    @Value("${app.client.private-key}")
    private String clientSecret;

    private final Logger log = LoggerFactory.getLogger(CardServiceImpl.class);
    private final CardFeignClient cardFeignClient;
    private final RegisterService registerService;
    private final MessageService messageService;
    private final JwtAuthConverter jwtAuthConverter;
    private final ObjectMapper objectMapper;

    @Override
    public ObjectNode readInfoCard(ObjectNode data) throws Exception {
        log.info("Read info card");
        ObjectNode response = cardFeignClient.readCard(data, generateSignature(data, clientSecret), clientId);
        addTraceTable(data, response);
        return response;
    }

    @Override
    public ObjectNode verifyCertC06(ObjectNode data) throws Exception {
        log.info("Verify C06 card");
        ObjectNode response = cardFeignClient.verifyC06(data, generateSignature(data, clientSecret), clientId);
        addTraceTable(data, response);
        return response;
    }
    
    @Override
    public ObjectNode getDeviceConfig(String deviceId) throws Exception {
        log.info("Get device config");
        ObjectNode response = cardFeignClient.getConfig(deviceId);
        addTraceTable(objectMapper.createObjectNode().put("deviceId", deviceId), response);
        return response;
    }
    
    @Override
    public ObjectNode verifyFace(ObjectNode data) throws Exception {
        log.info("Verify face 2");
        String readCardId = data.get("request_id").asText();
        data.remove("request_id");
        ObjectNode response = cardFeignClient.verifyFace(data, generateSignature(data, clientSecret), clientId, readCardId);
        addTraceTable(data, response);
        return response;
    }
    
    public void addTraceTable(ObjectNode data, ObjectNode response) throws JsonProcessingException {
        String userId = jwtAuthConverter.getUserId();
        if (registerService.getRegisterByUserId(userId).isEmpty()) {
            Register register = new Register();
            register.setUserId(userId);
            register.setCreatorUserId(userId);
            register.setDeleted(false);
            registerService.register(register);
        }
        Message message = new Message();
        message.setUserId(userId);
        message.setPartnerCode(StringUtil.randomString(8));
        if (!response.isEmpty()) {
            message.setStatus("Success");
        } else {
            message.setStatus("Failure");
        }
        message.setResponseCode(response.get("status").asText());
        message.setResponseDescription(response.get("status").asInt() == 200 ? "":response.get("message").asText());
        message.setCiRequest(objectMapper.writeValueAsString(data));
        message.setCiResponse(objectMapper.writeValueAsString(response.get("data")));
        message.setPartnerRequest(objectMapper.writeValueAsString(data));
        message.setPartnerResponse(objectMapper.writeValueAsString(response));
        messageService.createMessage(message);
    }
    
    public String generateSignature(ObjectNode body, String clientSecret) throws Exception {
        ObjectNode sortedNode = objectMapper.createObjectNode();
        
        // Load the private key from PEM string
        String privateKeyPem = clientSecret
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replaceAll("\\s+", "");
        
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyPem);
        
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = kf.generatePrivate(keySpec);
        
        // Sign the data
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(body.toString().getBytes(StandardCharsets.UTF_8));
        
        byte[] signatureBytes = privateSignature.sign();
        
        // Encode the signature in Base64
        String signature = Base64.getEncoder().encodeToString(signatureBytes);
        log.info("---signature: {}", signature);
        return signature;
    }
}
