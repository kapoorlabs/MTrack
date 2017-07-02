package lineFinder;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import javax.security.auth.login.AccountExpiredException;
import javax.swing.JProgressBar;


import LineModels.UseLineModel.UserChoiceModel;
import graphconstructs.KalmanTrackproperties;
import graphconstructs.Trackproperties;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.Overlay;
import ij.gui.Roi;
import interactiveMT.Interactive_MTDoubleChannel.Whichend;
import labeledObjects.Indexedlength;
import labeledObjects.KalmanIndexedlength;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Pair;
import net.imglib2.util.ValuePair;
import peakFitter.SubpixelLengthPCKalmanLine;
import peakFitter.SubpixelLengthPCLine;
import peakFitter.SubpixelVelocityPCLine;
import peakFitter.SubpixelVelocityUserSeed;
import preProcessing.Kernels;

public  class FindlinesVia {

	public static  enum LinefindingMethod {

		MSER, Hough, MSERwHough;

	}
	
	public static int Accountedframes ;
	
      protected LinefindingMethod MSER, Hough, MSERwHough;

	public static Pair<ArrayList<Indexedlength>,ArrayList<Indexedlength>> LinefindingMethod(final RandomAccessibleInterval<FloatType> source,
			final RandomAccessibleInterval<FloatType> Preprocessedsource, 
			final int framenumber, final double[] psf, final Linefinder linefinder, final UserChoiceModel model, 
			final boolean DoMask, final double Intensityratio, final double Inispacing, JProgressBar jpb ) {

		
		Pair<ArrayList<Indexedlength>,ArrayList<Indexedlength>>	PrevFrameparam = null;
		

			
			SubpixelLengthPCLine MTline = new SubpixelLengthPCLine(source, linefinder, psf, model, framenumber, DoMask, jpb);
			MTline.setIntensityratio(Intensityratio);
			
			MTline.setInispacing(Inispacing);
			MTline.checkInput();
			MTline.process();
			PrevFrameparam = MTline.getResult();
			
		
		return PrevFrameparam;

	}
	
	

	public static Pair<Pair<ArrayList<Trackproperties>, ArrayList<Trackproperties>>, Pair<ArrayList<Indexedlength>, ArrayList<Indexedlength>>> 
	LinefindingMethodHF(final RandomAccessibleInterval<FloatType> source,
			final RandomAccessibleInterval<FloatType> Preprocessedsource,Pair<ArrayList<Indexedlength>,ArrayList<Indexedlength>> PrevFrameparam,
			 final int framenumber, final double[] psf,  final LinefinderHF linefinder, final UserChoiceModel model,
			final boolean DoMask, final double intensityratio, final double Inispacing, final HashMap<Integer, Whichend> Trackstart, final JProgressBar jpb,
			final int thirdDimsize) {

		Pair<Pair<ArrayList<Trackproperties>, ArrayList<Trackproperties>>,Pair<ArrayList<Indexedlength>,ArrayList<Indexedlength>>> returnVector = null;
		
		

			final SubpixelVelocityPCLine growthtracker = new SubpixelVelocityPCLine(source, linefinder,
					PrevFrameparam.getA(), PrevFrameparam.getB(), psf, framenumber, model, DoMask, Trackstart,jpb, thirdDimsize);
			growthtracker.setIntensityratio(intensityratio);
			growthtracker.setInispacing(Inispacing);
			//growthtracker.setSlopetolerance(slopetolerance);
			growthtracker.checkInput();
			growthtracker.process();
			Accountedframes  = growthtracker.getAccountedframes();
			
			Pair<ArrayList<Indexedlength>,ArrayList<Indexedlength>> NewFrameparam = growthtracker.getResult();
			ArrayList<Trackproperties> startStateVectors = growthtracker.getstartStateVectors();
			ArrayList<Trackproperties> endStateVectors = growthtracker.getendStateVectors();
			Pair<ArrayList<Trackproperties>, ArrayList<Trackproperties>> Statevectors = new ValuePair<ArrayList<Trackproperties>, ArrayList<Trackproperties>>(startStateVectors, endStateVectors); 
			returnVector = 
					new ValuePair<Pair<ArrayList<Trackproperties>, ArrayList<Trackproperties>>,Pair<ArrayList<Indexedlength>,ArrayList<Indexedlength>>>(Statevectors, NewFrameparam);
			
			
			
			
		
			
		
		
		
		return returnVector;

	}
	
	public static Pair<ArrayList<Trackproperties>, ArrayList<Indexedlength>> 
	LinefindingMethodHFUser(final RandomAccessibleInterval<FloatType> source,
			final RandomAccessibleInterval<FloatType> Preprocessedsource,ArrayList<Indexedlength> PrevFrameparam,
			 final int framenumber, final double[] psf,  final LinefinderHF linefinder, final UserChoiceModel model,
			final boolean DoMask, final double intensityratio, final double Inispacing, final JProgressBar jpb,
			final int thirdDimsize) {

		Pair<ArrayList<Trackproperties>,ArrayList<Indexedlength>> returnVector = null;
		
		

			final SubpixelVelocityUserSeed growthtracker = new SubpixelVelocityUserSeed(source, linefinder,
					PrevFrameparam, psf, framenumber, model, DoMask,jpb, thirdDimsize);
			growthtracker.setIntensityratio(intensityratio);
			growthtracker.setInispacing(Inispacing);
			growthtracker.checkInput();
			growthtracker.process();
			Accountedframes  = growthtracker.getAccountedframes();
			
			ArrayList<Indexedlength> NewFrameparam = growthtracker.getResult();
			ArrayList<Trackproperties> startStateVectors = growthtracker.getstartStateVectors();
			returnVector = 
					new ValuePair<ArrayList<Trackproperties>,ArrayList<Indexedlength>>(startStateVectors, NewFrameparam);
			
			
			
			
		
			
		
		
		
		return returnVector;

	}
	
	
	
	public static int getAccountedframes(){
		
		return Accountedframes;
	}
	
	
}
