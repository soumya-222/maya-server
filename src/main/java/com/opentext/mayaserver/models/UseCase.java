package com.opentext.mayaserver.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "maya_use_case")
public class UseCase {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id")
    private String id;

    @Column(name = "use_case_name")
    private String useCaseName;

    @Column(name = "cloud_provider")
    private String cloudProvider;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private StateEnum state;

    @Column(name = "account_url")
    private String accountURL;

    @Column(name = "mockoon_port")
    private int mockoonPort;

    @Column(name = "billing_url")
    private String billingURL;

    @Column(name = "recommendation_url")
    private String recommendationURL;

    @Column(name = "payload")
    private String payload;

    @Column(name = "is_demo_mode_enabled")
    private Boolean isDemoModeEnabled;

    @Column(name = "demo_root_account_id")
    private String demoRootAccountId;

    @OneToMany(targetEntity = AccountMetadata.class, fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "use_case_id", referencedColumnName = "id")
    private List<AccountMetadata> accountMetadataList;

    @OneToMany(targetEntity = AccountData.class, fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "use_case_id", referencedColumnName = "id")
    private List<AccountData> accountDataList;

}
