package com.yms.user_service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * The Class Response.
 *
 * @param <T> the generic type
 */
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse<T> implements Serializable {
	
	/** Serial version Id. */
	private static final long serialVersionUID = -8877699538349583940L;
	
	/** The status. */
	private Integer status;
	
	/** The message. */
	private String message;
	
	/** The data. */
	private T data;

	/**
	 * Instantiates a new response.
	 *
	 * @param status the status
	 */
	public ApiResponse(Integer status) {
		super();
		this.status = status;
	}

	/**
	 * Instantiates a new response.
	 *
	 * @param status the status
	 * @param message the message
	 */
	public ApiResponse(Integer status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	/**
	 * Instantiates a new response.
	 *
	 * @param status the status
	 * @param message the message
	 * @param data the data
	 */
	public ApiResponse(Integer status, String message, T data) {
		super();
		this.status = status;
		this.message = message;
		this.data = data;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 *
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public T getData() {
		return data;
	}

	/**
	 * Sets the data.
	 *
	 * @param data the data to set
	 */
	public void setData(T data) {
		this.data = data;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "Response [status=" + status + ", message=" + message + ", data=" + data + "]";
	}
	
	private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        if (data instanceof Serializable) {
            oos.writeObject(data);
        } else {
            oos.writeObject(null); // Handle non-serializable data
        }
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        data = (T) ois.readObject();
    }

}
