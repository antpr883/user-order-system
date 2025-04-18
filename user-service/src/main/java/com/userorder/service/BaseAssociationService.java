package com.userorder.service;


import java.util.List;

public interface BaseAssociationService<T> {

    List<T> findByUserId(Long userId, boolean withAudit);
}
