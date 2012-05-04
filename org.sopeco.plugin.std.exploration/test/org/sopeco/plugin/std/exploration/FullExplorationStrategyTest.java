package org.sopeco.plugin.std.exploration;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.sopeco.engine.EngineFactory;
import org.sopeco.engine.IEngine;
import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.experimentseries.IExplorationStrategy;
import org.sopeco.engine.experimentseries.IParameterVariation;
import org.sopeco.engine.experimentseries.IParameterVariationExtension;
import org.sopeco.engine.helper.ConfigurationBuilder;
import org.sopeco.engine.helper.DummyMEController;
import org.sopeco.engine.registry.IExtensionRegistry;
import org.sopeco.persistence.dataset.util.ParameterType;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.definition.DynamicValueAssignment;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.ExplorationStrategy;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.plugin.std.exploration.full.FullExplorationStrategyExtension;

public class FullExplorationStrategyTest {

	static final double MIN = 4;
	static final double MAX = 28.99999999999;
	static final double STEP = 12.5;
	private static final String VARIATION_NAME = "Linear Numeric Variation";

	IExplorationStrategy fes = null;
	
	private DummyMEController meController;
	private IExperimentController expController;
	private ExperimentSeriesDefinition expSeriesDef; 
	private ConfigurationBuilder builder;
	private DynamicValueAssignment dva;
	
	private IEngine engine;
	
	@Before
	public void setUp() throws Exception {
		engine = EngineFactory.INSTANCE.createEngine();
		
		builder = new ConfigurationBuilder("test");
		builder.createNamespace("initialization");
		ParameterDefinition pdef = builder.createParameter("initParameter", ParameterType.DOUBLE, ParameterRole.INPUT);

		FullExplorationStrategyExtension fese = new FullExplorationStrategyExtension();
		fes = fese.createExtensionArtifact();

		meController = new DummyMEController();
		expController = builder.createExperimentController(meController);
		
		expSeriesDef = new ExperimentSeriesDefinition();
		builder.createNumberOfRunsCondition(2);
		expSeriesDef.setExperimentTerminationCondition(builder.getTerminationCondition());
		
		ExplorationStrategy es = new ExplorationStrategy();
		es.setName(FullExplorationStrategyExtension.NAME);
		expSeriesDef.setExplorationStrategy(es);
		
		expSeriesDef.setName("experiment series");
		
		Map<String, String> config = new HashMap<String, String>();
		config.put("min", String.valueOf(MIN));
		config.put("max", String.valueOf(MAX));
		config.put("step", String.valueOf(STEP));
		
		dva = builder.createDynamicValueAssignment(VARIATION_NAME, pdef, config);
	}

	@Test
	public void testCanRun() {
		fes.canRun(expSeriesDef);
	}

	@Test
	public void testRunExperimentSeries() {
		IExtensionRegistry er = engine.getExtensionRegistry();
		
		IParameterVariation ipv = er.getExtensionArtifact(IParameterVariationExtension.class, VARIATION_NAME);
		assertNotNull(ipv);
		
		List<IParameterVariation> ipvList = new ArrayList<IParameterVariation>();
		ipvList.add(ipv);
		
		ExperimentSeries es = new ExperimentSeries();
		es.setName(expSeriesDef.getName());
		
		ExperimentSeriesRun esr = new ExperimentSeriesRun();
		esr.setTimestamp(System.nanoTime());
		esr.setExperimentSeries(es);
		es.getExperimentSeriesRuns().add(esr);
		
		fes.runExperimentSeries(esr, ipvList);
	}
	
}
