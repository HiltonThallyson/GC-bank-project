package front;

import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

import back.Banco;
import back.BancoServices;
import back.Conta;

public class Main {


	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		boolean sair = false;
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
				tipoDaConta = sc.nextInt();
				
				int resultadoDaCriacaoDeConta = bancoServices.criarConta(numeroDaConta, tipoDaConta);
				isValid = resultadoDaCriacaoDeConta == 0 ? true : false;
				if(isValid) {
					System.out.println("Conta criada com sucesso!");
				}else {
					if(resultadoDaCriacaoDeConta == -1) {
						System.out.println("Não foi possivel cadastrar a conta!!!");
					}else {
						System.out.println("Tipo de conta inválida!");
					}
				};
				
			}while(!isValid);

			
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
						System.out.println("Erro: O número de conta deve ser um inteiro\n");
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
						System.out.println("Erro: O valor a ser sacado deve ser um número\n");

					}

				} while (isInvalidValue);

				if (bancoServices.debitar(numeroConta, valorASacar)) {
					System.out.println("Operação realizada com sucesso!\n");
				} else {
					System.out.println("Conta não encontrada! Operação cancelada.\n");
				}

				break;

			} catch (Exception e) {
				System.out.println("Erro ao realizar operação, tente novamente.\n");
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
					System.out.println("Erro: O número de conta deve ser um inteiro\n");
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
					System.out.println("Erro: O número de conta deve ser um inteiro\n");
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
					System.out.println("Erro: O valor a ser transferido deve ser um número\n");
				}
			} while (isInvalidNumber);

			int isOpSuccessful = bancoServices.transferir(numeroDaContaOrigem, numeroDaContaDestino, valor);
			if (isOpSuccessful == -1) {
				System.out.println("Conta de origem não encontrada! Tente novamente.");
			} else if(isOpSuccessful == -2) {
				System.out.println("Conta de destino não encontrada! Tente novamente.");
			} else {
				System.out.println("Transferência de " + valor +
						" da conta " + numeroDaContaOrigem +
						" para a conta " + numeroDaContaDestino +
						" realizada com sucesso!");
			}

			break;
		case 6:
		var isDone = false;
		do{
			System.out.println("Digite o número da conta:");
			numeroDaConta = sc.nextInt();
			if(!bancoServices.checarNumeroDaConta(numeroDaConta)){
				var taxaDeJuros = 1.0;
				System.out.println("Digite a taxa de juros em %:");
				taxaDeJuros = sc.nextDouble();
				isDone = bancoServices.renderJuros(numeroDaConta, taxaDeJuros);
				if(!isDone) {
					System.out.println("A conta selecionada não é uma conta poupança. Operação inválida!");
					System.out.println("Deseja cancelar a operação? 1 - sim 2 - não");
				opcao = sc.nextInt();
					System.out.println();
					if(opcao == 1) {
						isDone = true;
					}
				}
			}else {
				System.out.println("Conta não cadastrada!");
				System.out.println("Deseja cancelar a operação? 1 - sim 2 - não");
				opcao = sc.nextInt();
				System.out.println();
				if(opcao == 1) {
					isDone = true;
				}
				isDone = false;
			}
		}while(!isDone);
			
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
