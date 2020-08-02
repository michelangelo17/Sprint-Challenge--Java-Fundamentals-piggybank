package com.lambdaschool.piggybank.controllers;

import com.lambdaschool.piggybank.models.Coin;
import com.lambdaschool.piggybank.repositories.CoinR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CoinC {
    @Autowired
    CoinR coinR;

    private static double round(double value) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @GetMapping(path = "/total", produces = {"application/json"})
    public ResponseEntity<?> logTotal() {
        List<Coin> coinList = new ArrayList<>();
        coinR.findAll().iterator().forEachRemaining(coinList::add);
        double total = coinList.stream().mapToDouble(c -> c.getValue() * c.getQuantity()).sum();
        coinList.forEach(System.out::println);
        System.out.println("The piggy bank holds " + total);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping(path = "/money/{amount}", produces = {"application/json"})
    public ResponseEntity<?> removeAmount(@PathVariable double amount) {
        List<Coin> coinList = new ArrayList<>();
        coinR.findAll().iterator().forEachRemaining(coinList::add);
        for (Coin c: coinList
             ) {
            if (amount > 0) {
                if (c.getValue() * c.getQuantity() <= amount) {
                    amount = round(amount - c.getValue() * c.getQuantity());
                    c.setQuantity(0);
                }
                else {
                    while (round(amount - c.getValue()) >= 0) {
                        c.setQuantity(c.getQuantity() - 1);
                        amount = round(amount - c.getValue());
                    }
                }
            }
        }
        if (amount > 0) {
            return new ResponseEntity<>("Money not available.", HttpStatus.OK);
        }
        for (Coin c: coinList
             ) {
            if (c.getQuantity() > 0) {
                System.out.println(c);
            }
        }
        double total = coinList.stream().mapToDouble(c -> c.getValue() * c.getQuantity()).sum();
        System.out.println("The piggy bank holds " + total);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
