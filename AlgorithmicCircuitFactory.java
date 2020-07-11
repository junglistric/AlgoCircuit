package z.eric.zhang;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

import javax.swing.JFrame;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.scope.AudioScope;
import com.jsyn.swing.SoundTweaker;
import com.jsyn.unitgen.Add;
import com.jsyn.unitgen.Circuit;
import com.jsyn.unitgen.Divide;
import com.jsyn.unitgen.FilterLowPass;
import com.jsyn.unitgen.ImpulseOscillator;
import com.jsyn.unitgen.ImpulseOscillatorBL;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.Maximum;
import com.jsyn.unitgen.Multiply;
import com.jsyn.unitgen.PassThrough;
import com.jsyn.unitgen.PulseOscillator;
import com.jsyn.unitgen.PulseOscillatorBL;
import com.jsyn.unitgen.RangeConverter;
import com.jsyn.unitgen.RedNoise;
import com.jsyn.unitgen.SawtoothOscillator;
import com.jsyn.unitgen.SawtoothOscillatorBL;
import com.jsyn.unitgen.SawtoothOscillatorDPW;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.unitgen.SquareOscillator;
import com.jsyn.unitgen.SquareOscillatorBL;
import com.jsyn.unitgen.Subtract;
import com.jsyn.unitgen.TriangleOscillator;
import com.jsyn.unitgen.UnitBinaryOperator;
import com.jsyn.unitgen.UnitFilter;
import com.jsyn.unitgen.UnitOscillator;
import com.softsynth.jmsl.JMSLRandom;

public class AlgorithmicCircuitFactory extends Circuit{
	
	// vector of oscillators
	Vector<UnitOscillator> myUnits = new Vector<UnitOscillator>();
	Vector<UnitOscillator> layerTwo = new Vector<UnitOscillator>();
	
	// vectors of math operators, comparators, and filters
	Vector<UnitBinaryOperator> maths = new Vector<UnitBinaryOperator>();
	Vector<UnitBinaryOperator> test = new Vector<UnitBinaryOperator>();
	Vector<UnitFilter> range = new Vector<UnitFilter>();
	
	//passthroughs for input ports
	PassThrough freq1 = new PassThrough();
	PassThrough freq2 = new PassThrough();
	PassThrough amppass = new PassThrough();
	PassThrough freqpassone = new PassThrough();
	PassThrough amppassone = new PassThrough();
	
	//lowpass filter
	FilterLowPass lowpass = new FilterLowPass();
	
	//input ports
	public UnitInputPort frequency1;
	public UnitInputPort frequency2;
	public UnitInputPort amplitude;
	public UnitInputPort freqone;
	public UnitInputPort ampone;
	public UnitInputPort filterQ;
	public UnitInputPort filterAmp;
	public UnitInputPort filterFreq;
	
