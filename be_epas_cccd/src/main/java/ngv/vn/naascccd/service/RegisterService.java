package ngv.vn.naascccd.service;

import ngv.vn.naascccd.entity.Register;

import java.util.Optional;

public interface RegisterService {
    public Optional<Register> register(Register register);
    
    public Optional<Register> updateRegister(Register register);
    
    public Optional<Register> getRegisterByUserId(String userId);
}
