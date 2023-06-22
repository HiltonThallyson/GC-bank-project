package br.ufrn.gc.bank.gcbank.services;

import br.ufrn.gc.bank.gcbank.models.Banco;
import br.ufrn.gc.bank.gcbank.models.Conta;
import br.ufrn.gc.bank.gcbank.models.ContaBonus;
import br.ufrn.gc.bank.gcbank.models.ContaPoupanca;
import br.ufrn.gc.bank.gcbank.repositories.BancoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Karine Piacentini (karine.piacentini.072@ufrn.edu.br)
 * @since 21/06/23
 */
class BancoServicesTest {

    private BancoServices bancoServices;

    @BeforeEach
    void setUp() {
        Banco banco = new Banco();
        BancoRepository bancoRepository = new BancoRepository(banco);
        bancoServices = new BancoServices(bancoRepository);
        bancoServices.criarConta(111, 1, 100);
        bancoServices.criarConta(222, 2, 100);
        bancoServices.criarConta(333, 3, 100);
    }


    @Test
    void cadastrarContaSimples() {
        // Ao se criar uma conta simples, deve-se informar o número da conta, o saldo inicial e o tipo 1
        int validation = bancoServices.criarConta(1, 1, 100);
        Optional<Conta> conta1 = bancoServices.getConta(1);

        assertEquals(0, validation);
        assertTrue(conta1.isPresent());
        assertEquals(100, conta1.get().getSaldo());
    }

    @Test
    void testaLimiteNegativoContaSimples_noLimite() {
        // Conta Simples e Conta Bônus devem possuir um limite máximo de saldo negativo de R$ -1.000,00
        bancoServices.criarConta(1, 1, 0);
        Optional<Conta> conta1 = bancoServices.getConta(1);
        assertTrue(conta1.isPresent());

        int validation = bancoServices.debitar(1, 1000);
        assertEquals(0, validation);
        assertEquals(-1000, conta1.get().getSaldo());

    }

    @Test
    void testaLimiteNegativoContaSimples_ultrapassaLimite() {
        // Conta Simples e Conta Bônus devem possuir um limite máximo de saldo negativo de R$ -1.000,00
        bancoServices.criarConta(1, 1, 0);
        Optional<Conta> conta1 = bancoServices.getConta(1);
        assertTrue(conta1.isPresent());

        int validation = bancoServices.debitar(1, 1000.1);
        assertEquals(-4, validation);
        assertEquals(0, conta1.get().getSaldo());

    }

    @Test
    void cadastrarContaPoupanca() {
        // Ao se criar uma conta Poupança deve-se informar o número da conta, o saldo inicial e o tipo 2

        int validation = bancoServices.criarConta(1, 2, 100);
        Optional<Conta> conta1 = bancoServices.getConta(1);

        assertEquals(0, validation);
        assertTrue(conta1.isPresent());
        assertTrue(conta1.get() instanceof ContaPoupanca);
        assertEquals(100, conta1.get().getSaldo());

    }

    @Test
    void cadastrarContaBonus() {
        // Ao se criar uma conta bônus, deve-se informar o número da conta e o tipo 3 e a pontuação inicial deve ser de 10 pontos

        int validation = bancoServices.criarConta(1, 3, 100);
        Optional<Conta> conta1 = bancoServices.getConta(1);

        assertEquals(0, validation);
        assertTrue(conta1.isPresent());
        assertTrue(conta1.get() instanceof ContaBonus);
        assertEquals(0, conta1.get().getSaldo());
        assertEquals(10, ((ContaBonus) conta1.get()).getPontuacao());
    }

    @Test
    void testaLimiteNegativoContaBonus_noLimite() {
        // Conta Simples e Conta Bônus devem possuir um limite máximo de saldo negativo de R$ -1.000,00
        bancoServices.criarConta(1, 3, 100);
        Optional<Conta> conta1 = bancoServices.getConta(1);
        assertTrue(conta1.isPresent());

        int validation = bancoServices.debitar(1, 1000);
        assertEquals(0, validation);
        assertEquals(-1000, conta1.get().getSaldo());
    }

