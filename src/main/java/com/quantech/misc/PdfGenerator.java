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

    public Context setPatientContext(Patient patient) {
        Context context = new Context();

        context.setVariable("patientTitle", patient.getTitle().toString());
        context.setVariable("patientFirstName", patient.getFirstName());
        context.setVariable("patientLastName", patient.getLastName());
        context.setVariable("doctorTitle", patient.getDoctor().getTitle().toString());
        context.setVariable("doctorFirstName", patient.getDoctor().getFirstName());
        context.setVariable("doctorLastName", patient.getDoctor().getLastName());

        return context;
    }

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

        Context context = setPatientContext(patient);

        // make html file
        String html = templateEngine.process("templates/print/template", context);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();

        // create pdf
        File out = new File("pdfout.pdf");
        OutputStream outputStream = new FileOutputStream(out);
        System.out.println("Hopefully created at "+ out.getAbsolutePath());
        renderer.createPDF(outputStream);
        outputStream.close();
    }
}
