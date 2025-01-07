package kafkaResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@lombok.Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Data {
    Attempt attempt;

    String personId;
    List<Biometric> biometric;
    List<kafkaJsondoc> doc;
    String messageCreatedDateTime;
    String messageDateTime;

}
