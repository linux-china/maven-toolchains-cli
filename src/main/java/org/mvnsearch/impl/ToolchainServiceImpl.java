package org.mvnsearch.impl;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.mvnsearch.ToolchainService;
import org.mvnsearch.model.Toolchain;
import org.mvnsearch.model.Toolchains;
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

    private File getToolchainsXml() {
        String userHome = System.getProperty("user.home");
        return new File(new File(userHome), ".m2/toolchains.xml");
    }
}
