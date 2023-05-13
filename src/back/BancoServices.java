package back;



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

	public boolean checarNumeroDaConta(int numeroDaConta) {
		for(int i=0; i<banco.getContas().size(); i++) {
			if(banco.getContas().get(i).getNumeroDaConta() == numeroDaConta) {
				return false;
				
			}
		}
		return true;
	}
}
