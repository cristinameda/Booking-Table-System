package com.nagarro.af.bookingtablesystem.service.impl;

import com.nagarro.af.bookingtablesystem.dto.MenuDTO;
import com.nagarro.af.bookingtablesystem.dto.RestaurantDTO;
import com.nagarro.af.bookingtablesystem.exception.CorruptedFileException;
import com.nagarro.af.bookingtablesystem.exception.NotFoundException;
import com.nagarro.af.bookingtablesystem.exception.NotUniqueException;
import com.nagarro.af.bookingtablesystem.mapper.impl.service.MenuMapper;
import com.nagarro.af.bookingtablesystem.mapper.impl.service.RestaurantMapper;
import com.nagarro.af.bookingtablesystem.model.*;
import com.nagarro.af.bookingtablesystem.repository.*;
import com.nagarro.af.bookingtablesystem.service.RestaurantService;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    private final RestaurantManagerRepository restaurantManagerRepository;

    private final MenuRepository menuRepository;

    private final RestaurantMapper restaurantMapper;

    private final MenuMapper menuMapper;

    private final AdminRepository adminRepository;

    private final CustomerRepository customerRepository;

    private final RestaurantManagerRepository managerRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository,
                                 RestaurantManagerRepository restaurantManagerRepository,
                                 MenuRepository menuRepository, RestaurantMapper restaurantMapper, MenuMapper menuMapper, AdminRepository adminRepository, CustomerRepository customerRepository, RestaurantManagerRepository managerRepository) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantManagerRepository = restaurantManagerRepository;
        this.menuRepository = menuRepository;
        this.restaurantMapper = restaurantMapper;
        this.menuMapper = menuMapper;
        this.adminRepository = adminRepository;
        this.customerRepository = customerRepository;
        this.managerRepository = managerRepository;
    }

    @Override
    public RestaurantDTO save(RestaurantDTO restaurantDTO) {
        Restaurant restaurant = restaurantMapper.mapDTOtoEntity(restaurantDTO);
        return restaurantMapper.mapEntityToDTO(restaurantRepository.save(restaurant));
    }

    @Override
    public RestaurantDTO update(RestaurantDTO restaurantDTO) {
        RestaurantDTO initialRestaurant = findById(restaurantDTO.getId());
        Restaurant updatedRestaurant = restaurantMapper.mapDTOtoEntity(restaurantDTO);

        if (!initialRestaurant.getEmail().equals(updatedRestaurant.getEmail())) {
            Optional<Restaurant> restaurantOptional = restaurantRepository.findByEmail(updatedRestaurant.getEmail());
            Optional<Admin> adminOptional = adminRepository.findByEmail(updatedRestaurant.getEmail());
            Optional<Customer> customerOptional = customerRepository.findByEmail(updatedRestaurant.getEmail());
            Optional<RestaurantManager> managerOptional = managerRepository.findByEmail(updatedRestaurant.getEmail());

            if (restaurantOptional.isPresent() || adminOptional.isPresent() || customerOptional.isPresent() ||
                    managerOptional.isPresent()) {
                throw new NotUniqueException("This email is already used!");
            }
        }

        return restaurantMapper.mapEntityToDTO(restaurantRepository.save(updatedRestaurant));
    }

    @Override
    public RestaurantDTO findById(UUID id) {
        return restaurantRepository.findById(id)
                .map(this::mapToRestaurantDTO)
                .orElseThrow(() -> new NotFoundException("Restaurant with id " + id +
                        " could not be found!"));
    }

    @Override
    public RestaurantDTO findByName(String name) {
        return restaurantRepository.findByName(name)
                .map(this::mapToRestaurantDTO)
                .orElseThrow(() -> new NotFoundException("Restaurant with name " + name +
                        " could not be found!"));
    }

    @Override
    public RestaurantDTO findByEmail(String email) {
        return restaurantRepository.findByEmail(email)
                .map(this::mapToRestaurantDTO)
                .orElseThrow(() -> new NotFoundException("Restaurant with email " + email +
                        " could not be found!"));
    }

    @Override
    public List<RestaurantDTO> findAll() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        return restaurantMapper.mapEntityListToDTOList(restaurants);
    }

    @Override
    public List<RestaurantDTO> findAllByRestaurantManagerId(UUID id) {
        List<Restaurant> restaurants = restaurantRepository.findAllByRestaurantManagerId(id);
        return restaurantMapper.mapEntityListToDTOList(restaurants);
    }

    @Override
    public List<RestaurantDTO> findAllByCountryAndCity(String country, String city) {
        List<Restaurant> restaurants = restaurantRepository.findAllByCountryAndCity(country, city);
        return restaurantMapper.mapEntityListToDTOList(restaurants);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Restaurant restaurant = findRestaurant(id);
        RestaurantManager manager = restaurant.getRestaurantManager();

        if (manager != null) {
            manager.removeRestaurant(restaurant);
            restaurantManagerRepository.save(manager);
        }
        restaurantRepository.delete(restaurant);
    }

    @Override
    public void assignRestaurantManager(UUID restaurantId, UUID managerId) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
        Optional<RestaurantManager> optionalManager = restaurantManagerRepository.findById(managerId);

        if (optionalRestaurant.isEmpty()) {
            throw new NotFoundException("Restaurant with id " + restaurantId +
                    " could not be found!");
        }

        if (optionalManager.isEmpty()) {
            throw new NotFoundException("Restaurant manager with id " + managerId +
                    " could not be found!");
        }

        Restaurant restaurant = optionalRestaurant.get();
        RestaurantManager manager = optionalManager.get();

        manager.addRestaurant(restaurant);
        restaurantRepository.save(restaurant);
        restaurantManagerRepository.save(manager);
    }

    @Override
    public void uploadMenu(UUID restaurantId, MultipartFile menuFile) {
        Optional<Menu> optionalMenu = menuRepository.findById(restaurantId);
        optionalMenu.ifPresent(menuRepository::delete);

        RestaurantDTO restaurantDTO = findById(restaurantId);
        MenuDTO menuDTO = null;
        if (menuFile != null) {
            menuDTO = getMenuDTO(menuFile);
        }

        Restaurant restaurant = restaurantMapper.mapDTOtoEntity(restaurantDTO);
        Menu menu = menuMapper.mapDTOtoEntity(menuDTO);
        menu.setRestaurant(restaurant);
        restaurant.setMenu(menu);

        restaurantRepository.save(restaurant);
    }

    @Override
    public void addToFavourite(UUID restaurantId, UUID customerId) {
        findCustomer(customerId);
        findRestaurant(restaurantId);

        restaurantRepository.addToFavourites(restaurantId, customerId);
    }

    @Override
    public void removeFromFavourites(UUID restaurantId, UUID customerId) {
        findCustomer(customerId);
        findRestaurant(restaurantId);

        restaurantRepository.deleteFromFavourites(restaurantId, customerId);
    }

    @Override
    public List<RestaurantDTO> getFavouriteRestaurants(UUID customerId) {
        List<Restaurant> faveRestaurants = restaurantRepository.findFaveRestaurantsByCustomerId(customerId);
        for (Restaurant restaurant : faveRestaurants) {
            if (menuRepository.findById(restaurant.getId()).isEmpty()) {
                restaurant.setMenu(null);
            }
        }
        return restaurantMapper.mapEntityListToDTOList(faveRestaurants);
    }

    private RestaurantDTO mapToRestaurantDTO(Restaurant restaurant) {
        return restaurantMapper.mapEntityToDTO(restaurant);
    }

    private MenuDTO getMenuDTO(@NotNull MultipartFile menu) {
        try {
            String menuFileName = StringUtils.cleanPath(menu.getOriginalFilename());
            return new MenuDTO(menuFileName, menu.getBytes(), menu.getContentType());
        } catch (IOException e) {
            throw new CorruptedFileException("Menu file is invalid!");
        }
    }

    private Restaurant findRestaurant(UUID restaurantId) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
        if (optionalRestaurant.isEmpty()) {
            throw new NotFoundException("Restaurant with id " + restaurantId + " could not be found!");
        }
        return optionalRestaurant.get();
    }

    private Customer findCustomer(UUID customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);

        if (optionalCustomer.isEmpty()) {
            throw new NotFoundException("Customer with id " + customerId + " could not be found!");
        }
        return optionalCustomer.get();
    }
}
