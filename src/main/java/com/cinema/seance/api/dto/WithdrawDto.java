package com.cinema.seance.api.dto;

import lombok.Data;

@Data
public class WithdrawDto {
    double toWithdraw;

    public WithdrawDto(double price) {
        toWithdraw = price;
    }
}
