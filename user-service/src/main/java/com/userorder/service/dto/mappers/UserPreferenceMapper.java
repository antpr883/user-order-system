package com.userorder.service.dto.mappers;

import com.userorder.persistance.model.UserPreference;
import com.userorder.service.dto.UserPreferenceDTO;
import org.mapstruct.*;

/**
 * UserPreference mapper for converting between UserPreference entities and DTOs.
 */
@Mapper(config = CommonMapperConfig.class, uses = {AuditMapper.class})
public interface UserPreferenceMapper extends GenericMapper<UserPreference, UserPreferenceDTO> {

    /**
     * Maps a UserPreference entity to a UserPreferenceDTO
     */
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "audit", expression = "java(AuditMapper.mapAuditData(userPreference))")
    UserPreferenceDTO toDto(UserPreference userPreference);

    /**
     * Maps a UserPreference entity to a UserPreferenceDTO with dynamic attribute inclusion
     */
    @Mapping(target = "userId", source = "user.id",
            conditionExpression = "java(options.includesPath(\"user\"))")
    @Mapping(target = "audit", expression = "java(options.includeAudit() ? AuditMapper.mapAuditData(userPreference) : null)")
    UserPreferenceDTO toDtoWithOptions(UserPreference userPreference, @Context MappingOptions options);

    /**
     * Maps a UserPreferenceDTO to a UserPreference entity
     */
    @Mapping(target = "user", ignore = true)
    UserPreference toEntity(UserPreferenceDTO dto);

}