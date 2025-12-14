package financetracker;

import financetracker.model.Transacao;
import financetracker.service.TransacaoService;
import javax.swing.*;
import java.awt.*;

public class FinanceTrackerGUI extends JFrame {

    private TransacaoService service;
    private JTextField campoDescricao;
    private JTextField campoValor;
    private JComboBox<String> comboTipo;
    private JTextArea areaTransacoes;
    private JLabel labelSaldo;

    public FinanceTrackerGUI() {
        service = new TransacaoService();

        setTitle("💰 Finance Tracker");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        criarInterface();
        setVisible(true);
    }

    private void criarInterface() {
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("💰 FINANCE TRACKER", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(new Color(41, 128, 185));
        painelPrincipal.add(titulo, BorderLayout.NORTH);

        painelPrincipal.add(criarPainelFormulario(), BorderLayout.CENTER);
        painelPrincipal.add(criarPainelLista(), BorderLayout.SOUTH);

        add(painelPrincipal);
    }

    private JPanel criarPainelFormulario() {
        JPanel painel = new JPanel();
        painel.setLayout(new GridBagLayout());
        painel.setBorder(BorderFactory.createTitledBorder("Nova Transação"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        campoDescricao = new JTextField(20);
        painel.add(campoDescricao, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        painel.add(new JLabel("Valor (R$):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        campoValor = new JTextField(20);
        painel.add(campoValor, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        painel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboTipo = new JComboBox<>(new String[]{"RECEITA", "DESPESA"});
        painel.add(comboTipo, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JPanel painelBotoes = new JPanel(new FlowLayout());

        JButton btnAdicionar = new JButton("✅ Adicionar");
        btnAdicionar.setBackground(new Color(46, 204, 113));
        btnAdicionar.setForeground(Color.WHITE);
        btnAdicionar.setFocusPainted(false);
        btnAdicionar.addActionListener(e -> adicionarTransacao());

        JButton btnListar = new JButton("📋 Listar");
        btnListar.addActionListener(e -> listarTransacoes());

        JButton btnAtualizar = new JButton("🔄 Atualizar");
        btnAtualizar.addActionListener(e -> atualizarSaldo());

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnListar);
        painelBotoes.add(btnAtualizar);
        painel.add(painelBotoes, gbc);

        gbc.gridy = 4;
        labelSaldo = new JLabel("Saldo: R$ 0,00", JLabel.CENTER);
        labelSaldo.setFont(new Font("Arial", Font.BOLD, 18));
        labelSaldo.setForeground(new Color(46, 204, 113));
        painel.add(labelSaldo, gbc);

        return painel;
    }

    private JPanel criarPainelLista() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(BorderFactory.createTitledBorder("Transações"));

        areaTransacoes = new JTextArea(8, 40);
        areaTransacoes.setEditable(false);
        areaTransacoes.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scroll = new JScrollPane(areaTransacoes);
        painel.add(scroll, BorderLayout.CENTER);

        return painel;
    }

    private void adicionarTransacao() {
        try {
            String descricao = campoDescricao.getText().trim();
            String valorTexto = campoValor.getText().trim();
            String tipo = (String) comboTipo.getSelectedItem();

            if (descricao.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha a descrição!", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (valorTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha o valor!", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double valor = Double.parseDouble(valorTexto);

            if (valor <= 0) {
                JOptionPane.showMessageDialog(this, "Valor deve ser maior que zero!", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Transacao transacao = new Transacao(descricao, valor, tipo);
            service.getTransacoes().add(transacao);

            campoDescricao.setText("");
            campoValor.setText("");

            atualizarSaldo();

            JOptionPane.showMessageDialog(this, "✅ Transação adicionada!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "❌ Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarTransacoes() {
        areaTransacoes.setText("");

        if (service.getTransacoes().isEmpty()) {
            areaTransacoes.setText("Nenhuma transação cadastrada.");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-20s %-10s %-10s%n", "DESCRIÇÃO", "TIPO", "VALOR"));
            sb.append("─".repeat(50)).append("\n");

            for (Transacao t : service.getTransacoes()) {
                String simbolo = t.getTipo().equals("RECEITA") ? "+" : "-";
                sb.append(String.format("%-20s %-10s %s R$ %.2f%n",
                        t.getDescricao(),
                        t.getTipo(),
                        simbolo,
                        t.getValor()));
            }

            areaTransacoes.setText(sb.toString());
        }
    }

    private void atualizarSaldo() {
        double receitas = service.getTransacoes().stream()
                .filter(t -> t.getTipo().equals("RECEITA"))
                .mapToDouble(Transacao::getValor)
                .sum();

        double despesas = service.getTransacoes().stream()
                .filter(t -> t.getTipo().equals("DESPESA"))
                .mapToDouble(Transacao::getValor)
                .sum();

        double saldo = receitas - despesas;

        labelSaldo.setText(String.format("Saldo: R$ %.2f", saldo));
        labelSaldo.setForeground(saldo >= 0 ? new Color(46, 204, 113) : new Color(231, 76, 60));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FinanceTrackerGUI());
    }
}