package com.unipi.msc.carapi.Controller;

import com.unipi.msc.carapi.Interface.ICar;
import com.unipi.msc.carapi.Request.CarRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("car")
@RequiredArgsConstructor
public class CarController {
    private final ICar carService;
    @GetMapping
    public ResponseEntity getCars(){
        return carService.getCars();
    }

    @GetMapping("{id}")
    public ResponseEntity getCar(@PathVariable int id){
        return carService.getCar(id);
    }

    @PostMapping
    public ResponseEntity postCar(@RequestBody CarRequest request){
        return carService.createCar(request);
    }

    @PatchMapping("{id}")
    public ResponseEntity editCar(@PathVariable int id,@RequestBody CarRequest request){
        return carService.editCar(id, request);
    }

    @DeleteMapping("{id}")
    public ResponseEntity postCar(@PathVariable int id){
        return carService.deleteCar(id);
    }

}
