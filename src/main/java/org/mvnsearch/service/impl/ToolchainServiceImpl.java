package org.mvnsearch.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.jetbrains.annotations.Nullable;
import org.mvnsearch.model.Toolchain;
import org.mvnsearch.model.Toolchains;
import org.mvnsearch.service.ToolchainService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class ToolchainServiceImpl implements ToolchainService {
    private final ObjectMapper xmlMapper = new XmlMapper().configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true).enable(SerializationFeature.INDENT_OUTPUT);

    @Override
    public Toolchains readToolchains() {
        try {
            return xmlMapper.readValue(getToolchainsXml(), Toolchains.class);
        } catch (IOException e) {
            return new Toolchains();
        }
    }

    @Override
    public List<Toolchain> findAllToolchains() {
        return readToolchains().getToolchain();
    }

    @Override
    @Nullable
    public Toolchain findToolchain(String version, @Nullable String vendor) {
        return readToolchains().findToolchain(version, vendor);
    }

    @Override
    public boolean deleteToolChain(String version, @Nullable String vendor) {
        Toolchains toolchains = readToolchains();
        try {
            Toolchain toolchain = toolchains.findToolchain(version, vendor);
            if (toolchain != null) {
                toolchains.getToolchain().remove(toolchain);
                xmlMapper.writeValue(getToolchainsXml(), toolchains);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private File getToolchainsXml() {
        String userHome = System.getProperty("user.home");
        return new File(new File(userHome), ".m2/toolchains.xml");
    }
}
