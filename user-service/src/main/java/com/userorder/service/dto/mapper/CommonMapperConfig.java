package com.userorder.service.dto.mapper;


import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

/**
 * Common configuration for all MapStruct mappers
 */
@MapperConfig(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CommonMapperConfig {
}