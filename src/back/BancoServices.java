package back;


import java.util.Objects;

import java.util.Optional;


public class BancoServices {
	Banco banco;
	
	public BancoServices(Banco banco) {
		this.banco = banco;
	}
	
	public int criarConta(int numeroDaConta, int tipoDaConta) {
		
		if(checarNumeroDaConta(numeroDaConta)){
			Conta novaConta;
			switch (tipoDaConta) {
				case 1:
					novaConta = new Conta(numeroDaConta);
					break;
				case 2:
					novaConta = new ContaPoupanca(numeroDaConta);
					break;
				default:
					return -2;
			}
			banco.getContas().add(novaConta);
			return 0;
		}else {
			return -1;
		}
		
	}

	public Optional<Conta> consultarSaldo (int numeroDaConta) {

		return
			 this
			.banco
			.getContas()
			.stream()
			.filter(c -> c.getNumeroDaConta() == numeroDaConta)
			.findFirst();

	}

	public boolean debitar(int numeroDaConta, double valor) {

		for (Conta c : banco.getContas()) {
			if(c.getNumeroDaConta() == numeroDaConta) {

				c.decrementarSaldo(valor);

				return true;
			}
		}

		return false;

	}

	public boolean depositarValor(int numeroDaConta,double valor) {
			
			for(int i=0; i<banco.getContas().size(); i++) {
				if(banco.getContas().get(i).getNumeroDaConta() == numeroDaConta) {
					
					banco.getContas().get(i).incrementarSaldo(valor);
					return true;
					
				}
			}
			return false;
			
	}
	public boolean checarNumeroDaConta(int numeroDaConta) {
		for(int i=0; i<banco.getContas().size(); i++) {
			if(banco.getContas().get(i).getNumeroDaConta() == numeroDaConta) {
				return false;
				
			}
		}
		return true;
	}

	private Conta getConta(int numeroDaConta) {

		for (Conta c : banco.getContas()) {
			if (c.getNumeroDaConta() == numeroDaConta) {
				return c;
			}
		}
		return null;
	}

	public int transferir(int numeroDaContaOrigem, int numeroDaContaDestino, double valor) {

		Conta contaOrigen = getConta(numeroDaContaOrigem);
		Conta contaDestino = getConta(numeroDaContaDestino);

		if (Objects.isNull(contaOrigen)) {
			return -1;
		} else if (Objects.isNull(contaDestino)) {
			return -2;
		}

		contaOrigen.decrementarSaldo(valor);
		contaDestino.incrementarSaldo(valor);

		return 0;
	}

	public void renderJuros(double taxaEmPorcentagem) {
		
		final double taxaEmDecimal = taxaEmPorcentagem / 100;
		for (Conta c : banco.getContas()) {
			if(c instanceof ContaPoupanca) {
				((ContaPoupanca) c).renderJuros(taxaEmDecimal);
		}
		}
	}
	
}