    @Test
    void testaLimiteNegativoContaBonus_ultrapassaLimite() {
        // Conta Simples e Conta Bônus devem possuir um limite máximo de saldo negativo de R$ -1.000,00
        bancoServices.criarConta(1, 1, 0);
        Optional<Conta> conta1 = bancoServices.getConta(1);
        assertTrue(conta1.isPresent());

        int validation = bancoServices.debitar(1, 1000.1);
        assertEquals(-4, validation);
        assertEquals(0, conta1.get().getSaldo());
    }

    @Test
    void consultaContaSimples() {
        Optional<Conta> conta1 = bancoServices.getConta(111);
        assertTrue(conta1.isPresent());
        assertTrue(!(conta1.get() instanceof ContaBonus) && !(conta1.get() instanceof ContaPoupanca));
        assertEquals(100, conta1.get().getSaldo());

    }

    @Test
    void consultaContaPoupanca() {
        Optional<Conta> conta1 = bancoServices.getConta(222);
        assertTrue(conta1.isPresent());
        assertTrue((conta1.get() instanceof ContaPoupanca));
        assertEquals(100, conta1.get().getSaldo());
    }

    @Test
    void consultaContaBonus() {
        Optional<Conta> conta1 = bancoServices.getConta(333);
        assertTrue(conta1.isPresent());
        assertTrue((conta1.get() instanceof ContaPoupanca));
        assertEquals(100, conta1.get().getSaldo());
    }

    @Test
    void consultaSaldo() {
        // Solicita um número de conta e exibe o saldo da conta
        bancoServices.criarConta(1, 1, 10);
        Optional<Conta> conta1 = bancoServices.getConta(1);
        assertTrue(conta1.isPresent());

        double saldo = bancoServices.saldo(1);
        assertEquals(10, saldo);
    }

    @Test
    void debitar() {
        bancoServices.criarConta(1, 1, 0);
        Optional<Conta> conta1 = bancoServices.getConta(1);
        assertTrue(conta1.isPresent());

        int validation = bancoServices.debitar(1, 10);
        assertEquals(0, validation);
        assertEquals(-10, conta1.get().getSaldo());
    }

    @Test
    void debitarValorNegativo() {
        bancoServices.criarConta(1, 1, 0);
        Optional<Conta> conta1 = bancoServices.getConta(1);
        assertTrue(conta1.isPresent());

        int validation = bancoServices.debitar(1, -10);
        assertEquals(-3, validation);
        assertEquals(0, conta1.get().getSaldo());
    }

    @Test
    void debitarValorMaiorQueSaldo_Poupanca() {
        bancoServices.criarConta(1, 2, 0);
        Optional<Conta> conta1 = bancoServices.getConta(1);
        assertTrue(conta1.isPresent());

        int validation = bancoServices.debitar(1, 10);
        assertEquals(-2, validation);
        assertEquals(0, conta1.get().getSaldo());
    }

    @Test
    void debitarValorMaiorQueSaldo_ContaChequeEspecial() {
        bancoServices.criarConta(1, 1, 0);
        Optional<Conta> conta1 = bancoServices.getConta(1);
        assertTrue(conta1.isPresent());

        int validation = bancoServices.debitar(1, 1001);
        assertEquals(-4, validation);
        assertEquals(0, conta1.get().getSaldo());
    }

    @Test
    void depositarValor() {
        bancoServices.criarConta(1, 1, 0);
        Optional<Conta> conta1 = bancoServices.getConta(1);
        assertTrue(conta1.isPresent());

        boolean validation = bancoServices.depositarValor(1, 10);
        assertTrue(validation);
        assertEquals(10, conta1.get().getSaldo());
    }

    @Test
    void depositarValorNegativo() {
        bancoServices.criarConta(1, 1, 0);
        Optional<Conta> conta1 = bancoServices.getConta(1);
        assertTrue(conta1.isPresent());

        boolean validation = bancoServices.depositarValor(1, -10);
        assertFalse(validation);
        assertEquals(0, conta1.get().getSaldo());
    }

    @Test
    @DisplayName("Teste a bonificação para contas do tipo Bônus ao depositar")
    void depositarContaBonus_semBonus() {
        // Para depósitos: 1 ponto para cada R$ 100,00 de depósito
        bancoServices.criarConta(1, 3, 0);
        Optional<Conta> conta1 = bancoServices.getConta(1);
        assertTrue(conta1.isPresent());

        boolean validation = bancoServices.depositarValor(1, 99);
        assertTrue(validation);
        assertEquals(10, ((ContaBonus) conta1.get()).getPontuacao());
    }

