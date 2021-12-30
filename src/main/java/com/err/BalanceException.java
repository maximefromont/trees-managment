package com.err;

import java.io.IOException;

public class BalanceException extends IOException {
    public BalanceException(){};
    public BalanceException(String s){
        super(s);
    }
}
