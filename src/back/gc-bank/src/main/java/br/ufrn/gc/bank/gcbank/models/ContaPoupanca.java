package br.ufrn.gc.bank.gcbank.models;

public class ContaPoupanca extends Conta{

    public ContaPoupanca() {}
    
    public ContaPoupanca(int numeroDaConta, double saldo) {
        super(numeroDaConta);
        this.setSaldo(saldo);
    }


    public void renderJuros(double taxa) {
        final double saldoAtual = this.getSaldo();
        double saldoFinal = (saldoAtual * (100 + taxa))/100;
        this.setSaldo(saldoFinal);
    }

    @Override
    public boolean decrementarSaldo(double valor) {

        super.setSaldo( super.getSaldo() - valor );
        return true;
    }

    
}
