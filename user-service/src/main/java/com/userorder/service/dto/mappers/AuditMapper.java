package com.userorder.service.dto.mappers;

import com.userorder.persistance.model.common.PersistenceModel;
import com.userorder.service.dto.base.AuditDTO;
import org.mapstruct.Named;

public class AuditMapper {

    @Named("mapAuditData")
    public static AuditDTO mapAuditData(PersistenceModel entity) {
        if (entity == null) {
            return null;
        }

        AuditDTO auditDto = new AuditDTO();
        auditDto.setCreatedBy(entity.getCreatedBy());
        auditDto.setModifiedBy(entity.getModifiedBy());

        if (entity.getCreatedDate() != null) {
            auditDto.setCreatedDate(entity.getCreatedDate().toString());
        }

        if (entity.getModifiedDate() != null) {
            auditDto.setModifiedDate(entity.getModifiedDate().toString());
        }

        return auditDto;
    }
}
