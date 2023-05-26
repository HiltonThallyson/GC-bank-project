package back;

public class ContaPoupanca extends Conta{

    public ContaPoupanca(int numeroDaConta, double saldo) {
        super(numeroDaConta);
        this.setSaldo(saldo);
    }


    public void renderJuros(double taxa) {
        final double saldoAtual = this.getSaldo();
        double saldoFinal = saldoAtual * (1 + taxa);
        this.setSaldo(saldoFinal);
    }

    

    
}
