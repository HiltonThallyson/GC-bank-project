package br.ufrn.gc.bank.gcbank.controllers;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public int cadastrarConta(@RequestBody() Map<String, Object> map) {
        
        return bancoServices.criarConta(((int)map.get("numeroDaConta")), ((int)map.get("tipoDaConta")), Double.parseDouble(map.get("saldo").toString()));
    }

    @GetMapping("/conta/{id}")
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

    @GetMapping("/conta/{numeroDaConta}/saldo/")
    public Double getSaldo(@PathVariable(value = "numeroDaConta") int numeroDaConta) {
        Optional<Conta> conta = bancoServices.getConta(numeroDaConta);
        
        if(conta.isPresent()) {
            return conta.get().getSaldo();
        }else {
            return null;//TODO: lançar exceção
        }
        
    }

}
