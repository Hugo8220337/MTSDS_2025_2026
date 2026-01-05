package com.domus.schedules.utils;

import java.util.List;
import java.lang.reflect.ParameterizedType;
import java.beans.PropertyDescriptor;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class BaseService<T, T_DTO, ID, TRepository extends JpaRepository<T, ID>>
        implements IBaseService<T, T_DTO, ID> {

    protected TRepository repository;

    private ModelMapper mapper;
    protected final String entityName;
    protected final String cacheName;

    public BaseService(ModelMapper mapper) {
        this.mapper = mapper;
        // Extract entity class name for cache naming
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        Class<?> entityClass = (Class<?>) genericSuperclass.getActualTypeArguments()[0];
        this.entityName = entityClass.getSimpleName();
        this.cacheName = entityClass.getSimpleName().toUpperCase() + "_CACHE";
    }
    
    @Override
    public List<T> findAll() {
        return repository.findAll();
    }
    
    @Override
    public T findById(ID id) {
        return repository.findById(id).orElse(null);
    }
    
    @Override
    public T save(T_DTO entity) {
        T mappedEntity = mapper.map(entity, (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0]);
        return repository.save(mappedEntity);
    }
    
    @Override
    public void delete(ID id) {
        repository.deleteById(id);
    }
    
    @Override
    public T update(ID id, T_DTO entity) {
        T mappedEntity = mapper.map(entity, (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0]);
        return repository.findById(id)
                .map(existingEntity -> {
                    // Copy non-null properties from source to target
                    BeanUtils.copyProperties(mappedEntity, existingEntity, getNullPropertyNames(mappedEntity));
                    return repository.save(existingEntity);
                })
                .orElse(null);
    }
    
    // @Override
    // public Page<T> findAllWithFilters(int page, int size, String[] sort, Map<String, String> filters) {
    //     Specification<T> spec = createSpecification(filters);
    //     Pageable pageable = createPageable(page, size, sort);
    //     return repository.findAll(spec, pageable);
    // }
    
    // Helper method to get null property names for selective update
    /**
     * Get names of properties with null values in the source object.
     * @param source
     * @return array of property names with null values
     */
    private String[] getNullPropertyNames(T source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        
        Set<String> emptyNames = new HashSet<>();
        for(PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        
        return emptyNames.toArray(new String[0]);
    }
    
    /**
     * Create JPA Specification based on provided filters.
     * @param filters
     * @return Specification<T> for filtering entities
     */
    protected Specification<T> createSpecification(Map<String, String> filters) {
        // Default implementation - override in specific services for entity-specific filtering
        return (root, query, criteriaBuilder) -> null; // No filtering by default
    }
    
    // protected Pageable createPageable(int page, int size, String[] sort) {
    //     // Default sorting by ID
    //     String sortField = "id";
    //     Sort.Direction direction = Sort.Direction.ASC;
        
    //     // Process sort parameters if provided
    //     if (sort != null && sort.length > 0) {
    //         String[] sortParams = sort[0].split(",");
    //         if (sortParams.length > 0 && StringUtils.hasText(sortParams[0])) {
    //             sortField = sortParams[0];
    //         }
            
    //         if (sortParams.length > 1) {
    //             direction = "desc".equalsIgnoreCase(sortParams[1]) ? 
    //                 Sort.Direction.DESC : Sort.Direction.ASC;
    //         }
    //     }
        
    //     return PageRequest.of(page, size, direction, sortField);
    // }
}