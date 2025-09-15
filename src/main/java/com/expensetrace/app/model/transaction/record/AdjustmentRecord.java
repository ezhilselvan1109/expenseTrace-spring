package com.expensetrace.app.model.transaction.record;

import com.expensetrace.app.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "debt_adjustment_records")
@Getter @Setter @NoArgsConstructor
@DiscriminatorValue("DEBT_ADJUSTMENT")
public class AdjustmentRecord extends DebtRecord {

}
