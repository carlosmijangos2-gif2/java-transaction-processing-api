package com.example.demo.transactionprocessing.dto;


public class TransactionResponse {
    private String operacion;
    private String cliente;
    private String estatus;
    
    public TransactionResponse(
            String operacion,
            String cliente,
            String estatus) {

        this.operacion = operacion;
        this.cliente = cliente;
        this.estatus = estatus;
    }
	public String getOperacion() {
		return operacion;
	}
	public void setOperacion(String operacion) {
		this.operacion = operacion;
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