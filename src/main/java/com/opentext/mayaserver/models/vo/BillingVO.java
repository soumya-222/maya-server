package com.opentext.mayaserver.models.vo;

import lombok.Data;

import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

@Data
public class BillingVO {

    @PositiveOrZero(message = "Records per day can't be negative value")
    private int recordsPerDay;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate dayOfInvoice;
    private LocalDate currentDate;
}
