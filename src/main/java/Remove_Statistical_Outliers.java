import ij.IJ;
import ij.ImagePlus;
import ij.measure.Calibration;
import ij.plugin.filter.ExtendedPlugInFilter;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import ij.process.ImageStatistics;
import net.imagej.*;
import net.imglib2.*;
import net.imglib2.view.Views;

import org.scijava.ItemIO;
import org.scijava.app.StatusService;
import org.scijava.command.Command;
import org.scijava.log.LogService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.table.Tables;
import org.scijava.ui.DialogPrompt;
import org.scijava.ui.UIService;


public class Remove_Statistical_Outliers implements PlugInFilter {

    @Override
    public int setup(String arg, ImagePlus image){
        return DOES_ALL;
    }


    @Override
    public void run(ImageProcessor ip) {
        ImageStatistics stats = ip.getStatistics();
        double IQR = calculatePosition(ip, stats.histogram, 0, stats.histogram.length, 0.75) - calculatePosition(ip, stats.histogram, 0, stats.histogram.length, 0.25);
        IJ.showMessage("IQR", "The IQR is: " + IQR);

    }

    double calculatePosition(ImageProcessor ip, int[] hist, int first, int last, double Percent) {
        //ij.IJ.log("calculateMedian: "+first+"  "+last+"  "+hist.length+"  "+pixelCount);
        if (ip.getPixelCount()==0) {
            return Double.NaN;
        }
        double sum = 0;
        int i = first-1;
        double stopPosition = ip.getPixelCount()/(1/Percent);
        do {
            sum += hist[++i];
        } while (sum<=stopPosition && i<last);
        return i;
    }
}
