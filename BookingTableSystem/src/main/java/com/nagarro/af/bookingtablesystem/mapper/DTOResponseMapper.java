package com.nagarro.af.bookingtablesystem.mapper;

import com.nagarro.af.bookingtablesystem.controller.response.UserResponse;
import com.nagarro.af.bookingtablesystem.dto.UserDTO;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class DTOResponseMapper<D extends UserDTO, R extends UserResponse> {

    protected final ModelMapper modelMapper;

    public DTOResponseMapper() {
        this.modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Converter<UUID, String> uuidStringConverter =
                ctx -> ctx == null ? null : ctx.getSource().toString();

        TypeMap<UserDTO, UserResponse> bookingDTOTypeMap = modelMapper.createTypeMap(UserDTO.class, UserResponse.class);
        bookingDTOTypeMap.addMappings(mapper -> {
            mapper.using(uuidStringConverter)
                    .map(
                            UserDTO::getId,
                            UserResponse::setId
                    );
        });


        modelMapper.addMappings(new PropertyMap<R, D>() {
            @Override
            protected void configure() {
                skip(destination.getPassword());
            }
        });
    }

    public abstract R mapDTOToResponse(D dto);

    public List<R> mapDTOListToResponseList(List<D> dtoList) {
        return dtoList
                .stream()
                .map(this::mapDTOToResponse)
                .collect(Collectors.toList());
    }
}
