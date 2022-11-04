package com.team6.onandthefarm.repository.admin;

import com.team6.onandthefarm.entity.admin.Admin;
import com.team6.onandthefarm.entity.seller.Seller;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.authentication.jaas.JaasPasswordCallbackHandler;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends CrudRepository<Admin, Long>  {

    Optional<Admin> findAdminByAdminEmailAndAdminPassword(String adminEmail, String adminPassword);
}
