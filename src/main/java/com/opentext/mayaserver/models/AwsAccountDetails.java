package com.opentext.mayaserver.models;

import com.opentext.mayaserver.models.vo.AccountMetadataVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AwsAccountDetails {

    private String rootAccountID;

    private List<AccountMetadataVO> accountMetadataVOList;

}
