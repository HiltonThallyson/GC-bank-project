package br.ufrn.gc.bank.gcbank.controllers;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.gc.bank.gcbank.enums.TipoDeTransacao;
import br.ufrn.gc.bank.gcbank.models.Conta;
import br.ufrn.gc.bank.gcbank.services.BancoServices;

@RestController
@RequestMapping("/banco/")
public class BancoController {

    final BancoServices bancoServices;

    public BancoController(BancoServices bancoServices) {
        this.bancoServices = bancoServices;
    }

    @PostMapping("/conta/")
    public int cadastrarConta(@RequestBody(required = true) Map<String, Object> map) {
        
        return bancoServices.criarConta(((int)map.get("numeroDaConta")), ((int)map.get("tipoDaConta")), Double.parseDouble(map.get("saldo").toString()));
    }

    @GetMapping("/conta/{numeroDaConta}")
    public Conta getConta(@PathVariable(value = "numeroDaConta") int numeroConta) {
        Optional<Conta> conta = bancoServices.getConta(numeroConta);
        
        if(conta.isPresent()) {
            return conta.get();
        }
        return null;
    }

    @GetMapping("/conta/")
    public ArrayList<Conta> getContas() {
        
        return bancoServices.getContas();
    }

    @GetMapping("/conta/{id}/saldo/")
    public Double getSaldo(@PathVariable(value = "id") int id) {
        Optional<Conta> conta = bancoServices.getConta(id);
        
        if(conta.isPresent()) {
            return conta.get().getSaldo();
        }else {
            return null;//TODO: lançar exceção
        }
        
    }

    @PutMapping("/conta/{id}/credito")
    public boolean creditarConta(@PathVariable(value = "id") int id, @RequestBody(required = true) Map<String, Double> valor) {
            
            return bancoServices.depositarValor(id, valor.get("valor"));
            
    }

    @PutMapping("/conta/{id}/debito")
    public int debitarConta(@PathVariable(value = "id") int id, @RequestBody(required = true) Map<String, Double> valor) {
        
            return bancoServices.debitar(id, valor.get("valor"));
        
    }

    @PutMapping("/conta/transferencia")
    public int transferir(@RequestBody() Map<String, Object> map) {
        return bancoServices.transferir((int) map.get("from"), (int) map.get("to"), Double.parseDouble(map.get("valor").toString()));
    }

    @PutMapping("/conta/rendimento")
    public void renderJuros(@RequestBody() Map<String, Double> map) {
        Double taxa = map.get("taxa");
        bancoServices.renderJuros(taxa);
    }

}
