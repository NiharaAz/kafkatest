package kafkaResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Attempt {
    int attempt;
    boolean maxAttempt;
}
