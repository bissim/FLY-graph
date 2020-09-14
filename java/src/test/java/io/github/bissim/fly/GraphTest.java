package io.github.bissim.fly;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.IncludeClassNamePatterns;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectPackages("io.github.bissim.fly")
@IncludeClassNamePatterns({"^.*Test?$"})
public class GraphTest {}
