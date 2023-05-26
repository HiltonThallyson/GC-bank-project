package back;

public class ContaBonus extends Conta{

    int pontuacao;

    public ContaBonus(int numeroDaConta, int pontuacao) {
        super(numeroDaConta);
        this.pontuacao = pontuacao;

        System.out.println("Conta [" + super.getNumeroDaConta() + "] registrada como conta bonus. (PONTUACAO):  " + this.pontuacao );
    }

    @Override
    public void incrementarSaldo(double valor, TipoDeTransacao modo) {
        super.incrementarSaldo(valor, modo);
        switch (modo){
            case TRANSFERENCIA:
                this.pontuacao += (int) valor/150;
                break;

            case DEPOSITO:
                this.pontuacao += (int) valor/100;
                break;

        }
        System.out.println("[" + super.getNumeroDaConta() + "](PONTUACAO):  " + this.pontuacao );

    }

}
