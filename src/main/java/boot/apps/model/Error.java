package boot.apps.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

public class Error implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String code;
	private String field;
	private String message;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer sequenceNo;
	private String additionalInfo;
	
	public Error() {	
	}
	
	public Error(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public Error(String code) {
		this.code = code;
	}
	
	public Error(String code, String message, String additionalInfo, Integer sequenceNo) {
		this.code = code;
		this.message = message;
		this.additionalInfo = additionalInfo;
		this.sequenceNo = sequenceNo;
	}
	
	public Error(String code, String field, String additionalInfo) {
		this.code = code;
		this.field = field;
		this.message = additionalInfo;
	}
	
	public Error(Integer sequenceNo, String code, String field) {
		this.sequenceNo = sequenceNo;
		this.code = code;
		this.field = field;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getField() {
		return field;
	}
	
	public void setField(String field) {
		this.field = field;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getAdditionalInfo() {
		return additionalInfo;
	}
	
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	
	public Integer getSequenceNo() {
		return sequenceNo;
	}
	
	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Error [code=");
		builder.append(code);
		builder.append(", field=");
		builder.append(field);
		builder.append(", message=");
		builder.append(message);
		builder.append(", sequenceNo=");
		builder.append(sequenceNo);
		builder.append(", additionalInfo=");
		builder.append(additionalInfo);
		builder.append("]");
		
		return builder.toString();
	}

}
