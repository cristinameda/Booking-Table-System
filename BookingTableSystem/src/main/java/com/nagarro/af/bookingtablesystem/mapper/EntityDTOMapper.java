package com.nagarro.af.bookingtablesystem.mapper;

import java.util.List;

public interface EntityDTOMapper<E, D> {
    D mapEntityToDTO(E entity);

    E mapDTOtoEntity(D dto);

    List<D> mapEntityListToDTOList(List<E> entityList);

    List<E> mapDTOListToEntityList(List<D> dtoList);
}
