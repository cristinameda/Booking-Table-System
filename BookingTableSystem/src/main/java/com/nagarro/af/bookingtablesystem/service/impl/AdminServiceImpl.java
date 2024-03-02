package com.nagarro.af.bookingtablesystem.service.impl;

import com.nagarro.af.bookingtablesystem.dto.AdminDTO;
import com.nagarro.af.bookingtablesystem.exception.NotFoundException;
import com.nagarro.af.bookingtablesystem.exception.NotUniqueException;
import com.nagarro.af.bookingtablesystem.mapper.ListMapper;
import com.nagarro.af.bookingtablesystem.model.Admin;
import com.nagarro.af.bookingtablesystem.model.Customer;
import com.nagarro.af.bookingtablesystem.model.RestaurantManager;
import com.nagarro.af.bookingtablesystem.repository.AdminRepository;
import com.nagarro.af.bookingtablesystem.repository.CustomerRepository;
import com.nagarro.af.bookingtablesystem.repository.RestaurantManagerRepository;
import com.nagarro.af.bookingtablesystem.service.AdminService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    private final CustomerRepository customerRepository;

    private final RestaurantManagerRepository managerRepository;

    private final ModelMapper modelMapper;

    private final ListMapper listMapper;

    private final PasswordEncoder passwordEncoder;

    public AdminServiceImpl(AdminRepository AdminRepository, CustomerRepository customerRepository, RestaurantManagerRepository managerRepository, ModelMapper modelMapper, ListMapper listMapper, PasswordEncoder passwordEncoder) {
        this.adminRepository = AdminRepository;
        this.customerRepository = customerRepository;
        this.managerRepository = managerRepository;
        this.modelMapper = modelMapper;
        this.listMapper = listMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AdminDTO save(AdminDTO adminDTO) {
        Admin admin = modelMapper.map(adminDTO, Admin.class);
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return modelMapper.map(adminRepository.save(admin), AdminDTO.class);
    }

    @Override
    public AdminDTO update(AdminDTO adminDTO) {
        AdminDTO initialAdmin = findById(adminDTO.getId());
        Admin updatedAdmin = modelMapper.map(adminDTO, Admin.class);

        // todo: extract code to remove duplicated code
        if (!initialAdmin.getUsername().equals(updatedAdmin.getUsername())) {
            Optional<Admin> adminOptional = adminRepository.findByUsername(updatedAdmin.getUsername());
            Optional<Customer> customerOptional = customerRepository.findByUsername(updatedAdmin.getUsername());
            Optional<RestaurantManager> managerOptional = managerRepository.findByUsername(updatedAdmin.getUsername());
            if (adminOptional.isPresent() || customerOptional.isPresent() || managerOptional.isPresent()) {
                throw new NotUniqueException("This username is already used!");
            }
        }

        if (!initialAdmin.getEmail().equals(updatedAdmin.getEmail())) {
            Optional<Admin> adminOptional = adminRepository.findByEmail(updatedAdmin.getEmail());
            Optional<Customer> customerOptional = customerRepository.findByEmail(updatedAdmin.getEmail());
            Optional<RestaurantManager> managerOptional = managerRepository.findByEmail(updatedAdmin.getEmail());
            if (adminOptional.isPresent() || customerOptional.isPresent() || managerOptional.isPresent()) {
                throw new NotUniqueException("This email is already used!");
            }
        }
        updatedAdmin.setPassword(passwordEncoder.encode(updatedAdmin.getPassword()));
        return modelMapper.map(adminRepository.save(updatedAdmin), AdminDTO.class);
    }

    @Override
    public AdminDTO findById(UUID id) {
        return adminRepository.findById(id)
                .map(this::mapToAdminDTO)
                .orElseThrow(() -> new NotFoundException("Admin with id " + id +
                        " could not be found!"));
    }

    @Override
    public AdminDTO findByEmail(String email) {
        return adminRepository.findByEmail(email)
                .map(this::mapToAdminDTO)
                .orElseThrow(() -> new NotFoundException("Admin with email " + email +
                        " could not be found!"));
    }

    @Override
    public AdminDTO findByUsername(String username) {
        return adminRepository.findByUsername(username)
                .map(this::mapToAdminDTO)
                .orElseThrow(() -> new NotFoundException("Admin with username " + username +
                        " could not be found!"));
    }

    @Override
    public List<AdminDTO> findAll() {
        List<Admin> admins = adminRepository.findAll();
        return listMapper.mapList(admins, AdminDTO.class);
    }

    @Override
    public List<AdminDTO> findAllByFullName(String name) {
        List<Admin> admins = adminRepository.findAllByFullName(name);
        return listMapper.mapList(admins, AdminDTO.class);
    }

    @Override
    public void delete(UUID id) {
        adminRepository.deleteById(id);
    }

    private AdminDTO mapToAdminDTO(Admin admin) {
        return modelMapper.map(admin, AdminDTO.class);
    }
}
