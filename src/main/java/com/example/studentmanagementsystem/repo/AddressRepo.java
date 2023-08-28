package com.example.studentmanagementsystem.repo;

import com.example.studentmanagementsystem.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository<Address,Integer> {
}
