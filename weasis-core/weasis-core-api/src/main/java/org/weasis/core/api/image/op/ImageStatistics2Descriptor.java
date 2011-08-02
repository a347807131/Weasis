/*******************************************************************************
 * Copyright (c) 2010 Nicolas Roduit.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Nicolas Roduit - initial API and implementation
 ******************************************************************************/
package org.weasis.core.api.image.op;

import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.awt.image.renderable.RenderedImageFactory;

import javax.media.jai.OperationDescriptorImpl;
import javax.media.jai.ROI;

public class ImageStatistics2Descriptor extends OperationDescriptorImpl implements RenderedImageFactory {

    /**
     * The resource strings that provide the general documentation and specify the parameter list for this operation.
     */
    private static final String[][] resources =
        {
            { "GlobalName", "ImageStatistics" }, //$NON-NLS-1$ //$NON-NLS-2$

            { "LocalName", "ImageStatistics" }, //$NON-NLS-1$ //$NON-NLS-2$

            { "Vendor", "" }, //$NON-NLS-1$ //$NON-NLS-2$

            {
                "Description", //$NON-NLS-1$
                "Finds the min, max, mean, standard deviation of pixel values in each band with the option to exclude a range of values." }, //$NON-NLS-1$

            { "DocURL", "" }, { "Version", "1.0" }, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

            { "arg0Desc", "The region of the image to scan" }, //$NON-NLS-1$ //$NON-NLS-2$

            { "arg1Desc", "The horizontal sampling rate, may not be less than 1." }, //$NON-NLS-1$ //$NON-NLS-2$

            { "arg2Desc", "The vertical sampling rate, may not be less than 1." }, //$NON-NLS-1$ //$NON-NLS-2$

            { "arg3Desc", "The lowest value to exlude" },

            { "arg4Desc", "The highest value to exlude" },

            { "arg5Desc", "The rescale slope" },

            { "arg6Desc", "The rescale intercept" }

        }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

    /**
     * The modes that this operator supports. maybe one or more of "rendered", "renderable", "collection", and
     * "renderableCollection".
     */
    private static final String[] supportedModes = { "rendered" }; //$NON-NLS-1$

    /** The parameter name list for this operation. */
    private static final String[] paramNames = {
        "roi", "xPeriod", "yPeriod", "excludedMin", "excludedMax", "slope", "intercept" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

    /** The parameter class list for this operation. */
    private static final Class[] paramClasses = { javax.media.jai.ROI.class, java.lang.Integer.class,
        java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class,
        java.lang.Double.class };

    /** The parameter default value list for this operation. */
    private static final Object[] paramDefaults = { null, 1, 1, null, null, null, null };

    /** Constructor. */
    public ImageStatistics2Descriptor() {
        super(resources, supportedModes, 1, paramNames, paramClasses, paramDefaults, null);
    }

    @Override
    public RenderedImage create(ParameterBlock paramBlock, RenderingHints renderHints) {
        if (!validateSources(paramBlock)) {
            return null;
        }
        if (!validateParameters(paramBlock)) {
            return null;
        }

        RenderedImage renderedimage = paramBlock.getRenderedSource(0);
        int i = renderedimage.getMinX();
        int j = renderedimage.getMinY();

        return new ImageStatistics2OpImage(renderedimage, (ROI) paramBlock.getObjectParameter(0), i, j,
            (Integer) paramBlock.getObjectParameter(1), (Integer) paramBlock.getObjectParameter(2),
            (Double) paramBlock.getObjectParameter(3), (Double) paramBlock.getObjectParameter(4),
            (Double) paramBlock.getObjectParameter(5), (Double) paramBlock.getObjectParameter(6));
    }

    public boolean validateSources(ParameterBlock parameterblock) {
        return parameterblock.getRenderedSource(0) != null;
    }

    public boolean validateParameters(ParameterBlock paramBlock) {
        Object arg = paramBlock.getObjectParameter(0);
        if (!(arg instanceof ROI) && arg != null) {
            return false;
        }
        return true;
    }
}