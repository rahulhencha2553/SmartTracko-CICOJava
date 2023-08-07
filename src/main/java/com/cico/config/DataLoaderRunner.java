package com.cico.config;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.cico.model.Admin;
import com.cico.repository.AdminRepository;
import com.cico.util.Roles;

@Component
public class DataLoaderRunner implements CommandLineRunner{
	
	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public void run(String... args) throws Exception {
		
		List<Admin> findAll = adminRepository.findAll();
		if(findAll.isEmpty()) {
			Admin admin = new Admin();
			admin.setAdminEmail("cicoadmin@gmail.com");
			admin.setAdminName("CICO");
			admin.setProfilePic("admin.png");
			admin.setPassword(bCryptPasswordEncoder.encode("12345"));
			admin.setUuid(UUID.randomUUID().toString());
			admin.setRole(Roles.ADMIN.toString());
			adminRepository.save(admin);
		}
		
	}


}
