package com.gina.simulator.address;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class Address {

    private String district;

    private String municipality;

    private String longitude;

    private String latitude;
}
