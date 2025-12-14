package financetracker;

import financetracker.service.TransacaoService;

public class Main {
    public static void main(String[] args) {
        new TransacaoService().menu();
    }
}