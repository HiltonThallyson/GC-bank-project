package front;

import java.util.Scanner;

import back.Banco;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		boolean sair = false;
		Banco banco = new Banco();
		int opcao;
		System.out.println("-------------Bem vindo ao banco GC---------------");	
		
		do {
		System.out.println("Por favor, digite a operação que deseja realizar:");
		System.out.println("1 - Criar conta");
		System.out.println("2 - Verificar saldo da conta");
		System.out.println("3 - Depositar valor");
		System.out.println("4 - Sacar valor");
		System.out.println("5 - Transferir valor");
		System.out.println("6 - Sair");
		
		
		opcao = sc.nextInt();
		
		switch(opcao) {	
		case 1:
			//Aqui será chamado o serviço para criar a conta no banco;
			break;
		case 2:
			//Aqui será chamado o serviço para verificar saldo da conta;
			break;
		case 3:
			//Aqui será chamado o serviço para depositar valor na conta;
			break;
		case 4:
			//Aqui será chamado o serviço para sacar valor da conta;
			break;
		case 5:
			//Aqui será chamado o serviço para transferir valores entre contas;
			break;
		case 6:
			sair = true;
			break;
		default:
			sair = true;
		}
		
		}while(!sair);
		
		System.out.println("Obrigado por utilizar nossos serviços! Tenha um ótimo dia.");
		
		
		
		sc.close();
	}

}
