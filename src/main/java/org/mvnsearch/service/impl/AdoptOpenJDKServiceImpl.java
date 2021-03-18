package org.mvnsearch.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mvnsearch.model.jdk.JdkRelease;
import org.mvnsearch.service.AdoptOpenJDKService;
import org.springframework.stereotype.Service;

import java.net.URL;

@Service
public class AdoptOpenJDKServiceImpl implements AdoptOpenJDKService {
    private static ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    public JdkRelease[] findReleases(String version) throws Exception {
        String majorVersion = version;
        if (majorVersion.startsWith("1.8")) {
            majorVersion = "8";
        }
        if (majorVersion.contains(".")) {
            majorVersion = majorVersion.substring(0, majorVersion.indexOf("."));
        }
        URL link = new URL("https://api.adoptopenjdk.net/v3/assets/latest/" + majorVersion + "/hotspot?release=latest&jvm_impl=hotspot&vendor=adoptopenjdk");
        return objectMapper.readValue(link, JdkRelease[].class);
    }
}
