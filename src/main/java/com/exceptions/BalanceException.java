package com.exceptions;

import java.io.IOException;

/**
 * @author Martin
 * Envoyée si une oppération mene vers un solde négatif du compte de l'association
 */
public class BalanceException extends IOException {
    public BalanceException(){};
    public BalanceException(String s){
        super(s);
    }
}
