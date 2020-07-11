package z.eric.zhang;

import java.awt.BorderLayout;

import javax.swing.JApplet;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.scope.AudioScope;
import com.jsyn.swing.SoundTweaker;
import com.jsyn.unitgen.LineOut;

public class AlgoSynthRange extends JApplet {

	AlgorithmicCircuitFactoryRange factory = new AlgorithmicCircuitFactoryRange();
	AlgoCircuit newCircuit = factory.generateCircuit();
	LineOut out;
	Synthesizer synth;
	
	public void start(){
		startSynthesisEngine();
		instantiateUnitGenerators();
		connectunitGenerators();
		startUnitGenerators();
		buildGUI();
	}

	private void buildGUI() {
		SoundTweaker SoundTweaker = new SoundTweaker(synth,"AlgoCircuit", newCircuit);
		add(SoundTweaker);
		final AudioScope scope = new AudioScope(synth);
		scope.addProbe( newCircuit.getOutput() );
		// Trigger on a threshold level vs AUTO trigger.
		scope.setTriggerMode( AudioScope.TriggerMode.NORMAL );
		scope.getView().setShowControls(true);
		scope.start();
		add(BorderLayout.EAST, scope.getView());
	}

	private void startUnitGenerators() {
		out.start();
	}

	private void connectunitGenerators() {
		newCircuit.getOutput().connect(0,out.input,0);
		newCircuit.getOutput().connect(0,out.input,1);
	}

	private void instantiateUnitGenerators() {
		synth.add(newCircuit);
		synth.add(out = new LineOut());
	}

	private void startSynthesisEngine() {
		synth = JSyn.createSynthesizer();
		synth.start();
	}
}