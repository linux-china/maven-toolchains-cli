package org.mvnsearch.service.impl;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.jetbrains.annotations.Nullable;
import org.mvnsearch.model.Toolchain;
import org.mvnsearch.model.Toolchains;
import org.mvnsearch.service.ToolchainService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class ToolchainServiceImpl implements ToolchainService {
    private final XmlMapper xmlMapper = new XmlMapper();

    @Override
    public List<Toolchain> findAllToolchains() {
        try {
            return xmlMapper.readValue(getToolchainsXml(), Toolchains.class).getToolchain();
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    @Nullable
    public Toolchain findToolchain(String version, @Nullable String vendor) {
        return findAllToolchains().stream()
                .filter(toolchain -> toolchain.findVersion().equals(version))
                .filter(toolchain -> vendor == null || vendor.equals(toolchain.findVendor()))
                .findFirst().orElse(null);
    }

    private File getToolchainsXml() {
        String userHome = System.getProperty("user.home");
        return new File(new File(userHome), ".m2/toolchains.xml");
    }
}
