package back;

import java.util.ArrayList;


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
