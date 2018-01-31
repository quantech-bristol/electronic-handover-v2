package com.quantech.misc;

import com.quantech.entities.patient.Patient;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class PdfGenerator {

    public void patientAsPdf(Patient patient) throws Exception {

        // set up thymeleaf rendering engine
        //
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setOrder(1);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        // the context data is used to fill the thymeleaf template
        Context context = new Context();
        context.setVariable("patient", patient);

        // make html file
        String html = templateEngine.process("templates/Print/template", context);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();

        // create pdf
        File out = new File("pdfout.pdf" );
        OutputStream outputStream = new FileOutputStream(out);
        renderer.createPDF(outputStream);
        outputStream.close();

        // delete patient data when the app closes
        out.deleteOnExit();
    }
}
