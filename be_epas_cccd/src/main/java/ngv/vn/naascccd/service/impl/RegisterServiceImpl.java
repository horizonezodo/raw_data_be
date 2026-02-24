package ngv.vn.naascccd.service.impl;

import lombok.RequiredArgsConstructor;
import ngv.vn.naascccd.entity.Register;
import ngv.vn.naascccd.repository.RegisterRepository;
import ngv.vn.naascccd.service.RegisterService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RegisterServiceImpl implements RegisterService {
    
    private final RegisterRepository registerRepository;
    
    @Override
    public Optional<Register> register(Register register) {
        Register registerEntity = registerRepository.save(register);
        return Optional.of(registerEntity);
    }
    
    @Override
    public Optional<Register> updateRegister(Register register) {
        Register registerEntity = registerRepository.save(register);
        return Optional.of(registerEntity);
    }
    
    @Override
    public Optional<Register> getRegisterByUserId(String userId) {
        Register registerEntity = registerRepository.getByUserId(userId);
        return Optional.ofNullable(registerEntity);
    }
}
