package org.example.service.impl;

import org.example.model.dto.UserDTO;
import org.example.model.entity.UserEntity;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    ModelMapper mapper = new ModelMapper();

    @Override
    public void registerUser(UserDTO userDTO) {
        UserEntity userEntity= mapper.map(userDTO, UserEntity.class );
        userRepository.save(userEntity);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return null;
    }
}
