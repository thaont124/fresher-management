package com.gr.freshermanagement.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortTokenReceipt {
    private Long amount;
    private  int idPassbook;
    private int idOtp;
    private boolean isAll;
    public static ShortTokenReceipt fromLinkedHashMap(LinkedHashMap<String, Object> map) {
        ShortTokenReceipt shortTokenReceipt = new ShortTokenReceipt();
        shortTokenReceipt.setAmount( ((Integer) map.get("amount")).longValue());
        shortTokenReceipt.setIdPassbook((Integer) map.get("idPassbook"));
        shortTokenReceipt.setIdOtp((Integer) map.get("idOtp"));
        shortTokenReceipt.setAll((Boolean) map.get("isAll"));
        return shortTokenReceipt;
    }
    public LinkedHashMap<String, Object> toLinkedHashMap() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("amount", this.amount);
        map.put("idPassbook", this.idPassbook);
        map.put("idOtp", this.idOtp);
        map.put("isAll", this.isAll);
        return map;
    }
}
