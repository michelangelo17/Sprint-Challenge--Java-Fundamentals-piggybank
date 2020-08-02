package com.lambdaschool.piggybank.controllers;

import com.lambdaschool.piggybank.models.Coin;
import com.lambdaschool.piggybank.repositories.CoinR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class CoinC {
    @Autowired
    CoinR coinR;

    @GetMapping(path = "/total", produces = {"application/json"})
    public ResponseEntity<?> logTotal() {
        List<Coin> coinList = new ArrayList<>();
        coinR.findAll().iterator().forEachRemaining(coinList::add);
        double total = coinList.stream().mapToDouble(c -> c.getValue() * c.getQuantity()).sum();
        coinList.forEach(System.out::println);
        System.out.println("The piggy bank holds " + total);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
