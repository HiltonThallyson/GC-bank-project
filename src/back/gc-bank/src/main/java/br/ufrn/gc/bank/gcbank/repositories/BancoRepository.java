package br.ufrn.gc.bank.gcbank.repositories;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.ufrn.gc.bank.gcbank.models.Banco;
import br.ufrn.gc.bank.gcbank.models.Conta;

@Repository
public class BancoRepository {

	Banco banco;

    public BancoRepository(Banco banco) {
        this.banco = banco;
    }

    public void cadastrarConta(Conta conta) {
        banco.getContas().add(conta);
    }

    public Optional<Conta> getConta(int numeroDaConta) {
        return banco
			.getContas()
			.stream()
			.filter(c -> c.getNumeroDaConta() == numeroDaConta)
			.findFirst();
    }

    public ArrayList<Conta> getContas() {
        return banco
			.getContas();
			
    }

    public void atualizarConta(Conta conta) {

        int index = 0;
        for(Conta c: banco.getContas()) {

            if(c.getNumeroDaConta() == conta.getNumeroDaConta()) {
                break;
            }
            index++;
        }

        banco.getContas().set(index, conta);
			
			
    }
}
