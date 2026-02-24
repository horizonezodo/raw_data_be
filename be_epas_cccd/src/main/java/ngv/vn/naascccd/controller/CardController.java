package ngv.vn.naascccd.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import ngv.vn.naascccd.service.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CardController{

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }
 
    @PostMapping("/card/read")
    @PreAuthorize("hasRole(T(ngv.vn.naascccd.security.Role).CARD_READ)")
    public ResponseEntity<ObjectNode> readInfoCard(@RequestBody ObjectNode body) throws Exception {
        return ResponseEntity.ok(cardService.readInfoCard(body));
    }
    

    @PostMapping("/card/verify")
    @PreAuthorize("hasRole(T(ngv.vn.naascccd.security.Role).CARD_VERIFY)")
    public ResponseEntity<ObjectNode> verifyCard(@RequestBody ObjectNode body) throws Exception {
        return ResponseEntity.ok(cardService.verifyCertC06(body));
    }
    
    
    @PostMapping("/face/verify")
    @PreAuthorize("hasRole(T(ngv.vn.naascccd.security.Role).FACE_VERIFY)")
    public ResponseEntity<ObjectNode> verifyFace(@RequestBody ObjectNode body) throws Exception {
        return ResponseEntity.ok(cardService.verifyFace(body));
    }
    
    @GetMapping("/device/config")
    @PreAuthorize("hasRole(T(ngv.vn.naascccd.security.Role).DEVICE_CONFIG)")
    public ResponseEntity<ObjectNode> getDeviceConfig(@RequestParam String deviceId) throws Exception {
        return ResponseEntity.ok(cardService.getDeviceConfig(deviceId));
    }
}
