package shoppingMall.gupang.replication.domaincontroller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@Transactional
@RequiredArgsConstructor
public class RollBackController {

    private final UserRepository userRepository;

    @PostMapping("/jpa/rollback")
    public ResponseEntity<User> checkJpaTransactionRollBack(@RequestParam @NotNull String username) {
        User testUser = new User();
        testUser.setName(username);

        userRepository.save(testUser);
        throw new RuntimeException("RollBack");

    }

    @PostMapping("/jpa")
    public ResponseEntity<Object> checkJpaTransaction(@RequestParam @NotNull String username) {
        User testUser = new User();
        testUser.setName(username);

        userRepository.save(testUser);
        return ResponseEntity.ok().build();
    }

}
