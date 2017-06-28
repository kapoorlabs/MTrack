package listeners;

import java.awt.Label;
import java.awt.Scrollbar;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import interactiveMT.Interactive_MTDoubleChannel;
import interactiveMT.Interactive_MTDoubleChannel.ValueChange;
import mpicbg.imglib.multithreading.SimpleMultiThreading;

public class MinSizeHoughListener implements AdjustmentListener {
	final Label label;
	final float min, max;
	final int scrollbarSize;
	final Interactive_MTDoubleChannel parent;
	final Scrollbar minsizeScrollbar;

	public MinSizeHoughListener(final Interactive_MTDoubleChannel parent, final Label label, final float min, final float max, final int scrollbarSize,
			final Scrollbar minsizeScrollbar) {
		this.label = label;
		this.min = min;
		this.max = max;
		this.scrollbarSize = scrollbarSize;
        this.parent = parent;
		this.minsizeScrollbar = minsizeScrollbar;
		minsizeScrollbar.addMouseListener( new StandardMouseListener( parent,ValueChange.SHOWMSERinHough ) );

	}

	@Override
	public void adjustmentValueChanged(final AdjustmentEvent event) {
		parent.minSize = (int) parent.computeValueFromScrollbarPosition(event.getValue(), min, max, scrollbarSize);

		minsizeScrollbar.setValue(parent.computeScrollbarPositionFromValue(parent.minSize, min, max, scrollbarSize));

		label.setText("Min # of pixels inside MSER Ellipses = " + parent.minSize);

		
	}
}