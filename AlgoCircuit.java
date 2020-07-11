package z.eric.zhang;

import com.jsyn.unitgen.Circuit;
import com.jsyn.unitgen.UnitVoice;
import com.softsynth.shared.time.TimeStamp;

abstract class AlgoCircuit extends Circuit implements UnitVoice{
	@Override
	public void noteOff(TimeStamp ts) {
	
	}

	@Override
	public void noteOn(double frequency, double amplitude, TimeStamp ts) {

	}
}
