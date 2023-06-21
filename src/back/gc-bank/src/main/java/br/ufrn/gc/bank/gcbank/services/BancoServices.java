package br.ufrn.gc.bank.gcbank.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufrn.gc.bank.gcbank.enums.TipoDeTransacao;
import br.ufrn.gc.bank.gcbank.models.Conta;
import br.ufrn.gc.bank.gcbank.models.ContaBonus;
import br.ufrn.gc.bank.gcbank.models.ContaPoupanca;
import br.ufrn.gc.bank.gcbank.repositories.BancoRepository;

@Service
public class BancoServices {

	@Autowired
	BancoRepository bancoRepository;

	public int criarConta(int numeroDaConta, int tipoDaConta, double saldo) {
		Optional<Conta> conta = bancoRepository.getConta(numeroDaConta);
		
		if(conta.isPresent()) {
			return -1;
		}	
		else if(saldo < 0) {
			return -2;
		}

		Conta novaConta;
		switch (tipoDaConta) {
			case 1:
				novaConta = new Conta(numeroDaConta, saldo);
				break;
			case 2:
				novaConta = new ContaPoupanca(numeroDaConta, saldo);
				break;
			case 3:
				novaConta = new ContaBonus(numeroDaConta, 10);
				break;
			default:
				return -2;
		}
		bancoRepository.cadastrarConta(novaConta);
		return 0;

	}

	public int debitar(int numeroDaConta, double valor) {

		if(valor <= 0) {
			return -3;
		}

		Optional<Conta> conta = bancoRepository.getConta(numeroDaConta);


		if (!conta.isPresent()) {
			return -1;
		} else if ((conta.get() instanceof ContaPoupanca) && (conta.get().getSaldo() < valor)) {
			return -2;
		}

		if (conta.get().decrementarSaldo(valor)) {
			bancoRepository.atualizarConta(conta.get());
			return 0;
		}

		return -4;
	}


	public boolean depositarValor(int numeroDaConta,double valor) {
			
		if(valor <= 0) {
			return false;
		}

		Optional<Conta> conta = bancoRepository.getConta(numeroDaConta);


		if (!conta.isPresent()) {
			return false;
		} else if ((conta.get() instanceof ContaPoupanca) && (conta.get().getSaldo() < valor)) {
			return false;
		}

		conta.get().incrementarSaldo(valor, TipoDeTransacao.DEPOSITO);
			bancoRepository.atualizarConta(conta.get());
			return true;
			
	}


	public Optional<Conta> getConta(int numeroDaConta) {
		return bancoRepository.getConta(numeroDaConta);
	}

	public ArrayList<Conta> getContas() {
		return bancoRepository.getContas();
	}


	public int transferir(int numeroDaContaOrigem, int numeroDaContaDestino, double valor) {

		if(valor <= 0) {
			return -4;
		}

		Optional<Conta> contaOrigemOptional = bancoRepository.getConta(numeroDaContaOrigem);
		Optional<Conta> contaDestinoOptional = bancoRepository.getConta(numeroDaContaDestino);

		if (!contaOrigemOptional.isPresent()) {
			return -1;
		} else if (!contaDestinoOptional.isPresent()) {
			return -2;
		} else if((contaOrigemOptional.get() instanceof ContaPoupanca) && (contaOrigemOptional.get().getSaldo() < valor)) {
			return -3;
		}

		Conta contaOrigem = contaOrigemOptional.get();
		Conta contaDestino = contaDestinoOptional.get();

		if(contaOrigem.decrementarSaldo(valor)){
			contaDestino.incrementarSaldo(valor, TipoDeTransacao.TRANSFERENCIA);
			bancoRepository.atualizarConta(contaOrigem);
			bancoRepository.atualizarConta(contaDestino);
			return 0;
		}

		return -5;
	}
	

	public void renderJuros(Double taxa) {
		var contas = bancoRepository.getContas();
		for (Conta conta : contas) {
			if(conta instanceof ContaPoupanca) {
				ContaPoupanca contaP = (ContaPoupanca) conta;
				contaP.renderJuros(taxa);
				bancoRepository.atualizarConta(contaP);
			}
		}
	}
}
