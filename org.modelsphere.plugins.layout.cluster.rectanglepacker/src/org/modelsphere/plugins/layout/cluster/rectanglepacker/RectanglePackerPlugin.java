package org.modelsphere.plugins.layout.cluster.rectanglepacker;

import org.modelsphere.jack.srtool.features.layout.LayoutAlgorithm;
import org.modelsphere.jack.srtool.features.layout.LayoutPlugin;

import java.util.ArrayList;
import java.util.List;


public class RectanglePackerPlugin extends LayoutPlugin {
    private RectanglePackerAlgorithm rectanglePackerAlgorithm;

    @Override
    public List<LayoutAlgorithm> getLayoutAlgorithms() {
        if (rectanglePackerAlgorithm == null)
            rectanglePackerAlgorithm = new RectanglePackerAlgorithm();

        List<LayoutAlgorithm> algorithms = new ArrayList<LayoutAlgorithm>();
        algorithms.add(rectanglePackerAlgorithm);
        return algorithms;
    }

	@Override
	public List<LayoutAlgorithm> getDefaultLayoutAlgorithms() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
//		return null;
	}

}
