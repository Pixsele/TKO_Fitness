package tko.controller.user;

import tko.model.dto.user.UsersDTO;
import tko.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер управления пользователем.
 * <p>
 * Методы:
 * <ul>
 *     <li>{@link #getUser(Long)} — получение данных пользователя по идентификатору</li>
 *     <li>{@link #updateUser(Long, UsersDTO)} — обновление данных пользователя</li>
 * </ul>
 */

@RestController
@RequestMapping("/user")
public class UsersController {

    private final UsersService usersService;

    @Autowired
    UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsersDTO> getUser(@PathVariable Long id){
        UsersDTO usersDTO = usersService.getUser(id);
        return ResponseEntity.ok(usersDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsersDTO> updateUser(@PathVariable Long id, @RequestBody UsersDTO usersDTO){
        UsersDTO newUser = usersService.updateUser(id, usersDTO);
        return ResponseEntity.ok(newUser);
    }
}
