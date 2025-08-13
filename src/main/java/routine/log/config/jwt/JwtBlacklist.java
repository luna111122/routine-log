package routine.log.config.jwt;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JwtBlacklist {

    private final Map<String, Long> blacklist = new ConcurrentHashMap<>();

    public void add(String token, long expiresAtMillis) {
        blacklist.put(token, expiresAtMillis);
    }

    public boolean contains(String token) {
        Long until = blacklist.get(token);
        if(until == null) return false;
        if(until <System.currentTimeMillis()){
            blacklist.remove(token);
            return false;
        }

        return true;
    }
}
