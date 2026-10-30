package org.modelsphere.plugins.layout.clusters;

import org.modelsphere.jack.srtool.features.layout.ClustersLayoutAlgorithm;
import org.modelsphere.jack.srtool.features.layout.LayoutAttribute;
import org.modelsphere.jack.srtool.features.layout.LayoutException;
import org.modelsphere.jack.srtool.features.layout.graph.Graph;
import org.modelsphere.jack.srtool.features.layout.graph.Node;

import javax.swing.*;
import java.util.Map;

/**
 * The Class NullClusterLayoutAlgorithm.
 */
public class NullClusterLayoutAlgorithm implements ClustersLayoutAlgorithm {

    /*
     * (non-Javadoc)
     * 
     * @seeorg.modelsphere.jack.srtool.features.layout.ClustersLayoutAlgorithm# layoutClusters(org.
     * modelsphere.jack.srtool.features.layout.graph.Graph, java.util.Map, java.util.Map)
     */
    @Override
    public void layoutClusters(Graph graph, Map<Node, Integer> clusters,
            Map<LayoutAttribute<?>, Object> attributes) throws LayoutException {
        // voluntarily do nothing!
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.modelsphere.jack.srtool.features.layout.LayoutAlgorithm# createOptionComponent()
     */
    @Override
    public JComponent createOptionComponent() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.srtool.features.layout.LayoutAlgorithm#getText()
     */
    @Override
    public String getText() {
        return "(none)";
    }

}
