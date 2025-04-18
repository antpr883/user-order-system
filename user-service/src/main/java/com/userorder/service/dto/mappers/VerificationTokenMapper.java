package com.userorder.service.dto.mappers;

import com.userorder.persistance.model.VerificationToken;
import com.userorder.service.dto.VerificationTokenDTO;
import org.mapstruct.*;

/**
 * Mapper for VerificationToken entities and DTOs
 */
@Mapper(config = CommonMapperConfig.class, uses = {AuditMapper.class})
public interface VerificationTokenMapper {

    /**
     * Maps a VerificationToken entity to a VerificationTokenDTO
     */
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "tokenType", expression = "java(token.getTokenType().name())")
    @Mapping(target = "audit", expression = "java(AuditMapper.mapAuditData(token))")
    VerificationTokenDTO toDto(VerificationToken token);

    /**
     * Maps a VerificationToken entity to a VerificationTokenDTO with dynamic attribute inclusion
     */
    @Mapping(target = "userId", source = "user.id",
            conditionExpression = "java(options.includesPath(\"user\"))")
    @Mapping(target = "tokenType", expression = "java(token.getTokenType().name())")
    @Mapping(target = "audit", expression = "java(options.includeAudit() ? AuditMapper.mapAuditData(token) : null)")
    VerificationTokenDTO toDtoWithOptions(VerificationToken token, @Context MappingOptions options);

    /**
     * Maps a VerificationTokenDTO to a VerificationToken entity
     */
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "tokenType", expression = "java(VerificationToken.TokenType.valueOf(dto.getTokenType()))")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    VerificationToken toEntity(VerificationTokenDTO dto);

    /**
     * Updates a VerificationToken entity from a VerificationTokenDTO
     */
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "tokenType", expression = "java(dto.getTokenType() != null ? VerificationToken.TokenType.valueOf(dto.getTokenType()) : token.getTokenType())")
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTokenFromDto(VerificationTokenDTO dto, @MappingTarget VerificationToken token);
}