	public AlgoCircuit generateCircuit(){
		
		//array of unit oscillator classnames as strings
		String[] classNames = {RedNoise.class.getName(), ImpulseOscillator.class.getName(), ImpulseOscillatorBL.class.getName(),SineOscillator.class.getName(),PulseOscillatorBL.class.getName(), PulseOscillator.class.getName(), SquareOscillator.class.getName(), SquareOscillatorBL.class.getName(), SawtoothOscillator.class.getName(), SawtoothOscillatorBL.class.getName(), TriangleOscillator.class.getName(), SawtoothOscillatorDPW.class.getName()};
		String[] binaryNames = {Add.class.getName(), Subtract.class.getName(), Multiply.class.getName(), Divide.class.getName()};
		
		//2-10 unit oscillators chosen at random and added to vector
		JMSLRandom.randomize();
		JMSLRandom.getSeed();
		int someNumber = JMSLRandom.choose(2,10);
		for (int i = 0; i < someNumber; i++)
		{
			UnitOscillator oneunit = null;
			try{
				JMSLRandom.nextSeed();
				String oscillatorclass = classNames[JMSLRandom.choose(0,11)];
				Class unit = Class.forName(oscillatorclass);
				Constructor cons = unit.getConstructor();
				oneunit = (UnitOscillator)cons.newInstance();
			}
			catch(ClassNotFoundException e)
			{
				System.out.println("Class not found.");
			}
			catch(NoSuchMethodException f)
			{
				System.out.println("Method not found.");
			}
			catch(InvocationTargetException g)
			{
				System.out.println("Invocation error.");
			}
			catch(IllegalAccessException h)
			{
				System.out.println("Access error.");
			}
			catch(Exception j)
			{
				System.out.println("Exception error.");
			}
			myUnits.add(oneunit);
		}
		
		//second layer
		for (int i = 0; i < someNumber; i++)
		{
			UnitOscillator secondunit = null;
			try{
				JMSLRandom.nextSeed();
				String oscillatorclass = classNames[JMSLRandom.choose(0,10)];
				Class second = Class.forName(oscillatorclass);
				Constructor cons2 = second.getConstructor();
				secondunit = (UnitOscillator)cons2.newInstance();
			}
			catch(ClassNotFoundException e)
			{
				System.out.println("Class not found.");
			}
			catch(NoSuchMethodException f)
			{
				System.out.println("Method not found.");
			}
			catch(InvocationTargetException g)
			{
				System.out.println("Invocation error.");
			}
			catch(IllegalAccessException h)
			{
				System.out.println("Access error.");
			}
			catch(Exception j)
			{
				System.out.println("Exception error.");
			}
			layerTwo.add(secondunit);
		}
		
		//set oscillator amplitudes to zero
		for (int i=0;i<someNumber;i++)
		{
			myUnits.get(i).amplitude.set(0);
			layerTwo.get(i).amplitude.set(0);
		}
		
		//create math operators (for amp and freq)
		for (int i = 0; i < someNumber * 2; i++)
		{
			UnitBinaryOperator math = null;
			try{
				JMSLRandom.nextSeed();
				String mathclass = binaryNames[JMSLRandom.choose(0,3)];
				Class mathunit = Class.forName(mathclass);
				Constructor mathcons = mathunit.getConstructor();
				math = (UnitBinaryOperator)mathcons.newInstance();
			}
			catch(Exception q){
				System.out.println("Exception error.");
			}
			maths.add(math);
		}	
		
		//create range converters
		for (int i = 0; i < someNumber * 2; i++)
		{
			UnitFilter ranger = null;
			try{
				JMSLRandom.nextSeed();
				Class rangeunit = Class.forName(RangeConverter.class.getName());
				Constructor rangecons = rangeunit.getConstructor();
				ranger = (UnitFilter)rangecons.newInstance();
			}
			catch(Exception q){
				System.out.println("Exception error.");
			}
			range.add(ranger);
		}
		
		//create final maximum comparators
		for (int i = 0; i < someNumber; i++)
		{
			UnitBinaryOperator tester = null;
			try{
				Class test = Class.forName(Maximum.class.getName());
				Constructor testy = test.getConstructor();
				tester = (Maximum)testy.newInstance();
			}
			catch(Exception q){
				System.out.println("Exception error.");
			}
			test.add(tester);
		}
	
		//connect output of myUnits to maths operators
		for(int i = 0; i < someNumber; i++)
		{
			myUnits.get(i).output.connect(maths.get(i).inputA);
		}
		
		//passthroughs for input ports
		freq1 = new PassThrough();
		freq2 = new PassThrough();
		freqpassone = new PassThrough();
		amppass = new PassThrough();
		amppassone = new PassThrough();
		
		//lowpass filter
		lowpass = new FilterLowPass();
		
		//connect passthroughs to myUnits; add freq inputs to freq maths; add amp mod to amp maths
		for(int i=0;i<someNumber;i++)
		{
			amppass.output.connect(maths.get(i).inputB);
			freq1.output.connect(maths.get(i+someNumber).inputA);
			freq2.output.connect(maths.get(i+someNumber).inputB);
			freqpassone.output.connect(myUnits.get(i).frequency);
			amppassone.output.connect(myUnits.get(i).amplitude);
		}
		
		//connect range and maths to layerTwo
		for(int i = 0;i<someNumber;i++)
		{
			maths.get(i).output.connect(layerTwo.get(i).amplitude);
			//range.get(i).output.connect(layerTwo.get(i).amplitude);
			maths.get(i+someNumber).output.connect(layerTwo.get(i).frequency);
			//range.get(i+someNumber).output.connect(layerTwo.get(i).frequency);
		}
		
		//cascaded maximum comparator for layerTwo outputs
		layerTwo.get(0).output.connect(test.get(0).inputA);
		for(int i = 0;i<someNumber-2;i++)
		{
			layerTwo.get(i+1).output.connect(test.get(i).inputB);
			test.get(i).output.connect(test.get(i+1).inputA);
		}
		layerTwo.get(someNumber-1).output.connect(test.get(someNumber-2).inputB);
		
		//connect cascaded layerTwo outputs to lowpass filter and add to final passthrough
		test.get(someNumber-2).output.connect(lowpass.input);
		
		//function to return the output of the algocircuit
		AlgoCircuit newCircuit = new AlgoCircuit(){
			public UnitOutputPort getOutput(){
				return lowpass.getOutput();
			}
		};
		
		//connect input ports
		newCircuit.addPort(frequency1 = freq1.input, "frequency 1");
		newCircuit.addPort(frequency2 = freq2.input, "frequency 2");
		newCircuit.addPort(amplitude = amppass.input, "ampmod");
		newCircuit.addPort(freqone = freqpassone.input, "freqone");
		newCircuit.addPort(ampone = amppassone.input, "ampone");
		newCircuit.addPort(filterQ = lowpass.Q, "Q");
		newCircuit.addPort(filterAmp = lowpass.amplitude, "filteramp");
		newCircuit.addPort(filterFreq = lowpass.frequency, "filterfreq");
	
		//defaults
		amplitude.setup(0,0.5,1);
		frequency1.setup(0,440,4000);
		frequency2.setup(0,440,4000);
		freqone.setup(0,440,4000);
		ampone.setup(0,0.5,1);
		filterQ.setup(0.1,5,10.0);
		filterAmp.setup(0,0.5,1.0);
		filterFreq.setup(20,500,6000);
		
		//add the passthroughs to the circuit
		newCircuit.add(freq1);
		newCircuit.add(freq2);
		newCircuit.add(amppass);
		newCircuit.add(freqpassone);
		
		//add lowpass filter
		newCircuit.add(lowpass);
		
		//add units to circuit
		for(int i=0;i<someNumber;i++)
		{
			newCircuit.add(myUnits.get(i));
			newCircuit.add(maths.get(i));
			newCircuit.add(maths.get(i+someNumber));
			newCircuit.add(test.get(i));
			newCircuit.add(layerTwo.get(i));
			newCircuit.add(range.get(i));
			newCircuit.add(range.get(i+someNumber));
		}
		
		//get ports of each unit and respective min and max
//		java.util.Collection<UnitPort> unitPortCollection = lowpass.getPorts();
//		for (UnitPort unitPort : unitPortCollection){
//			if((unitPort instanceof UnitInputPort)){
//				String alias = unitPort.getName();
//				System.out.println("PORT NAME: " + alias);
//				double minValue = ((UnitInputPort) unitPort).getMinimum();
//				double maxValue = ((UnitInputPort) unitPort).getMaximum();
//				System.out.println("min/max: " + minValue + "/" + maxValue);
//			}
//		}
		return newCircuit;
	}
	
