package com.pdfutil.pdfutil;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;

public class ReadForm {

	public static void main(String[] args) throws IOException {
		Properties properties = getProperties("./src/main/resources/project.properties");
		String tessDataPath = properties.getProperty("tessdata_path");
		String pdfFilePath = properties.getProperty("pdf_file_path");
		String imageFilePath = properties.getProperty("image_file_path");
		String textFilePath = properties.getProperty("text_file_path");

		createImage(pdfFilePath, imageFilePath);
		createTextFile(getImageText(imageFilePath, tessDataPath), textFilePath);
	}

	private static void createTextFile(String data, String textFilePath) {
		FileWriter fw;
		try {
			fw = new FileWriter(textFilePath);
			fw.write(data);
			fw.close();
		} catch (IOException e) {
			e.getMessage();
		}

	}

	private static String getImageText(String imageFilePath, String tessDataLocation) {
		ITesseract instance = new Tesseract();
		String imgText = null;
		// set the path for the tessdata folder
		instance.setDatapath(tessDataLocation);

		try {
			imgText = instance.doOCR(new File(imageFilePath));
			return imgText;
		} catch (TesseractException e) {
			e.getMessage();
		}

		return imgText;
	}

	private static void createImage(String pdfFilePath, String imageFilePath) throws IOException {
		// Loading an existing PDF document
		File file = new File(pdfFilePath);
		PDDocument document = PDDocument.load(file);

		// Instantiating the PDFRenderer class
		PDFRenderer renderer = new PDFRenderer(document);

		// Rendering a high dpi image from the PDF document
		BufferedImage image = renderer.renderImageWithDPI(0, 300, ImageType.RGB);

		// Writing the image to a file
		ImageIO.write(image, "JPEG", new File(imageFilePath));

		// Closing the document
		document.close();
	}

	public static Properties getProperties(String fileName) throws IOException {
		FileInputStream stream = null;
		Properties properties = null;
		try {
			stream = new FileInputStream(fileName);
			properties = new Properties();
			properties.load(stream);
		} catch (FileNotFoundException e) {
			e.getMessage();
		} catch (IOException ie) {
			ie.getMessage();
		} finally {
			stream.close();
		}
		return properties;
	}

}
