package com.quantech.misc;

import com.quantech.entities.patient.Patient;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class PdfGenerator {

    public void patientAsPdf(List<Patient> patients) throws Exception {

        // set up thymeleaf rendering engine
        //
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setOrder(1);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        // prepare file and renderer
        File out = new File("pdfout.pdf" );
        ITextRenderer renderer = new ITextRenderer();

        OutputStream outputStream = new FileOutputStream(out);
        // the context data is used to fill the thymeleaf template
        Context context = new Context();
        context.setVariable("patients", patients);

        // make html file
        String html = templateEngine.process("templates/Print/template", context);

        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);
        outputStream.close();

        // delete patient data when the app closes
        out.deleteOnExit();
    }
}
