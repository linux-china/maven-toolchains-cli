package org.mvnsearch.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.Nullable;
import org.mvnsearch.model.adoptium.JdkBinary;
import org.mvnsearch.model.adoptium.JdkPackage;
import org.mvnsearch.model.adoptium.JdkRelease;
import org.mvnsearch.service.AdoptiumService;
import org.mvnsearch.service.JdkDownloadLink;
import org.springframework.stereotype.Service;

import java.net.URL;

@Service
public class AdoptiumServiceImpl extends JdkDistributionSupport implements AdoptiumService {
    private static final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Nullable
    @Override
    public JdkDownloadLink findReleases(String version) throws Exception {
        String majorVersion = version;
        if (majorVersion.startsWith("1.8")) {
            majorVersion = "8";
        }
        if (majorVersion.contains(".")) {
            majorVersion = majorVersion.substring(0, majorVersion.indexOf("."));
        }
        URL link = new URL("https://api.adoptium.net/v3/assets/latest/" + majorVersion + "/hotspot?release=latest&jvm_impl=hotspot&vendor=adoptium");
        final JdkRelease[] releases = objectMapper.readValue(link, JdkRelease[].class);
        String os = getOsName();
        String arch = getArchName();
        for (JdkRelease release : releases) {
            JdkBinary binary = release.getBinary();
            if (binary.getImageType().equals("jdk") && os.contains(binary.getOs()) && arch.contains(binary.getArchitecture())) {
                JdkPackage jdkPackage = binary.getJdkPackage();
                return new JdkDownloadLink(jdkPackage.getLink(), jdkPackage.getName());
            }
        }
        return null;
    }

}
