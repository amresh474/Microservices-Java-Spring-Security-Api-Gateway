package com.lcwa.hotel.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwa.hotel.entites.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, String> {

}
