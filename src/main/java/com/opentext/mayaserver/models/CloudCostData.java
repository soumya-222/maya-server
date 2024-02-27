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
@Table(name = "cloud_cost_data")
public class CloudCostData {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id")
    private String id;

    @OneToOne(targetEntity = UseCase.class,  orphanRemoval = true)
    @JoinColumn(name = "use_case_id", referencedColumnName = "id")
    private UseCase useCaseId;

    @Column(name = "root_account_cost")
    private String rootAccountCost;

    @Column(name = "member_account_cost")
    private String memberAccountCost;
}
