package com.nagarro.af.bookingtablesystem.service.impl;

import com.nagarro.af.bookingtablesystem.dto.CustomerDTO;
import com.nagarro.af.bookingtablesystem.exception.NotFoundException;
import com.nagarro.af.bookingtablesystem.exception.NotUniqueException;
import com.nagarro.af.bookingtablesystem.mapper.ListMapper;
import com.nagarro.af.bookingtablesystem.mapper.impl.service.RestaurantMapper;
import com.nagarro.af.bookingtablesystem.model.Admin;
import com.nagarro.af.bookingtablesystem.model.Customer;
import com.nagarro.af.bookingtablesystem.model.RestaurantManager;
import com.nagarro.af.bookingtablesystem.repository.AdminRepository;
import com.nagarro.af.bookingtablesystem.repository.CustomerRepository;
import com.nagarro.af.bookingtablesystem.repository.RestaurantManagerRepository;
import com.nagarro.af.bookingtablesystem.service.CustomerService;
import com.nagarro.af.bookingtablesystem.service.RestaurantService;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final AdminRepository adminRepository;

    private final RestaurantManagerRepository managerRepository;

    private final RestaurantService restaurantService;

    private final ModelMapper modelMapper;

    private final ListMapper listMapper;

    private final RestaurantMapper restaurantMapper;

    private final PasswordEncoder passwordEncoder;

    public CustomerServiceImpl(CustomerRepository customerRepository, AdminRepository adminRepository,
                               RestaurantManagerRepository managerRepository, RestaurantService restaurantService,
                               ModelMapper modelMapper, ListMapper listMapper, RestaurantMapper restaurantMapper,
                               PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.adminRepository = adminRepository;
        this.managerRepository = managerRepository;
        this.restaurantService = restaurantService;
        this.modelMapper = modelMapper;
        this.listMapper = listMapper;
        this.restaurantMapper = restaurantMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public CustomerDTO save(CustomerDTO customerDTO) {
        Customer customer = modelMapper.map(customerDTO, Customer.class);
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        return modelMapper.map(customerRepository.save(customer), CustomerDTO.class);
    }

    @Override
    public CustomerDTO update(CustomerDTO customerDTO) {
        CustomerDTO initialCustomer = findById(customerDTO.getId());
        Customer updatedCustomer = modelMapper.map(customerDTO, Customer.class);

        if (!initialCustomer.getUsername().equals(updatedCustomer.getUsername())) {
            Optional<Admin> adminOptional = adminRepository.findByUsername(updatedCustomer.getUsername());
            Optional<Customer> customerOptional = customerRepository.findByUsername(updatedCustomer.getUsername());
            Optional<RestaurantManager> managerOptional = managerRepository.findByUsername(updatedCustomer.getUsername());
            if (adminOptional.isPresent() || customerOptional.isPresent() || managerOptional.isPresent()) {
                throw new NotUniqueException("This username is already used!");
            }
        }

        if (!initialCustomer.getEmail().equals(updatedCustomer.getEmail())) {
            Optional<Admin> adminOptional = adminRepository.findByEmail(updatedCustomer.getEmail());
            Optional<Customer> customerOptional = customerRepository.findByEmail(updatedCustomer.getEmail());
            Optional<RestaurantManager> managerOptional = managerRepository.findByEmail(updatedCustomer.getEmail());
            if (adminOptional.isPresent() || customerOptional.isPresent() || managerOptional.isPresent()) {
                throw new NotUniqueException("This email is already used!");
            }
        }
        updatedCustomer.setPassword(passwordEncoder.encode(updatedCustomer.getPassword()));
        return modelMapper.map(customerRepository.save(updatedCustomer), CustomerDTO.class);
    }

    @Override
    public CustomerDTO findById(UUID id) {
        return customerRepository.findById(id)
                .map(this::mapToCustomerDTO)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id +
                        " could not be found!"));
    }

    @Override
    public CustomerDTO findByEmail(String email) {
        return customerRepository.findByEmail(email)
                .map(this::mapToCustomerDTO)
                .orElseThrow(() -> new NotFoundException("Customer with email " + email +
                        " could not be found!"));
    }

    @Override
    public CustomerDTO findByUsername(String username) {
        return customerRepository.findByUsername(username)
                .map(this::mapToCustomerDTO)
                .orElseThrow(() -> new NotFoundException("Customer with username " + username +
                        " could not be found!"));
    }

    @Override
    public List<CustomerDTO> findAll() {
        List<Customer> customers = customerRepository.findAll();
        return listMapper.mapList(customers, CustomerDTO.class);
    }

    @Override
    public List<CustomerDTO> findAllByFullName(String name) {
        List<Customer> customers = customerRepository.findAllByFullName(name);
        return listMapper.mapList(customers, CustomerDTO.class);
    }

    @Override
    public void delete(UUID id) {
        customerRepository.deleteById(id);
    }

    private CustomerDTO mapToCustomerDTO(Customer customer) {
        return modelMapper.map(customer, CustomerDTO.class);
    }

    @Override
    public void saveCustomersXML(List<CustomerDTO> customers) {
        StringBuilder stringBuilder = new StringBuilder();
        for (CustomerDTO customerDTO : customers) {
            stringBuilder.append(objectToXml(customerDTO, CustomerDTO.class)).append("\n");
        }
        Path fileName = Path.of(
                "C:/Users/Kitty/Documents/Facultate/Nagarro/AF/af22-meda-titu/BookingTableSystem/src/main/resources/customers.xml");

        try {
            Files.writeString(fileName, stringBuilder.toString(), StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String objectToXml(Object object, Class<?> clazz) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(object, sw);
            return sw.toString();
        } catch (JAXBException e) {
            throw new IllegalArgumentException("Error while converting object to xml", e);
        }
    }
}
