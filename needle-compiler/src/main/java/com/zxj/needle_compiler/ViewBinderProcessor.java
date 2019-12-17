package com.zxj.needle_compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.zxj.needle_annotations.ViewBinder;

import java.io.IOException;
import java.rmi.MarshalledObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class ViewBinderProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Messager messager;
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> set = new HashSet<>();
        set.add(ViewBinder.class.getCanonicalName());
        return set;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(ViewBinder.class);
        if (elementsAnnotatedWith.size() > 0){
            LinkedHashMap<TypeElement, List<Entity>> map = groupByElement(elementsAnnotatedWith);
            for (List<Entity> value : map.values()) {
                Entity entity1 = value.get(0);
                ClassName activity = ClassName.get(entity1.packageName, entity1.className);
                MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("findViewById")
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(ParameterSpec.builder(activity, "activity").build());
                for (Entity entity : value) {
                    //activity.filed = activity.findViewById(id)
                    String statement = String.format("activity.%s = activity.findViewById(%d)", entity.filedName, entity.viewId);
                    print(statement);
                    methodBuilder.addStatement(statement);
                }
                MethodSpec methodSpec = methodBuilder.build();
                ClassName absViewBinder = ClassName.get("com.zxj.needle_runtime", "AbsViewBinder");
                ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(absViewBinder, activity);
                TypeSpec typeSpec = TypeSpec.classBuilder(entity1.className + "_Binder")
                        .addSuperinterface(parameterizedTypeName)
                        .addModifiers(Modifier.PUBLIC)
                        .addMethod(methodSpec)
                        .build();
                JavaFile javaFile = JavaFile.builder(entity1.packageName, typeSpec).build();
                try {
                    javaFile.writeTo(filer);
                    print(javaFile.toString());
                } catch (IOException e) {
                    print("error:" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private LinkedHashMap<TypeElement, List<Entity>> groupByElement(Set<? extends Element> elementsAnnotatedWith) {
        LinkedHashMap<TypeElement, List<Entity>> map = new LinkedHashMap<>();
        for (Element element : elementsAnnotatedWith) {
            VariableElement variableElement = (VariableElement) element;
            ViewBinder annotation = variableElement.getAnnotation(ViewBinder.class);
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            List<Entity> entities = map.get(enclosingElement);
            if (entities == null){
                entities = new ArrayList<>();
                map.put(enclosingElement,entities);
            }

            int viewId = annotation.id();
            String filedName = variableElement.getSimpleName().toString();
            String className = enclosingElement.getSimpleName().toString();
            String packageName = elementUtils.getPackageOf(enclosingElement).getQualifiedName().toString();
            Entity entity = new Entity(className,packageName,filedName,viewId);
            print(entity.toString());
            entities.add(entity);
        }
        return map;
    }

    private void print(String content){
        messager.printMessage(Diagnostic.Kind.NOTE,"zxj: "+content);
    }
    private class Entity{
        String className;
        String packageName;
        String filedName;
        int viewId;

        public Entity(String className, String packageName, String filedName, int viewId) {
            this.className = className;
            this.packageName = packageName;
            this.filedName = filedName;
            this.viewId = viewId;
        }

        @Override
        public String toString() {
            return "Entity{" +
                    "className='" + className + '\'' +
                    ", packageName='" + packageName + '\'' +
                    ", filedName='" + filedName + '\'' +
                    ", viewId=" + viewId +
                    '}';
        }
    }
}
