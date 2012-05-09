/**
 * 
 */
package org.sopeco.core.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;
import org.sopeco.core.SoPeCoRunner;
import org.sopeco.engine.registry.ExtensionRegistry;
import org.sopeco.engine.registry.IExtensionRegistry;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.PersistenceProviderFactory;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetInputColumn;
import org.sopeco.persistence.entities.ScenarioInstance;

/**
 * This is a test for running SoPeCo as a simple Java application.
 * 
 * @author Roozbeh Farahbod
 *
 */
public class SoPeCoExecutableTest {

	private static final Logger logger = LoggerFactory.getLogger(SoPeCoExecutableTest.class);
	
	public static void main(String[] args) {
//		testPluginRegistry();
		
		run();
		
	}

	/* 
	 * Test one run of SoPeCo
	 */
	public static void run() {
		
		/*
		 * If you would like to mimic the command-line call of the runner,
		 * you would use the following code:
		 *
		 
			String[] arguments = new String[] {
					"-meClass", "org.sopeco.core.test.SampleMEController",
					"-sd", "rsc/test.configuration"
			};
			
			SoPeCoRunner runner = new SoPeCoRunner();
			runner.setArguments(arguments);
			runner.run();
		/**/
		/**/
		IConfiguration config = Configuration.getSingleton();

		// setting the MEController class name
		final String className = "org.sopeco.core.test.SampleMEController";
		config.setProperty(IConfiguration.CONF_MEASUREMENT_CONTROLLER_CLASS_NAME, "org.sopeco.core.test.SampleMEController");
		config.setProperty(IConfiguration.CONF_MEASUREMENT_CONTROLLER_URI, "class://" + className);
		
		// setting the scenario definition file name
		config.setProperty(IConfiguration.CONF_SCENARIO_DESCRIPTION_FILE_NAME, "rsc/test.configuration");

		
		
		SoPeCoRunner runner = new SoPeCoRunner();
		runner.run();
		/**/
		
		ScenarioInstance si = runner.getScenarioInstance();
		
		DataSetAggregated data = si.getExperimentSeriesList().get(0).getExperimentSeriesRuns().get(0).getResultDataSet();
		
		logger.debug("Logging the result of the first experiement series run:");
		
		for (DataSetInputColumn<?> ic: data.getInputColumns()) {
			logger.debug("{}: {}", ic.getParameter().getName(), ic.getValueList());
		}

		IPersistenceProvider pp = PersistenceProviderFactory.getPersistenceProvider();
	}
	
	public static void testPluginRegistry() {
		IConfiguration conf = Configuration.getSingleton();
		IExtensionRegistry registry = ExtensionRegistry.getSingleton();
	}
}
