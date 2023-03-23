package com.nagarro.af.bookingtablesystem.mapper.impl;

import com.nagarro.af.bookingtablesystem.dto.RestaurantDTO;
import com.nagarro.af.bookingtablesystem.mapper.EntityDTOMapper;
import com.nagarro.af.bookingtablesystem.model.Restaurant;
import com.nagarro.af.bookingtablesystem.model.RestaurantManager;
import com.nagarro.af.bookingtablesystem.repository.RestaurantManagerRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantMapper implements EntityDTOMapper<Restaurant, RestaurantDTO> {

    private final ModelMapper modelMapper;

    private final RestaurantManagerRepository managerRepository;

    public RestaurantMapper(ModelMapper modelMapper, RestaurantManagerRepository managerRepository) {
        this.modelMapper = modelMapper;
        this.managerRepository = managerRepository;
    }

    @Override
    public RestaurantDTO mapEntityToDTO(Restaurant restaurant) {
        Converter<RestaurantManager, UUID> restaurantManagerUUIDConverter =
                ctx -> ctx.getSource() == null ? null : ctx.getSource().getId();

        modelMapper.addConverter(restaurantManagerUUIDConverter);

        modelMapper.addMappings(new PropertyMap<RestaurantDTO, Restaurant>() {
            @Override
            protected void configure() {
                skip(destination.getDateCapacityAvailability());
            }
        });

        return modelMapper.map(restaurant, RestaurantDTO.class);
    }

    @Override
    public Restaurant mapDTOtoEntity(RestaurantDTO restaurantDTO) {
        Converter<UUID, RestaurantManager> UUIDRestaurantConverter =
                ctx -> ctx.getSource() == null ? null :
                        modelMapper.map(managerRepository.findById(ctx.getSource()), RestaurantManager.class);

        modelMapper.createTypeMap(RestaurantDTO.class, Restaurant.class)
                .addMappings(map -> map
                        .using(UUIDRestaurantConverter)
                        .map(
                                RestaurantDTO::getRestaurantManagerId,
                                Restaurant::setRestaurantManager
                        )
                );

        return modelMapper.map(restaurantDTO, Restaurant.class);
    }

    @Override
    public List<RestaurantDTO> mapEntityListToDTOList(List<Restaurant> restaurants) {
        return restaurants
                .stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Restaurant> mapDTOListToEntityList(List<RestaurantDTO> restaurantDTOS) {
        return restaurantDTOS
                .stream()
                .map(this::mapDTOtoEntity)
                .collect(Collectors.toList());
    }
}
