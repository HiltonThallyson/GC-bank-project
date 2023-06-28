package br.ufrn.gc.bank.gcbank.models;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

@Component
public class Banco {
	ArrayList<Conta> contas;
	
	public Banco(ArrayList<Conta> contas) {
		this.contas = contas;
	}
	
	public Banco() {
		this.contas = new ArrayList<Conta>();
	}
	
	public ArrayList<Conta> getContas() {
		return contas;
	}
	
}
