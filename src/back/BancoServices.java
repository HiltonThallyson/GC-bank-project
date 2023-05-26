package back;


import java.util.Objects;
import java.util.Optional;


public class BancoServices {
	Banco banco;
	
	public BancoServices(Banco banco) {
		this.banco = banco;
	}
	
	public int criarConta(int numeroDaConta, double saldo) {

		if(!checarNumeroDaConta(numeroDaConta)){
			return -1;
		} else if(saldo < 0) {
			return -2;
		}

		banco.getContas().add(new Conta(numeroDaConta, saldo));
		return 0;
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

	public int debitar(int numeroDaConta, double valor) {

		if(valor <= 0) {
			return -3;
		}

		Conta conta = getConta(numeroDaConta);
		if (Objects.isNull(conta)) {
			return -1;
		} else if (conta.getSaldo() < valor) {
			return -2;
		}

		conta.decrementarSaldo(valor);

		return 0;
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

		if(valor <= 0) {
			return -4;
		}

		Conta contaOrigem = getConta(numeroDaContaOrigem);
		Conta contaDestino = getConta(numeroDaContaDestino);

		if (Objects.isNull(contaOrigem)) {
			return -1;
		} else if (Objects.isNull(contaDestino)) {
			return -2;
		} else if(contaOrigem.getSaldo() < valor) {
			return -3;
		}

		contaOrigem.decrementarSaldo(valor);
		contaDestino.incrementarSaldo(valor);

		return 0;
	}
	
	
}
