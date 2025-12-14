package financetracker.service;

import financetracker.model.Transacao;
import java.util.ArrayList;
import java.util.List;

public class TransacaoService {
    private List<Transacao> transacoes;

    public TransacaoService() {
        this.transacoes = new ArrayList<>();
    }

    public List<Transacao> getTransacoes() {
        return transacoes;
    }

    public void menu() {
        System.out.println("=== Finance Tracker ===");
        System.out.println("1 - Adicionar transação");
        System.out.println("2 - Listar transações");
        System.out.println("3 - Sair");
    }
}