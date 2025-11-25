package com.teste.sat.enums;

public enum ErrorMessage {
    CLIENTE_NAO_ENCONTRADO("Cliente não encontrado."),
    CUSTOMER_NOT_FOUND("Cliente não encontrado."),
    CLIENTE_SALVAR_ERRO("Erro ao salvar o cliente."),
    CUSTOMER_SAVE_ERROR("Erro ao salvar o cliente."),
    CUSTOMER_EMAIL_ERROR("Erro ao salvar o cliente. Já existe um cliente com este e-mail cadastrado!"),
    CUSTOMER_UPDATE_ERROR("Erro ao atualizar o cliente."),
    CUSTOMER_DELETE_ERROR("Erro ao excluir o cliente."),
    STATUS_NOT_FOUND("Status não encontrado."),
    STATUS_ID_INVALID("Status inválido(s): ");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}