    @Test
    @DisplayName("Teste a bonificação para contas do tipo Bônus ao depositar")
    void depositarContaBonus_geraBonus() {
        // Para depósitos: 1 ponto para cada R$ 100,00 de depósito
        bancoServices.criarConta(1, 3, 0);
        Optional<Conta> conta1 = bancoServices.getConta(1);
        assertTrue(conta1.isPresent());

        boolean validation = bancoServices.depositarValor(1, 101);
        assertTrue(validation);
        assertEquals(11, ((ContaBonus) conta1.get()).getPontuacao());
    }

    @Test
    void transferir() {
        bancoServices.criarConta(1, 1, 0);
        Optional<Conta> conta1 = bancoServices.getConta(1);
        assertTrue(conta1.isPresent());

        bancoServices.criarConta(2, 2, 0);
        Optional<Conta> conta2 = bancoServices.getConta(2);
        assertTrue(conta2.isPresent());

        int validation = bancoServices.transferir(1, 2, 10);
        assertEquals(0, validation);
        assertEquals(-10, conta1.get().getSaldo());
        assertEquals(10, conta2.get().getSaldo());
    }

    @Test
    void transferirValorNegativoContaOrigem() {
        bancoServices.criarConta(1, 1, 0);
        Optional<Conta> conta1 = bancoServices.getConta(1);
        assertTrue(conta1.isPresent());

        bancoServices.criarConta(2, 2, 0);
        Optional<Conta> conta2 = bancoServices.getConta(2);
        assertTrue(conta2.isPresent());

        int validation = bancoServices.transferir(1, 2, -10);
        assertEquals(-4, validation);
        assertEquals(0, conta1.get().getSaldo());
        assertEquals(0, conta2.get().getSaldo());
    }


    @Test
    void transferirValorMaiorQueSaldoContaOrigem() {
        bancoServices.criarConta(1, 2, 0);
        Optional<Conta> conta1 = bancoServices.getConta(1);
        assertTrue(conta1.isPresent());

        bancoServices.criarConta(2, 2, 0);
        Optional<Conta> conta2 = bancoServices.getConta(2);
        assertTrue(conta2.isPresent());

        int validation = bancoServices.transferir(1, 2, 10);
        assertEquals(-3, validation);
        assertEquals(0, conta1.get().getSaldo());
        assertEquals(0, conta2.get().getSaldo());
    }

    @Test
    @DisplayName("Teste a bonificação para contas do tipo Bônus ao receber transferência")
    void transferirContaBonus_abaixoLimite() {
        // Para transferências recebidas: 1 ponto para cada R$ 150,00 recebidos
        bancoServices.criarConta(1, 2, 200);
        Optional<Conta> conta1 = bancoServices.getConta(1);
        assertTrue(conta1.isPresent());

        bancoServices.criarConta(2, 3, 0);
        Optional<Conta> conta2 = bancoServices.getConta(2);
        assertTrue(conta2.isPresent());

        int validation = bancoServices.transferir(1, 2, 149);
        assertEquals(10, ((ContaBonus) conta2.get()).getPontuacao());
    }

    @Test
    void transferirContaBonus_acimaLimite() {
        // Para transferências recebidas: 1 ponto para cada R$ 150,00 recebidos
        bancoServices.criarConta(1, 2, 200);
        Optional<Conta> conta1 = bancoServices.getConta(1);
        assertTrue(conta1.isPresent());

        bancoServices.criarConta(2, 3, 0);
        Optional<Conta> conta2 = bancoServices.getConta(2);
        assertTrue(conta2.isPresent());

        int validation = bancoServices.transferir(1, 2, 151);
        assertEquals(11, ((ContaBonus) conta2.get()).getPontuacao());
    }

    @Test
    void renderJurosContaPoupanca() {
        // Render Juros solicita a taxa de juros acrescenta os juros ao saldo da conta com base na taxa informada
        bancoServices.criarConta(1, 2, 200);
        Optional<Conta> conta1 = bancoServices.getConta(1);
        assertTrue(conta1.isPresent());

        bancoServices.renderJuros(10.5);
        assertEquals(221, conta1.get().getSaldo());

    }

//    @Test
//    void naoRenderJurosContaSimples() {
//    }
//
//    @Test
//    void naoRenderJurosContaBonus() {
//    }
}