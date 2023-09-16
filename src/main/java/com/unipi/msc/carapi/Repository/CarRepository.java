package com.unipi.msc.carapi.Repository;

import com.unipi.msc.carapi.Interface.ICar;
import com.unipi.msc.carapi.Model.Car;
import com.unipi.msc.carapi.Request.CarRequest;
import com.unipi.msc.carapi.Response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.unipi.msc.carapi.Response.ErrorMessages.CAR_NOT_FOUND;

@Component
public class CarRepository implements ICar {
    private Connection con;

    private void establishConnection(){
        try {
            con = DriverManager.getConnection("jdbc:mysql://host.docker.internal:3306/carapi","root","root");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity getCars() {
        List<Car> carList = new ArrayList<>();
        Statement stmt;
        try {
            establishConnection();
            stmt = con.createStatement();
            ResultSet rs=stmt.executeQuery("SELECT * FROM car");
            while(rs.next()) {
                carList.add(buildCar(rs));
            }
            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok(carList);
    }

    @Override
    public ResponseEntity getCar(int id) {
        Car car = null;
        Statement stmt;
        try {
            establishConnection();
            stmt = con.createStatement();
            ResultSet rs=stmt.executeQuery("SELECT * FROM car WHERE id = "+id);
            if (rs.next()){
                car = buildCar(rs);
            }
            con.close();
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(ErrorResponse.builder().errorMessage(e.getMessage()));
        }
        if (car == null){
            return ResponseEntity.badRequest().body(ErrorResponse.builder().errorMessage(CAR_NOT_FOUND).build());
        }
        return ResponseEntity.ok(car);
    }

    @Override
    public ResponseEntity createCar(CarRequest request) {
        Statement stmt;
        try {
            establishConnection();
            stmt = con.createStatement();
            String query = """
                INSERT INTO car (model, price) VALUES ( "{MODEL}" , {PRICE} )
            """;
            query = query.replace("{MODEL}", request.getModel())
                         .replace("{PRICE}", String.valueOf(request.getPrice()));
            stmt.execute(query);
            con.close();
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(ErrorResponse.builder().errorMessage(e.getMessage()).build());
        }
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity editCar(int id, CarRequest request) {
        boolean rs;
        Statement stmt;
        try {
            establishConnection();
            stmt = con.createStatement();
            String query = """
                UPDATE car SET model = "{MODEL}", price = {PRICE} WHERE id = {ID}
            """;
            query = query.replace("{ID}", String.valueOf(id))
                         .replace("{MODEL}", request.getModel())
                         .replace("{PRICE}", String.valueOf(request.getPrice()));
            rs=stmt.execute(query);
            con.close();
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(ErrorResponse.builder().errorMessage(e.getMessage()).build());
        }
        if (!rs){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity deleteCar(int id) {
        Statement stmt;
        try {
            establishConnection();
            stmt = con.createStatement();
            String query = """
                DELETE FROM CAR WHERE ID = {ID}
            """;
            query = query.replace("{ID}", String.valueOf(id));
            stmt.execute(query);
            con.close();
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(ErrorResponse.builder().errorMessage(e.getMessage()).build());
        }
        return ResponseEntity.ok().build();
    }
    private Car buildCar(ResultSet rs) throws SQLException {
        return Car.builder()
                .id(rs.getInt(1))
                .model(rs.getString(2))
                .price(rs.getInt(3))
                .build();
    }
}
