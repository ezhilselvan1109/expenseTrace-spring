package com.expensetrace.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Attachment {
    @Id @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    private String fileName;
    private String contentType;

    @Lob
    private byte[] data; // PDF or image
}
