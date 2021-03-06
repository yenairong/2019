package com.ly.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

//https://www.cnblogs.com/chenpi/p/5534595.html
//https://blog.csdn.net/weixin_36380516/article/details/76984283
@Component
public class Jobs {
	
	
	//@Scheduled(cron = "0/1 *  *  * * ? ") // 每5秒执行一次
	public void createPDf() throws DocumentException, IOException{
		
		 String uuid = UUID.randomUUID().toString().replaceAll("-",""); 
		 
			// 指定路径如果没有则创建并添加		
			File file = new File("c:/pdf/"+uuid+".pdf");//获取父目录	
			File fileParent = file.getParentFile();//判断是否存在	
			if (!fileParent.exists()) {
				//创建父目录文件			
				fileParent.mkdirs();
			}		
			file.createNewFile();
		 
	    Document document = new Document();  
	    //Step 2—Get a PdfWriter instance.  
	    PdfWriter.getInstance(document, new FileOutputStream(file.toString()));  
	    //Step 3—Open the Document.  
	    document.open();  
	    //Step 4—Add content.  
	   /* document.add(new Paragraph("Hello World"));*/  
	    //Step 5—Close the Document.  
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    PdfPTable table = new PdfPTable(3); // 3 columns.
        table.setWidthPercentage(100); // Width 100%
        table.setSpacingBefore(10f); // Space before table
        table.setSpacingAfter(10f); // Space after table

        // Set Column widths
        float[] columnWidths = { 1f, 1f, 1f };
        table.setWidths(columnWidths);

        PdfPCell cell1 = new PdfPCell(new Paragraph("Cell 1"));
        cell1.setBorderColor(BaseColor.BLUE);
        cell1.setPaddingLeft(10);
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell2 = new PdfPCell(new Paragraph("Cell 2"));
        cell2.setBorderColor(BaseColor.GREEN);
        cell2.setPaddingLeft(10);
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell3 = new PdfPCell(new Paragraph("Cell 3"));
        cell3.setBorderColor(BaseColor.RED);
        cell3.setPaddingLeft(10);
        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);

        // To avoid having the cell border and the content overlap, if you
        // are having thick cell borders
        // cell1.setUserBorderPadding(true);
        // cell2.setUserBorderPadding(true);
        // cell3.setUserBorderPadding(true);

        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);

        document.add(table);
	    
	    
	    document.close();  

		
	}

}
