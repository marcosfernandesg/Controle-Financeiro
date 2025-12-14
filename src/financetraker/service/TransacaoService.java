package financetracker.service;

import financetracker.model.Transacao;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TransacaoService {
    private List<Transacao> transacoes;
    private Scanner scanner;

    public TransacaoService() {
        this.transacoes = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    public void menu() {
        int opcao = 0;

        do {
            exibirMenu();
            try {
                opcao = Integer.parseInt(scanner.nextLine());

                switch(opcao) {
                    case 1:
                        adicionarTransacao();
                        break;
                    case 2:
                        listarTransacoes();
                        break;
                    case 3:
                        exibirSaldo();
                        break;
                    case 4:
                        excluirTransacao();
                        break;
                    case 5:
                        System.out.println("\n✓ Encerrando o Finance Tracker. Até logo!");
                        break;
                    default:
                        System.out.println("\n✗ Opção inválida! Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\n✗ Por favor, digite apenas números!");
            }

            if (opcao != 5) {
                System.out.println("\nPressione ENTER para continuar...");
                scanner.nextLine();
            }

        } while(opcao != 5);

        scanner.close();
    }

    private void exibirMenu() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║     💰 FINANCE TRACKER 💰         ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println("  1 - Adicionar transação");
        System.out.println("  2 - Listar transações");
        System.out.println("  3 - Ver saldo");
        System.out.println("  4 - Excluir transação");
        System.out.println("  5 - Sair");
        System.out.println("════════════════════════════════════");
        System.out.print("→ Escolha uma opção: ");
    }

    private void adicionarTransacao() {
        System.out.println("\n┌─ NOVA TRANSAÇÃO ─────────────────┐");

        System.out.print("│ Descrição: ");
        String descricao = scanner.nextLine();

        System.out.print("│ Valor: R$ ");
        double valor = 0;
        try {
            valor = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("│ ✗ Valor inválido!");
            return;
        }

        System.out.print("│ Tipo (1-Receita / 2-Despesa): ");
        int tipo = 0;
        try {
            tipo = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("│ ✗ Tipo inválido!");
            return;
        }

        String tipoStr = tipo == 1 ? "RECEITA" : "DESPESA";

        Transacao transacao = new Transacao(descricao, valor, tipoStr);
        transacoes.add(transacao);

        System.out.println("│ ✓ Transação adicionada com sucesso!");
        System.out.println("└──────────────────────────────────┘");
    }

    private void listarTransacoes() {
        System.out.println("\n┌─ LISTA DE TRANSAÇÕES ────────────┐");

        if (transacoes.isEmpty()) {
            System.out.println("│ Nenhuma transação cadastrada.");
        } else {
            for (int i = 0; i < transacoes.size(); i++) {
                Transacao t = transacoes.get(i);
                String simbolo = t.getTipo().equals("RECEITA") ? "+" : "-";
                System.out.printf("│ %d. %s | %s R$ %.2f%n",
                        (i + 1), t.getDescricao(), simbolo, t.getValor());
            }
        }

        System.out.println("└──────────────────────────────────┘");
    }

    private void exibirSaldo() {
        double saldo = calcularSaldo();
        double receitas = calcularReceitas();
        double despesas = calcularDespesas();

        System.out.println("\n┌─ RESUMO FINANCEIRO ──────────────┐");
        System.out.printf("│ Receitas:  + R$ %.2f%n", receitas);
        System.out.printf("│ Despesas:  - R$ %.2f%n", despesas);
        System.out.println("│ ─────────────────────────────────");
        System.out.printf("│ Saldo:     %s R$ %.2f%n",
                saldo >= 0 ? "💚" : "❤️", Math.abs(saldo));
        System.out.println("└──────────────────────────────────┘");
    }

    private void excluirTransacao() {
        listarTransacoes();

        if (transacoes.isEmpty()) {
            return;
        }

        System.out.print("\n→ Digite o número da transação para excluir: ");
        try {
            int indice = Integer.parseInt(scanner.nextLine()) - 1;

            if (indice >= 0 && indice < transacoes.size()) {
                transacoes.remove(indice);
                System.out.println("✓ Transação excluída com sucesso!");
            } else {
                System.out.println("✗ Transação não encontrada!");
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Número inválido!");
        }
    }

    private double calcularSaldo() {
        return calcularReceitas() - calcularDespesas();
    }

    private double calcularReceitas() {
        return transacoes.stream()
                .filter(t -> t.getTipo().equals("RECEITA"))
                .mapToDouble(Transacao::getValor)
                .sum();
    }

    private double calcularDespesas() {
        return transacoes.stream()
                .filter(t -> t.getTipo().equals("DESPESA"))
                .mapToDouble(Transacao::getValor)
                .sum();
    }
}