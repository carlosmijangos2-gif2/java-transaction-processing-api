package com.example.demo.transactionprocessing.dto;

import java.math.BigDecimal;

public class TransactionStorageRequest {

    private String operacion;
    private String importe;
    private String cliente;
    private String estatus;

    public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public TransactionStorageRequest() {
    }

    public TransactionStorageRequest(
            String operacion,
            String importe,
            String cliente, String estatus) {
    	this.estatus=estatus;
        this.operacion = operacion;
        this.importe = importe;
        this.cliente = cliente;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }
}