package org.sqlunet;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestPlugin
{
	private static final String ID = "org.sqlunet.plugin.querybuilder";

	@Test
	public void pluginTest(){
		Project project = ProjectBuilder.builder().build();
		project.getPluginManager().apply(ID);

		assertTrue(project.getPluginManager()
				.hasPlugin(ID));

		assertNotNull(project.getTasks().getByName("generateV"));
		assertNotNull(project.getTasks().getByName("generateQV"));
		assertNotNull(project.getTasks().getByName("generateV"));
	}
}
