package peakFitter;

import java.util.ArrayList;

import javax.swing.JProgressBar;

import LineModels.UseLineModel.UserChoiceModel;
import labeledObjects.CommonOutputHF;
import labeledObjects.Indexedlength;
import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Pair;
import net.imglib2.util.ValuePair;
import net.imglib2.view.Views;
import preProcessing.GetLocalmaxmin;

public class FitterUtils {

	
	
	public static final Pair<double[], double[]> MakeinitialEndpointguess(ArrayList<CommonOutputHF> imgs, double maxintensityline, 
			double Intensityratio, int ndims, int label, double slope, double intercept, double Curvature, double Inflection){
		long[] newposition = new long[ndims];
		double[] minVal = { Double.MAX_VALUE, Double.MAX_VALUE };
		double[] maxVal = { -Double.MIN_VALUE, -Double.MIN_VALUE };
		
		RandomAccessibleInterval<FloatType> currentimg = imgs.get(label).Roi;

		FinalInterval interval = imgs.get(label).interval;

		currentimg = Views.interval(currentimg, interval);

		final Cursor<FloatType> outcursor = Views.iterable(currentimg).localizingCursor();
		
		
		
		while (outcursor.hasNext()) {

			outcursor.fwd();

			outcursor.localize(newposition);
			
			long pointonline = (int)Math.round(newposition[1] - slope * newposition[0] - Curvature * newposition[0]* newposition[0] - 
					Inflection *  newposition[0]* newposition[0] * newposition[0] - intercept);
			
			if (outcursor.getDoublePosition(0) <= minVal[0]
					&& outcursor.get().get() / maxintensityline > Intensityratio ) {
				minVal[0] = outcursor.getDoublePosition(0);
				minVal[1] = outcursor.getDoublePosition(1);
			}

			if (outcursor.getDoublePosition(0) >= maxVal[0]
					&& outcursor.get().get() / maxintensityline > Intensityratio ) {
				maxVal[0] = outcursor.getDoublePosition(0);
				maxVal[1] = outcursor.getDoublePosition(1);
			
		}
			
		}
		Pair<double[], double[]> minmaxpair = new ValuePair<double[], double[]>(minVal, maxVal);
		
		return minmaxpair;
		
	}
	
	
	public static void SetProgressBar(JProgressBar jpb, double percent){
		
		jpb.setValue((int) Math.round(percent));
		jpb.setOpaque(true);
		jpb.setStringPainted(true);
		jpb.setString("Finding MT ends" );
		
		
	}
	
public static void SetProgressBarTime(JProgressBar jpb, double percent, int framenumber, int thirdDimsize){
		
	jpb.setValue((int) percent);
	jpb.setOpaque(true);
	jpb.setStringPainted(true);
	jpb.setString("Time point = " + framenumber + "/" + thirdDimsize);
		
		
	}
	
public static void SetProgressBarTime(JProgressBar jpb, double percent, int framenumber, int thirdDimsize, String message){
	
	jpb.setValue((int) percent);
	jpb.setOpaque(true);
	jpb.setStringPainted(true);
	jpb.setString( message +  "= " + framenumber + "/" + thirdDimsize);
		
		
	}
	
	
}
