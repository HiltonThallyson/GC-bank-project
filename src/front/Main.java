package front;

import java.net.URI;
import java.util.*;
import java.net.http.*;


public class Main {

	private enum Operations {
		GET_CONTA_BY_ID, GET_CONTAS, POST_CONTA, PUT_CREDITO, PUT_TRANSFERENCIA, PUT_RENDIMENTO
	}

	private static HttpResponse<String> makeARequest(HttpClient httpClient, HttpRequest httpRequest){
		try{
			System.out.println("[INFO]: Making a HTTP Request to " + httpRequest.method() + " " +  httpRequest.uri().toString());
			var response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());;
			return response;
		}catch (Exception e) {

		}
		return null; //TODO
	}

	private static HttpRequest buildRequest(String url, Operations method, List<String> arguments) {

		switch (method){
			case GET_CONTAS, GET_CONTA_BY_ID:
				return HttpRequest
						.newBuilder(URI.create(url))
						.build();

			case POST_CONTA:
				return HttpRequest
						.newBuilder(URI.create(url))
						.header("Content-Type", "application/json")
						.POST(HttpRequest.BodyPublishers.ofString(bodyGenerator(method, arguments)))
						.build();

			case PUT_CREDITO, PUT_TRANSFERENCIA, PUT_RENDIMENTO:
				return HttpRequest
						.newBuilder(URI.create(url))
						.header("Content-Type", "application/json")
						.PUT(HttpRequest.BodyPublishers.ofString(bodyGenerator(method, arguments)))
						.build();

		}
		return null;
	}

	private static String bodyGenerator(Operations op, List<String> arguments){
		switch (op){
			case POST_CONTA:
				return "{\n" +
						"\t\"numeroDaConta\":" + arguments.get(0) + ",\n" +
						"\t\"tipoDaConta\":"+ arguments.get(1) + ",\n" +
						"\t\"saldo\":" + arguments.get(2)+ "\n" +
						"}";
			case PUT_CREDITO:
				return "{\n" +
						"\t\"valor\":" + arguments.get(0) + "\n" +
						"}";

			case PUT_TRANSFERENCIA:
				return "{\n" +
						"\t\"from\":" + arguments.get(0) + ",\n" +
						"\t\"to\":"+ arguments.get(1) + ",\n" +
						"\t\"valor\":" + arguments.get(2)+ "\n" +
						"}";

			case PUT_RENDIMENTO:
				return "{\n" +
						"\t\"taxa\":" + arguments.get(0) + "\n" +
						"}";
			default:
				return "{}";

		}
	}

	private static String httpResponseHandler(HttpResponse<String> response) {
		if (response.statusCode() == 200){
			return (response.body());
		}
		System.out.println("[ERROR]: During the HTTP Request... the response code is " + response.statusCode() + " and the body is " + response.body());
		return "";
	}




	public static void main(String[] args) {
		String HOST = "127.0.0.1";
		String PORT	= "8080";
		String BASE_URL = "http://" + HOST + ":" + PORT + "/banco";

		HttpClient client = HttpClient.newBuilder().build();
		Scanner sc = new Scanner(System.in);
		boolean sair = false;
		boolean ehBonus = false;
//		Banco banco = new Banco();
		int numeroDaConta;
		boolean isValid = true;
		double valor;

		int tipoDaConta = 1;

//		BancoServices bancoServices = new BancoServices(banco);
		Map<Operations, HttpRequest> requestHandler = new HashMap<>();


		int opcao;
		System.out.println("-------------Bem vindo ao banco GC---------------");	
		
		do {
		System.out.println("Por favor, digite a operação que deseja realizar:");
		System.out.println("1 - Criar conta");
		System.out.println("2 - Verificar conta pelo número");
		System.out.println("3 - Depositar valor");
		System.out.println("4 - Sacar valor");
		System.out.println("5 - Transferir valor");
		System.out.println("6 - Ver todas as contas");
		System.out.println("7 - Consultar uma conta pelo número");
		System.out.println("8 - Render Juros");
		System.out.println("9 - Sair");
		
		
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
				if (tipoDaConta == 1 || tipoDaConta == 2) {
					System.out.print("Por favor, digite o saldo inicial da conta poupança: ");
					saldo = sc.nextDouble();
				}

				List<String> arguments = new ArrayList<>();

				arguments.add(String.valueOf(numeroDaConta));
				arguments.add(String.valueOf(tipoDaConta));
				arguments.add(String.valueOf(saldo));

				HttpResponse<String> result = makeARequest(client, buildRequest(BASE_URL + "/conta/", Operations.POST_CONTA, arguments));
				int resultadoDaCriacaoDeConta = Integer.parseInt(httpResponseHandler(result));

				isValid = resultadoDaCriacaoDeConta == 0;
				if(isValid) {
					System.out.println("Conta criada com sucesso!");
				} else if(resultadoDaCriacaoDeConta == -1){
					System.out.println("Número de conta já cadastrado! Operação cancelada.");
				} else if(resultadoDaCriacaoDeConta == -2) {
					System.out.println("Uma conta pode ser iniciada apenas com valores positivos! Operação cancelada.");
				} else {
					System.out.println("Falha desconhecida no backend ou tipo de conta inválida");
				}
				
			} while(!isValid);

			break;
		case 2:
			System.out.print("Por favor, digite o número da conta: ");
			numeroDaConta = sc.nextInt();
			System.out.println();
			HttpResponse<String> result = makeARequest(client, buildRequest(BASE_URL + "/conta/" + numeroDaConta + "/saldo/", Operations.GET_CONTA_BY_ID, new ArrayList<>()));
			Optional.ofNullable(result).ifPresentOrElse(
					(c) -> {
						System.out.println("Saldo descrito abaixo");
						System.out.println(c.body());
					},
					() -> {
						System.out.println("Nenhuma conta foi achada");
					}
			);
			break;
		case 3:
			isValid = false;
			do {
				System.out.print("Digite o número da sua conta: ");
				numeroDaConta = sc.nextInt();
				System.out.print("Digite o valor que deseja creditar na conta: ");
				valor = sc.nextDouble();
				if(valor < 0) {
					System.out.println("Valor inválido: o valor a ser creditado precisa ser maior que R$ 0.0");
					isValid = false;
				}else {
					List<String> arguments = new ArrayList<>();
					arguments.add(String.valueOf(valor));
					HttpResponse<String> requestResponse = makeARequest(client, buildRequest(BASE_URL + "/conta/" + numeroDaConta + "/credito", Operations.PUT_CREDITO, arguments));
					Boolean resultadoDaCriacaoDeConta = Boolean.parseBoolean(httpResponseHandler(requestResponse));
					if(resultadoDaCriacaoDeConta) {
						System.out.println("Valor creditado com sucesso!");
						isValid = true;

					}else {
						System.out.println("Conta não encontrada! ou Falha no backend");
						System.out.println();
						isValid = true;
					}
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

				List<String> arguments = new ArrayList<>();
				arguments.add(String.valueOf(valorASacar));

				HttpResponse<String> requestResponse = makeARequest(client, buildRequest(BASE_URL + "/conta/" + numeroConta + "/debito", Operations.PUT_CREDITO, arguments));
				int isOpSuccessful = Integer.parseInt(httpResponseHandler(requestResponse));
				if (isOpSuccessful == -1) {
					System.out.println("Conta não encontrada! Operação cancelada.");
				} else if(isOpSuccessful == -2){
					System.out.println("Saldo insuficiente! Operação cancelada.");
				} else if(isOpSuccessful == -3) {
					System.out.println("Apenas valores positivos podem ser sacados! Operação cancelada.");
				} else if (isOpSuccessful == -4) {
					System.out.println("A conta atingiu o limite de cheque especial");
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

			List<String> arguments = new ArrayList<>();

			arguments.add(String.valueOf(numeroDaContaOrigem));
			arguments.add(String.valueOf(numeroDaContaDestino));
			arguments.add(String.valueOf(valor));

			HttpResponse<String> reqResult = makeARequest(client, buildRequest(BASE_URL + "/conta/transferencia", Operations.PUT_TRANSFERENCIA, arguments));
			int isOpSuccessful = Integer.parseInt(httpResponseHandler(reqResult));

			if (isOpSuccessful == -1) {
				System.out.println("Conta de origem não encontrada! Tente novamente.");
			} else if(isOpSuccessful == -2) {
				System.out.println("Conta de destino não encontrada! Tente novamente.");
			} else if(isOpSuccessful == -3) {
				System.out.println("Saldo insuficiente! Operação cancelada.");
			} else if(isOpSuccessful == -4) {
				System.out.println("Apenas valores positivos podem ser transferidos! Operação cancelada.");
			} else if(isOpSuccessful == -5) {
					System.out.println("Conta de origem atingiu o limite de cheque especial");
			} else {
				System.out.println("Transferência de " + valor +
						" da conta " + numeroDaContaOrigem +
						" para a conta " + numeroDaContaDestino +
						" realizada com sucesso!");
			}

			break;

		case 6:
			System.out.println();
			HttpResponse<String> req = makeARequest(client, buildRequest(BASE_URL + "/conta/", Operations.GET_CONTAS, new ArrayList<>()));
			Optional.ofNullable(req.body()).ifPresentOrElse(
					(c) -> {
						System.out.println("Contas abaixo");
						System.out.println(c);
					},
					() -> {
						System.err.println("[FATAL]: Erro no backend");
					}
			);
			break;

		case 7:
			System.out.print("Por favor, digite o número da conta: ");
			numeroDaConta = sc.nextInt();
			System.out.println();
			HttpResponse<String> req_ = makeARequest(client, buildRequest(BASE_URL + "/conta/" + numeroDaConta, Operations.GET_CONTA_BY_ID, new ArrayList<>()));
			Optional.ofNullable(req_).ifPresentOrElse(
					(c) -> {
						System.out.println("Conta descrito abaixo");
						System.out.println(c.body());
					},
					() -> {
						System.out.println("Nenhuma conta foi achada");
					}
			);
			break;

		case 8:
			System.out.print("Por favor, digite o valor da taxa: ");
			double taxa = sc.nextDouble();
			System.out.println();
			List<String> arg = new ArrayList<>();
			arg.add(String.valueOf(taxa));

			HttpResponse<String> request = makeARequest(client, buildRequest(BASE_URL + "/conta/rendimento", Operations.PUT_RENDIMENTO, arg));

			if(request.statusCode() == 200) System.out.println("Operação realizada com sucesso");
			else System.err.println("[FATAL]: Erro no backend");
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
