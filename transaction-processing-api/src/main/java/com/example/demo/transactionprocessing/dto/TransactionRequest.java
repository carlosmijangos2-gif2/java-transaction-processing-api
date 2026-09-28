package com.example.demo.transactionprocessing.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class TransactionRequest {

    

    @NotBlank(message = "La operación es obligatoria")
    @Size(min = 3, max = 20,
          message = "La operación debe tener entre 3 y 20 caracteres")
    @Pattern(
        regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$",
        message = "La operación solo debe contener caracteres"
    )
    private String operacion;


    @NotBlank(message = "El importe es obligatorio")
    @Pattern(
        regexp = "^\\d+(\\.\\d{2})$",
        message = "El importe debe tener formato monetario, ejemplo: 100.00"
    )
    private String importe;


    @NotBlank(message = "El cliente es obligatorio")
    @Size(min = 2, max = 100,
          message = "El cliente debe tener entre 2 y 100 caracteres")
    @Pattern(
        regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$",
        message = "El cliente solo debe contener caracteres"
    )
    private String cliente;


    @NotBlank(message = "El secreto es obligatorio")
    private String secreto;
    
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
	public String getSecreto() {
		return secreto;
	}
	public void setSecreto(String secreto) {
		this.secreto = secreto;
	}
}
    
    
	
  