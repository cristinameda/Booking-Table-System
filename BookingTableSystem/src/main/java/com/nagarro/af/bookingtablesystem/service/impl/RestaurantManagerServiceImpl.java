package com.nagarro.af.bookingtablesystem.service.impl;

import com.nagarro.af.bookingtablesystem.dto.RestaurantManagerDTO;
import com.nagarro.af.bookingtablesystem.exception.NotFoundException;
import com.nagarro.af.bookingtablesystem.exception.NotUniqueException;
import com.nagarro.af.bookingtablesystem.mapper.impl.service.RestaurantManagerMapper;
import com.nagarro.af.bookingtablesystem.model.Admin;
import com.nagarro.af.bookingtablesystem.model.Customer;
import com.nagarro.af.bookingtablesystem.model.RestaurantManager;
import com.nagarro.af.bookingtablesystem.repository.AdminRepository;
import com.nagarro.af.bookingtablesystem.repository.CustomerRepository;
import com.nagarro.af.bookingtablesystem.repository.RestaurantManagerRepository;
import com.nagarro.af.bookingtablesystem.service.RestaurantManagerService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RestaurantManagerServiceImpl implements RestaurantManagerService {

    private final RestaurantManagerRepository managerRepository;

    private final AdminRepository adminRepository;

    private final CustomerRepository customerRepository;

    private final RestaurantManagerMapper managerMapper;

    private final PasswordEncoder passwordEncoder;

    public RestaurantManagerServiceImpl(RestaurantManagerRepository managerRepository, AdminRepository adminRepository, CustomerRepository customerRepository, RestaurantManagerMapper managerMapper, PasswordEncoder passwordEncoder) {
        this.managerRepository = managerRepository;
        this.adminRepository = adminRepository;
        this.customerRepository = customerRepository;
        this.managerMapper = managerMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public RestaurantManagerDTO save(RestaurantManagerDTO restaurantManagerDTO) {
        RestaurantManager restaurantManager = managerMapper.mapDTOtoEntity(restaurantManagerDTO);
        restaurantManager.setPassword(passwordEncoder.encode(restaurantManager.getPassword()));
        return managerMapper.mapEntityToDTO(managerRepository.save(restaurantManager));
    }

    @Override
    public RestaurantManagerDTO update(RestaurantManagerDTO restaurantManagerDTO) {
        RestaurantManagerDTO initialManager = findById(restaurantManagerDTO.getId());
        RestaurantManager updatedManager = managerMapper.mapDTOtoEntity(restaurantManagerDTO);

        if (!initialManager.getUsername().equals(updatedManager.getUsername())) {
            Optional<Admin> adminOptional = adminRepository.findByUsername(updatedManager.getUsername());
            Optional<Customer> customerOptional = customerRepository.findByUsername(updatedManager.getUsername());
            Optional<RestaurantManager> managerOptional = managerRepository.findByUsername(updatedManager.getUsername());
            if (adminOptional.isPresent() || customerOptional.isPresent() || managerOptional.isPresent()) {
                throw new NotUniqueException("This username is already used!");
            }
        }

        if (!initialManager.getEmail().equals(updatedManager.getEmail())) {
            Optional<Admin> adminOptional = adminRepository.findByEmail(updatedManager.getEmail());
            Optional<Customer> customerOptional = customerRepository.findByEmail(updatedManager.getEmail());
            Optional<RestaurantManager> managerOptional = managerRepository.findByEmail(updatedManager.getEmail());
            if (adminOptional.isPresent() || customerOptional.isPresent() || managerOptional.isPresent()) {
                throw new NotUniqueException("This email is already used!");
            }
        }
        updatedManager.setPassword(passwordEncoder.encode(updatedManager.getPassword()));
        return managerMapper.mapEntityToDTO(managerRepository.save(updatedManager));
    }

    @Override
    public RestaurantManagerDTO findById(UUID id) {
        return managerRepository.findById(id)
                .map(this::mapToRestaurantManagerDTO)
                .orElseThrow(() -> new NotFoundException("Restaurant manager with id "
                        + id + " could not be found!"));
    }

    @Override
    public RestaurantManagerDTO findByEmail(String email) {
        return managerRepository.findByEmail(email)
                .map(this::mapToRestaurantManagerDTO)
                .orElseThrow(() -> new NotFoundException("Restaurant manager with email "
                        + email + " could not be found!"));
    }

    @Override
    public RestaurantManagerDTO findByUsername(String username) {
        return managerRepository.findByUsername(username)
                .map(this::mapToRestaurantManagerDTO)
                .orElseThrow(() -> new NotFoundException("Restaurant manager with username "
                        + username + " could not be found!"));
    }

    @Override
    public List<RestaurantManagerDTO> findAll() {
        List<RestaurantManager> managers = managerRepository.findAll();
        return managerMapper.mapEntityListToDTOList(managers);
    }

    @Override
    public List<RestaurantManagerDTO> findAllByFullName(String name) {
        List<RestaurantManager> managers = managerRepository.findAllByFullName(name);
        return managerMapper.mapEntityListToDTOList(managers);
    }

    @Override
    public void delete(UUID id) {
        managerRepository.deleteById(id);
    }

    private RestaurantManagerDTO mapToRestaurantManagerDTO(RestaurantManager restaurantManager) {
        return managerMapper.mapEntityToDTO(restaurantManager);
    }
}
