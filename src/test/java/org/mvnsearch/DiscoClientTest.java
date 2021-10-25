package org.mvnsearch;

import io.foojay.api.discoclient.DiscoClient;
import io.foojay.api.discoclient.pkg.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Queue;


public class DiscoClientTest {
    private DiscoClient discoClient = new DiscoClient();

    @Test
    public void testListDistribution() {
        final List<Distribution> distributions = discoClient.getDistributions();
        for (Distribution distribution : distributions) {
            System.out.println(distribution.getApiString());
        }
    }

    @Test
    public void testFindJDK() {
        //version: 6-16
        final Queue<MajorVersion> allMajorVersions = discoClient.getAllMajorVersions();
        //distribution: ORACLE_OPEN_JDK, ORACLE, GRAALVM_CE11, ZULU
        //distribution for version
        //openjdk-11.0.2_osx-x64_bin.tar.gz
        final List<Pkg> pkgs = discoClient.getPkgs(
                List.of(new Distribution(null, null, "oracle")),
                new VersionNumber(17),
                Latest.OVERALL,
                OperatingSystem.MACOS,
                LibCType.LIBC,
                Architecture.X64,
                Bitness.BIT_64,
                ArchiveType.TAR_GZ,
                PackageType.JDK,
                false,
                true,
                List.of(ReleaseStatus.GA),  //GA or EA
                TermOfSupport.NONE,  // LTS, MTS, STS
                List.of(Scope.DIRECTLY_DOWNLOADABLE),
                Match.ANY);
        pkgs.forEach(pkg -> {
            System.out.println(pkg);
            System.out.println(pkg.getId());
        });
    }

    @Test
    public void testPrintDownloadUrl() {
        String pkgId = "5b43d63862207af0091ab9b6cfb0d4c0";
        final Pkg pkg = discoClient.getPkg(pkgId);
        final String pkgDirectDownloadUri = discoClient.getPkgDirectDownloadUri(pkg.getEphemeralId());
        System.out.println(pkgDirectDownloadUri);
    }

    @Test
    public void testGraalVMDownload() {
        //graalvm-ce-java11-darwin-amd64-21.2.0.tar.gz
        final List<Pkg> pkgs = discoClient.getPkgs(
                List.of(new Distribution(null, null, "graalvm_ce11")),
                new VersionNumber(21), //GraalVM version starts with 21
                Latest.OVERALL,
                OperatingSystem.MACOS,
                LibCType.LIBC,
                Architecture.AMD64,
                Bitness.BIT_64,
                ArchiveType.TAR_GZ,
                PackageType.JDK,
                false,
                true,
                List.of(ReleaseStatus.GA),  //GA or EA
                TermOfSupport.NONE,  // LTS, MTS, STS
                List.of(Scope.DIRECTLY_DOWNLOADABLE),
                Match.ANY);
        pkgs.forEach(System.out::println);
    }
}
