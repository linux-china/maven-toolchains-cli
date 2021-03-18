package org.mvnsearch.service.impl;

import org.junit.jupiter.api.Test;
import org.mvnsearch.model.Toolchain;

public class ToolchainServiceImplTest {

    private final ToolchainServiceImpl toolchainService = new ToolchainServiceImpl();

    @Test
    public void testFindAllToolchains() {
        for (Toolchain toolchain : toolchainService.findAllToolchains()) {
            System.out.println(toolchain.findVersion());
        }
    }
}
