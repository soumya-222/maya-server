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
@Table(name = "account_data")
public class AccountData {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id")
    private String id;

    @Column(name = "root_account")
    private String rootAccount;

    @Column(name = "member_accounts")
    private String memberAccounts;
}
