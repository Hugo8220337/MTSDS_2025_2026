package com.domus.schedules.utils;

import java.util.List;

public interface IBaseService<T, T_DTO, ID> {
    List<T> findAll();

    T findById(ID id);

    T save(T_DTO entity);

    void delete(ID id);

    T update(ID id, T_DTO entity);

}