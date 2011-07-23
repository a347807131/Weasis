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
package org.weasis.core.ui.graphic;

import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.weasis.core.api.image.measure.MeasurementsAdapter;
import org.weasis.core.api.media.data.ImageElement;
import org.weasis.core.ui.Messages;
import org.weasis.core.ui.util.MouseEventDouble;

/**
 * @author Nicolas Roduit
 */
public class EllipseGraphic extends RectangleGraphic {

    public static final Icon ICON = new ImageIcon(EllipseGraphic.class.getResource("/icon/22x22/draw-eclipse.png")); //$NON-NLS-1$

    public static final Measurement Area = new Measurement("Area", true, true, true);
    public static final Measurement Perimeter = new Measurement("Perimeter", true, true, false);
    public static final Measurement CenterX = new Measurement("Center X", true, true, false);
    public static final Measurement CenterY = new Measurement("Center Y", true, true, false);
    public static final Measurement Width = new Measurement("Width", true, true, false);
    public static final Measurement Height = new Measurement("Height", true, true, false);

    public EllipseGraphic(float lineThickness, Color paint, boolean labelVisible) {
        super(lineThickness, paint, labelVisible);
    }

    @Override
    public Icon getIcon() {
        return ICON;
    }

    @Override
    public String getUIName() {
        return Messages.getString("MeasureToolBar.ellipse"); //$NON-NLS-1$
    }

    @Override
    protected void updateShapeOnDrawing(MouseEventDouble mouseevent) {
        Rectangle2D rectangle = new Rectangle2D.Double();
        rectangle.setFrameFromDiagonal(handlePointList.get(eHandlePoint.NW.index),
            handlePointList.get(eHandlePoint.SE.index));

        setShape(new Ellipse2D.Double(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight()),
            mouseevent);
        updateLabel(mouseevent, getDefaultView2d(mouseevent));
    }

    @Override
    public List<MeasureItem> getMeasurements(ImageElement imageElement, boolean releaseEvent, boolean drawOnLabel) {

        if (imageElement != null && isShapeValid()) {
            MeasurementsAdapter adapter = imageElement.getMeasurementAdapter();

            if (adapter != null) {
                ArrayList<MeasureItem> measVal = new ArrayList<MeasureItem>();
                Rectangle2D rect = new Rectangle2D.Double();

                rect.setFrameFromDiagonal(handlePointList.get(eHandlePoint.NW.index),
                    handlePointList.get(eHandlePoint.SE.index));

                double ratio = adapter.getCalibRatio();

                if (CenterX.isComputed() && (!drawOnLabel || CenterX.isGraphicLabel())) {
                    Double val =
                        releaseEvent || CenterX.isQuickComputing() ? adapter.getXCalibratedValue(rect.getCenterX())
                            : null;
                    measVal.add(new MeasureItem(CenterX, val, adapter.getUnit()));
                }
                if (CenterY.isComputed() && (!drawOnLabel || CenterY.isGraphicLabel())) {
                    Double val =
                        releaseEvent || CenterY.isQuickComputing() ? adapter.getYCalibratedValue(rect.getCenterY())
                            : null;
                    measVal.add(new MeasureItem(CenterY, val, adapter.getUnit()));
                }

                if (Width.isComputed() && (!drawOnLabel || Width.isGraphicLabel())) {
                    Double val = releaseEvent || Width.isQuickComputing() ? ratio * rect.getWidth() : null;
                    measVal.add(new MeasureItem(Width, val, adapter.getUnit()));
                }
                if (Height.isComputed() && (!drawOnLabel || Height.isGraphicLabel())) {
                    Double val = releaseEvent || Height.isQuickComputing() ? ratio * rect.getHeight() : null;
                    measVal.add(new MeasureItem(Height, val, adapter.getUnit()));
                }

                if (Area.isComputed() && (!drawOnLabel || Area.isGraphicLabel())) {
                    Double val =
                        releaseEvent || Area.isQuickComputing() ? Math.PI * rect.getWidth() * ratio * rect.getHeight()
                            * ratio / 4.0 : null;
                    String unit = "pix".equals(adapter.getUnit()) ? adapter.getUnit() : adapter.getUnit() + "2";
                    measVal.add(new MeasureItem(Area, val, unit));
                }

                List<MeasureItem> stats = getImageStatistics(imageElement, releaseEvent);
                if (stats != null) {
                    measVal.addAll(stats);
                }
                return measVal;
            }
        }
        return null;
    }

}
