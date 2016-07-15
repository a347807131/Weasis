package org.weasis.core.ui.model.graphic.imp;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.weasis.core.ui.Messages;
import org.weasis.core.ui.model.graphic.AbstractGraphic;
import org.weasis.core.ui.model.graphic.Graphic;
import org.weasis.core.ui.model.utils.exceptions.InvalidShapeException;

@XmlType(name = "point")
@XmlRootElement(name = "point")
public class PointGraphic extends AbstractGraphic {
    private static final long serialVersionUID = 3485484151733273261L;

    static final Integer DEFAULT_POINT_SIZE = 1;
    
    public static final Integer POINTS_NUMBER = 1;

    private Integer pointSize = DEFAULT_POINT_SIZE;;

    public PointGraphic() {
        super(POINTS_NUMBER);
    }

    public PointGraphic(PointGraphic pointGaphic) {
        super(pointGaphic);
    }

    @Override
    public Graphic copy() {
        return new PointGraphic(this);
    }

    @Override
    protected void initCopy(Graphic graphic) {
        if (graphic instanceof PointGraphic) {
            setPointSize(((PointGraphic) graphic).getPointSize());
        }
    }

    @Override
    protected void prepareShape() throws InvalidShapeException {
        if (!isShapeValid()) {
            throw new InvalidShapeException("This shape cannot be drawn"); //$NON-NLS-1$
        }
        buildShape();
    }

    @Override
    public void buildShape() {
        pts.stream().findFirst().ifPresent(p -> {
            Ellipse2D ellipse =
                new Ellipse2D.Double(p.getX() - pointSize / 2.0f, p.getY() - pointSize / 2.0f, pointSize, pointSize);
            setShape(ellipse, null);
            updateLabel(null, null);
        });
    }

    @Override
    public String getUIName() {
        return Messages.getString("PointGraphic.point"); //$NON-NLS-1$
    }

    public Point2D getPoint() {
        return pts.stream().findFirst().map(p -> (Point2D) p.clone()).orElse(null);
    }

    @XmlAttribute
    public Integer getPointSize() {
        return pointSize;
    }

    public void setPointSize(Integer pointSize) {
        this.pointSize = pointSize;
    }
}
