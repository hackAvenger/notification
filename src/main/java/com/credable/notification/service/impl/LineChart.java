package com.credable.notification.service.impl;

import com.credable.notification.dto.NotificationDataDTO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class LineChart {

    public byte[] notificationCount(List<NotificationDataDTO> notificationDataDTOList) throws IOException {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();


        for (NotificationDataDTO notificationDataDTO : notificationDataDTOList) {
            String key = notificationDataDTO.getDate().format(DateTimeFormatter.ofPattern("dd-MMM"));
            dataset.addValue(notificationDataDTO.getSmsCount(), "SMS", key);
            dataset.addValue(notificationDataDTO.getEmailCount(), "Email", key);
            dataset.addValue(notificationDataDTO.getWhatsappCount(), "Whatsapp", key);
        }

        JFreeChart chart = ChartFactory.createLineChart("Notification Stats", "Dates", "Count", dataset, PlotOrientation.VERTICAL, true, true, false);

        Color trans = new Color(0xFF, 0xFF, 0xFF, 0);
        chart.setBackgroundPaint(trans);
        chart.getPlot().setBackgroundPaint(trans);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.white);
        plot.getRenderer().setDefaultFillPaint(Color.BLUE);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        chart.getLegend().setFrame(BlockBorder.NONE);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(outputStream, chart, 750, 400);
        return outputStream.toByteArray();
    }
}
