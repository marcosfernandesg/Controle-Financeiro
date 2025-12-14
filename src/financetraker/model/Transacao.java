package financetracker.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transacao {
    private String descricao;
    private double valor;
    private String tipo; // RECEITA ou DESPESA
    private LocalDateTime data;

    public Transacao(String descricao, double valor, String tipo) {
        this.descricao = descricao;
        this.valor = valor;
        this.tipo = tipo.toUpperCase();
        this.data = LocalDateTime.now();
    }

    // Getters
    public String getDescricao() {
        return descricao;
    }

    public double getValor() {
        return valor;
    }

    public String getTipo() {
        return tipo;
    }

    public LocalDateTime getData() {
        return data;
    }

    public String getDataFormatada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return data.format(formatter);
    }

    // Setters
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo.toUpperCase();
    }

    @Override
    public String toString() {
        return String.format("%s | %s: R$ %.2f - %s",
                getDataFormatada(), tipo, valor, descricao);
    }
}