	public static void main(String[] args){
		
		final Synthesizer synth = JSyn.createSynthesizer();
		synth.start();
		
		AlgorithmicCircuitFactory factory = new AlgorithmicCircuitFactory();
		AlgoCircuit newCircuit = factory.generateCircuit();
		synth.add(newCircuit);
		
		LineOut out = new LineOut();
		synth.add(out);
		out.start();
		
		//get the output of the algocircuit and connect to the inputs of the lineout
		newCircuit.getOutput().connect(0,out.input,0);
		newCircuit.getOutput().connect(0,out.input,1);
		
		final AudioScope scope = new AudioScope(synth);
		scope.addProbe( newCircuit.getOutput() );
		// Trigger on a threshold level vs AUTO trigger.
		scope.setTriggerMode( AudioScope.TriggerMode.NORMAL );
		scope.getView().setShowControls(true);
		scope.start();

		//JFrame
		JFrame jf = new JFrame();
		jf.setPreferredSize(new Dimension(1000,1000));
		SoundTweaker tweaker = new SoundTweaker(synth, "AlgoCircuit", newCircuit);
		jf.add(tweaker);
		jf.pack();
		jf.add( BorderLayout.EAST, scope.getView() );
		jf.setVisible(true);
		jf.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				synth.stop();
				scope.stop();
				System.exit(0);
			}
		});
	}
}