package core.dto;

import core.database.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUsersDTO {
    private String name;
    private String login;
    private String password;
    private Integer age;
    private Double weight;
    private Double height;
    private Gender gender;
}
