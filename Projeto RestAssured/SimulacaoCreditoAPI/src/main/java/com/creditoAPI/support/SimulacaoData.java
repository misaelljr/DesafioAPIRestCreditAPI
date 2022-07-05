package com.creditoAPI.support;

import com.creditoAPI.utils.GeraValidaCPF;
import com.github.javafaker.Faker;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;

import static io.restassured.RestAssured.*;

public class SimulacaoData {

    SecureRandom random;
    Faker faker;
    GeraValidaCPF geradorCPF;
    public List<String[]> getSetSimulacoesCadastradas() {

        List<String[]> getSetSimulacoesCadastradas =
                given()
                .when()
                .get("/simulacoes/")
                .then()
                .assertThat()
                .extract()
                .response().jsonPath().getList("$");

        return getSetSimulacoesCadastradas;
    }

    public List<String> getSetCPFCadastrado() {

        List<String> getSetCPFCadastrado =
                given()
                .when()
                .get("/simulacoes/")
                .then()
                .assertThat()
                .extract()
                .response().jsonPath().getList("cpf");

        return getSetCPFCadastrado;
    }

    public List<Integer> getSetIDCadastrado() {

        List<Integer> getSetIDCadastrado =
                given()
                .when()
                .get("/simulacoes/")
                .then()
                .assertThat()
                .extract()
                .response().jsonPath().getList("id");

        return getSetIDCadastrado;
    }

    public JSONObject getValidoJSONRandomFromDataPOST() {

        faker = new Faker();
        geradorCPF = new GeraValidaCPF();

        JSONObject requestParams = new JSONObject();
        requestParams.put("nome", faker.name().firstName());
        requestParams.put("cpf", geradorCPF.cpf(false));
        requestParams.put("email", faker.internet().emailAddress());
        requestParams.put("valor", new BigDecimal(faker.number().numberBetween(1000, 40000)));
        requestParams.put("parcelas",faker.number().numberBetween(2, 48));
        requestParams.put("seguro", faker.bool().bool());

        return requestParams;
    }

    public JSONObject getInvalidoJSONRandomFromDataPOST() {

        faker = new Faker();
        geradorCPF = new GeraValidaCPF();

        JSONObject requestParams = new JSONObject();
        requestParams.put("nome", StringUtils.EMPTY);
        requestParams.put("cpf", StringUtils.EMPTY);
        requestParams.put("email", faker.internet().emailAddress());
        requestParams.put("valor", faker.number().numberBetween(2, 500));
        requestParams.put("parcelas",faker.number().numberBetween(0, 1));
        requestParams.put("seguro", faker.bool().bool());

        return requestParams;
    }

    public JSONObject getInvalidoCPFDuplicadoJSONRandomFromDataPOST(String cpf) {

        faker = new Faker();
        geradorCPF = new GeraValidaCPF();

        JSONObject requestParams = new JSONObject();
        requestParams.put("nome", faker.name().firstName());
        requestParams.put("cpf", cpf);
        requestParams.put("email", faker.internet().emailAddress());
        requestParams.put("valor", new BigDecimal(faker.number().numberBetween(1000, 40000)));
        requestParams.put("parcelas",faker.number().numberBetween(2, 48));
        requestParams.put("seguro", faker.bool().bool());

        return requestParams;
    }

    public JSONObject getValidoAtualizarSimulacaoJSONRandomFromDataPOST(String cpf) {

        faker = new Faker();
        geradorCPF = new GeraValidaCPF();

        JSONObject requestParams = new JSONObject();
        requestParams.put("nome", faker.name().firstName());
        requestParams.put("cpf", cpf);
        requestParams.put("email", faker.internet().emailAddress());
        requestParams.put("valor", new BigDecimal(faker.number().numberBetween(1000, 40000)));
        requestParams.put("parcelas",faker.number().numberBetween(2, 48));
        requestParams.put("seguro", faker.bool().bool());

        return requestParams;
    }
}
