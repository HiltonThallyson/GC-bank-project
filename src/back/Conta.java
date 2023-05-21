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
	
	
	public void incrementarSaldo(double valor) {
		this.saldo += valor;
	}
	
	public void decrementarSaldo(double valor) {
		this.saldo -= valor;
	}
	
}
