package core.service;

import core.database.entity.UsersEntity;
import core.database.repository.UsersRepository;
import core.dto.RegisterUsersDTO;
import core.dto.UsersDTO;
import core.exception.UserAlreadyExists;
import core.mapper.UsersMapper;
import core.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UsersRepository usersRepository, UsersMapper usersMapper, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usersRepository = usersRepository;
        this.usersMapper = usersMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public UsersDTO registerUser(RegisterUsersDTO registerUsersDTO) {
        UsersEntity usersEntity = new UsersEntity();

        usersEntity.setName(registerUsersDTO.getName());
        if(usersRepository.existsByLogin(usersEntity.getLogin())) {
            throw new UserAlreadyExists(usersEntity.getLogin());
        }
        usersEntity.setLogin(registerUsersDTO.getLogin());
        usersEntity.setPassword(passwordEncoder.encode(registerUsersDTO.getPassword()));
        usersEntity.setAge(registerUsersDTO.getAge());
        usersEntity.setWeight(registerUsersDTO.getWeight());
        usersEntity.setHeight(registerUsersDTO.getHeight());
        usersEntity.setTargetKcal(1900);
        usersEntity.setGender(registerUsersDTO.getGender());
        usersEntity.setCurrentTrainingProgram(null);
        usersEntity.setCurrentNutritionProgram(null);
        usersEntity.setCreatedAt(LocalDateTime.now());

        UsersEntity savedUser = usersRepository.save(usersEntity);

        return usersMapper.toDTO(savedUser);
    }

    public String login(String login, String password) {
        UsersEntity usersEntity = usersRepository.findByLogin(login);
        if(usersEntity != null && passwordEncoder.matches(password, usersEntity.getPassword())) {
            return jwtUtil.generateToken(usersEntity);
        }
        throw new RuntimeException("Invalid login or password");
    }

}
