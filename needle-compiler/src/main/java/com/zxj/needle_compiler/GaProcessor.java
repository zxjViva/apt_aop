package com.zxj.needle_compiler;

import com.google.auto.service.AutoService;
import com.zxj.needle_annotations.GaAnnotation;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)
public class GaProcessor extends AbstractProcessor {
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return super.getSupportedSourceVersion();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> set = new HashSet<>();
        set.add(GaAnnotation.class.getCanonicalName());
        return set;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(GaAnnotation.class);
        if (elementsAnnotatedWith.size() > 0){
            GaFileWriter gaFileWriter = new GaFileWriter(new File("GA_file.txt"));
            for (Element element : elementsAnnotatedWith) {
                GaAnnotation gaAnnotation = element.getAnnotation(GaAnnotation.class);
                String line = String.format("%s,%s,%s\n", gaAnnotation.category(), gaAnnotation.action(), gaAnnotation.label());
                gaFileWriter.write(line);
            }
            gaFileWriter.close();
        }
        return false;
    }
}
