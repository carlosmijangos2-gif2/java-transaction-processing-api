package com.example.demo.transactionstorage.dto;

import java.math.BigDecimal;

public class TransactionRequest {

    private String operacion;
    private BigDecimal importe;
    private String cliente;
    private String estatus;

    public TransactionRequest() {
    }

    public TransactionRequest(
            String operacion,
            BigDecimal importe,
            String cliente,
            String estatus) {

        this.operacion = operacion;
        this.importe = importe;
        this.cliente = cliente;
        this.estatus = estatus;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
}