package boot.apps.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ResponseHelper implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Metadata metadata;
	private transient Object data;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<Error> errors;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Pagination pagination;
	
	public ResponseHelper() {
	}
	
	public ResponseHelper(Object data, String uri) {
		this.data = data;
	}
	
	public ResponseHelper(Object data, Metadata metadata) {
		this.data = data;
		this.metadata = metadata;
	}
	
	public ResponseHelper(Object data, Metadata metadata, Pagination pagination) {
		this.data = data;
		this.metadata = metadata;
		this.pagination = pagination;
	}
	
	public ResponseHelper(Object data, Metadata metadata, Pagination pagination, List<Error> errors) {
		this.data = data;
		this.metadata = metadata;
		this.pagination = pagination;
		this.errors = errors;
	}
	
	public static ResponseHelper serializeException(Metadata metadata, List<Error> errors, int httpCode) {
		ResponseHelper js = new ResponseHelper();
		metadata.setHttpCode(httpCode);
		js.setMetadata(metadata);
		js.setErrors(errors);
		return js;
	}
	
	public static ResponseHelper serializeException(String errorMsg, int code) {
		ResponseHelper js = new ResponseHelper();
		List<Error> lsError = new ArrayList<>();
		Error error = new Error();
		error.setCode(Integer.toString(code));
		error.setMessage(errorMsg);
		lsError.add(error);
		js.setErrors(lsError);
		return js;
	}
	
	public Metadata getMetadata() {
		return metadata;
	}
	
	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}
	
	public Object getData() {
		return data;
	}
	
	public void setData(Object data) {
		this.data = data;
	}
	
	public Pagination getPagination() {
		return pagination;
	}
	
	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}
	
	public List<Error> getErrors() {
		return errors;
	}
	
	public void setErrors(List<Error> errors) {
		this.errors = errors;
	}

}
