package kafkaResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@lombok.Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KafkaJsonWhole {

    String transaction_id;
    String transaction_category;
    String location_id;
    String created_time;
    String sent_time;
    String touchpoint_id;
    String touchpoint_type;

    Data data;
    int file_details;

}
