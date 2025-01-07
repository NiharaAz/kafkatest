package kafkaResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class kafkaJsondoc {
    String type;
    String pptNum;
    String optionalElement;
    String natcd ;
    String dob;

    String terminal;
    String direction;
    String fltArrvlDateTime ;
    String docId;


}
