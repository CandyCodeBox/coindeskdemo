package com.coindesk.demo.service;

import com.coindesk.demo.model.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoinService extends JpaRepository<Coin, Integer>{

    List<Coin> findByCode(String code);
}
