package ngv.vn.naascccd.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Getter
@Setter
public class Card {

    private String sodData;
    private String dg1DataB64;
    private String dg2DataB64;
    private String dg13DataB64;
    private String dg14DataB64;
}
