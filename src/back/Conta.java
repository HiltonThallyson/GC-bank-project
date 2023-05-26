package back;

public class Conta {
	final private int numeroDaConta;
	private double saldo;

	
	public Conta(int numeroDaConta) {
		this.numeroDaConta = numeroDaConta;
		this.saldo = 0;
	}
	
	public int getNumeroDaConta() {
		return numeroDaConta;
	}
	
	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
	
	public void incrementarSaldo(double valor, TipoDeTransacao modo) {
		this.saldo += valor;
	}
	
	public boolean decrementarSaldo(double valor) {

		if (this.saldo - valor < -1000) {
			return false;
		}
		this.saldo -= valor;
		return true;
	}

	
	
}
