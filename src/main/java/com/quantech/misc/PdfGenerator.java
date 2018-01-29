package com.quantech.misc;

import com.quantech.entities.patient.Patient;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import static com.lowagie.text.html.HtmlTags.HTML;

public class PdfGenerator {

    public void patientAsPdf(Patient patient) throws Exception {

        // set up thymeleaf rendering engine
        //
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        // the context data is used to fill the thymeleaf template
        Context context = new Context();
        context.setVariable("pTitle", "MR");

        // make html file
        String html = templateEngine.process("viewPatientPDF", context);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();

        // create pdf
        File out = new File("/patient_out.pdf");
        OutputStream outputStream = new FileOutputStream(out);
        System.out.println("Hopefully created at "+ out.getAbsolutePath());
        renderer.createPDF(outputStream);
        outputStream.close();
    }
}
