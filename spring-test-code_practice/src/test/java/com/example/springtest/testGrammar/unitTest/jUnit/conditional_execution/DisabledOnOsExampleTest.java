package com.example.springtest.testGrammar.unitTest.jUnit.conditional_execution;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

@Slf4j
public class DisabledOnOsExampleTest {

    @DisabledOnOs(OS.WINDOWS)
   @Test
   void testNotOnWindows() {
      log.info("testNotOnWindows");
   }

   @DisabledOnOs(OS.MAC)
    @Test
    void testNotOnMac() {
        log.info("testNotOnMac");
   }

   @DisabledOnOs(value = {OS.MAC}, architectures = {"x86_64"})
   @Test
   void testNotOnMacX86() {
        log.info("testNotOnMacX86");
   }

    @DisabledOnOs(value = {OS.MAC}, architectures = {"aarch64"})
    @Test
    void testNotOnMacArch64() {
        log.info("testNotOnMacAarch64");
    }
}