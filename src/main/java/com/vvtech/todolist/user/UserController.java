package com.vvtech.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private IUserRepository userRepository;

  @PostMapping("/")
  public ResponseEntity create(@RequestBody UserModel userModel) {
    var isUserExists = this.userRepository.findByUsername(userModel.getUsername()) != null ? true : false;

    if (isUserExists) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("ERRO: Usuário existente!");
    }

    userModel.setPassword(BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray()));

    return ResponseEntity.status(HttpStatus.OK)
        .body(this.userRepository.save(userModel));
  }
}
