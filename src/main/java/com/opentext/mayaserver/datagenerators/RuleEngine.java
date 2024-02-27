package com.opentext.mayaserver.datagenerators;

import com.opentext.mayaserver.models.vo.UseCaseVO;

import java.io.IOException;

/**
 * @author Rajiv
 */
public interface RuleEngine {

    void cloudProviderDelegator(UseCaseVO useCaseVO) throws IOException;
}

