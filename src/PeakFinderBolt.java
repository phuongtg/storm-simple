import java.util.ArrayList;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;


public class PeakFinderBolt extends BaseRichBolt{

	private OutputCollector _collector;
	private PeakDetector1 peakDetector;
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		// TODO Auto-generated method stub
		_collector = collector;
	}

	/*
	 * Takes a float of byte array as input, and find peaks on the values
	 * */
	public void execute(Tuple input) {
		// TODO create peak detector according to sampling rate
		String metadata = input.getString(0);
		int chunkIndex = Integer.parseInt(metadata.split(",")[2]);
		byte[] data = input.getBinary(1);
		float[] vals = Utils.toFloatArr(data);
		ArrayList[] peaks = PeakDetector.peakDetect(vals, 300, (float)0.1);
		ArrayList<Integer> maxPeaks = peaks[0];
		ArrayList<Integer> minPeaks = peaks[1];
		
		byte[] maxPeaksByta = Utils.toBytaArr(maxPeaks.toArray(new Integer[0]));
		byte[] minPeaksByta = Utils.toBytaArr(minPeaks.toArray(new Integer[0]));
		System.out.println("---------- SENDING PEAKS FOR PART: "+chunkIndex+"-----------");
		_collector.emit(new Values(metadata, data, maxPeaksByta, minPeaksByta));
	}
	

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields("metadata","content", "maxPeaks", "minPeaks"));
	}

}
