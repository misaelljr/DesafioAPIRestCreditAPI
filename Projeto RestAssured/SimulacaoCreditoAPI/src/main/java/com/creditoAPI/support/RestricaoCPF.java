package com.creditoAPI.support;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

import com.creditoAPI.utils.GeraValidaCPF;

public class RestricaoCPF {
    static List<String> cpfRestricao = Arrays.asList("97093236014","60094146012","84809766080","62648716050","26276298085","01317496094","55856777050","19626829001","24094592008","58063164083");
    GeraValidaCPF geradorCPF;
    String cpfSemRestricao;
    String cpfComRestricao;
    SecureRandom random;

    public String getCpfComRestricao() {
        random = new SecureRandom();
        return this.cpfComRestricao = cpfRestricao.get(random.nextInt(cpfRestricao.size()));
    }

    public String getCpfSemRestricao(){

        geradorCPF = new GeraValidaCPF();

        this.cpfSemRestricao = geradorCPF.cpf(false);

        if (cpfRestricao.contains(this.cpfSemRestricao)){
            this.cpfSemRestricao = geradorCPF.cpf(false);
        }

        return this.cpfSemRestricao;

    }

}
