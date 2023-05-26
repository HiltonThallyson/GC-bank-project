package front;

import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

import back.Banco;
import back.BancoServices;
import back.Conta;

public class Main {


	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		boolean sair = false;
		boolean ehBonus = false;
		Banco banco = new Banco();
		int numeroDaConta;
		boolean isValid = true;
		double valor;

		int tipoDaConta = 1;

		
		BancoServices bancoServices = new BancoServices(banco);
		int opcao;
		System.out.println("-------------Bem vindo ao banco GC---------------");	
		
		do {
		System.out.println("Por favor, digite a operação que deseja realizar:");
		System.out.println("1 - Criar conta");
		System.out.println("2 - Verificar saldo da conta");
		System.out.println("3 - Depositar valor");
		System.out.println("4 - Sacar valor");
		System.out.println("5 - Transferir valor");
		System.out.println("6 - Render juros");
		System.out.println("8 - Sair");
		
		
		opcao = sc.nextInt();
		
		switch(opcao) {	
		case 1:
			do {
				System.out.print("Por favor, digite o número da conta: ");
				numeroDaConta = sc.nextInt();

				System.out.println();
				System.out.println("Digite o tipo da conta:");
				System.out.println("1 - Simples");
				System.out.println("2 - Poupança");
				System.out.println("3 - Bonus");
				tipoDaConta = sc.nextInt();

				double saldo = 0;
				if (tipoDaConta == 2 ) {
					System.out.print("Por favor, digite o saldo inicial da conta poupança: ");
					saldo = sc.nextDouble();
				}

				int resultadoDaCriacaoDeConta = bancoServices.criarConta(numeroDaConta, tipoDaConta, saldo);
				isValid = resultadoDaCriacaoDeConta == 0;

				if(isValid) {
					System.out.println("Conta criada com sucesso!");
				} else {
					if (resultadoDaCriacaoDeConta == -1) {
						System.out.println("Número de conta já cadastrado! Operação cancelada.");
					} else if (resultadoDaCriacaoDeConta == -2) {
						System.out.println("Uma conta pode ser iniciada apenas com valores positivos! Operação cancelada.");
					} else {
						System.out.println("Tipo de conta inválida!");
					}
				};
				
			} while(!isValid);

			
			break;
		case 2:
			System.out.print("Por favor, digite o número da conta: ");
			numeroDaConta = sc.nextInt();
			System.out.println();
			Optional<Conta> conta = bancoServices.consultarSaldo(numeroDaConta);
			conta.ifPresentOrElse(
				c -> {
					System.out.println("O saldo atual da conta eh: " + conta.get().getSaldo());
				},
				() -> {
					System.out.println("Conta inexistente");
				}
			);
			break;
		case 3:
			isValid = false;
			do {
				System.out.print("Digite o número da sua conta: ");
				numeroDaConta = sc.nextInt();
				isValid = !bancoServices.checarNumeroDaConta(numeroDaConta);
				if(isValid) {
					System.out.print("Digite o valor que deseja creditar na conta: ");
					valor = sc.nextDouble();
					if(valor < 0) {
						System.out.println("Valor inválido: o valor a ser creditado precisa ser maior que R$ 0.0");
						isValid = false;
					}else {
						if(bancoServices.depositarValor(numeroDaConta, valor)) {
							System.out.println("Valor creditado com sucesso!");
							
						};
					}
				}else {
					System.out.println("Conta não encontrada! Tente novamente.");
					System.out.println();
				}
				
			}while(!isValid);
			
			break;
		case 4:
			// Aqui será chamado o serviço para sacar valor da conta;
			Integer numeroConta = null;

			double valorASacar = 0;
			try {
				boolean isInvalidNumber = true;
				do {
					System.out.println("Qual o número da conta?");
					try {
						numeroConta = sc.nextInt();
						isInvalidNumber = false;

					} catch (InputMismatchException e) {
						System.out.println("Erro: O número de conta deve ser um inteiro");
					}

				} while (isInvalidNumber);

				valorASacar = 0;
				boolean isInvalidValue = true;
				do {
					System.out.println("Qual o valor deseja sacar?");

					try {
						valorASacar = sc.nextDouble();
						isInvalidValue = false;

					} catch (InputMismatchException e) {
						System.out.println("Erro: O valor a ser sacado deve ser um número");

					}

				} while (isInvalidValue);

				int isOpSuccessful = bancoServices.debitar(numeroConta, valorASacar);
				if (isOpSuccessful == -1) {
					System.out.println("Conta não encontrada! Operação cancelada.");
				} else if(isOpSuccessful == -2){
					System.out.println("Saldo insuficiente! Operação cancelada.");
				} else if(isOpSuccessful == -3) {
					System.out.println("Apenas valores positivos podem ser sacados! Operação cancelada.");
				} else {
					System.out.println("Operação realizada com sucesso!");
				}

				break;

			} catch (Exception e) {
				System.out.println("Erro ao realizar operação, tente novamente.");
				break;
			}

		case 5:
			// serviço para transferir valores entre contas
			Integer numeroDaContaOrigem = null;
			boolean isInvalidNumber = true;
			do {
				System.out.println("Digite o número da conta de origem");
				try {
					numeroDaContaOrigem = sc.nextInt();
					isInvalidNumber = false;
				} catch (InputMismatchException e) {
					System.out.println("Erro: O número de conta deve ser um inteiro");
				}
			} while (isInvalidNumber);

			Integer numeroDaContaDestino = null;
			isInvalidNumber = true;
			do {
				System.out.println("Digite o número da conta de destino");
				try {
					numeroDaContaDestino = sc.nextInt();
					isInvalidNumber = false;
				} catch (InputMismatchException e) {
					System.out.println("Erro: O número de conta deve ser um inteiro");
				}
			} while (isInvalidNumber);

			valor = 0;
			isInvalidNumber = true;
			do {
				System.out.println("Qual valor deseja transferir?");
				try {
					valor = sc.nextDouble();
					isInvalidNumber = false;
				} catch (InputMismatchException e) {
					System.out.println("Erro: O valor a ser transferido deve ser um número");
				}
			} while (isInvalidNumber);

			int isOpSuccessful = bancoServices.transferir(numeroDaContaOrigem, numeroDaContaDestino, valor);
			if (isOpSuccessful == -1) {
				System.out.println("Conta de origem não encontrada! Tente novamente.");
			} else if(isOpSuccessful == -2) {
				System.out.println("Conta de destino não encontrada! Tente novamente.");
			} else if(isOpSuccessful == -3) {
				System.out.println("Saldo insuficiente! Operação cancelada.");
			} else if(isOpSuccessful == -4) {
				System.out.println("Apenas valores positivos podem ser transferidos! Operação cancelada.");
			} else {
				System.out.println("Transferência de " + valor +
						" da conta " + numeroDaContaOrigem +
						" para a conta " + numeroDaContaDestino +
						" realizada com sucesso!");
			}

			break;
		case 6:
				var taxaDeJuros = 1.0;
				System.out.println("Digite a taxa de juros em %:");
				taxaDeJuros = sc.nextDouble();
				bancoServices.renderJuros(taxaDeJuros);
				System.out.println("Rendimentos atualizados com sucesso!");
			break;
		default:
			sair = true;
		}
		
		System.out.println();
		}while(!sair);
		
		System.out.println("Obrigado por utilizar nossos serviços! Tenha um ótimo dia.");
		
		
		
		sc.close();
	}

}
