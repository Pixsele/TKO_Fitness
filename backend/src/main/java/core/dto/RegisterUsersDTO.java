package core.dto;

import lombok.Data;

@Data
public class RegisterUsersDTO {
    private String name;
    private String login;
    private String password;
    private Integer age;
    private Double weight;
    private Double height;
}
