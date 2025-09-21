package me.vyashemang.spring_redis_restaurant.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> implements Serializable {
    private int status;
    private String message;
    private T data;
    private boolean success;

    // Helper methods
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<T>()
                .setStatus(200)
                .setMessage("Success")
                .setData(data)
                .setSuccess(true);
    }
    
    public static <T> BaseResponse<T> success(String message, T data) {
        return new BaseResponse<T>()
                .setStatus(200)
                .setMessage(message)
                .setData(data)
                .setSuccess(true);
    }

    
    public static <T> BaseResponse<T> created(String message, T data) {
        return new BaseResponse<T>()
                .setStatus(201)
                .setMessage(message)
                .setData(data)
                .setSuccess(true);
    }
    
    public static <T> BaseResponse<T> error(int status, String message) {
        return new BaseResponse<T>()
                .setStatus(status)
                .setMessage(message)
                .setSuccess(false);
    }
    
    public static <T> BaseResponse<T> notFound(String message) {
        return new BaseResponse<T>()
                .setStatus(404)
                .setMessage(message)
                .setSuccess(false);
    }
    
    public static <T> BaseResponse<T> badRequest(String message) {
        return new BaseResponse<T>()
                .setStatus(400)
                .setMessage(message)
                .setSuccess(false);
    }
}
