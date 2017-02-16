package cane.brothers.rpc;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class RpcUtils {

    /**
     *
     * @return
     */
    public static String getGroupName() {
        String triggerGroup = "RpcGroup";

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            triggerGroup = authentication.getName() + "Group";
        }
        return triggerGroup;
    }
}
