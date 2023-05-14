package back;


import java.util.Objects;

import java.util.Optional;
import java.util.Objects;


public class BancoServices {
	Banco banco;
	
	public BancoServices(Banco banco) {
		this.banco = banco;
	}
	
	public boolean criarConta(int numeroDaConta) {
		
		if(checarNumeroDaConta(numeroDaConta)){
			banco.getContas().add(new Conta(numeroDaConta));
			return true;
		}else {
			return false;
		}
		
	}

	public double consultarSaldo (int numeroDaConta) {

		Optional<Conta> conta = this
								.banco
								.getContas()
								.stream()
								.filter(c -> c.getNumeroDaConta() == numeroDaConta)
								.findFirst();

		return conta.isPresent() ? conta.get().getSaldo() : -1d;
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

//	public void verificarSaldo(Scanner sc) {
//		
//		int numeroDaConta;
//		
//		boolean isFinished = false;
//		Conta minhaConta = null;
//		
//		do {
//			System.out.println();
//			System.out.print("Por favor digite o número da sua conta: ");
//			numeroDaConta = sc.nextInt();
//			for(int i=0; i<contas.size(); i++) {
//				if(numeroDaConta == contas.get(i).getNumeroDaConta()) {
//					minhaConta = contas.get(i);
//				}
//			}
//			
//			if(minhaConta == null) {
//				System.out.println("Conta não encontrada!!!");
//				isFinished = false;
//			}else {
//				isFinished = true;
//			}
//		}while(!isFinished);
//		
//		System.out.printf("Seu saldo é: R$ %.2f%n", minhaConta.getSaldo());
//		System.out.println();
//		
//	}

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
	
	
}
