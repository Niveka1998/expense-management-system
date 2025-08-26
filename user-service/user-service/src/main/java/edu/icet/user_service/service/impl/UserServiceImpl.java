package edu.icet.user_service.service.impl;

import edu.icet.user_service.model.dto.UserDTO;
import edu.icet.user_service.model.entity.UserEntity;
import edu.icet.user_service.repository.UserRepository;
import edu.icet.user_service.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    //ModelMapper mapper = new ModelMapper();

    @Override
    public void registerUser(UserDTO userDTO) {
        System.out.println("Got userDTO: "+userDTO);
        UserEntity userEntity= new UserEntity();
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setUsername(userDTO.getUsername());
        userEntity.setPassword(userDTO.getPassword());
        System.out.println("Entity to save: "+userEntity);
        userRepository.save(userEntity);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if(userEntity!= null){
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(userEntity.getUserId());
            userDTO.setEmail(userEntity.getEmail());
            userDTO.setUsername(userEntity.getUsername());
            userDTO.setPassword(userEntity.getPassword());
            System.out.println("Got user: "+userDTO);
            return userDTO;
        }
        return null;
    }
}
