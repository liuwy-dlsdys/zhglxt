package com.zhglxt.activiti.config;

import org.activiti.bpmn.model.GraphicInfo;
import org.activiti.image.impl.DefaultProcessDiagramCanvas;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

/**
 * @Description 自定义流程图画布
 * @Author liuwy
 * @Date 2022/10/28
 **/
public class CustomProcessDiagramCanvas extends DefaultProcessDiagramCanvas{

    /**
     * 流程图连线上的文字颜色
     **/
    protected static Color LABEL_COLOR = new Color(10, 10, 10);

    protected String labelFontName = "宋体";

    public CustomProcessDiagramCanvas(int width, int height, int minX, int minY, String imageType, String activityFontName, String labelFontName, String annotationFontName, ClassLoader customClassLoader) {
        super(width, height, minX, minY, imageType, activityFontName, labelFontName, annotationFontName, customClassLoader);
    }

    public CustomProcessDiagramCanvas(int width, int height, int minX, int minY, String imageType) {
        super(width, height, minX, minY, imageType);
    }

    /**
     *  绘制标签
     **/
    @Override
    public void drawLabel(String text, GraphicInfo graphicInfo, boolean centered) {
        float interline = 1.0f;

        // text
        if (text != null && text.length()>0) {
            Paint originalPaint = g.getPaint();
            Font originalFont = g.getFont();

            //TODO 修改连线上文字的样式
            g.setPaint(LABEL_COLOR);
            g.setFont(new Font(labelFontName,Font.BOLD,10));

            int wrapWidth = 100;
            int textY = (int) graphicInfo.getY();

            // TODO: use drawMultilineText()
            AttributedString as = new AttributedString(text);
            as.addAttribute(TextAttribute.FOREGROUND, g.getPaint());
            as.addAttribute(TextAttribute.FONT, g.getFont());
            AttributedCharacterIterator aci = as.getIterator();
            FontRenderContext frc = new FontRenderContext(null, true, false);
            LineBreakMeasurer lbm = new LineBreakMeasurer(aci, frc);

            while (lbm.getPosition() < text.length()) {
                TextLayout tl = lbm.nextLayout(wrapWidth);
                textY += tl.getAscent();
                Rectangle2D bb = tl.getBounds();
                double tX = graphicInfo.getX();
                if (centered) {
                    tX += (int) (graphicInfo.getWidth() / 2 - bb.getWidth() / 2);
                }
                tl.draw(g, (float) tX, textY);
                textY += tl.getDescent() + tl.getLeading() + (interline - 1.0f) * tl.getAscent();
            }

            // restore originals
            g.setFont(originalFont);
            g.setPaint(originalPaint);
        }
    }
}
