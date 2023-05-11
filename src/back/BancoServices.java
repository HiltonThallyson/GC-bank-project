package back;

import java.util.Scanner;

public class BancoServices {
	Banco banco;
	
	public BancoServices(Banco banco) {
		this.banco = banco;
	}
	
	/*public void criarConta(int numeroDaConta) {
		
		
		boolean isValid = true;
		
		do {
			System.out.print("Por favor, digite o número da conta: ");
			numeroDaConta = sc.nextInt();
			System.out.println();
			for(int i=0; i<banco.contas.size(); i++) {
				if(banco.contas.get(i).getNumeroDaConta() == numeroDaConta) {
					isValid = false;
					break;
				}
			}
			
			if(!isValid) {
				System.out.println("Número da conta já existe!!!");
			}
			
			
			
		}while(!isValid);
		
		Conta minhaConta = new Conta(numeroDaConta);
		banco.contas.add(minhaConta);
		System.out.println("Conta cadastrada com sucesso!");
		System.out.println();
		
	}

	public void verificarSaldo(Scanner sc) {
		
		int numeroDaConta;
		
		boolean isFinished = false;
		Conta minhaConta = null;
		
		do {
			System.out.println();
			System.out.print("Por favor digite o número da sua conta: ");
			numeroDaConta = sc.nextInt();
			for(int i=0; i<contas.size(); i++) {
				if(numeroDaConta == contas.get(i).getNumeroDaConta()) {
					minhaConta = contas.get(i);
				}
			}
			
			if(minhaConta == null) {
				System.out.println("Conta não encontrada!!!");
				isFinished = false;
			}else {
				isFinished = true;
			}
		}while(!isFinished);
		
		System.out.printf("Seu saldo é: R$ %.2f%n", minhaConta.getSaldo());
		System.out.println();
		
	}*/
}
