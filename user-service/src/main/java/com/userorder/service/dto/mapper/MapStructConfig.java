package com.userorder.service.dto.mapper;

import com.userorder.persistence.model.User;
import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

/**
 * Configuration for all MapStruct mappers
 */
@MapperConfig(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = {
                User.class
        }
)
public interface MapStructConfig {
}