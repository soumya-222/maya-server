package com.opentext.mayaserver.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author Rajiv
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "account_metadata")
public class AccountMetadata {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id")
    private String id;

    @Column(name = "endpoint_type")
    private String endpointType;

    @Column(name = "data_file_path")
    private String dataFilePath;

}
