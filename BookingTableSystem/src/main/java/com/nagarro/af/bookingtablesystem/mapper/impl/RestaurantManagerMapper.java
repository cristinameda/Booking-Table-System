package com.nagarro.af.bookingtablesystem.mapper.impl;

import com.nagarro.af.bookingtablesystem.dto.RestaurantManagerDTO;
import com.nagarro.af.bookingtablesystem.exception.NotFoundException;
import com.nagarro.af.bookingtablesystem.mapper.EntityDTOMapper;
import com.nagarro.af.bookingtablesystem.model.Restaurant;
import com.nagarro.af.bookingtablesystem.model.RestaurantManager;
import com.nagarro.af.bookingtablesystem.repository.RestaurantRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantManagerMapper implements EntityDTOMapper<RestaurantManager, RestaurantManagerDTO> {

    private final ModelMapper modelMapper;

    private final RestaurantRepository restaurantRepository;

    public RestaurantManagerMapper(ModelMapper modelMapper, RestaurantRepository restaurantRepository) {
        this.modelMapper = modelMapper;
        this.restaurantRepository = restaurantRepository;
    }

    public RestaurantManagerDTO mapEntityToDTO(RestaurantManager restaurantManager) {
        Converter<List<Restaurant>, List<UUID>> restaurantsListToIdsListConverter =
                ctx -> ctx.getSource()
                        .stream()
                        .map(Restaurant::getId)
                        .collect(Collectors.toList());

        modelMapper.createTypeMap(RestaurantManager.class, RestaurantManagerDTO.class)
                .addMappings(map -> map
                        .using(restaurantsListToIdsListConverter)
                        .map(
                                RestaurantManager::getRestaurants,
                                RestaurantManagerDTO::setRestaurantIds
                        )
                );
        return modelMapper.map(restaurantManager, RestaurantManagerDTO.class);
    }

    @Override
    public RestaurantManager mapDTOtoEntity(RestaurantManagerDTO restaurantManagerDTO) {
        Converter<List<UUID>, List<Restaurant>> idListToRestaurantListConverter =
                ctx -> ctx.getSource()
                        .stream()
                        .map(id -> {
                            Optional<Restaurant> restaurant = restaurantRepository.findById(id);
                            if (restaurant.isPresent()) {
                                return restaurant.get();
                            } else {
                                throw new NotFoundException("RestaurantManagerMapper: could not found restaurant with id " + id);
                            }
                        })
                        .collect(Collectors.toList());

        modelMapper.createTypeMap(RestaurantManagerDTO.class, RestaurantManager.class)
                .addMappings(map -> map
                        .using(idListToRestaurantListConverter)
                        .map(
                                RestaurantManagerDTO::getRestaurantIds,
                                RestaurantManager::setRestaurants
                        )
                );

        return modelMapper.map(restaurantManagerDTO, RestaurantManager.class);
    }

    @Override
    public List<RestaurantManagerDTO> mapEntityListToDTOList(List<RestaurantManager> restaurantManagers) {
        return restaurantManagers
                .stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RestaurantManager> mapDTOListToEntityList(List<RestaurantManagerDTO> restaurantManagerDTOS) {
        return restaurantManagerDTOS
                .stream()
                .map(this::mapDTOtoEntity)
                .collect(Collectors.toList());
    }
}
