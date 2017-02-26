package cane.brothers.rpc.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Created by cane on 26.02.17.
 */
@RestController
@RequestMapping("/api/rpc")
public class RpcUserController extends BaseController {

    @GetMapping("/user")
    public Principal getAuthUser(Principal principal) {
        logger.info(" get '/api/rpc/user'");
        if (principal != null) {
            logger.debug("авторизованный пользователь: {}", principal.getName());
        }
        return principal;
    }
}
