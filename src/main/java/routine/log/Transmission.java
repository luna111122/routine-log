package routine.log;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Transmission {



    @GetMapping(value="/message", produces=MediaType.APPLICATION_JSON_VALUE)
    public Map<String,String> message(){ return Map.of("message","Success!!"); }



}




