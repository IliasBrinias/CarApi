package com.unipi.msc.carapi.Interface;

import com.unipi.msc.carapi.Request.CarRequest;
import org.springframework.http.ResponseEntity;

public interface ICar {
    ResponseEntity getCars();
    ResponseEntity getCar(int id);
    ResponseEntity createCar(CarRequest request);
    ResponseEntity editCar(int id, CarRequest request);


    ResponseEntity deleteCar(int id);
}
